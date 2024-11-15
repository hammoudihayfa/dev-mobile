package com.example.bookstore.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class AddBookActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText titleEditText, authorEditText, priceEditText;
    private ImageView bookImageView;
    private Button selectImageButton, saveButton;
    private String imageUri; // Ensure this is declared as a String
    private BookRepository bookRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        authorEditText = findViewById(R.id.authorEditText);
        priceEditText = findViewById(R.id.priceEditText);
        bookImageView = findViewById(R.id.bookImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveButton = findViewById(R.id.saveButton);

        // Initialize repository
        bookRepository = new BookRepository(getApplicationContext());

        // Set up button to open image chooser
        selectImageButton.setOnClickListener(v -> openImageChooser());

        // Set up save button to save book details
        saveButton.setOnClickListener(v -> saveBook());
    }

    // Opens the image picker to select an image from the device
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            // Save the image locally and get the URI
            Uri localImageUri = saveImageToLocalStorage(selectedImageUri);
            if (localImageUri != null) {
                imageUri = localImageUri.toString();
                bookImageView.setImageURI(localImageUri); // Display the saved image
            } else {
                Toast.makeText(this, "Failed to save image locally.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri saveImageToLocalStorage(Uri sourceUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            if (inputStream == null) return null;

            // Define a file in internal storage to save the image
            File imageFile = new File(getFilesDir(), "book_image_" + System.currentTimeMillis() + ".jpg");
            OutputStream outputStream = new FileOutputStream(imageFile);

            // Copy data from the source Uri to the new file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close the streams
            outputStream.close();
            inputStream.close();

            // Return the URI of the saved file
            return Uri.fromFile(imageFile);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Save the book details to the database
    private void saveBook() {
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

        // Create a new Book object with the image URI
        Book book = new Book(title, author, price, imageUri);
        bookRepository.insert(book);

        Toast.makeText(this, "Book added!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
