package com.manning.aip.brewmap.xml;

import com.manning.aip.brewmap.model.BrewLocation;

import java.util.ArrayList;

public interface BeerMappingParser {

   ArrayList<BrewLocation> parseCity(String city);

   ArrayList<BrewLocation> parseState(String state);

   ArrayList<BrewLocation> parsePiece(String piece);
}
