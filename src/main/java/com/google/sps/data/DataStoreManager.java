// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for creating new tasks. **/
public class DataStoreManager implements Datamanager {

  private Datastore datastore;

  public DataStoreManager (){  
  }

  /**
    * Stores the static data of a single game in datastore as an entity
    * @param game an Game variable representing a single instance of a game.
  */
  public void storeGame (Game game){
    long timestamp = System.currentTimeMillis();

    Entity gameEntity = new Entity("Game", game.getGameID());
    gameEntity.setProperty("gameName", game.getGameName());
    gameEntity.setProperty("gameCreator", game.getGameCreator());
    gameEntity.setProperty("gameDescription", game.getGameDescription());
    gameEntity.setProperty("numStages", game.getNumStages());
    gameEntity.setProperty("stages", game.getStages());
    gameEntity.setProperty("numTimesPlayed", game.getNumTimesPlayed());
    gameEntity.setProperty("numTimesFinished", game.getNumTimesFinished());
    gameEntity.setProperty("numStarVotes", game.getNumStarVotes());
    gameEntity.setProperty("totalStars", game.getTotalStars());
    gameEntity.setProperty("numDifficultyVotes", game.getNumDifficultyVotes());
    gameEntity.setProperty("totalDifficulty", game.getTotalDifficulty());
    gameEntity.setProperty("timestamp", timestamp);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(gameEntity);
  }
  
  /**
    * Updates an entity of a single game data in datastore
    * @param game an Game variable representing a single instance of a game.
  */
  public void updateGame(Game game){
    long timestamp = System.currentTimeMillis();
    Key gameEntityKey = KeyFactory.createKey("Game", game.getGameID());
    try {
      Entity gameEntity = datastore.get(gameEntityKey);
    }
    catch(Exception e) {
      return;
    }
      
    gameEntity.setProperty("gameName", game.getGameName());
    gameEntity.setProperty("gameCreator", game.getGameCreator());
    gameEntity.setProperty("gameDescription", game.getGameDescription());
    gameEntity.setProperty("numStages", game.getNumStages());
    gameEntity.setProperty("stages", game.getStages());
    gameEntity.setProperty("numTimesPlayed", game.getNumTimesPlayed());
    gameEntity.setProperty("numTimesFinished", game.getNumTimesFinished());
    gameEntity.setProperty("numStarVotes", game.getNumStarVotes());
    gameEntity.setProperty("totalStars", game.getTotalStars());
    gameEntity.setProperty("numDifficultyVotes", game.getNumDifficultyVotes());
    gameEntity.setProperty("totalDifficulty", game.getTotalDifficulty());
    gameEntity.setProperty("timestamp", timestamp);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(gameEntity);
  }
  
  /**
    * Retrieves a single game entity from the datastore.
    * @param gameID the unique code used to identify this specific game.
    * @return a Game object with the properties specified in the Builder.
  */
  public Game retrieveGame(String gameID){
    Key gameEntityKey = KeyFactory.createKey("Game", gameID);
    Entity gameEntity = datastore.get(gameEntityKey);

    String gameName = (String) gameEntity.getProperty("gameName");
    String gameDescription = (String) gameEntity.getProperty("gameDescription");
    String gameCreator = (String) gameEntity.getProperty("gameCreator");
    int numStages = (int)gameEntity.getProperty("numStages");
    ArrayList<String> stages = (ArrayList<String>)gameEntity.getProperty("stages");
    int numTimesPlayed = (int)gameEntity.getProperty("numTimesPlayed");
    int numTimesFinished = (int)gameEntity.getProperty("numTimesFinished");
    int numStarVotes = (int)gameEntity.getProperty("numStarVotes");
    int totalStars = (int)gameEntity.getProperty("totalStars");
    int numDifficultyVotes = (int)gameEntity.getProperty("numDifficultyVotes");
    int totalDifficulty = (int)gameEntity.getProperty("totalDifficulty");
  
    Game.Builder game = new Game.Builder(gameID, gameName).setGameCreator(gameCreator).setNumStages(numStages);
    game.setGameDescription(gameDescription).setNumTimesPlayed(numTimesPlayed).setNumTimesFinished(numTimesFinished);
    game.setNumStarVotes(numStarVotes).setTotalStars(totalStars).setNumDifficultyVotes(numDifficultyVotes).setTotalDifficulty(totalDifficulty);       
    return game.build();
  }

