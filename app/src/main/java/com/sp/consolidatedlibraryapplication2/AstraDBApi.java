package com.sp.consolidatedlibraryapplication2;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

// https://guides.codepath.com/android/Consuming-APIs-with-Retrofit
// https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet#5
// https://stackoverflow.com/questions/32771021/using-retrofit-2-0-in-android
// https://medium.com/@KaushalVasava/retrofit-in-android-5a28c8e988ce
// https://www.youtube.com/watch?v=zAGsSmCxm84
public interface AstraDBApi {
    String BASE_URL = "https://37f51461-a483-48ca-a40a-e900300d8a5e-us-east-2.apps.astra.datastax.com/";

    // Fetching Library Catalogue
    @Headers("Content-Type: application/json")
    @GET("api/rest/v2/keyspaces/default_keyspace/library_catalogue")
    Call<BooksResponse> getBookCatalogue(
            @Header("x-cassandra-token") String apiKey,
            @Query("where") String whereClause
    );

    // Fetching Room Bookings
    @Headers("Content-Type: application/json")
    @GET("api/rest/v2/keyspaces/default_keyspace/room_bookings_v2")
    Call<RoomBookingResponse> getRoomBookings(
            @Header("x-cassandra-token") String apiKey,
            @Query("where") String whereClause
    );

    // Inserting in room bookings
    @Headers("Content-Type: application/json")
    @POST("api/rest/v2/keyspaces/default_keyspace/room_bookings_v2")
    Call<Void> insertRoomBooking(
            @Header("x-cassandra-token") String apiKey,
            @Body RoomBookingModel roomBooking
    );

    // Deleting room bookings
    @Headers("Content-Type: application/json")
    @DELETE("/api/rest/v2/keyspaces/default_keyspace/room_bookings_v2/{user_id}/{booking_date}/{booking_time}")
    Call<Map<String, Object>> deleteRoomBooking(
            @Header("x-cassandra-token") String apiKey,
            @Path("user_id") String userId,
            @Path("booking_date") String bookingDate,
            @Path("booking_time") String bookingTime
    );


    // Getting and confirming user accounts
    @Headers("Content-Type: application/json")
    @GET("api/rest/v2/keyspaces/default_keyspace/user_accounts")
    Call<UserResponse> loginUser(
            @Header("x-cassandra-token") String apiKey,
            @Query("where") String whereClause
    );

    // Getting Event data
    @GET("api/rest/v2/keyspaces/default_keyspace/events")
    Call<EventsResponse> getEvents(
            @Header("x-cassandra-token") String apiKey,
            @Query("where") String whereClause
    );
}


