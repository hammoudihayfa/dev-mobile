package com.example.bookstore.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookstore.R;
import com.example.bookstore.models.Book;
import com.example.bookstore.repository.BookRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditBookActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText titleEditText, authorEditText, priceEditText;
    private ImageView bookImageView;
    private Button saveButton;
    private BookRepository bookRepository;
    private ExecutorService executorService;
    private int bookId;
    private boolean isFavorite;
    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        // Initialize views
        titleEditText = findViewById(R.id.editTitleEditText);
        authorEditText = findViewById(R.id.editAuthorEditText);
        priceEditText = findViewById(R.id.editPriceEditText);
        bookImageView = findViewById(R.id.editBookImageView); // Correctly initialize ImageView
        saveButton = findViewById(R.id.saveEditButton);
        ImageView backIcon = findViewById(R.id.backIcon);

        // Back button functionality
        backIcon.setOnClickListener(v -> finish());

        // Initialize repository and executor
        bookRepository = new BookRepository(getApplication());
        executorService = Executors.newSingleThreadExecutor();

        // Get book ID from intent
        bookId = getIntent().getIntExtra("bookId", -1);

        // Load book details asynchronously
        loadBookDetails();

        // Set up image picker
        bookImageView.setOnClickListener(v -> openImagePicker());

        // Save button click listener
        saveButton.setOnClickListener(v -> updateBook());
    }

    private void loadBookDetails() {
        executorService.execute(() -> {
            Book book = bookRepository.getBookById(bookId);
            if (book != null) {
                runOnUiThread(() -> {
                    titleEditText.setText(book.getTitle());
                    authorEditText.setText(book.getAuthor());
                    priceEditText.setText(String.valueOf(book.getPrice()));
                    isFavorite = book.isFavorite();
                    imageUri = book.getImageUri();

                    // Use helper method to set image
                    displayBookImage(bookImageView, imageUri);
                });
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            Uri localImageUri = saveImageToLocalStorage(selectedImageUri);
            if (localImageUri != null) {
                imageUri = localImageUri.toString();
                displayBookImage(bookImageView, imageUri);
            } else {
                Toast.makeText(this, "Failed to save image locally.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri saveImageToLocalStorage(Uri sourceUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            if (inputStream == null) return null;

            File imageFile = new File(getFilesDir(), "book_image_" + System.currentTimeMillis() + ".jpg");
            OutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return Uri.fromFile(imageFile);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateBook() {
        String title = titleEditText.getText().toString().trim();
        String author = authorEditText.getText().toString().trim();
        String priceText = priceEditText.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            Book updatedBook = new Book(bookId, title, author, price, isFavorite, imageUri);
            bookRepository.update(updatedBook);
            runOnUiThread(() -> {
                Toast.makeText(this, "Book updated!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        });
    }

    private void displayBookImage(ImageView imageView, String imageUri) {
        if (imageUri != null && !imageUri.isEmpty()) {
            imageView.setImageURI(Uri.parse(imageUri));
        } else {
            imageView.setImageResource(R.drawable.ic_placeholder_image); // Placeholder image
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
