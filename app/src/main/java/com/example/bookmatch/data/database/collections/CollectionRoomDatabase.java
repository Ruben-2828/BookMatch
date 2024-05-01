package com.example.bookmatch.data.database.collections;

import static com.example.bookmatch.utils.Constants.COLLECTION_DATABASE_NAME;
import static com.example.bookmatch.utils.Constants.DATABASE_VERSION;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bookmatch.model.Collection;
import com.example.bookmatch.utils.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Collection.class}, version = DATABASE_VERSION)
@TypeConverters({Converters.class})
public abstract class CollectionRoomDatabase extends RoomDatabase {

    public abstract CollectionDao collectionDao();

    private static volatile CollectionRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CollectionRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CollectionRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CollectionRoomDatabase.class, COLLECTION_DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}