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
* Represents the static data containing progress a user has made in a game.
*/
public class Progress {
    private final String progressID;
    private final ArrayList<String> hintsFound;
    private final ArrayList<String> players;
    private final String stageID;
    private final String gameID;
    private final boolean gameOver;

    /**
    * Builder class used to create Progress objects.
    */
    public static class Builder {
        // Required parameters
        private final String progressID;

        // Optional parameters - initialized to default values
        private ArrayList<String> hintsFound = null;
        private ArrayList<String> players = null;
        private String stageID = null;
        private String gameID = null;
        private boolean gameOver = false;

        /**
        * Constructor that sets the progress ID.
        * @param progressID the unique code used to identify this specific progress.
        */
        public Builder(String progressID) {
            this.progressID = progressID;
        }

        /**
        * Sets the list of hints found on the current stage so far.
        * @param hintsFound an ArrayList of String with the hintIDs of the hints found so far.
        * @return a Builder with the modified list of found hints.
        */
        public Builder setHintsFound(ArrayList<String> hintsFound) {
            this.hintsFound = hintsFound;
            return this;
        }

        /**
        * Sets the list of players.
        * @param players an ArrayList of String with the userIDs of the people playing together.
        * @return a Builder with the modified list of players.
        */
        public Builder setPlayers(ArrayList<String> players) {
            this.players = players;
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
        * Sets the ID of the game that is being played.
        * @param gameID a String containing the gameID of the current game.
        * @return a Builder with the modified gameID.
        */
        public Builder setGameID(String gameID) {
            this.gameID = gameID;
            return this;
        }

        /**
        * Sets a flag indicating whether the game is over.
        * @param gameOver a boolean containing whether the game is over.
        * @return a Builder with the modified game over flag.
        */
        public Builder setGameOver(boolean gameOver) {
            this.gameOver = gameOver;
            return this;
        }

        /**
        * Gets the Progress object created by this Builder.
        * @return a Progress object with the properties specified in the Builder.
        */
        public Progress build() {
            return new Progress(this);
        }
    }

    /**
    * Constructor that converts a Builder into a Progress.
    * @param builder the Builder object to be converted.
    * @see Builder#build()
    */
    private Progress(Builder builder) {
        this.progressID = builder.progressID;
        this.hintsFound = builder.hintsFound;
        this.players = builder.players;
        this.stageID = builder.stageID;
        this.gameID = builder.gameID;
        this.gameOver = builder.gameOver;
    }

    /**
    * Retrieves the progress ID.
    * @return a String containing the progress ID.
    */
    public String getProgressID() {
        return this.progressID;
    }

    /**
    * Retrieves the list of hints found on the current stage so far.
    * @return an ArrayList of String with the hintIDs of the hints found so far.
    */
    public ArrayList<String> getHintsFound() {
        return this.hintsFound;
    }

    /**
    * Retrieves the list of players.
    * @return an ArrayList of String with the userIDs of the people playing together.
    */
    public ArrayList<String> getPlayers() {
        return this.players;
    }

    /**
    * Retrieves the ID of the current stage.
    * @return a String containing the stageID of the current stage.
    */
    public String getStageID() {
        return this.stageID;
    }

    /**
    * Retrieves the ID of the game that is being played.
    * @return a String containing the gameID of the current game.
    */
    public String getGameID() {
        return this.gameID;
    }

    /**
    * Retrieves a flag indicating whether the game is over.
    * @return a boolean containing whether the game is over.
    */
    public boolean getGameOver() {
        return this.gameOver;
    }
}
