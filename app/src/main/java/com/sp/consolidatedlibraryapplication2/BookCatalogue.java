package com.sp.consolidatedlibraryapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class BookCatalogue extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private Spinner genreSpinner;
    private String selectedGenre = "Classic Fiction";  // Default genre

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_catalogue);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the DatabaseHelper to fetch data from AstraDB
        dbHelper = new DatabaseHelper();

        // All the options within the spinner.
        // We use this also to filter the books through the "wherecaluse" to AstraDB
        genreSpinner = findViewById(R.id.BookGenreSelect);
        String[] genres = new String[] {
                "Null", "Classic Fiction", "Dystopian Fiction", "Romantic Fiction", "Coming-of-Age Fiction", "Science Fiction", "Mystery", "Biography", "Horror", "Non-Fiction", "Fantasy"
        };

        // Set up the adapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(adapter);

        // Set listener for Spinner item selection
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            // This is the code that actually sends the filter from the spinner to the fetchdata function below.
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedGenre = parentView.getItemAtPosition(position).toString();  // Get the selected genre
                Log.d("BookCatalogue", "Selected Genre: " + selectedGenre);
                fetchBooksData(selectedGenre);  // Fetch data based on selected genre --> Function below is called
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nothing to be done
                // We sort for Nil selected in the DatabaseHelper function
            }
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_catalogue);  // Set "Catalogue" as the default item

        // Set listener for bottom navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Navigate to the selected activity based on the item ID
            if (itemId == R.id.nav_map) {
                Intent mapIntent = new Intent(BookCatalogue.this, GoogleMaps.class);
                startActivity(mapIntent);
                return true;
            } else if (itemId == R.id.nav_room_booking) {
                Intent bookingIntent = new Intent(BookCatalogue.this, RoomBookingPg1.class);
                startActivity(bookingIntent);
                return true;
            } else if (itemId == R.id.nav_home) {
                Intent homeIntent = new Intent(BookCatalogue.this, HomePage.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.nav_information) {
                Intent infoIntent = new Intent(BookCatalogue.this, BookingInformation.class);
                startActivity(infoIntent);
                return true;
            } else if (itemId == R.id.nav_catalogue) {
                // Since the user is already in this screen, we do nothing
                return true;
            }
            return false;
        });

        // Fetch initial data with the default genre
        // I'm leaving this here for now because my code works currently.
        // In theory this shouldn't be needed
        fetchBooksData(selectedGenre);
    }

    // Method to fetch book data from the database
    private void fetchBooksData(String genre) {
        Log.d("BookCatalogue", "Starting to fetch data for genre: " + genre);
        dbHelper.fetchData(genre, new DatabaseHelper.OnDataFetchedListener() {  // fetchdata calls the DatabaseHelper function
            @Override
            public void onSuccess(List<BookModel> books) {  // BookModel shapes the structure of how the code is sent to the variable books
                Log.d("BookCatalogue", "Books fetched successfully: " + books.size());
                if (books != null && !books.isEmpty()) {
                    bookAdapter = new BookAdapter(books); // Puts the books into the adapter
                    recyclerView.setAdapter(bookAdapter); // Loads into recycler view
                } else {
                    Toast.makeText(BookCatalogue.this, "No books found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e("BookCatalogue", "Error fetching books: " + error);
                Toast.makeText(BookCatalogue.this, "Failed to load data: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

