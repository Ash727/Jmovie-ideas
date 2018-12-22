package com.Ash727.movies.model;

import com.github.slugify.Slugify;

import java.util.*;
import java.util.stream.Collectors;

public class    movieIdea {
    private String title;
    private String creator;
    private String slug;
    private Set<String> voters;
    private int num = 5;

    public movieIdea(String title, String createor) {
        this.voters = new HashSet<>();
        this.title = title;
        this.creator = createor;
        // From dependency slugify in build.gradle
        try {
            Slugify slugify = new Slugify(); // sluggify object
            slug = slugify.slugify(title);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }
    public String getSlug() {
        return slug;
    }

    public boolean addVoter(String voterUserName){
       return voters.add(voterUserName);
    }

    public int getVoteCount(){
        return voters.size();
    }
    public List getVoters() {
//        return voters.stream().collect(Collectors.toList());
        // note that this is a copy its not a brand new list just a copy so
        // will not affect the brand new voters


    return new ArrayList<>(voters);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        movieIdea movieIdea = (movieIdea) o;
        return Objects.equals(title, movieIdea.title) &&
                Objects.equals(creator, movieIdea.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, creator);
    }
}
