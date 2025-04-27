package com.sp.consolidatedlibraryapplication2;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.sp.consolidatedlibraryapplication2.databinding.ActivityGoogleMapsBinding;

public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 100;
    private Button requestLocationButton;

    // This is the destination location.
    // This is the location of SP's Library and FabLab
    private LatLng destination = new LatLng(1.3088925590474734, 103.77994749630693);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Find the request location button
        requestLocationButton = findViewById(R.id.request_location_button);

        // Set click listener to request permission
        requestLocationButton.setOnClickListener(v -> requestLocationPermission());

        // Load Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googlemap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // **Check & Request Location Permissions**
        if (checkLocationPermission()) {
            enableUserLocation();
        } else {
            requestLocationButton.setVisibility(View.VISIBLE); // Show button if permission is denied
        }
    }

    // Check if Location Permission is Granted
    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Request Location Permission
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
    }

    // Handle Permission Request Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                requestLocationButton.setVisibility(View.GONE); // Hide button after granting permission
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Enable User Location and Fetch Current Location
    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; // Ensure permission is granted before enabling location
        }

        mMap.setMyLocationEnabled(true);
        getCurrentLocation();
    }

    // Fetch User's Current Location
    private void getCurrentLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Get current location
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    // Add marker for current location
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));

                    // Move camera to current location
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                    // Add marker for destination
                    mMap.addMarker(new MarkerOptions().position(destination).title("SP Library"));
                } else {
                    Toast.makeText(GoogleMaps.this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


