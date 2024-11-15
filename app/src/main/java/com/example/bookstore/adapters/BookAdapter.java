// File: src/main/java/com/example/bookstore/adapters/BookAdapter.java
package com.example.bookstore.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.activities.EditBookActivity; // New activity for editing books
import com.example.bookstore.models.Book;
import com.example.bookstore.repository.BookRepository;
import android.app.Activity;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> bookList;
    private BookRepository bookRepository;
    private Context context;

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
        this.bookRepository = new BookRepository(context.getApplicationContext());
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.priceTextView.setText("$" + book.getPrice());

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditBookActivity.class);
            intent.putExtra("bookId", book.getId()); // Pass the book ID
            ((Activity) context).startActivityForResult(intent, 1);  // Start for result
        });



        // Delete button action
        holder.deleteButton.setOnClickListener(v -> {
            bookRepository.delete(book); // Delete the book from database
            bookList.remove(position); // Remove the item from the list
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, bookList.size());
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, priceTextView;
        Button editButton, deleteButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.bookTitle);
            authorTextView = itemView.findViewById(R.id.bookAuthor);
            priceTextView = itemView.findViewById(R.id.bookPrice);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
