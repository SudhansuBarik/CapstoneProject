package io.github.sudhansubarik.moviescentral.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/*
 LiveData:: https://medium.com/@guendouz/room-livedata-and-recyclerview-d8e96fb31dfe
 */

@Database(entities = {DbMovies.class}, version = 1)
public abstract class FavoritesDatabase extends RoomDatabase {

    private static final Object sLock = new Object();
    private static FavoritesDatabase INSTANCE;

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

    public abstract DataAccessObject dataAccessObject();
}
