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

import java.util.ArrayList;

public interface IDataManager {
  /**
  * Creates or Replaces an entity of a single User data in datastore
  * @param user a User variable representing a single instance of a User.
  */
  public void createOrReplaceUser(User user);

  /**
  * Retrieves a single user entity from the datastore.
  * @param userID the unique code used to identify this specific user.
  * @return a user object with the properties specified in the Builder.
  */
  public User retrieveUser(String userID);

  /**
  * Creates or Replaces the static data of a single game in datastore as an entity
  * @param game an Game variable representing a single instance of a Game
  */
  public void createOrReplaceGame(Game game);

  /**
  * Retrieves a single game entity from the datastore.
  * @param gameID the unique code used to identify this specific game.
  * @return a Game object with the properties specified in the Builder.
  */
  public Game retrieveGame(String gameID);
  
  /**
  * Creates the static data of a single stage in datastore as an entity
  * @param stage a Stage variable representing a single instance of a Stage.
  */
  public void createOrReplaceStage(Stage stage);

  /**
  * Retrieves a single stage entity from the datastore.
  * @param stageID the unique code used to identify this specific stage.
  * @return a Stage object with the properties specified in the Builder.
  */
  public Stage retrieveStage(String stageID);

  /**
  * Updates an entity of a single stage data in datastore
  * @param progress a SinglePlayerProgress variable representing a single instance of a SinglePlayerProgress.
  */
  public void createOrReplaceSinglePlayerProgress(SinglePlayerProgress progress);

  /**
  * Retrieves a single progress entity from the datastore.
  * @param userID the unique code used to identify this specific user
  * @param gameID the unique code used to identify this specific game
  * @return a SinglePlayerProgress object with the properties specified in the Builder
  */
  public SinglePlayerProgress retrieveSinglePlayerProgress(String userID, String gameID);
  
  /**
  * Retrieves all Games entity from the datastore
  * @return an ArrayList object with all Game entities with the properties specified in the Builder
  */
  public ArrayList<Game> retrieveAllGames();

  /**
  * Checks to see if an entity has its username property set to a given userName
  * @param userName the userName that is being looked for
  * @return a boolean if the userName exists or not
  */
  public boolean doesUsernameExist(String userName);

  /**
  * Deletes a single progress entity from the datastore.
  * @param userID the unique code used to identify this specific user
  * @param gameID the unique code used to identify this specific game
  */
  public void deleteSinglePlayerProgress(String userID, String gameID);

  /**
  * Retrieves an entity of a single User data in datastore by the username
  * @param username a User variable representing a single instance of a User.
  */
  public User retrieveUserByUsername(String username);

  /**
  * Creates or Replaces the identification of a user in datastore as an entity
  * @param email a String variable representing the user's email address
  * @param id a String variable representing the user's id
  */
  public void createOrReplaceIdentification(String email, String id);

  /**
  * Retrieves an id of a user in datastore by the email address
  * @param email a String variable representing a user's email address
  */
  public String retrieveIdByEmail(String email);
}

