package io.github.sudhansubarik.moviescentral.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/*
 LiveData:: https://medium.com/@guendouz/room-livedata-and-recyclerview-d8e96fb31dfe
 */

@Database(entities = {DbMovies.class}, version = 1, exportSchema = false)
public abstract class FavoritesDatabase extends RoomDatabase {

    private static FavoritesDatabase INSTANCE;

    public abstract DataAccessObject dataAccessObject();

    private static final Object sLock = new Object();

    public static FavoritesDatabase getINSTANCE(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavoritesDatabase.class, "DbMovies.db")
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }
    }
}
