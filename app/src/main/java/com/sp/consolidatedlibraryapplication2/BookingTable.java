package com.sp.consolidatedlibraryapplication2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class BookingTable extends AppCompatActivity {

    private TableLayout tableLayout;
    private DatabaseHelper dbHelper;
    private String selectedDate;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_table);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_room_booking);


        dbHelper = new DatabaseHelper();
        tableLayout = findViewById(R.id.booking_table);
        refreshButton = findViewById(R.id.refresh_button);

        // **Retrieve Date Passed from RoomBookingPg1**
        selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        Log.d("BookRoom", "Selected Date: " + selectedDate);

        // Load bookings when activity starts
        fetchBookingsForDate();

        // Next Page to Book Room
        findViewById(R.id.bookroom).setOnClickListener(v -> {
                    Intent intent = new Intent(BookingTable.this, BookRoom.class);
                    intent.putExtra("SELECTED_DATE", selectedDate);
                    startActivity(intent);
                });

        // Refresh button to reload bookings
        refreshButton.setOnClickListener(v -> fetchBookingsForDate()); // Calls funciton below so you fetch data again to refresh table.
        //findViewById(R.id.instructionsText).setOnClickListener(v -> startActivity(new Intent(BookingTable.this, Instructions.class)));

        // Set listener for bottom navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_map) {// Open Map Activity (create MapActivity.java if not yet created)
                    Intent mapIntent = new Intent(BookingTable.this, GoogleMaps.class);
                    startActivity(mapIntent);
                    return true;
                } else if (itemId == R.id.nav_room_booking) {// Open Booking Activity
                    Intent bookingIntent = new Intent(BookingTable.this, RoomBookingPg1.class);
                    startActivity(bookingIntent);
                    return true;
                } else if (itemId == R.id.nav_home) {
                    Intent homeIntent = new Intent(BookingTable.this, HomePage.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.nav_information) {// Open Info Activity
                    Intent infoIntent = new Intent(BookingTable.this, BookingInformation.class);
                    startActivity(infoIntent);
                    return true;
                } else if (itemId == R.id.nav_catalogue) {// Open Menu Activity
                    Intent catalogueIntent = new Intent(BookingTable.this, BookCatalogue.class);
                    startActivity(catalogueIntent);
                    return true;
                }
                return false;
            }

        });
    }

    private void fetchBookingsForDate() {
        if (selectedDate == null) {
            Toast.makeText(this, "No date selected", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.fetchRoomBookings(selectedDate, new DatabaseHelper.OnRoomBookingsFetchedListener() {  // Calls function from DatabaseHelper
            @Override
            public void onSuccess(List<RoomBookingModel> bookings) {
                displayBookings(bookings);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(BookingTable.this, "Error fetching bookings: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    // This entire code makes the table that shows all the bookings.
    // I couldn't find a way to make the table more interactive or easier to understand so this is all I have for now.
    private void displayBookings(List<RoomBookingModel> bookings) {  // Calls model to format the variables.
        tableLayout.removeAllViews(); // Clear old data

        // Using Drawables to style the different things like a CSS file.
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundResource(R.drawable.table_border); // Add borders --> Using table_border drawbales resource.

        addStyledCell(headerRow, "Room", true);
        addStyledCell(headerRow, "Time Slot", true);
        addStyledCell(headerRow, "Status", true);
        tableLayout.addView(headerRow);

        // Lists of the timeslots and the available rooms. We're going to go through each of these one by one.
        String[] rooms = {
                "Pod L1.1", "Pod L1.2", "Pod L1.3", "Pod L1.4", "Pod L1.5", "Pod L1.6", "Pod L1.7", "Pod L1.8", "Pod L2.1", "Pod L2.2",
                "Pod L4.1", "Pod L4.2", "Pod L4.3", "Pod L3.1", "Pod L3.2", "Pod L3A.1", "Pod L3A.2", "Pod L3A.3", "Pod L3A.4", "Pod L3A.5",
                "Meeting Room L4", "Meeting Room L4A",
                "Studio L4A", "Studio2 L4A"
        };


        String[] timeSlots = {
                "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM"
        };

        // We go through the timeslots and the rooms and slowly finds if they are availabe or not and then determine the available or etc...
        for (String room : rooms) {
            for (String timeSlot : timeSlots) {
                TableRow row = new TableRow(this);
                row.setBackgroundResource(R.drawable.table_border); // Add border

                addStyledCell(row, room, false); // Room Name
                addStyledCell(row, timeSlot, false); // Time Slot

                // Checks if the Slot is Already Booked
                boolean isBooked = false;
                for (RoomBookingModel booking : bookings) {
                    if (booking.getRoomType().equals(room) && booking.getBookingTime().contains(timeSlot)) {
                        isBooked = true;
                        break;
                    }
                }

                // More formatting.
                // I might be wrong but I'm pretty sure that this is for the background
                TextView statusCell = new TextView(this);
                statusCell.setPadding(16, 12, 16, 12);
                statusCell.setTextSize(16);
                statusCell.setBackgroundResource(R.drawable.table_border);

                if (isBooked) {
                    statusCell.setText("Booked");
                    statusCell.setTextColor(getResources().getColor(R.color.red)); // When booked the text is red
                } else {
                    statusCell.setText("Available");
                    statusCell.setTextColor(getResources().getColor(R.color.green));  // When available the

                    // Interactivity --> At least an attempt
                    // When you click the row with the booking status, if available, it will send a booking through the submitBooking function below
                    statusCell.setOnClickListener(v -> submitBooking(room, timeSlot));
                }

                // And now the whole cycle repeats
                row.addView(statusCell);
                tableLayout.addView(row);
            }
        }
    }


    // More formatting for cells.
    // This should be for each cell for timeslot, room and booking status

    private void addStyledCell(TableRow row, String text, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 12, 16, 12);
        textView.setTextSize(16);
        textView.setBackgroundResource(R.drawable.table_border); // Border

        if (isHeader) {
            textView.setTypeface(null, Typeface.BOLD);
        }
        row.addView(textView);
    }


    // Inserting bookings
    private void submitBooking(String roomType, String timeSlot) {
        // We need to make sure that the user is logged in first
        if (selectedDate == null) {
            Toast.makeText(this, "No date selected", Toast.LENGTH_SHORT).show();
            return;
        }

        String loggedInUser = UserSession.getInstance().getUserId();
        if (loggedInUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        RoomBookingModel booking = new RoomBookingModel(loggedInUser, selectedDate, timeSlot, roomType); // Calls the model to format the varibles before we submit it

        dbHelper.insertRoomBooking(booking, new DatabaseHelper.OnDataInsertedListener() {   // Calls database helper function
            @Override
            public void onSuccess(String message) {
                Toast.makeText(BookingTable.this, "Booking Confirmed: " + roomType + " at " + timeSlot, Toast.LENGTH_SHORT).show();
                fetchBookingsForDate(); // Refresh table
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(BookingTable.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void addCell(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 8, 16, 8);
        row.addView(textView);
    }
}
