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
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.io.IOException;
import java.lang.reflect.Type;
import com.google.appengine.api.datastore.PreparedQuery;
import java.util.ArrayList;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class DatastoreManagerTest {

  DatastoreManager datastoreManager;

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  
  @Before
  public void setUp() {
    helper.setUp();
    datastoreManager = new DatastoreManager();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void createOrReplaceUserAndRetrieveTest() {
    User expected;
    User actual;

    ArrayList<String> gamesCreated = new ArrayList<String>();
    gamesCreated.add("game1");
    gamesCreated.add("game2");
    gamesCreated.add("game3");
    gamesCreated.add("game4");
    
    //Makes use of the User class defined previously to build a single user for testing purposes
    User.Builder user = new User.Builder("0001").setUsername("firstUser").setFirstName("Ade").setLastName("Ademiluyi");
    user.setProfilePictureUrl("images/defaultProfilePicture.jpg").setGamesCreated(gamesCreated);
    user.setGamesCompletedWithTime(new ArrayList<Pair<String, Long>>());
    expected = user.build();

    datastoreManager.createOrReplaceUser(expected);

    try {
      actual = datastoreManager.retrieveUser("0001");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected.getUserID(), actual.getUserID());
    Assert.assertEquals(expected.getUsername(), actual.getUsername());
    Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
    Assert.assertEquals(expected.getLastName(), actual.getLastName());
    Assert.assertEquals(expected.getGamesCreated(), actual.getGamesCreated());
    Assert.assertEquals(expected.getGamesCompletedWithTime(), actual.getGamesCompletedWithTime());

  
    //--- Replaces the information in the previously created user(with the same userID) and test to see if it actually replaces in datastore---//

    ArrayList<String> replacedGamesCreated = new ArrayList<String>();
    replacedGamesCreated.add("game5");
    replacedGamesCreated.add("game6");
    replacedGamesCreated.add("game7");
    replacedGamesCreated.add("game8");
    
    //Makes use of the User class defined previously to build a single user for testing purposes
    User.Builder replacedUser = new User.Builder("0001").setUsername("replacedUser").setFirstName("sara").setLastName("lawrence");
    replacedUser.setProfilePictureUrl("images/defaultProfilePicture.jpg").setGamesCreated(replacedGamesCreated);
    replacedUser.setGamesCompletedWithTime(new ArrayList<Pair<String, Long>>());
    expected = replacedUser.build();

    datastoreManager.createOrReplaceUser(expected);

    try {
      actual = datastoreManager.retrieveUser("0001");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected.getUserID(), actual.getUserID());
    Assert.assertEquals(expected.getUsername(), actual.getUsername());
    Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
    Assert.assertEquals(expected.getLastName(), actual.getLastName());
    Assert.assertEquals(expected.getGamesCreated(), actual.getGamesCreated());
    Assert.assertEquals(expected.getGamesCompletedWithTime(), actual.getGamesCompletedWithTime());
  }

  @Test 
  public void retrieveUserTest() {
    User expected = null;
    User actual;
    try {
      actual = datastoreManager.retrieveUser("0002");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void createOrReplaceGameAndRetrieveTest() {
    Game expected;
    Game actual;

    ArrayList<String> stages = new ArrayList<String>();
    stages.add("stage1id");
    stages.add("stage2id");
    
    //Makes use of the Game class defined previously to build a single game for testing purposes
    Game.Builder game = new Game.Builder("0001", "game1").setGameCreator("Adetowo").setStages(stages);
    game.setGameDescription("For Testing").setNumTimesPlayed(14).setNumTimesFinished(12);
    game.setNumStarVotes(6).setTotalStars(17).setNumDifficultyVotes(6).setTotalDifficulty(6);       
    expected = game.build();
    
    datastoreManager.createOrReplaceGame(expected);
    
    try {
      actual = datastoreManager.retrieveGame("0001");
    } catch(Exception e) {
      actual = null;
    }
    Assert.assertEquals(expected.getGameID(), actual.getGameID());
    Assert.assertEquals(expected.getGameName(), actual.getGameName());
    Assert.assertEquals(expected.getGameCreator(), actual.getGameCreator());
    Assert.assertEquals(expected.getGameDescription(), actual.getGameDescription());
    Assert.assertEquals(expected.getStages(), actual.getStages());
    Assert.assertEquals(expected.getNumTimesPlayed(), actual.getNumTimesPlayed());
    Assert.assertEquals(expected.getNumTimesFinished(), actual.getNumTimesFinished());
    Assert.assertEquals(expected.getNumStarVotes(), actual.getNumStarVotes());
    Assert.assertEquals(expected.getTotalStars(), actual.getTotalStars());
    Assert.assertEquals(expected.getNumDifficultyVotes(), actual.getNumDifficultyVotes());
    Assert.assertEquals(expected.getTotalDifficulty(), actual.getTotalDifficulty());

    //--- Replaces the information in the previously created user(with the same userID) and test to see if it actually replaces in datastore---//
    
    ArrayList<String> replacedStages = new ArrayList<String>();
    replacedStages.add("stage3id");
    replacedStages.add("stage4id");
    
    //Makes use of the Game class defined previously to build a single game for testing purposes
    Game.Builder replacedGgame = new Game.Builder("0001", "game1").setGameCreator("sara").setStages(replacedStages);
    replacedGgame.setGameDescription("For Testing").setNumTimesPlayed(16).setNumTimesFinished(14);
    replacedGgame.setNumStarVotes(7).setTotalStars(16).setNumDifficultyVotes(9).setTotalDifficulty(4);       
    expected = replacedGgame.build();

    datastoreManager.createOrReplaceGame(expected);

    try {
      actual = datastoreManager.retrieveGame("0001");
    } catch(Exception e) {
      actual = null;
    }
    
    Assert.assertEquals(expected.getGameID(), actual.getGameID());
    Assert.assertEquals(expected.getGameID(), actual.getGameID());
    Assert.assertEquals(expected.getGameName(), actual.getGameName());
    Assert.assertEquals(expected.getGameCreator(), actual.getGameCreator());
    Assert.assertEquals(expected.getGameDescription(), actual.getGameDescription());
    Assert.assertEquals(expected.getStages(), actual.getStages());
    Assert.assertEquals(expected.getNumTimesPlayed(), actual.getNumTimesPlayed());
    Assert.assertEquals(expected.getNumTimesFinished(), actual.getNumTimesFinished());
    Assert.assertEquals(expected.getNumStarVotes(), actual.getNumStarVotes());
    Assert.assertEquals(expected.getTotalStars(), actual.getTotalStars());
    Assert.assertEquals(expected.getNumDifficultyVotes(), actual.getNumDifficultyVotes());
    Assert.assertEquals(expected.getTotalDifficulty(), actual.getTotalDifficulty());
  }

  @Test 
  public void retrieveGameTest() {
    Game expected = null;
    Game actual;
    try {
      actual = datastoreManager.retrieveGame("0001");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createOrReplaceStageAndRetrieveTest() {
    Stage expected;
    Stage actual;
    
    //Makes use of the Stage class and it's functions defined previously to build a single stage for testing purposes
    Stage.Builder stage = new Stage.Builder("0001", 4);
    stage.setKey("Test").setStartingHint("Testing").setStartingLocation(new Coordinates()).setHints(new ArrayList<Hint>());
    expected = stage.build();

    datastoreManager.createOrReplaceStage(expected);

    try {
      actual = datastoreManager.retrieveStage("0001");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected.getStageID(), actual.getStageID());
    Assert.assertEquals(expected.getStageNumber(), actual.getStageNumber());
    Assert.assertEquals(expected.getKey(), actual.getKey());
    Assert.assertEquals(expected.getStartingHint(), actual.getStartingHint());
    Assert.assertEquals(expected.getHints(), actual.getHints());

    //--- Replaces the information in the previously created user(with the same userID) and test to see if it actually replaces in datastore---//

    //Makes use of the Stage class and it's functions defined previously to build a single stage for testing purposes
    Stage.Builder replacedStage = new Stage.Builder("0001", 4);
    replacedStage.setKey("repleacedTest").setStartingHint("replacedTesting").setStartingLocation(new Coordinates()).setHints(new ArrayList<Hint>());
    expected = replacedStage.build();

    datastoreManager.createOrReplaceStage(expected);

    try {
      actual = datastoreManager.retrieveStage("0001");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected.getStageID(), actual.getStageID());
    Assert.assertEquals(expected.getStageNumber(), actual.getStageNumber());
    Assert.assertEquals(expected.getKey(), actual.getKey());
    Assert.assertEquals(expected.getStartingHint(), actual.getStartingHint());
    Assert.assertEquals(expected.getHints(), actual.getHints());
  }

  @Test 
  public void retrieveStageTest() {
    Stage expected = null;
    Stage actual;
    try {
      actual = datastoreManager.retrieveStage("0001");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createOrReplaceSinglePlayerProgressAndRetrieveTest() {
    SinglePlayerProgress expected;
    SinglePlayerProgress actual;
    
    //Makes use of the SinglePlayerProgress class defined previously to build a single SinglePlayerProgress for testing purposes
    SinglePlayerProgress.Builder progress = new SinglePlayerProgress.Builder("0000", "0001");
    progress.setLocation(new Coordinates()).setHintsFound(new ArrayList<Integer>()).setStageID("0002");
    expected = progress.build();

    datastoreManager.createOrReplaceSinglePlayerProgress(expected);

    try {
      actual = datastoreManager.retrieveSinglePlayerProgress("0000", "0001");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected.getUserID(), actual.getUserID());
    Assert.assertEquals(expected.getHintsFound(), actual.getHintsFound());
    Assert.assertEquals(expected.getStageID(), actual.getStageID());

    //--- Replaces the information in the previously created user(with the same userID) and test to see if it actually replaces in datastore---//

    //Makes use of the SinglePlayerProgress class defined previously to build a single SinglePlayerProgress for testing purposes
    SinglePlayerProgress.Builder replacedProgress = new SinglePlayerProgress.Builder("0000", "0001");
    replacedProgress.setLocation(new Coordinates()).setHintsFound(new ArrayList<Integer>()).setStageID("0003");
    expected = replacedProgress.build();

    datastoreManager.createOrReplaceSinglePlayerProgress(expected);

    try {
      actual = datastoreManager.retrieveSinglePlayerProgress("0000", "0001");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected.getUserID(), actual.getUserID());
    Assert.assertEquals(expected.getHintsFound(), actual.getHintsFound());
    Assert.assertEquals(expected.getStageID(), actual.getStageID());
  }

  @Test 
  public void retrieveSinglePlayerProgressTest() {
    SinglePlayerProgress expected = null;
    SinglePlayerProgress actual;
    try {
      actual = datastoreManager.retrieveSinglePlayerProgress("0001", "0001");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createOrReplaceHintAndRetrieveTest() {
    Hint expected;
    Hint actual;
    
    //Makes use of the Hint class defined previously to build a single Hint for testing purposes
    Hint.Builder hint = new Hint.Builder("0001", 3);
    hint.setLocation(new Coordinates()).setText("For testing");
    expected = hint.build();

    datastoreManager.createOrReplaceHint(expected);

    try {
      actual = datastoreManager.retrieveHint("0001");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected.getHintID(), actual.getHintID());
    Assert.assertEquals(expected.getText(), actual.getText());

    //--- Replaces the information in the previously created user(with the same userID) and test to see if it actually replaces in datastore---//

    //Makes use of the Hint class defined previously to build a single Hint for testing purposes
    Hint.Builder replacedHint = new Hint.Builder("0001", 3);
    replacedHint.setLocation(new Coordinates()).setText("For testing");
    expected = replacedHint.build();

    datastoreManager.createOrReplaceHint(expected);

    try {
      actual = datastoreManager.retrieveHint("0001");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected.getHintID(), actual.getHintID());
    Assert.assertEquals(expected.getText(), actual.getText());
  }

  @Test 
  public void retrieveHintTest() {
    Hint expected = null;
    Hint actual;
    try {
      actual = datastoreManager.retrieveHint("0000");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void retrieveAllGamesTest() {
    int expected = 10;
    ArrayList<Game> actual = new ArrayList<>();
  
    for(int i = 0; i < 10; i++) {
      datastoreManager.createOrReplaceGame(getRandomGame());
    }

    actual = datastoreManager.retrieveAllGames();

    Assert.assertEquals(expected, actual.size());
  }
  
  @Test
  public void doesUsernameExistTestTrue() {
    User test;
    boolean actual;
    boolean expected = true;
    
    //Makes use of the User class defined previously to build a single user for testing purposes
    User.Builder user = new User.Builder("0001").setUsername("user1").setFirstName("Ade").setLastName("Ademiluyi");
    user.setProfilePictureUrl("images/defaultProfilePicture.jpg").setGamesCreated(new ArrayList<String>());
    user.setGamesCompletedWithTime(new ArrayList<Pair<String, Long>>());
    test = user.build();

    datastoreManager.createOrReplaceUser(test);

    actual = datastoreManager.doesUsernameExist("user1");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void doesUsernameExistTestFalse() {
    boolean actual;
    boolean expected = false;
    actual = datastoreManager.doesUsernameExist("user9");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void deleteSinglePlayerProgressTest() {
    SinglePlayerProgress test1;
    SinglePlayerProgress test2;
    boolean actual = true;
    boolean expected = false;
    
    //Makes use of the SinglePlayerProgress class defined previously to build a single SinglePlayerProgress for testing purposes
    SinglePlayerProgress.Builder progress = new SinglePlayerProgress.Builder("0000", "0001");
    progress.setLocation(new Coordinates()).setHintsFound(new ArrayList<Integer>()).setStageID("0002");
    test1 = progress.build();

    datastoreManager.createOrReplaceSinglePlayerProgress(test1);

    datastoreManager.deleteSinglePlayerProgress("0000", "0001");

    try {
      test2 = datastoreManager.retrieveSinglePlayerProgress("0000", "0001");
    } catch(Exception e) {
      test2 = null;
    }

    if (test2 == null) {
      actual = false;
    }

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void deleteSinglePlayerProgressTest2() {
    SinglePlayerProgress test2;
    boolean expected = false;
    boolean actual = true;
    try {
      test2 = datastoreManager.retrieveSinglePlayerProgress("0007", "0004");
    } catch(Exception e) {
      test2 = null;
    }

    if (test2 == null) {
      actual = false;
    }

    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void retrieveUserByUsernameTest() {
    User expected;
    User actual;

    ArrayList<String> gamesCreated = new ArrayList<String>();
    gamesCreated.add("game1");
    gamesCreated.add("game2");
    gamesCreated.add("game3");
    gamesCreated.add("game4");
    
    //Makes use of the User class defined previously to build a single user for testing purposes
    User.Builder user = new User.Builder("0001").setUsername("user1").setFirstName("Ade").setLastName("Ademiluyi");
    user.setProfilePictureUrl("images/defaultProfilePicture.jpg").setGamesCreated(gamesCreated);
    user.setGamesCompletedWithTime(new ArrayList<Pair<String, Long>>());
    expected = user.build();

    datastoreManager.createOrReplaceUser(expected);

    try {
      actual = datastoreManager.retrieveUserByUsername("user1");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected.getUsername(), actual.getUsername());
    Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
    Assert.assertEquals(expected.getLastName(), actual.getLastName());
    Assert.assertEquals(expected.getProfilePictureUrl(), actual.getProfilePictureUrl());
    Assert.assertEquals(expected.getGamesCreated(), actual.getGamesCreated());
  }

  @Test
  public void retrieveUserByUsernameTest2() {
    User expected = null;
    User actual;
    try {
      actual = datastoreManager.retrieveUserByUsername("user1");
    } catch(Exception e) {
      actual = null;
    }

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createOrReplaceIdentificationTest() {
    String testID = "000-testing-000";
    String testEmail = "ademiluyi@google.com";
    String actual;
    
    datastoreManager.createOrReplaceIdentification(testEmail, testID);
    
    try {
      actual = datastoreManager.retrieveIdByEmail(testEmail);
    } catch(Exception e) {
      actual = null;
    }
    String expected = testID;

    Assert.assertEquals(expected, actual);
  }

  /**
  * Returns a random integer in the interval [left,right].
  * @param left the left endpoint of the interval (inclusive).
  * @param right the right endpoint of the interval (inclusive).
  * @return a random integer in [left,right].
  */
  private int getRandomIntegerBetween(int left, int right) {
    int res = left + (int)(Math.random()*(right-left+1));
    return res;
  }

  /**
  * Returns a random word from the array of words.
  *@return a random word.
  */
  private String getRandomWord() {
    String[] words = {"absorption", "knowledge", "wear", "egg",
                       "befall", "staking", "light", "muddled",
                       "dynamic", "attempt", "accurate", "meal",
                       "trap", "progress", "walk", "muscle"};
    return words[getRandomIntegerBetween(0, words.length-1)];
  }
  
  /**
  * Creates a game with a random id, title, difficulty, and stars.
  * All other fields are left as default.
  * @return a random Game.
  */
  private Game getRandomGame() {
    Game.Builder res = new Game.Builder(IDGenerator.gen(), getRandomWord() + " " + getRandomWord());
    int numStarVotes = getRandomIntegerBetween(0, 10);
    res.setNumStarVotes(numStarVotes);
    res.setTotalStars(getRandomIntegerBetween(numStarVotes, 5*numStarVotes));
    int numDifficultyVotes = getRandomIntegerBetween(0, 10);
    res.setNumDifficultyVotes(numDifficultyVotes);
    res.setTotalDifficulty(getRandomIntegerBetween(numDifficultyVotes, 3*numDifficultyVotes));
    return res.build();
  }
}

