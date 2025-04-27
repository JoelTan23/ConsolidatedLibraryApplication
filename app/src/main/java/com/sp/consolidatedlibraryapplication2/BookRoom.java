package com.sp.consolidatedlibraryapplication2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Arrays;
import java.util.List;

public class BookRoom extends AppCompatActivity implements QRScannerFragment.QRScanListener {

    private Spinner roomTypeSpinner, timeSlotSpinner;
    private Button bookButton, scanQRButton;
    private DatabaseHelper dbHelper;
    private String selectedRoomType = "Pod L1.3"; // Default selection
    private String selectedTimeSlot = "09:00 AM"; // Default selection
    private String selectedDate;
    private TextView bookRoomDate;
    private List<String> roomTypes;


    // This file is for manual booking
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);

        dbHelper = new DatabaseHelper();

        // **Retrieve Date Passed from RoomBookingPg1**
        selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        Log.d("BookRoom", "Received Date: " + selectedDate);

        // **Set the Title to Match the Selected Date**
        bookRoomDate = findViewById(R.id.book_room_date);
        if (selectedDate != null) {
            bookRoomDate.setText("Bookings for: " + selectedDate);
        } else {
            bookRoomDate.setText("Error");
        }


        // Room Type Spinner
        roomTypeSpinner = findViewById(R.id.room_type);
        roomTypes = Arrays.asList(
                "Pod L1.1", "Pod L1.2", "Pod L1.3", "Pod L1.4", "Pod L1.5", "Pod L1.6", "Pod L1.7", "Pod L1.8", "Pod L2.1", "Pod L2.2",
                "Pod L4.1", "Pod L4.2", "Pod L4.3", "Pod L3.1", "Pod L3.2", "Pod L3A.1", "Pod L3A.2", "Pod L3A.3", "Pod L3A.4", "Pod L3A.5",
                "Meeting Room L4", "Meeting Room L4A", "Studio L4A", "Studio2 L4A"
        );


        // For the spinner
        ArrayAdapter<String> roomTypeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, roomTypes);
        roomTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeSpinner.setAdapter(roomTypeAdapter);

        // Time Slot Spinner
        timeSlotSpinner = findViewById(R.id.timeslot);
        List<String> timeSlots = Arrays.asList(
                "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM"
        );

        ArrayAdapter<String> timeSlotAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, timeSlots);
        timeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotSpinner.setAdapter(timeSlotAdapter);

        timeSlotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTimeSlot = timeSlots.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // **Scan QR Button**
        scanQRButton = findViewById(R.id.scan_qr_button);
        scanQRButton.setOnClickListener(v -> openQRScanner());

        // **Book Room Button**
        bookButton = findViewById(R.id.book_button);
        bookButton.setOnClickListener(v -> submitBooking());
    }

    // This is QR Scan with  and externalfragment that is connected to this activity.
    private void openQRScanner() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        QRScannerFragment qrScannerFragment = new QRScannerFragment();
        qrScannerFragment.show(fragmentManager, "QRScannerFragment");
    }

    @Override
    // This is the actual scannign of the QR
    public void onQRScanned(String scannedData) {
        Log.d("QRScanner", "Scanned Data: " + scannedData);
        try {
            int roomIndex = Integer.parseInt(scannedData) - 1; // This adapts the value from the QR scan into something that can fit into the array for room type
            // Since we have 24 room types, we need to minus 1 since we're storing them in an array
            if (roomIndex >= 0 && roomIndex < roomTypes.size()) {
                // Changing the spinner to the room type that was scanned.
                roomTypeSpinner.setSelection(roomIndex);
                // This is to store the variable so that it is sent when we call the function to insert booking
                selectedRoomType = roomTypes.get(roomIndex);
                Toast.makeText(this, "QR Scanned Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid QR Code Data", Toast.LENGTH_SHORT).show();
        }
    }



    // Same submit booking as in the Booking Table
    // We do extra precautions to make sure that the selected rooms and all are not null.
    private void submitBooking() {
        if (selectedRoomType == null || selectedTimeSlot == null || selectedDate == null) {
            Toast.makeText(this, "Please select all options", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get Logged-in User ID
        String loggedInUser = UserSession.getInstance().getUserId();
        if (loggedInUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        // **Check if the Room is Available**
        dbHelper.fetchRoomBookings(selectedDate, new DatabaseHelper.OnRoomBookingsFetchedListener() {
            @Override
            public void onSuccess(List<RoomBookingModel> bookings) {
                // Check if any booking exists for the selected room and timeslot
                boolean roomIsAvailable = true;
                for (RoomBookingModel booking : bookings) {
                    if (booking.getRoomType().equals(selectedRoomType) && booking.getBookingTime().equals(selectedTimeSlot)) {
                        roomIsAvailable = false;
                        break;
                    }
                }

                if (!roomIsAvailable) {
                    // If room is unavailable, show a toast message
                    Toast.makeText(BookRoom.this, "Room Unavailable for this time slot!", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                // If the room is available, proceed with booking
                RoomBookingModel booking = new RoomBookingModel(loggedInUser, selectedDate, selectedTimeSlot, selectedRoomType);
                dbHelper.insertRoomBooking(booking, new DatabaseHelper.OnDataInsertedListener() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(BookRoom.this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(BookRoom.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(BookRoom.this, "Error fetching room bookings: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}


/*
// With External Device

package com.sp.consolidatedlibraryapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.IntentIntegrator;
import com.journeyapps.barcodescanner.IntentResult;

import java.util.Arrays;
import java.util.List;

public class BookRoom extends AppCompatActivity implements QRScannerFragment.QRScanListener {

    private Spinner roomTypeSpinner, timeSlotSpinner;
    private Button bookButton, scanQRButton;
    private DatabaseHelper dbHelper;
    private String selectedRoomType = "Pod L1.3"; // Default selection
    private String selectedTimeSlot = "09:00 AM"; // Default selection
    private String selectedDate;
    private TextView bookRoomDate;
    private List<String> roomTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);

        dbHelper = new DatabaseHelper();

        // **Retrieve Date Passed from RoomBookingPg1**
        selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        Log.d("BookRoom", "Received Date: " + selectedDate);

        // **Set the Title to Match the Selected Date**
        bookRoomDate = findViewById(R.id.book_room_date);
        if (selectedDate != null) {
            bookRoomDate.setText("Bookings for: " + selectedDate);
        } else {
            bookRoomDate.setText("Error");
        }

        // **Room Type Spinner**
        roomTypeSpinner = findViewById(R.id.room_type);
        roomTypes = Arrays.asList(
                "Pod L1.1", "Pod L1.2", "Pod L1.3", "Pod L1.4", "Pod L1.5", "Pod L1.6", "Pod L1.7", "Pod L1.8", "Pod L2.1", "Pod L2.2",
                "Pod L4.1", "Pod L4.2", "Pod L4.3", "Pod L3.1", "Pod L3.2", "Pod L3A.1", "Pod L3A.2", "Pod L3A.3", "Pod L3A.4", "Pod L3A.5",
                "Meeting Room L4", "Meeting Room L4A", "Studio L4A", "Studio2 L4A"
        );

        ArrayAdapter<String> roomTypeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, roomTypes);
        roomTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeSpinner.setAdapter(roomTypeAdapter);

        roomTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRoomType = roomTypes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // **Time Slot Spinner**
        timeSlotSpinner = findViewById(R.id.timeslot);
        List<String> timeSlots = Arrays.asList(
                "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM"
        );

        ArrayAdapter<String> timeSlotAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, timeSlots);
        timeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotSpinner.setAdapter(timeSlotAdapter);

        timeSlotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTimeSlot = timeSlots.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // **Scan QR Button**
        scanQRButton = findViewById(R.id.scan_qr_button);
        scanQRButton.setOnClickListener(v -> startQRScanner());

        // **Book Room Button**
        bookButton = findViewById(R.id.book_button);
        bookButton.setOnClickListener(v -> submitBooking());
    }

    private void startQRScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a Room QR Code");
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(true);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.initiateScan();
    }

    @Override
    public void onQRScanned(String scannedData) {
        try {
            int roomIndex = Integer.parseInt(scannedData) - 1;
            if (roomIndex >= 0 && roomIndex < roomTypes.size()) {
                roomTypeSpinner.setSelection(roomIndex);
            } else {
                Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid QR Code Data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            onQRScanned(result.getContents());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

 */