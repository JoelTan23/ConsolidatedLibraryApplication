package com.sp.consolidatedlibraryapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FAQs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        // Set up the Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_room_booking);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_map) {
                    startActivity(new Intent(FAQs.this, GoogleMaps.class));
                    return true;
                } else if (itemId == R.id.nav_room_booking) {
                    startActivity(new Intent(FAQs.this, RoomBookingPg1.class));
                    return true;
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(FAQs.this, HomePage.class));
                    return true;
                } else if (itemId == R.id.nav_information) {
                    startActivity(new Intent(FAQs.this, BookingInformation.class));
                    return true;
                } else if (itemId == R.id.nav_catalogue) {
                    startActivity(new Intent(FAQs.this, BookCatalogue.class));
                    return true;
                }
                return false;
            }
        });

        // Handle WebView Fragment Click
        TextView webViewButton = findViewById(R.id.contactus);
        webViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FAQWebView());
            }
        });
    }

    // Replaces the frameview in the xml to expand into the fragement with the webview
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(null); // Allows back navigation
        transaction.commit();
    }
}
