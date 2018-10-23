package io.github.sudhansubarik.moviescentral.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/*
Refer for Room Persistence DB:: https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9
 */

@Dao
public interface DataAccessObject {
    @Insert
    void insertOnlySingleMovie(DbMovies dbMovies);

    @Insert
    void insertMultipleMovies(List<DbMovies> dbMoviesList);

    @Query("SELECT * FROM favorites WHERE movieId = :movieId")
    DbMovies fetchOneMovieByMovieId(int movieId);

    @Query("SELECT * FROM favorites")
    LiveData<List<DbMovies>> fetchAllMovies();

    @Update
    void updateMovie(DbMovies dbMovies);

    @Delete
    void deleteMovie(DbMovies dbMovies);
}
