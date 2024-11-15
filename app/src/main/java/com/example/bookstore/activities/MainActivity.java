// File: src/main/java/com/example/bookstore/activities/MainActivity.java
package com.example.bookstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.BookAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.repository.BookRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private RecyclerView bookRecyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private List<Book> filteredBookList;
    private BookRepository bookRepository;
    private ExecutorService executorService;
    private SearchView searchView;
    private Spinner sortSpinner;
    private Button viewFavoritesButton;
    private boolean isViewingFavorites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize bookRepository and ExecutorService
        bookRepository = new BookRepository(getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();

        bookRecyclerView = findViewById(R.id.bookRecyclerView);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookList = new ArrayList<>();
        filteredBookList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, filteredBookList);
        bookRecyclerView.setAdapter(bookAdapter);

        searchView = findViewById(R.id.searchView);
        sortSpinner = findViewById(R.id.sortSpinner);
        viewFavoritesButton = findViewById(R.id.viewFavoritesButton);

        // Set up button to toggle between viewing all books and only favorites
        viewFavoritesButton.setOnClickListener(v -> toggleFavoritesView());

        findViewById(R.id.addBookButton).setOnClickListener(v ->
                startActivityForResult(new Intent(MainActivity.this, AddBookActivity.class), 1)
        );

        loadBooks();

        // Set up the SearchView to filter results
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return true;
            }
        });

        // Set up sorting options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortBooks(position); // Sort based on selected option
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadBooks() {
        executorService.execute(() -> {
            List<Book> booksFromDatabase = isViewingFavorites ? bookRepository.getFavoriteBooks() : bookRepository.getAllBooks();
            runOnUiThread(() -> {
                bookList.clear();
                bookList.addAll(booksFromDatabase);
                filteredBookList.clear();
                filteredBookList.addAll(bookList); // Show all books initially
                bookAdapter.notifyDataSetChanged();
            });
        });
    }

    private void filterBooks(String query) {
        filteredBookList.clear();
        if (query.isEmpty()) {
            filteredBookList.addAll(bookList);
        } else {
            for (Book book : bookList) {
                if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredBookList.add(book);
                }
            }
        }
        bookAdapter.notifyDataSetChanged();
    }

    private void sortBooks(int position) {
        switch (position) {
            case 0: // Sort by Title
                Collections.sort(filteredBookList, Comparator.comparing(Book::getTitle));
                break;
            case 1: // Sort by Price (Low to High)
                Collections.sort(filteredBookList, Comparator.comparingDouble(Book::getPrice));
                break;
            case 2: // Sort by Price (High to Low)
                Collections.sort(filteredBookList, (b1, b2) -> Double.compare(b2.getPrice(), b1.getPrice()));
                break;
        }
        bookAdapter.notifyDataSetChanged();
    }

    private void toggleFavoritesView() {
        isViewingFavorites = !isViewingFavorites;
        viewFavoritesButton.setText(isViewingFavorites ? "View All" : "View Favorites");
        loadBooks(); // Reload the books based on the current view mode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadBooks(); // Reload the books after editing or adding a new book
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
