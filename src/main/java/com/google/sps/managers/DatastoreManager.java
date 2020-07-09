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

package com.google.sps.managers;
import com.google.sps.data.*;
import com.google.sps.utils.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.Type;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/** Servlet responsible for implementing the functions in the interface. **/
public class DatastoreManager implements IDataManager {

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  Gson gson = new Gson();

  /**
  * Creates or Replaces an entity of a single User data in datastore
  * @param user a User variable representing a single instance of a User.
  */
  public void createOrReplaceUser(User user) {
    Entity userEntity = new Entity("User", user.getUserID());
    userEntity.setProperty("username", user.getUsername());
    userEntity.setProperty("firstname", user.getFirstName());
    userEntity.setProperty("lastname", user.getLastName());
    userEntity.setProperty("gamesCreated", user.getGamesCreated());
    userEntity.setProperty("profilePictureUrl", user.getProfilePictureUrl());
    String numbersJson = gson.toJson(user.getGamesCompletedWithTime());
    userEntity.setProperty("gamesCompletedWithTime", numbersJson);
    datastore.put(userEntity);
  }
  
  /**
  * Retrieves a single user entity from the datastore.
  * @param userID the unique code used to identify this specific user.
  * @return a User object with the properties specified in the Builder.
  */
  public User retrieveUser(String userID) {
    Key userEntityKey = KeyFactory.createKey("User", userID);
    Entity userEntity;
    try {
      userEntity = datastore.get(userEntityKey);
    }
    catch(Exception e) {
      return null;
    }
    String userName = (String) userEntity.getProperty("username");
    String firstName = (String) userEntity.getProperty("firstname");
    String lastName = (String) userEntity.getProperty("lastname");
    ArrayList<String> gamesCreated = (ArrayList<String>) userEntity.getProperty("gamesCreated");
    String profilePictureUrl = (String) userEntity.getProperty("profilePictureUrl");
    String gamesCompletedWithTimeJson = (String) userEntity.getProperty("gamesCompletedWithTime");
    Type pairListType = new TypeToken<ArrayList<Pair<String, Long>>>(){}.getType();
    ArrayList<Pair<String, Long>> gamesCompletedWithTime = gson.fromJson(gamesCompletedWithTimeJson, pairListType);
  
    User.Builder user = new User.Builder(userID).setUsername(userName).setFirstName(firstName).setLastName(lastName);
    user.setProfilePictureUrl(profilePictureUrl).setGamesCreated(gamesCreated);
    user.setGamesCompletedWithTime(gamesCompletedWithTime);
    return user.build();
  }

  /**
  * Creates or Replaces the static data of a single game in datastore as an entity
  * @param game an Game variable representing a single instance of a Game.
  */
  public void createOrReplaceGame(Game game) {
    Entity gameEntity = new Entity("Game", game.getGameID());
    gameEntity.setProperty("gameID", game.getGameID());
    gameEntity.setProperty("gameName", game.getGameName());
    gameEntity.setProperty("gameCreator", game.getGameCreator());
    gameEntity.setProperty("gameDescription", game.getGameDescription());
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
      return null;
    }

    String gameName = (String) gameEntity.getProperty("gameName");
    String gameDescription = (String) gameEntity.getProperty("gameDescription");
    String gameCreator = (String) gameEntity.getProperty("gameCreator");
    ArrayList<String> stages = (ArrayList<String>)gameEntity.getProperty("stages");
    int numTimesPlayed = ((Long)gameEntity.getProperty("numTimesPlayed")).intValue();
    int numTimesFinished = ((Long)gameEntity.getProperty("numTimesFinished")).intValue();
    int numStarVotes = ((Long)gameEntity.getProperty("numStarVotes")).intValue();
    int totalStars = ((Long)gameEntity.getProperty("totalStars")).intValue();
    int numDifficultyVotes = ((Long)gameEntity.getProperty("numDifficultyVotes")).intValue();
    int totalDifficulty = ((Long)gameEntity.getProperty("totalDifficulty")).intValue();
  
