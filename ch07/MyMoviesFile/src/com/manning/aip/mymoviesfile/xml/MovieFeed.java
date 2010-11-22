package com.manning.aip.mymoviesfile.xml;

import com.manning.aip.mymoviesfile.model.Movie;
import com.manning.aip.mymoviesfile.model.MovieSearchResult;

import java.util.ArrayList;

public interface MovieFeed {
   
   ArrayList<MovieSearchResult> search(String name);
   
   Movie get(String tmdbId);
   
}
