package com.sp.consolidatedlibraryapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private TextView userIdTextView;
    private ImageButton mapbutton, cataloguebutton, websitebutton, bookmarkbutton, bookinginfobutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Image buttons
        findViewById(R.id.mapbutton).setOnClickListener(v -> startActivity(new Intent(HomePage.this, GoogleMaps.class)));
        findViewById(R.id.cataloguebutton).setOnClickListener(v -> startActivity(new Intent(HomePage.this, BookCatalogue.class)));

        // Handle Website Button click - Replace with WebView Fragment
        websitebutton = findViewById(R.id.websitebutton);
        websitebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new librarywebsite());
            }
        });


        findViewById(R.id.bookmarkbutton).setOnClickListener(v -> startActivity(new Intent(HomePage.this, RoomBookingPg1.class)));
        findViewById(R.id.bookinginfobutton).setOnClickListener(v -> startActivity(new Intent(HomePage.this, BookingInformation.class)));

        // Initialize Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Optimize Navigation Drawer - Load User ID Only When Opened
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                View headerView = navigationView.getHeaderView(0);
                userIdTextView = headerView.findViewById(R.id.user_id);
                String userId = UserSession.getInstance().getUserId();
                userIdTextView.setText("Logged in as: " + userId);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                userIdTextView.setText(null); // Clear memory when drawer is closed
            }

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        // Setup Drawer Toggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Navigation Drawer Click Handling
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                logoutUser();
                return true;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Setup RecyclerView
        dbHelper = new DatabaseHelper();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        fetchEventsData();
    }

    // Load the Fragment --> This was called earlier
    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(null); // Allows back navigation
        transaction.commit();
    }

    // Bottom Navigation Handling
    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        if (item.getItemId() == R.id.nav_map) {
            intent = new Intent(HomePage.this, GoogleMaps.class);
        } else if (item.getItemId() == R.id.nav_room_booking) {
            intent = new Intent(HomePage.this, RoomBookingPg1.class);
        } else if (item.getItemId() == R.id.nav_home) {
            intent = new Intent(HomePage.this, HomePage.class);
        } else if (item.getItemId() == R.id.nav_information) {
            intent = new Intent(HomePage.this, BookingInformation.class);
        } else if (item.getItemId() == R.id.nav_catalogue) {
            intent = new Intent(HomePage.this, BookCatalogue.class);
        }
        if (intent != null) {
            startActivity(intent);
            return true;
        }
        return false;
    }

    // Fetch Events
    private void fetchEventsData() {
        Log.d("HomePage", "Starting to fetch data for events...");
        dbHelper.fetchEventsData(new DatabaseHelper.onEventsFetchedListener() {
            @Override
            public void onSuccess(List<EventModel> events) {
                Log.d("HomePage", "Events fetched successfully: " + events.size());
                if (events != null && !events.isEmpty()) {
                    eventsAdapter = new EventsAdapter(events, HomePage.this);
                    recyclerView.setAdapter(eventsAdapter);
                } else {
                    Toast.makeText(HomePage.this, "No events found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e("HomePage", "Error fetching events: " + error);
                Toast.makeText(HomePage.this, "Failed to load data: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Logout and Return to Login Page
    private void logoutUser() {
        UserSession.getInstance().clearSession();
        Intent intent = new Intent(HomePage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Handle Navigation Toggle
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
