package com.example.bookmatch.data.database.collections.grouping;

import static com.example.bookmatch.utils.Constants.COLLECTION_GROUP_DATABASE_NAME;
import static com.example.bookmatch.utils.Constants.DATABASE_VERSION;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.CollectionContainer;
import com.example.bookmatch.model.CollectionGroup;
import com.example.bookmatch.utils.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {CollectionContainer.class, Book.class, CollectionGroup.class}, version = DATABASE_VERSION)
@TypeConverters({Converters.class})
public abstract class CollectionGroupRoomDatabase extends RoomDatabase {

    //public abstract CollectionContainerDao collectionContainerDao();
    //public abstract BookDao bookDao();
    public abstract CollectionGroupDao collectionGroupDao();

    private static volatile CollectionGroupRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CollectionGroupRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CollectionGroupRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CollectionGroupRoomDatabase.class, COLLECTION_GROUP_DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}