    Game.Builder game = new Game.Builder(gameID, gameName).setGameCreator(gameCreator).setStages(stages);
    game.setGameDescription(gameDescription).setNumTimesPlayed(numTimesPlayed).setNumTimesFinished(numTimesFinished);
    game.setNumStarVotes(numStarVotes).setTotalStars(totalStars).setNumDifficultyVotes(numDifficultyVotes).setTotalDifficulty(totalDifficulty);       
    return game.build();
  }

  /**
  * Creates the static data of a single stage in datastore as an entity
  * @param game an Stage variable representing a single instance of a Stage.
  */
  public void createOrReplaceStage(Stage stage) {
    ArrayList<String> hints = new ArrayList<String>();
    Entity stageEntity = new Entity("Stage", stage.getStageID());
    stageEntity.setProperty("stageNumber", stage.getStageNumber());
    stageEntity.setProperty("key", stage.getKey());
    stageEntity.setProperty("startingHint", stage.getStartingHint());
    stageEntity.setProperty("latitude", stage.getStartingLocation().getLatitude());
    stageEntity.setProperty("longitude", stage.getStartingLocation().getLongitude());
    for (Hint hint : stage.getHints()) {
      createOrReplaceHint(hint);
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
  public Stage retrieveStage(String stageID) {
    Key stageEntityKey = KeyFactory.createKey("Stage", stageID);
    Entity stageEntity;
    try {
      stageEntity = datastore.get(stageEntityKey);
    } catch(Exception e) {
      return null;
    }
    int stageNumber = ((Long)stageEntity.getProperty("stageNumber")).intValue();
    String key = (String) stageEntity.getProperty("key");
    String startingHint = (String) stageEntity.getProperty("startingHint");
    double latitude = (double) stageEntity.getProperty("latitude");
    double longitude = (double) stageEntity.getProperty("longitude");
    Coordinates startingLocation;
    startingLocation = new Coordinates(latitude, longitude);
    ArrayList<String> hintIDs = (ArrayList<String>)stageEntity.getProperty("hints");
    ArrayList<Hint> hints = new ArrayList<Hint>();
    for (String hintid : hintIDs) {
      hints.add(retrieveHint(hintid));
    }
  
    Stage.Builder stage = new Stage.Builder(stageID, stageNumber);
    stage.setKey(key).setStartingHint(startingHint).setStartingLocation(startingLocation).setHints(hints);
    return stage.build();
  }

  /**
  * Creates or Replaces an entity of a single progress data in datastore
  * @param stage a Stage variable representing a single instance of a stage.
  */
  public void createOrReplaceSinglePlayerProgress(SinglePlayerProgress progress) {
    long timestamp = System.currentTimeMillis();

    Entity singlePlayerProgressEntity = new Entity("singlePlayerProgress", progress.getUserID());
    singlePlayerProgressEntity.setProperty("userID", progress.getUserID());
    singlePlayerProgressEntity.setProperty("stageID", progress.getStageID());
    singlePlayerProgressEntity.setProperty("gameID", progress.getGameID());
    singlePlayerProgressEntity.setProperty("latitude", progress.getLocation().getLatitude());
    singlePlayerProgressEntity.setProperty("longitude", progress.getLocation().getLongitude());
    singlePlayerProgressEntity.setProperty("hintsFound", progress.getHintsFound());
    datastore.put(singlePlayerProgressEntity);
  }
  
  /**
  * Retrieves a single progress entity from the datastore.
  * @param userID the unique code used to identify this specific user.
  * @param gameID the unique code used to identify this specific game.
  * @return a SinglePlayerProgress object with the properties specified in the Builder.
  */
  public SinglePlayerProgress retrieveSinglePlayerProgress(String userID, String gameID) {
    Filter userIdentification = new FilterPredicate("userID", FilterOperator.EQUAL, userID);
    Filter gameIdentification = new FilterPredicate("gameID", FilterOperator.EQUAL, gameID);
    CompositeFilter identification = CompositeFilterOperator.and(userIdentification, gameIdentification);
    Query query = new Query("singlePlayerProgress").setFilter(identification);

    PreparedQuery results = datastore.prepare(query);
    if (results.countEntities() == 0) {
      return null;
    }
    Entity singlePlayerProgress = results.asSingleEntity();
    
    String stageID = (String) singlePlayerProgress.getProperty("stageID");
    double latitude = (double) singlePlayerProgress.getProperty("latitude");
    double longitude = (double) singlePlayerProgress.getProperty("longitude");
    Coordinates Location;
    Location = new Coordinates(latitude, longitude);
<<<<<<< HEAD
    ArrayList<String> hintsFound = (ArrayList<String>) singlePlayerProgress.getProperty("hintsFound");
=======
    ArrayList<Integer> hintsFound = (ArrayList<Integer>) singlePlayerProgress.getProperty("hintsFound");

>>>>>>> 2fe849ded860733a9910029ccc18a343977c4464
    SinglePlayerProgress.Builder progress = new SinglePlayerProgress.Builder(userID, gameID);
    progress.setLocation(Location).setHintsFound(hintsFound).setStageID(stageID);
    return progress.build();
  }
  
  
  /**
  * Creates or Replaces the static data of a single hint in datastore as an entity
  * @param hint a Hint variable representing a single instance of a hint.
  */
  public void createOrReplaceHint(Hint hint) {
    Entity hintEntity = new Entity("Hint", hint.getHintID());
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
    Key hintEntityKey = KeyFactory.createKey("Hint", hintID);
    Entity hintEntity;
    try {
      hintEntity = datastore.get(hintEntityKey);
    }
    catch(Exception e) {
      return null;
    }
<<<<<<< HEAD

    int hintNumber = ((Long) hintEntity.getProperty("hintNumber")).intValue();
    String text = (String) hintEntity.getProperty("key");
=======
    int hintNumber = ((Long)hintEntity.getProperty("hintNumber")).intValue();
    String text = (String) hintEntity.getProperty("text");
>>>>>>> 2fe849ded860733a9910029ccc18a343977c4464
    double latitude = (double) hintEntity.getProperty("latitude");
    double longitude = (double) hintEntity.getProperty("longitude");
    Coordinates startingLocation;
    startingLocation = new Coordinates(latitude, longitude);

    Hint.Builder hint = new Hint.Builder(hintID, hintNumber);
    hint.setLocation(startingLocation).setText(text);
    return hint.build();
  }

  /**
  * Retrieves all Games entity from the datastore.
  * @return an ArrayList object with all Game entities with the properties specified in the Builder.
  */
  public ArrayList<Game> retrieveAllGames() {
    Query query = new Query("Game");
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Game> gamesList = new ArrayList<>();
    for (Entity gameEntity : results.asIterable()) {
      String gameID = (String) gameEntity.getProperty("gameID");
      String gameName = (String) gameEntity.getProperty("gameName");
      String gameDescription = (String) gameEntity.getProperty("gameDescription");
      String gameCreator = (String) gameEntity.getProperty("gameCreator");
      ArrayList<String> stages = (ArrayList<String>)gameEntity.getProperty("stages");
      int numTimesPlayed = ((Long)gameEntity.getProperty("numTimesPlayed")).intValue();
      int numTimesFinished = ((Long)gameEntity.getProperty("numTimesFinished")).intValue();
      int numStarVotes = ((Long)gameEntity.getProperty("numStarVotes")).intValue();
      int totalStars = ((Long)gameEntity.getProperty("totalStars")).intValue();
      int numDifficultyVotes = ((Long)gameEntity.getProperty("numDifficultyVotes")).intValue();
      int totalDifficulty = ((Long)gameEntity.getProperty("totalDifficulty")).intValue();
    
      Game.Builder game = new Game.Builder(gameID, gameName).setGameCreator(gameCreator).setStages(stages);
      game.setGameDescription(gameDescription).setNumTimesPlayed(numTimesPlayed).setNumTimesFinished(numTimesFinished);
      game.setNumStarVotes(numStarVotes).setTotalStars(totalStars).setNumDifficultyVotes(numDifficultyVotes).setTotalDifficulty(totalDifficulty);       
      gamesList.add(game.build());
    }
    return gamesList;
  }
  
  /**
  * Checks to see if an entity has its username property set to a given userName
  * @param userName the userName that is being looked for
  * @return a boolean if the userName exists or not
  */
  public boolean doesUsernameExist(String userName) {
    Query query = new Query("User").setFilter(new FilterPredicate("username", FilterOperator.EQUAL, userName));
    PreparedQuery pq = datastore.prepare(query);
  
    if (pq.countEntities() == 0) {
      return false;
    }
    return true;
  }
}

