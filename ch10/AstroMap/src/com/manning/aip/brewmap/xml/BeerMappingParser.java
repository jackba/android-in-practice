package com.manning.aip.brewmap.xml;

import com.manning.aip.brewmap.model.Pub;

import java.util.ArrayList;

public interface BeerMappingParser {

   ArrayList<Pub> parseCity(String city);

   ArrayList<Pub> parseState(String state);

   ArrayList<Pub> parsePiece(String piece);

}
