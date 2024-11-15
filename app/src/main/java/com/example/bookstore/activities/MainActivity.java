// File: src/main/java/com/example/bookstore/activities/MainActivity.java
package com.example.bookstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.BookAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private RecyclerView bookRecyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private BookRepository bookRepository;
    private ExecutorService executorService;

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
        bookAdapter = new BookAdapter(this, bookList);
        bookRecyclerView.setAdapter(bookAdapter);

        findViewById(R.id.addBookButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddBookActivity.class), 1);
            }
        });

        loadBooks();
    }

    private void loadBooks() {
        // Run database query on background thread
        executorService.execute(() -> {
            List<Book> booksFromDatabase = bookRepository.getAllBooks();
            if (booksFromDatabase != null && !booksFromDatabase.isEmpty()) {
                runOnUiThread(() -> {
                    bookList.clear();
                    bookList.addAll(booksFromDatabase);
                    bookAdapter.notifyDataSetChanged();
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadBooks(); // Reload the books after editing a book
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
