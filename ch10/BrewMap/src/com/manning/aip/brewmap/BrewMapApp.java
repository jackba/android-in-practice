package com.manning.aip.brewmap;

import android.app.Application;

import com.manning.aip.brewmap.model.BrewLocation;

import java.util.List;

public class BrewMapApp extends Application {

   public static final String PUB_INDEX = "PUB_INDEX";
   
   private List<BrewLocation> brewLocations;

   public List<BrewLocation> getBrewLocations() {
      return this.brewLocations;
   }

   public void setBrewLocations(List<BrewLocation> brewLocations) {
      this.brewLocations = brewLocations;
   }
   
}