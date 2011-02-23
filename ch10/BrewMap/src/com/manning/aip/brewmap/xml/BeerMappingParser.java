package com.manning.aip.brewmap.xml;

import com.manning.aip.brewmap.model.BrewLocation;

import java.util.ArrayList;

public interface BeerMappingParser {

   ArrayList<BrewLocation> parseCity(String city);

   ArrayList<BrewLocation> parseState(String state);

   ArrayList<BrewLocation> parsePiece(String piece);
   
   // TODO need a data structure to hold
   /*
   [location] => Array
   (
       [0] => SimpleXMLElement Object
           (
               [imageid] => 67
               [directurl] => http://beermapping.com/maps/reviews/images.php?img=67
               [imageurl] => http://beermapping.com/maps/reviews/images/locations/PieceTaplist.jpg
               [width] => 320
               [height] => 240
               [thumburl] => http://beermapping.com/maps/reviews/images/resizer/image.php/PieceTaplist.jpg?width=150&height=150&cropratio=1:1&quality=90&image=/maps/reviews/images/locations/PieceTaplist.jpg
               [caption] => The Taplist at Piece - June 2007
               [credit] => BeerPrincess
               [crediturl] => http://beermapping.com/maps/reviews/reviewlist.php?uid=26
               [imagedate] => 2007-06-11 10:24:22
               [score] => 0
           )

       [1] => SimpleXMLElement Object
           (
               [imageid] => 1103
               [directurl] => http://beermapping.com/maps/reviews/images.php?img=1103
               [imageurl] => http://beermapping.com/maps/reviews/images/locations/piecefront.jpg
               [width] => 450
               [height] => 600
               [thumburl] => http://beermapping.com/maps/reviews/images/resizer/image.php/piecefront.jpg?width=150&height=150&cropratio=1:1&quality=90&image=/maps/reviews/images/locations/piecefront.jpg
               [caption] => The sign above the front entry
               [credit] => beerinator
               [crediturl] => http://beermapping.com/maps/reviews/reviewlist.php?uid=1
               [imagedate] => 2008-03-14 08:16:09
               [score] => 1
           )
       */
   ///ArrayList<String> parseImages(int locIndex);

}
