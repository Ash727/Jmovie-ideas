package com.Ash727.movies.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class simpleMovieIDeaDAO implements DAO {
    private List<movieIdea> ideas;

    public simpleMovieIDeaDAO() {
        this.ideas = new ArrayList<>();
    }

    @Override
    public boolean add(movieIdea idea) {
        return this.ideas.add(idea);
    }

    @Override
    public List<movieIdea> findAll() {
        return new ArrayList<>(ideas); // returns array list of ideas
    }

    @Override
    public movieIdea findMovieBySlug(String slug) {
//
//        Function<String,movieIdea> ball = new  Function<String,movieIdea> (){
//            @Override
//            public movieIdea apply(String s) {
//                movieIdea st = new movieIdea("s","s");
//                return st;
//            }
//        }
          return ideas.stream()
                  .filter(idea->idea.getSlug().equals(slug))
                  .findFirst()
                  .orElseThrow(NotFoundException::new);

    }
}
