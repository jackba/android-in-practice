package com.manning.aip.xml;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.manning.aip.Movie;

public class XmlPullMovieParser {

   private Movie movie;

   private XmlPullParser xpp;

   public static Movie parseMovie(InputStream xml) throws Exception {
      return new XmlPullMovieParser(xml).parse();
   }

   public XmlPullMovieParser(InputStream xml) throws Exception {
      xpp = XmlPullParserFactory.newInstance().newPullParser();
      xpp.setInput(xml, "UTF-8");
   }

   public Movie parse() throws Exception {
      movie = new Movie();

      skipToTag("name");
      movie.setTitle(xpp.nextText());

      skipToTag("rating");
      movie.setRating(xpp.nextText());

      return movie;
   }

   public void skipToTag(String tagName) throws Exception {
      int event = xpp.getEventType();
      while (event != XmlPullParser.END_DOCUMENT
               && !tagName.equals(xpp.getName())) {
         event = xpp.next();
      }
   }
}
