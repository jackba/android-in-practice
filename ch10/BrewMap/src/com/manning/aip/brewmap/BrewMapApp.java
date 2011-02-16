package com.manning.aip.brewmap;

import android.app.Application;

import com.manning.aip.brewmap.model.Pub;

import java.util.List;

public class BrewMapApp extends Application {

   private List<Pub> pubs;

   public List<Pub> getPubs() {
      return this.pubs;
   }

   public void setPubs(List<Pub> pubs) {
      this.pubs = pubs;
   }
   
}