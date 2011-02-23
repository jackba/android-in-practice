package com.manning.aip.brewmap;

import android.app.Application;

import com.manning.aip.brewmap.model.BrewLocation;

import java.util.List;

public class BrewMapApp extends Application {

   private List<BrewLocation> brewLocations;

   public List<BrewLocation> getPubs() {
      return this.brewLocations;
   }

   public void setPubs(List<BrewLocation> brewLocations) {
      this.brewLocations = brewLocations;
   }
   
}