package com.example.bookstore.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.activities.EditBookActivity;
import com.example.bookstore.models.Book;
import com.example.bookstore.repository.BookRepository;
import android.app.Activity;
import android.util.Log;


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

//    @Override
//    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
//        Book book = bookList.get(position);
//
//        // Set book details
//        holder.titleTextView.setText(book.getTitle());
//        holder.authorTextView.setText(book.getAuthor());
//        holder.priceTextView.setText("$" + book.getPrice());
//
//        // Display book image
//        displayBookImage(holder.bookImageView, book.getImageUri());
//
//        // Set favorite icon based on book's favorite status
//        holder.favoriteIcon.setImageResource(book.isFavorite() ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
//
//        // Favorite button action
//        holder.favoriteIcon.setOnClickListener(v -> {
//            boolean newFavoriteStatus = !book.isFavorite();
//            book.setFavorite(newFavoriteStatus); // Toggle favorite status
//            bookRepository.update(book); // Update in the database
//            notifyItemChanged(position); // Refresh the item view
//        });
//
//        // Edit button action
//        holder.editButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, EditBookActivity.class);
//            intent.putExtra("bookId", book.getId()); // Pass the book ID
//            ((Activity) context).startActivityForResult(intent, 1); // Start for result
//        });
//
//        // Delete button action
//        holder.deleteButton.setOnClickListener(v -> {
//            bookRepository.delete(book); // Delete the book from the database
//            bookList.remove(position); // Remove the item from the list
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, bookList.size());
//        });
//    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        // Log the image URI for debugging
        Log.d("HamzaBookAdapter", "Book Image URI at position " + position + ": " + book.getImageUri());

        // Bind the data to the views
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.priceTextView.setText("$" + book.getPrice());

        // Properly bind the image
        if (book.getImageUri() != null && !book.getImageUri().isEmpty()) {
            holder.bookImageView.setImageURI(Uri.parse(book.getImageUri()));
        } else {
            holder.bookImageView.setImageResource(R.drawable.ic_placeholder_image);
        }

        // Set the favorite icon
        holder.favoriteIcon.setImageResource(book.isFavorite() ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);

        // Favorite toggle functionality
        holder.favoriteIcon.setOnClickListener(v -> {
            boolean newFavoriteStatus = !book.isFavorite();
            book.setFavorite(newFavoriteStatus);
            bookRepository.update(book); // Update the database
            notifyItemChanged(position); // Refresh this item only
        });

        // Edit button functionality
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditBookActivity.class);
            intent.putExtra("bookId", book.getId());
            ((Activity) context).startActivityForResult(intent, 1);
        });

        // Delete button functionality
        holder.deleteButton.setOnClickListener(v -> {
            bookRepository.delete(book); // Remove the book from the database
            bookList.remove(position); // Remove from the list
            notifyItemRemoved(position); // Notify the adapter about the removed item
            notifyItemRangeChanged(position, bookList.size());
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // Helper method to display the book image
    private void displayBookImage(ImageView imageView, String imageUri) {
        if (imageUri != null && !imageUri.isEmpty()) {
            imageView.setImageURI(Uri.parse(imageUri));
        } else {
            imageView.setImageResource(R.drawable.ic_placeholder_image); // Placeholder image
        }
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, priceTextView;
        ImageView favoriteIcon, bookImageView; // ImageView for book image
        Button editButton, deleteButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.bookTitle);
            authorTextView = itemView.findViewById(R.id.bookAuthor);
            priceTextView = itemView.findViewById(R.id.bookPrice);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
            bookImageView = itemView.findViewById(R.id.bookImageView); // Initialize book image view
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
