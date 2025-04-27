package com.sp.consolidatedlibraryapplication2;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class BookingInformation extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RoomBookingAdapter adapter;
    private DatabaseHelper dbHelper;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_information);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_information);

        // Recyclerview set up
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper();

        // I'm putting this here just in case for troubleshooting purposes
        currentUserId = UserSession.getInstance().getUserId(); // Ensure you have a UserSession class

        fetchBookings();

        // Navigation bar code
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_map) {
                    startActivity(new Intent(BookingInformation.this, GoogleMaps.class));
                    return true;
                } else if (itemId == R.id.nav_room_booking) {
                    startActivity(new Intent(BookingInformation.this, RoomBookingPg1.class));
                    return true;
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(BookingInformation.this, HomePage.class));
                    return true;
                } else if (itemId == R.id.nav_information) {
                    startActivity(new Intent(BookingInformation.this, BookingInformation.class));
                    return true;
                } else if (itemId == R.id.nav_catalogue) {
                    startActivity(new Intent(BookingInformation.this, BookCatalogue.class));
                    return true;
                }
                return false;
            }
        });
    }

    private void fetchBookings() {
        // Code below gets current user ID
        currentUserId = UserSession.getInstance().getUserId();

        if (currentUserId == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        // **Fetch Only the Bookings of the Logged-in User**
        dbHelper.fetchRoomBookingsInfo(currentUserId, new DatabaseHelper.OnRoomBookingsFetchedListener() { // This line calls DatabaseHelper function
            @Override
            public void onSuccess(List<RoomBookingModel> roomBookings) {
                adapter = new RoomBookingAdapter(BookingInformation.this, roomBookings, currentUserId);
                recyclerView.setAdapter(adapter);  // Loads data into recycler view
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(BookingInformation.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
