package com.sp.consolidatedlibraryapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.view.MenuItem;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

public class RoomBookingPg1 extends AppCompatActivity {

    private CalendarView calendarView;
    private long minDate, maxDate;
    private String selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_booking_pg1);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_room_booking);
        calendarView = findViewById(R.id.calendarView2);

        // Get today's date
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)); // Today
        minDate = calendar.getTimeInMillis();

        // Set max date to 2 weeks from now
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        maxDate = calendar.getTimeInMillis();

        // Restrict the calendar
        calendarView.setMinDate(minDate);
        calendarView.setMaxDate(maxDate);

        // Listen for date selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            Log.d("RoomBookingPg1", "Selected Date: " + selectedDate);
        });

        // **Navigate to BookRoom and Pass the Selected Date**
        findViewById(R.id.next).setOnClickListener(v -> {
            if (selectedDate != null) {
                Intent intent = new Intent(RoomBookingPg1.this, BookingTable.class);
                intent.putExtra("SELECTED_DATE", selectedDate);
                startActivity(intent);
            } else {
                Toast.makeText(RoomBookingPg1.this, "Please select a date first", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate to Booking Instructions
        findViewById(R.id.instructionsText).setOnClickListener(v -> startActivity(new Intent(RoomBookingPg1.this, Instructions.class)));

        // Navigate to FAQs
        findViewById(R.id.faqsText).setOnClickListener(v -> startActivity(new Intent(RoomBookingPg1.this, FAQs.class)));

        // Navigate to Room Info
        findViewById(R.id.roomInfoText).setOnClickListener(v -> startActivity(new Intent(RoomBookingPg1.this, RoomInformation.class)));

        // Navigate to Library Map
        findViewById(R.id.mapText).setOnClickListener(v -> startActivity(new Intent(RoomBookingPg1.this, LibraryMap.class)));


        // Set listener for bottom navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_map) {// Open Map Activity (create MapActivity.java if not yet created)
                    Intent mapIntent = new Intent(RoomBookingPg1.this, GoogleMaps.class);
                    startActivity(mapIntent);
                    return true;
                } else if (itemId == R.id.nav_room_booking) {// Open Booking Activity
                    Intent bookingIntent = new Intent(RoomBookingPg1.this, RoomBookingPg1.class);
                    startActivity(bookingIntent);
                    return true;
                } else if (itemId == R.id.nav_home) {
                    Intent homeIntent = new Intent(RoomBookingPg1.this, HomePage.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.nav_information) {// Open Info Activity
                    Intent infoIntent = new Intent(RoomBookingPg1.this, BookingInformation.class);
                    startActivity(infoIntent);
                    return true;
                } else if (itemId == R.id.nav_catalogue) {// Open Menu Activity
                    Intent catalogueIntent = new Intent(RoomBookingPg1.this, BookCatalogue.class);
                    startActivity(catalogueIntent);
                    return true;
                }
                return false;
            }

        });
    }
}
