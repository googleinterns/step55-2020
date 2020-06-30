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
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for creating new tasks. **/
public class DatastoreManager implements DataManager {

  private DatastoreService datastore = DatastoreServiceFactory.getDataStoreService();
  Gson gson = new Gson();
  
  public DataStoreManager() {  
  }

  /**
    * Stores the static data of a single game in datastore as an entity
    * @param game an Game variable representing a single instance of a game.
  */
  public void storeGame(Game game) {
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
    datastore.put(gameEntity);
  }
  
  /**
    * Updates an entity of a single game data in datastore
    * @param game an Game variable representing a single instance of a game.
  */
  public void updateGame(Game game) {
    Key gameEntityKey = KeyFactory.createKey("Game", game.getGameID());
    Entity gameEntity;
    try {
      gameEntity = datastore.get(gameEntityKey);
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
    datastore.put(gameEntity);
  }
  
  /**
    * Retrieves a single game entity from the datastore.
    * @param gameID the unique code used to identify this specific game.
    * @return a Game object with the properties specified in the Builder.
  */
  public Game retrieveGame(String gameID) {
    Key gameEntityKey = KeyFactory.createKey("Game", gameID);
    Entity gameEntity;
    try {
      gameEntity = datastore.get(gameEntityKey);
    }
    catch(Exception e) {
      return;
    }

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
  public void storeStage(Stage stage) {
    Entity stageEntity = new Entity("Stage", stage.getStageID());
    stageEntity.setProperty("stageNumber", stage.getStageNumber());
    stageEntity.setProperty("key", stage.getKey());
    stageEntity.setProperty("startingHint", stage.getStartingHint());
    stageEntity.setProperty("latitude", stage.getStartingLocation().getLatitude());
    stageEntity.setProperty("longitude", stage.getStartingLocation().getLongitude());
    for (Hint hint : stage.getHints()) {
      storeHint(hint);
    }
    ArrayList<String> hints;
    for (Hint hint : stage.getHints()) {
      hints.add(hint.getHintID());
    }
    stageEntity.setProperty("hints", hints);
    datastore.put(stageEntity);
  }
  
  /**
    * Updates an entity of a single stage data in datastore
    * @param stage a Stage variable representing a single instance of a stage.
  */
  public void updateStage(Stage stage) {
    Key stageEntityKey = KeyFactory.createKey("Stage", stage.getStageID());
    Entity stageEntity;
    try {
      stageEntity = datastore.get(stageEntityKey);
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
      updateHint(hint);
    }
    ArrayList<String> hints;
    for (Hint hint : stage.getHints()) {
      hints.add(hint.getHintID());
    }
    stageEntity.setProperty("hints", hints);
    datastore.put(stageEntity);
  }
  
  /**
    * Retrieves a single stage entity from the datastore.
    * @param stageID the unique code used to identify this specific stage.
    * @return a Stage object with the properties specified in the Builder.
  */
  public Game retrieveStage(String stageID) {
    Key stageEntityKey = KeyFactory.createKey("Stage", stageID);
    Entity stageEntity;
    try {
      stageEntity = datastore.get(stageEntityKey);
    }
    catch(Exception e) {
      return;
    }

    int stageNumber = (int) stageEntity.getProperty("stageNumber");
    String key = (String) stageEntity.getProperty("key");
    String startingHint = (String) stageEntity.getProperty("startingHint");
    double latitude = (double) stageEntity.getProperty("latitude");
    double longitude = (double) stageEntity.getProperty("longitude");
    Coordinates startingLocation;
    startingLocation = new Coordinates(latitude, longitude);
    ArrayList<String> hintIDs = stageEntity.getProperty("hints");
    ArrayLIst<Hint> hints = new ArrayList<Hint>();
    for (String hintid : hintIDs) {
      hints.add(retrieveHint(hintid));
    }
  
    Stage.Builder stage = new Stage.Builder(stageID, stageNUmber);
    stage.setKey(key).setStartingHint(startingHint).setStartingLocation(startingLocation).setHints(hints);
    return stage.build();
  }
  
  /**
    * Stores the static data of a single hint in datastore as an entity
    * @param hint a Hint variable representing a single instance of a hint.
  */
  public void storeHint(Hint hint) {
    Entity hintEntity = new Entity("Hint", hint.getHintID());
    hintEntity.setProperty("hintNumber", hint.getHintNumber());
    hintEntity.setProperty("longitude", hint.getLocation().getLongitude());
    hintEntity.setProperty("latitude", hint.getLocation().getLatitude());
    hintEntity.setProperty("text", hint.getText());
    datastore.put(hintEntity);
  }
  
  /**
    * Updates an entity of a single hint data in datastore
    * @param hint a Hint variable representing a single instance of a hint.
  */
  public void updateHint(Hint hint) {
    Key hintEntityKey = KeyFactory.createKey("Hint", hint.getHintID());
    Entity hintEntity;
    try {
      hintEntity = datastore.get(hintEntityKey);
    }
    catch(Exception e) {
      return;
    }
    hintEntity.setProperty("hintNumber", hint.getHintNumber());
    hintEntity.setProperty("longitude", hint.getLocation().getLongitude());
    hintEntity.setProperty("latitude", hint.getLocation().getLatitude());
    hintEntity.setProperty("text", hint.getText());
    datastore.put(hintEntity);
  }
  
  /**
    * Retrieves a single hint entity from the datastore.
    * @param hintID the unique code used to identify this specific hint.
    * @return a Hint object with the properties specified in the Builder.
  */
  public Hint retrieveHint(String hintID) {
    Key hintEntityKey = KeyFactory.createKey("Hint", HintID);
    Entity hintEntity;
    try {
      hintEntity = datastore.get(hintEntityKey);
    }
    catch(Exception e) {
      return;
    }

    int hintNumber = (int) hintEntity.getProperty("hintNumber");
    String text = (String) hintEntity.getProperty("key");
    double latitude = (double) hintEntity.getProperty("latitude");
    double longitude = (double) hintEntity.getProperty("longitude");
    Coordinates startingLocation;
    startingLocation = new Coordinates(latitude, longitude);

    Hint.Builder hint = new Hint.Builder(hint.getHintID, hintNumber);
    hint.setLocation(startingLocation).setText(text);
    return hint.build();
  }

  /**
    * Stores the static data of a single user in datastore as an entity
    * @param user a User variable representing a single instance of a user.
  */
  public void storeUser(User user) {
    Entity userEntity = new Entity("User", user.getUserID());
    userEntity.setProperty("username", user.getUsername());
    userEntity.setProperty("firstname", user.getFirstName());
    userEntity.setProperty("lastname", user.getLastName());
    userEntity.setProperty("gamesCreated", user.getGamesCreated());
    userEntity.setProperty("profilePictureUrl", user.getProfilePictureUrl());
    userEntity.setProperty("numGamesFinished", user.getNumGamesFinished());
    String numbersJson = gson.toJson(user.getGamesCompletedWithTime());
    userEntity.setProperty("gamesCompletedWithTime", numbersJson);
    userEntity.setProperty("numGamesCreated", user.getNumGamesCreated());
    datastore.put(userEntity);
  }
  
  /**
    * Updates an entity of a single user data in datastore
    * @param user a User variable representing a single instance of a user.
  */
  public void updateUser(User user) {
    Key userEntityKey = KeyFactory.createKey("User", user.getUserID());
    Entity userEntity;
    try {
      userEntity = datastore.get(userEntityKey);
    }
    catch(Exception e) {
      return;
    }
      
    userEntity.setProperty("username", user.getUsername());
    userEntity.setProperty("firstname", user.getFirstName());
    userEntity.setProperty("lastname", user.getLastName());
    userEntity.setProperty("gamesCreated", user.getGamesCreated());
    userEntity.setProperty("profilePictureUrl", user.getProfilePictureUrl());
    userEntity.setProperty("numGamesFinished", user.getNumGamesFinished());
    String numbersJson = gson.toJson(user.getGamesCompletedWithTime());
    userEntity.setProperty("gamesCompletedWithTime", numbersJson);
    userEntity.setProperty("numGamesCreated", user.getNumGamesCreated());
    datastore.put(userEntity);
  }
  
  /**
    * Retrieves a single user entity from the datastore.
    * @param userID the unique code used to identify this specific user.
    * @return a user object with the properties specified in the Builder.
  */
  public Game retrieveUser(String userID) {
    Key userEntityKey = KeyFactory.createKey("User", userID);
    Entity userEntity;
    try {
      userEntity = datastore.get(userEntityKey);
    }
    catch(Exception e) {
      return;
    }

    String userName = (int) userEntity.getProperty("stageNumber");
    String firstName = (String) userEntity.getProperty("key");
    String lastName = (String) userEntity.getProperty("startingHint");
    String gamesCreated = (String) userEntity.getProperty("gamesCreated");
    String profilePictureUrl = (String) userEntity.getProperty("profilePictureUrl");
    String gamesCompletedWithTimeJson = (String) userEntity.getProperty("gamesCompletedWithTime");
    Type pairListType = new TypeToken<ArrayList<Pair<String, Long>>>(){}.getType();
    ArrayList<Pair<String, Long>> gamesCompletedWithTime =gson.from.Json(gamesCompletedWithTimeJson, pairListType);
    int numGamesFinished = (int) userEntity.getProperty("numGamesFinished");
    String numGamesCreated = (int) userEntity.getProperty("numGamesCreated");
  
    User.Builder user = new User.Builder(userID).setUsername(userName).setFirstName(firstName).setLastName(lastName);
    user.setProfilePictureUrl(profilePictureUrl).setGamesCreated(numGamesCreated).setNumGamesFinished(numGamesFinished);
    user.setNumGamesCreated(numGamesCreated).setGamesCompletedWithTime(gamesCompletedWithTime);
    return user.build();
  }
}

