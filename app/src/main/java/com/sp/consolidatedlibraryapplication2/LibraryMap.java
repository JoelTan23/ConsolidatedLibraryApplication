package com.sp.consolidatedlibraryapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// Nothing here we are just displaying images in this activity
public class LibraryMap extends AppCompatActivity {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_library_map);


       BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
       bottomNavigationView.setSelectedItemId(R.id.nav_room_booking);

       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               int itemId = item.getItemId();
               if (itemId == R.id.nav_map) {
                   startActivity(new Intent(LibraryMap.this, GoogleMaps.class));
                   return true;
               } else if (itemId == R.id.nav_room_booking) {
                   startActivity(new Intent(LibraryMap.this, RoomBookingPg1.class));
                   return true;
               } else if (itemId == R.id.nav_home) {
                   startActivity(new Intent(LibraryMap.this, HomePage.class));
                   return true;
               } else if (itemId == R.id.nav_information) {
                   startActivity(new Intent(LibraryMap.this, BookingInformation.class));
                   return true;
               } else if (itemId == R.id.nav_catalogue) {
                   startActivity(new Intent(LibraryMap.this, BookCatalogue.class));
                   return true;
               }
               return false;
           }
       });

    }
}