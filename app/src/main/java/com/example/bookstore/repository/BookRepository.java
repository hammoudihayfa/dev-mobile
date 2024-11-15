// File: src/main/java/com/example/bookstore/repository/BookRepository.java
package com.example.bookstore.repository;

import android.content.Context;
import com.example.bookstore.database.BookDao;
import com.example.bookstore.database.BookDatabase;
import com.example.bookstore.models.Book;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookRepository {
    private final BookDao bookDao;
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

    public List<Book> getFavoriteBooks() {
        return bookDao.getFavoriteBooks();  // Call to get favorite books
    }

    public Book getBookById(int bookId) {
        return bookDao.getBookById(bookId);
    }

    public void update(Book book) {
        executorService.execute(() -> bookDao.update(book));
    }

    public void delete(Book book) {
        executorService.execute(() -> bookDao.delete(book));
    }
}
