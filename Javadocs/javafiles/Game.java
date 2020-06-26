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
* Represents the static data of a single game.
*/
public class Game {
    private final String gameID;
    private final String gameName;
    private final String gameDescription;
    private final int numStages;
    private final ArrayList<String> stages;
    private final int numTimesPlayed;
    private final int numTimesFinished;
    private final int numStarVotes;
    private final int totalStars;
    private final int numDifficultyVotes;
    private final int totalDifficulty;

    /**
    * Builder class used to create Game objects.
    */
    public static class Builder {
        // Required parameters
        private final String gameID;
        private final String gameName;

        // Optional paramters - initialized to default values
        private String gameDescription = null;
        private int numStages = -1;
        private ArrayList<String> stages = null;
        private int numTimesPlayed = -1;
        private int numTimesFinished = -1;
        private int numStarVotes = -1;
        private int totalStars = -1;
        private int numDifficultyVotes = -1;
        private int totalDifficulty = -1;

        /**
        * Constructor that sets the game ID and the game name.
        * @param gameID the unique code used to identify this specific game.
        * @param gameName a string representing the game's name.
        */
        public Builder(String gameID, String gameName) {
            this.gameID = gameID;
            this.gameName = gameName;
        }

        /**
        * Sets the game's description, which is displayed on the game's page.
        * @param val a String representing the new description.
        * @return a Builder object with the modified description.
        */
        public Builder setGameDescription(String val) {
            this.gameDescription = val;
            return this;
        }
        
        /**
        * Sets the number of stages in the game.
        * @param val an int representing the number of stages.
        * @return a Builder object with the modified number of stages.
        */
        public Builder setNumStages(int val) {
            this.numStages = val;
            return this;
        }

        /**
        * Specifies which stages are in the game.
        * @param val an ArrayList of String representing the list of stages in the game.
        *            Each stage is represented by its ID.
        * @return a Builder object with the modified list of stages.
        */
        public Builder setStages(ArrayList<String> val) {
            this.stages = val;
            return this;
        }

        /**
        * Sets the number of times the game has been played.
        * @param val an int representing the number of times the game has been played.
        * @return a Builder object with the modified number of times played.
        */
        public Builder setNumTimesPlayed(int val) {
            this.numTimesPlayed = val;
            return this;
        }

        /**
        * Sets the number of times the game has been played to completion.
        * @param val an int representing the number of times this game has been finished.
        * @return a Builder object with the modified number of times finished.
        */
        public Builder setNumTimesFinished(int val) {
            this.numTimesFinished = val;
            return this;
        }

        /**
        * Sets the number of star votes this game has received.
        * @param val an int representing the number of times this game has been voted on.
        * @return a Builder object with the modified number of star votes.
        */
        public Builder setNumStarVotes(int val) {
            this.numStarVotes = val;
            return this;
        }

        /**
        * Sets the total number of stars this game has received.
        * @param val an int representing the total number of stars this game has received.
        * @return a Builder object with the modified total number of stars.
        */
        public Builder setTotalStars(int val) {
            this.totalStars = val;
            return this;
        }

        /**
        * Sets the number of difficulty votes this game has received.
        * @param val an int representing the number of times this game has received a difficulty vote.
        * @return a Builder object with the modified number of difficulty votes.
        */
        public Builder setNumDifficultyVotes(int val) {
            this.numDifficultyVotes = val;
            return this;
        }

        /**
        * Sets the sum of all difficulty votes received by this game (1 = Easy, 2 = Medium, 3 = Hard).
        * @param val an int representing the total sum of difficulty votes.
        * @return a Builder object with the modified total difficulty.
        */
        public Builder setTotalDifficulty(int val) {
            this.totalDifficulty = val;
            return this;
        }

        /**
        * Gets the Game object created by this Builder.
        * @return a Game object with the properties specified in the Builder.
        */
        public Game build() {
            return new Game(this);
        }
    }
    
    /**
    * Constructor that converts a Builder into a Game.
    * @param builder the Builder object to be converted.
    * @see Builder#build()
    */
    private Game(Builder builder) {
        this.gameID = builder.gameID;
        this.gameName = builder.gameName;
        this.gameDescription = builder.gameDescription;
        this.numStages = builder.numStages;
        this.stages = builder.stages;
        this.numTimesPlayed = builder.numTimesPlayed;
        this.numTimesFinished = builder.numTimesFinished;
        this.numStarVotes = builder.numStarVotes;
        this.totalStars = builder.totalStars;
        this.numDifficultyVotes = builder.numDifficultyVotes;
        this.totalDifficulty = builder.totalDifficulty;
    }
}
