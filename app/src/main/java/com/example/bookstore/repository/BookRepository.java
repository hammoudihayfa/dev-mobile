package com.example.bookstore.repository;
import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.bookstore.database.BookDao;
import com.example.bookstore.database.BookDatabase;
import com.example.bookstore.models.Book;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookRepository {
    private final com.example.bookstore.database.BookDao bookDao;
    private final ExecutorService executorService;

    public BookRepository(Context context) {
        BookDatabase database = BookDatabase.getInstance(context);
        bookDao = database.bookDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Book book) {
        executorService.execute(() -> bookDao.insert(book));
    }

    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    public Book getBookById(int bookId) {
        return bookDao.getBookById(bookId); // Ensure this method is implemented in BookDao
    }


    public void update(Book book) {
        executorService.execute(() -> bookDao.update(book));
    }

    public void delete(Book book) {
        executorService.execute(() -> bookDao.delete(book));
    }
}
