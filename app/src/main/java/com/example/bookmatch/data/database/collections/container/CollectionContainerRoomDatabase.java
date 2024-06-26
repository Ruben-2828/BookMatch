package com.example.bookmatch.data.database.collections.container;

import static com.example.bookmatch.utils.Constants.COLLECTION_CONTAINER_DATABASE_NAME;
import static com.example.bookmatch.utils.Constants.DATABASE_VERSION;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bookmatch.model.CollectionContainer;
import com.example.bookmatch.utils.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {CollectionContainer.class}, version = DATABASE_VERSION)
@TypeConverters({Converters.class})
public abstract class CollectionContainerRoomDatabase extends RoomDatabase {

    public abstract CollectionContainerDao collectionDao();

    private static volatile CollectionContainerRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CollectionContainerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CollectionContainerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CollectionContainerRoomDatabase.class, COLLECTION_CONTAINER_DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}