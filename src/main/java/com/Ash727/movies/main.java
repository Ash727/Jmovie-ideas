package com.Ash727.movies;
import com.Ash727.movies.model.DAO;
import com.Ash727.movies.model.NotFoundException;
import com.Ash727.movies.model.movieIdea;
import com.Ash727.movies.model.simpleMovieIDeaDAO;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.*;

import static spark.Spark.*;

public class main {

    private static final String FLASH_MESSAGE_KEY = "FLash Message";

    public class name {

        String Name;
        public  name (String name) {
            this.Name = name;
        }

    }
 
    public static void main(String[] args) {
        // Static files are files that are not change
        // this will be the default folder  if cant be found
        staticFileLocation("public");
        //simple implementation shouldnt be used
        DAO dao = new simpleMovieIDeaDAO();

        //List<Integer> food = new ArrayList<Integer>();
        before((req,res)->{
           if (req.cookie("username")!=null){ // if we have a cookie its not null
               // Set request attribute to username
               req.attribute("username",req.cookie("username"));
           }
        });

        // remember this before middle ware runs before every ideas request
        before("/ideas", (req,res)->{
            if(req.attribute("username")==null){
                setFlashMessage(req,"Please login first.");
               res.redirect("/");
               halt(); // Static method saying hault no more in the processing
           }
        });

        // URI - uniform resource identifier
        get("/hello", (req, res) -> "Hello CLarice");

        get("/", (req, res) ->
        {   Map<String, String> model = new HashMap<>();
            model.put("flashMessage",captureFlashMessage(req));

            model.put("ursername_Cookie",req.attribute("username"));
             return new ModelAndView(model,"index.hbs");
        }, new HandlebarsTemplateEngine());


        post("/sign-in", (req, res) ->
        {
            Map<String, String> model = new HashMap<>();
            // Request is message that arrive to server
            String username = req.queryParams("username"); // Hey server request the parameter of username from
            //  Hey server Set this as the cookie so we can use it in multiple routes
            res.cookie("username", username);
            model.put("username", username);
            return new ModelAndView(model,"Profile.hbs");
        }, new HandlebarsTemplateEngine());

        get("/ideas", (req, res) ->
        {
            Map<String, Object> model = new HashMap<>();
            model.put("ideas", dao.findAll());
            model.put("flashMessage", getFlashMessage(req));
            return new ModelAndView(model,"ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post ("/ideas", (req, res) ->
        {
            // remember its a loop back and forth that starts on either request or  response
            // and picks it up from there
            String movieTitle = req.queryParams("movieTitle");

            String creator = req.attribute("username");


            movieIdea clientIdea = new movieIdea(movieTitle,creator);
            //DAO pushIdea = clientIdea;
            dao.add(clientIdea);
            Map<String, Object> model = new HashMap<>();
            model.put("ideas", dao.findAll());
            // Server respond with redirect
            res.redirect("/ideas"); // redirect the user to different page
            return null;// have to return something so return null

//            return new ModelAndView(model,"ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas/:slug/vote", (req, res)->{
            // req.params pulls offf the slug in the url
            movieIdea movie = dao.findMovieBySlug(req.params("slug"));

            // If this is sucessfull added will be true
            boolean added = movie.addVoter(req.attribute("username"));

            if(added){
                setFlashMessage(req,"Thanks For Your Vote!");
            }
            else
                setFlashMessage(req,"You already Voted ");


            res.redirect("/ideas");
            return null;
        });

        get("/ideas/:slug", (req, res)->{
            // req.params pulls offf the slug in the url
            // remember costructor of movie idea the title gets sluggified and method finds movidea by sluf
            movieIdea selectedMovie = dao.findMovieBySlug(req.params("slug"));

            Map<String, Object> model = new HashMap<>();
            model.put("selectedMovie",dao.findMovieBySlug(req.params("slug")));
//            model.put("voters",selectedMovie.getAllVoters());


            return new ModelAndView(model,"movieDetail.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas/:slug/", (req, res)->{
            // req.params pulls offf the slug in the url
            movieIdea movie = dao.findMovieBySlug(req.params("slug"));
            movie.addVoter(req.attribute("username"));
            res.redirect("/ideas/:slug");
            return null;
        });

        // This exception comes Spark Static
        // exc : is the exception, withinthehandler
        exception(NotFoundException.class, (exc,req,res)->{
            res.status(404);
            HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
            String html = engine.render(new ModelAndView(null, "not-found.hbs"));
            res.body(html);
        });








    }

    private static void setFlashMessage(Request req, String message) {

        req.session().attribute(FLASH_MESSAGE_KEY, message);

    }
    private static String captureFlashMessage(Request req) {

        String message = getFlashMessage(req);
        if(message!=null){
            req.session().removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;

    }

    private static String getFlashMessage (Request req){
        if(req.session(false)==null){
            return null;
        }
        if(!req.session().attributes().contains(FLASH_MESSAGE_KEY)) {
            return null;
        }
        return (String) req.session().attribute(FLASH_MESSAGE_KEY);
    }

}