  /**
    * Stores the static data of a single stage in datastore as an entity
    * @param game an Stage variable representing a single instance of a stage.
  */
  public void storeStage(Stage stage){
    Entity stageEntity = new Entity("Stage", stage.getStageID());
    stageEntity.setProperty("stageNumber", stage.getStageNumber());
    stageEntity.setProperty("key", stage.getKey());
    stageEntity.setProperty("startingHint", stage.getStartingHint());
    stageEntity.setProperty("latitude", stage.getStartingLocation().getLatitude());
    stageEntity.setProperty("longitude", stage.getStartingLocation().getLongitude());
    for (Hint hint : stage.getHints())
    {
      storeHints(hint);
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(stageEntity);
  }
  
  /**
    * Updates an entity of a single stage data in datastore
    * @param stage a Stage variable representing a single instance of a stage.
  */
  public void updateStage(Stage stage){
    Key stageEntityKey = KeyFactory.createKey("Stage", stage.getStageID());
    try {
      Entity stageEntity = datastore.get(stageEntityKey);
    }
    catch(Exception e) {
      return;
    }
      
    stageEntity.setProperty("stageNumber", stage.getStageNumber());
    stageEntity.setProperty("key", stage.getKey());
    stageEntity.setProperty("startingHint", stage.getStartingHint());
    stageEntity.setProperty("latitude", stage.getStartingLocation().getLatitude());
    stageEntity.setProperty("longitude", stage.getStartingLocation().getLongitude());
    for (Hint hint : stage.getHints())
    {
      updateHints(hint);
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(stageEntity);
  }
  
  /**
    * Retrieves a single stage entity from the datastore.
    * @param stageID the unique code used to identify this specific stage.
    * @return a Stage object with the properties specified in the Builder.
  */
  public Game retrieveStage(String stageID){
    Key stageEntityKey = KeyFactory.createKey("Stage", stageID);
    Entity stageEntity = datastore.get(stageEntityKey);

    int stageNumber = (int) stageEntity.getProperty("stageNumber");
    String key = (String) stageEntity.getProperty("key");
    String startingHint = (String) stageEntity.getProperty("startingHint");
    double latitude = (double) stageEntity.getProperty("latitude");
    double longitude = (double) stageEntity.getProperty("longitude");
    Coordinates startingLocation;
    startingLocation.Coordinates(latitude, longitude);
    ArrayList<Hint> hints = stageEntity.getProperty("hints");
  
    Stage.Builder stage = new Stage.Builder(stageID, stageNUmber);
    stage.setKey(key).setStartingHint(startingHint).setStartingLocation(startingLocation).setHints(hints);
    return stage.build();
  }
  
  /**
    * Stores the static data of a single hint in datastore as an entity
    * @param hint an Game variable representing a single instance of a hint.
  */
  public void storeHint (Hint hint){
    Entity hintEntity = new Entity("Hint");
    hintEntity.setProperty("hintID", hint.getHintID());
    hintEntity.setProperty("hintNumber", hint.getHintNumber());
    hintEntity.setProperty("longitude", hint.getLocation().getLongitude());
    hintEntity.setProperty("latitude", hint.getLocation().getLatitude());
    hintEntity.setProperty("text", hint.getText());
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(hintEntity);
  }
  
  /**
    * Updates an entity of a single hint data in datastore
    * @param hint a Hint variable representing a single instance of a hint.
  */
  public void updateHint (Hint hint){
    Key hintEntityKey = KeyFactory.createKey("Hint", hint.getHintID());
    try {
      Entity hintEntity = datastore.get(hintEntityKey);
    }
    catch(Exception e) {
      return;
    }

    hintEntity.setProperty("hintNumber", hint.getHintNumber());
    hintEntity.setProperty("longitude", hint.getLocation().getLongitude());
    hintEntity.setProperty("latitude", hint.getLocation().getLatitude());
    hintEntity.setProperty("text", hint.getText());
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(hintEntity);
  }
  
  /**
    * Retrieves a single hint entity from the datastore.
    * @param hintID the unique code used to identify this specific stage.
    * @return a Hint object with the properties specified in the Builder.
  */
  public Game retrieveHint(Hint hintID){
    Key hintEntityKey = KeyFactory.createKey("Hint", HintID);
    Entity hintEntity = datastore.get(hintEntityKey);

    int hintNumber = (int) hintEntity.getProperty("hintNumber");
    String text = (String) hintEntity.getProperty("key");
    double latitude = (double) hintEntity.getProperty("latitude");
    double longitude = (double) hintEntity.getProperty("longitude");
    Coordinates startingLocation;
    startingLocation.Coordinates(latitude, longitude);

    Hint.Builder hint = new Hint.Builder(hint.getHintID, hintNumber);
    hint.setLocation(startingLocation).setText(text);
    return stage.build();
  }
}

