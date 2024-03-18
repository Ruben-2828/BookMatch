package com.example.bookmatch.data.database;

import static com.example.bookmatch.utils.Constants.BOOK_DATABASE_NAME;
import static com.example.bookmatch.utils.Constants.DATABASE_VERSION;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bookmatch.model.Book;
import com.example.bookmatch.utils.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Book.class}, version = DATABASE_VERSION)
@TypeConverters({Converters.class})
public abstract class BookRoomDatabase extends RoomDatabase {

    public abstract BookDao bookDao();

    private static volatile BookRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static BookRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BookRoomDatabase.class, BOOK_DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}