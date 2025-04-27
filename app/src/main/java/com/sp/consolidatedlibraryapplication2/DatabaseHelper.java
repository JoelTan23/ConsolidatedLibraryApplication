    package com.sp.consolidatedlibraryapplication2;
    // https://docs.datastax.com/en/astra-db-serverless/databases/java-client.html
    import android.util.Log;
    import androidx.annotation.NonNull;

    import org.json.JSONException;
    import org.json.JSONObject;

    import okhttp3.MediaType;
    import okhttp3.RequestBody;
    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;
    import java.util.List;
    import java.util.Map;

    public class DatabaseHelper {

        private static final String API_KEY = "AstraCS:jlUPfMkRGQSrUOtCgwYfHjHH:88aa5679849e3a4b7a4307eedb5156d6fba260a71ee632ff6dd822ab6b8e6859";
        // This API key is needed for Querys sent to AstraDB
        private final AstraDBApi api;

        public DatabaseHelper() {
            api = RetrofitClient.getClient().create(AstraDBApi.class);
        }
        private String currentUserId = UserSession.getInstance().getUserId(); // --> To get the current user ID, however for some functions, we are sending the current user id from the activities that they were called from

        private String status;


        // Fetch Data for Room Bookings
        // This one sorts by the selected date. This is used for the table of bookings showing the date selected
        public void fetchRoomBookings(String selectedDate, final OnRoomBookingsFetchedListener listener) {
            String whereClause = "{\"booking_date\": {\"$in\": [\"" + selectedDate + "\"]}}";  // --> Using REST API Means that we need to submit the clause in JSON
           // String whereClause = "{}"; --> This shows all the bookings


            // For some reason we need this RoomBookingResponse for seemingly a intermediary for the RoomBookingModel

            Call<RoomBookingResponse> call = api.getRoomBookings(API_KEY, whereClause);    // This calls the API and actually sends the REST Query via the Base_url link
            call.enqueue(new Callback<RoomBookingResponse>() {
                @Override
                public void onResponse(@NonNull Call<RoomBookingResponse> call, @NonNull Response<RoomBookingResponse> response) {
                    if (response.isSuccessful()) {
                        List<RoomBookingModel> bookings = response.body().getData();
                        listener.onSuccess(bookings);
                    } else {
                        listener.onFailure("Error fetching bookings: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RoomBookingResponse> call, @NonNull Throwable t) {
                    listener.onFailure("Failed to fetch bookings: " + t.getMessage());
                }
            });
        }

        // This room bookings is used for the BookingInformation page where we show all bookings from the same userID
        public void fetchRoomBookingsInfo(String userId, final OnRoomBookingsFetchedListener listener) {
            String whereClause = "{\"user_id\": {\"$eq\": \"" + userId + "\"}}";
            // String whereClause = "{}";

            Call<RoomBookingResponse> call = api.getRoomBookings(API_KEY, whereClause);  // Calls API file, same as the function below, but with a different whereclause
            call.enqueue(new Callback<RoomBookingResponse>() {
                @Override
                public void onResponse(@NonNull Call<RoomBookingResponse> call, @NonNull Response<RoomBookingResponse> response) {
                    if (response.isSuccessful()) {
                        List<RoomBookingModel> bookings = response.body().getData();
                        listener.onSuccess(bookings);
                    } else {
                        listener.onFailure("Error fetching bookings: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RoomBookingResponse> call, @NonNull Throwable t) {
                    listener.onFailure("Failed to fetch bookings: " + t.getMessage());
                }
            });
        }



        // User Login
        public void loginUser(String userId, String password, final OnUserLoginListener listener) {
            // First, retrieve the user data using only user_id
            String whereClause = "{\"user_id\": {\"$eq\": \"" + userId + "\"}}";

            Call<UserResponse> call = api.loginUser(API_KEY, whereClause); // Call to API file
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().getData().isEmpty()) {
                        // Extract user data
                        UserModel user = response.body().getData().get(0);

                        // I couldn't filter through AstraDB so we have to do it here
                        if (user.getPassword().equals(password)) {
                            listener.onSuccess();
                        } else {
                            listener.onFailure("Invalid credentials.");
                        }
                    } else {
                        listener.onFailure("User not found.");
                    }
                }
                @Override
                public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                    listener.onFailure("Failed to connect: " + t.getMessage());
                }
            });
        }

        //  Insert Room Booking
        public void insertRoomBooking(RoomBookingModel booking, final OnDataInsertedListener listener) {
            Call<Void> call = api.insertRoomBooking(API_KEY, booking); // Call API file
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        listener.onSuccess("Room booked successfully!");
                    } else {
                        listener.onFailure("Error booking room: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    listener.onFailure("Failed to book room: " + t.getMessage());
                }
            });
        }


        // This function caused a lot of problems
        // This function Deletes the room bookings with the delete room booking button from Booking Information
        public void deleteRoomBooking(String userId, String bookingDate, String bookingTime, final OnDataDeletedListener listener) {
            Call<Map<String, Object>> call = api.deleteRoomBooking(API_KEY, userId, bookingDate, bookingTime);  // We need to use all these things to fitler the correct thing to delete
            // This code also calls the API file

            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(@NonNull Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        listener.onSuccess("Booking permanently deleted.");
                    } else {
                        listener.onFailure("Failed to delete booking. Response Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Map<String, Object>> call, @NonNull Throwable t) {
                    listener.onFailure("Error: " + t.getMessage());
                }
            });
        }

        // This fetches the data for the book catalogue
        public void fetchData(String genre, final OnDataFetchedListener listener) {
            Log.d("DatabaseHelper", "Starting data fetch for genre: " + genre);

            String whereClause;
            // This sorts the where clause to determine the genre type that we are displaying
            if (genre == "Null") {
                whereClause = "{}";  // This shows all the bookgs
                Log.d("BookCatalogue", "Null is? : " + whereClause);
            } else {
                whereClause = "{\"book_genre\": {\"$eq\": \"" + genre + "\"}}";
            }
            Log.d("BookCatalogue", "Constructed where clause: " + whereClause);

            Call<BooksResponse> call = api.getBookCatalogue(API_KEY, whereClause); // Calls API file
            call.enqueue(new Callback<BooksResponse>() {
                @Override
                public void onResponse(@NonNull Call<BooksResponse> call, @NonNull Response<BooksResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<BookModel> books = response.body().getData();
                        Log.d("DatabaseHelper", "Books fetched successfully: " + books.size());
                        listener.onSuccess(books);
                    } else {
                        Log.e("DatabaseHelper", "Error fetching books. Response Code: " + response.code());
                        listener.onFailure("Error fetching books. Response Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BooksResponse> call, @NonNull Throwable t) {
                    Log.e("DatabaseHelper", "Failed to fetch books: " + t.getMessage());
                    listener.onFailure("Failed to fetch books: " + t.getMessage());
                }
            });
        }

        // Data fetching for events.
        // Pretty much the same as room bookings, but there is no filter, we are just fetchign for all.
        public void fetchEventsData(final onEventsFetchedListener listener) {
            Log.d("DatabaseHelper", "Fetching events from AstraDB...");

            String whereClause = "{}"; // Fetch all events
            Call<EventsResponse> call = api.getEvents(API_KEY, whereClause);

            call.enqueue(new Callback<EventsResponse>() {
                @Override
                public void onResponse(@NonNull Call<EventsResponse> call, @NonNull Response<EventsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<EventModel> events = response.body().getData();
                        Log.d("DatabaseHelper", "Events fetched successfully: " + events.size());
                        listener.onSuccess(events); // ✅ Now correctly passes List<EventModel>
                    } else {
                        Log.e("DatabaseHelper", "Error fetching events: " + response.code());
                        listener.onFailure("Error fetching events. Response Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EventsResponse> call, @NonNull Throwable t) {
                    Log.e("DatabaseHelper", "Failed to fetch events: " + t.getMessage());
                    listener.onFailure("Failed to fetch events: " + t.getMessage());
                }
            });
        }



        public interface OnDataFetchedListener {
            void onSuccess(List<BookModel> books);
            void onFailure(String error);
        }

        public interface OnRoomBookingsFetchedListener { // Fixed name
            void onSuccess(List<RoomBookingModel> roomBookings); // Fixed: List instead of a single object
            void onFailure(String error);
        }

        public interface OnUserLoginListener {
            void onSuccess();
            void onFailure(String error);
        }

        public interface OnDataInsertedListener {
            void onSuccess(String message);
            void onFailure(String error);
        }
        public interface OnDataDeletedListener {
            void onSuccess(String message);
            void onFailure(String error);
        }
        public interface onEventsFetchedListener{
            void onSuccess(List<EventModel> events);
            void onFailure(String error);
        }
    }

