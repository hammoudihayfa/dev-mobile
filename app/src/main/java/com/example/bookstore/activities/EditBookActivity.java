// File: src/main/java/com/example/bookstore/activities/EditBookActivity.java
package com.example.bookstore.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookstore.R;
import com.example.bookstore.models.Book;
import com.example.bookstore.repository.BookRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditBookActivity extends AppCompatActivity {
    private EditText titleEditText, authorEditText, priceEditText;
    private Button saveButton;
    private BookRepository bookRepository;
    private int bookId;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        // Initialize bookRepository and ExecutorService
        bookRepository = new BookRepository(getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();

        titleEditText = findViewById(R.id.editTitleEditText);
        authorEditText = findViewById(R.id.editAuthorEditText);
        priceEditText = findViewById(R.id.editPriceEditText);
        saveButton = findViewById(R.id.saveEditButton);

        bookId = getIntent().getIntExtra("bookId", -1);
        if (bookId == -1) {
            Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load book details asynchronously
        loadBookDetails(bookId);

        saveButton.setOnClickListener(v -> saveChanges());
    }

    private void loadBookDetails(int bookId) {
        executorService.execute(() -> {
            Book book = bookRepository.getBookById(bookId);
            if (book != null) {
                runOnUiThread(() -> {
                    titleEditText.setText(book.getTitle());
                    authorEditText.setText(book.getAuthor());
                    priceEditText.setText(String.valueOf(book.getPrice()));
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void saveChanges() {
        String title = titleEditText.getText().toString().trim();
        String author = authorEditText.getText().toString().trim();
        double price;

        try {
            price = Double.parseDouble(priceEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        Book updatedBook = new Book(bookId, title, author, price);
        executorService.execute(() -> {
            bookRepository.update(updatedBook);
            runOnUiThread(() -> {
                Toast.makeText(this, "Book updated!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);  // Set the result to OK
                finish();
            });
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
