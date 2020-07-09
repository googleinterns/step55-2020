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

package com.google.sps.data;

import java.util.ArrayList;

/**
* Represents the static data containing progress a single user has made in a game.
*/
public class SinglePlayerProgress {
    private final String userID;
    private final String gameID;
    private final Coordinates location;
    private final ArrayList<Integer> hintsFound;
    private final String stageID;

    /**
    * Builder class used to create SinglePlayerProgress objects.
    */
    public static class Builder {
        // Required parameters
        private final String userID;
        private final String gameID;

        // Optional parameters - initialized to default values
        private Coordinates location;
        private ArrayList<Integer> hintsFound = new ArrayList<>();
        private ArrayList<String> players = new ArrayList<>();
        private String stageID = "N/A";

        /**
        * Constructor that sets the user ID and the game ID.
        * @param userID the user playing this game.
        * @param gameID the id of the game being played.
        */
        public Builder(String userID, String gameID) {
            this.userID = userID;
            this.gameID = gameID;
        }

        /**
        * Sets the location where the user currently is at.
        * @param location a Coordinates object containing the location of the user.
        * @return a Builder with the modified location.
        */
        public Builder setLocation(Coordinates location) {
            this.location = location;
            return this;
        }

        /**
        * Sets the list of hints found on the current stage so far.
        * @param hintsFound an ArrayList of Integer with the numbers of the hints found so far.
        * @return a Builder with the modified list of found hints.
        */
        public Builder setHintsFound(ArrayList<Integer> hintsFound) {
            this.hintsFound = hintsFound;
            return this;
        }

        /**
        * Sets the ID of the current stage.
        * @param stageID a String containing the stageID of the current stage.
        * @return a Builder with the modified stageID.
        */
        public Builder setStageID(String stageID) {
            this.stageID = stageID;
            return this;
        }

        /**
        * Gets the SinglePlayerProgress object created by this Builder.
        * @return a SinglePlayerProgress object with the properties specified in the Builder.
        */
        public SinglePlayerProgress build() {
            return new SinglePlayerProgress(this);
        }
    }

    /**
    * Constructor that converts a Builder into a SinglePlayerProgress.
    * @param builder the Builder object to be converted.
    * @see Builder#build()
    */
    private SinglePlayerProgress(Builder builder) {
        this.userID = builder.userID;
        this.gameID = builder.gameID;
        this.location = builder.location;
        this.hintsFound = builder.hintsFound;
        this.stageID = builder.stageID;
    }

    /**
    * Retrieves the user ID.
    * @return a String containing the user ID.
    */
    public String getUserID() {
        return this.userID;
    }

    /**
    * Retrieves the ID of the game that is being played.
    * @return a String containing the gameID of the current game.
    */
    public String getGameID() {
        return this.gameID;
    }

    /**
    * Retrieves the location where the user is at.
    * @return a Coordinates object containing the current location.
    */
    public Coordinates getLocation() {
        return this.location;
    }

    /**
    * Retrieves the list of hints found on the current stage so far.
    * @return an ArrayList of Integer with the numbers of the hints found so far.
    */
    public ArrayList<Integer> getHintsFound() {
        return this.hintsFound;
    }

    /**
    * Retrieves the ID of the current stage.
    * @return a String containing the stageID of the current stage.
    */
    public String getStageID() {
        return this.stageID;
    }
}
