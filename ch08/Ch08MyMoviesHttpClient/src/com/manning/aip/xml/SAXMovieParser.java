package com.manning.aip.xml;

import java.io.InputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.text.TextUtils;
import android.util.Xml;
import android.util.Xml.Encoding;

import com.manning.aip.Movie;

public class SAXMovieParser extends DefaultHandler {

   private Movie movie = new Movie();

   private boolean parseMovie, parseName, parseRating;

   public static Movie parseMovie(InputStream xml) throws Exception {
      SAXMovieParser parser = new SAXMovieParser();
      Xml.parse(xml, Encoding.UTF_8, parser);
      return parser.getMovie();
   }

   public Movie getMovie() {
      return movie;
   }

   @Override
   public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
      if ("movie".equals(localName)) {
         parseMovie = true;
      } else if ("name".equals(localName)) {
         parseName = true;
      } else if ("rating".equals(localName)) {
         parseRating = true;
      }
   }

   @Override
   public void endElement(String uri, String localName, String qName)
      throws SAXException {
      if ("movie".equals(localName)) {
         parseMovie = false;
      } else if ("name".equals(localName)) {
         parseName = false;
      } else if ("rating".equals(localName)) {
         parseRating = false;
      }
   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      if (!parseMovie) {
         return;
      }
      String text = new String(ch, start, length).trim();
      if (TextUtils.isEmpty(text)) {
         return;
      }

      if (parseName) {
         movie.setTitle(text);
         parseName = false;
      } else if (parseRating) {
         movie.setRating(text);
         parseRating = false;
      }
   }
}
