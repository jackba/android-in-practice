package com.manning.aip.mymoviesdatabase.xml;

import com.manning.aip.mymoviesdatabase.model.Movie;
import com.manning.aip.mymoviesdatabase.model.MovieSearchResult;

import java.util.ArrayList;

public interface MovieFeed {

   ArrayList<MovieSearchResult> search(String name);

   Movie get(String tmdbId);

}
