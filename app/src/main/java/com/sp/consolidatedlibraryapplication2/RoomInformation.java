package com.sp.consolidatedlibraryapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RoomInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_information);

        // **Setup Bottom Navigation**
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_room_booking);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

    }


    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        if (item.getItemId() == R.id.nav_map) {
            intent = new Intent(RoomInformation.this, GoogleMaps.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_room_booking) {
            intent = new Intent(RoomInformation.this, RoomBookingPg1.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_home) {
            intent = new Intent(RoomInformation.this, HomePage.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_information) {
            intent = new Intent(RoomInformation.this, BookingInformation.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_catalogue) {
            intent = new Intent(RoomInformation.this, BookCatalogue.class);
            startActivity(intent);
        }
        return false;
    }
}