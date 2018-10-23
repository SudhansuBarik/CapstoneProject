package io.github.sudhansubarik.moviescentral.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.github.sudhansubarik.moviescentral.room.DataAccessObject;
import io.github.sudhansubarik.moviescentral.room.DbMovies;
import io.github.sudhansubarik.moviescentral.room.FavoritesDatabase;

public class MoviesViewModel extends AndroidViewModel {

    private DataAccessObject dataAccessObject;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        dataAccessObject = FavoritesDatabase.getINSTANCE(application).dataAccessObject();
    }

    public LiveData<List<DbMovies>> getAllMovies() {
        return dataAccessObject.fetchAllMovies();
    }

    public void addMovie(DbMovies movie) {
        dataAccessObject.insertOnlySingleMovie(movie);
    }

    public void deleteMovie(DbMovies movie) {
        dataAccessObject.deleteMovie(movie);
    }

    public DbMovies getMovie(int movieId) {
        return dataAccessObject.fetchOneMovieByMovieId(movieId);
    }
}
