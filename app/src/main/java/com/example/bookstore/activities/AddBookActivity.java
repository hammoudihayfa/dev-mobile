package com.example.bookstore.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookstore.R;
import com.example.bookstore.models.Book;
import com.example.bookstore.repository.BookRepository;

public class AddBookActivity extends AppCompatActivity {
    private EditText titleEditText, authorEditText, priceEditText;
    private BookRepository bookRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        titleEditText = findViewById(R.id.titleEditText);
        authorEditText = findViewById(R.id.authorEditText);
        priceEditText = findViewById(R.id.priceEditText);

        bookRepository = new BookRepository(getApplication());

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook();
            }
        });
    }

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

        Book book = new Book(title, author, price);
        bookRepository.insert(book);

        Toast.makeText(this, "Book added!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
