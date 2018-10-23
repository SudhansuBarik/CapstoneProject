package io.github.sudhansubarik.moviescentral.utils;

import io.github.sudhansubarik.moviescentral.models.Movie;
import io.github.sudhansubarik.moviescentral.models.MoviesList;
import io.github.sudhansubarik.moviescentral.models.MoviesReviewsList;
import io.github.sudhansubarik.moviescentral.models.MoviesTrailersList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApiService {
    @GET("movie/popular")
    Call<MoviesList> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesList> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<MoviesList> getUpcomingMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MoviesReviewsList> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MoviesTrailersList> getMovieVideos(@Path("id") int id, @Query("api_key") String apiKey);
}
