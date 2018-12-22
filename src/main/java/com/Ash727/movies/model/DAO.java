package com.Ash727.movies.model;

import java.util.List;

/*
* The following Class stands for Data Acces Object
* */
public interface DAO {
    boolean add(movieIdea idea); // returns a boolean through a mehtod
    List<movieIdea> findAll(); // returns a list through the find all function
    movieIdea findMovieBySlug(String slug);
}
