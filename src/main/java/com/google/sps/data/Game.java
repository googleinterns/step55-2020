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

// Represents the data of a single Game
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

        public Builder(String gameID, String gameName) {
            this.gameID = gameID;
            this.gameName = gameName;
        }

        public Builder setGameDescription(String val) {
            this.gameDescription = val;
            return this;
        }
        
        public Builder setNumStages(int val) {
            this.numStages = val;
            return this;
        }

        public Builder setStages(ArrayList<String> val) {
            this.stages = val;
            return this;
        }

        public Builder setNumTimesPlayed(int val) {
            this.numTimesPlayed = val;
            return this;
        }

        public Builder setNumTimesFinished(int val) {
            this.numTimesFinished = val;
            return this;
        }

        public Builder setNumStarVotes(int val) {
            this.numStarVotes = val;
            return this;
        }

        public Builder setTotalStars(int val) {
            this.totalStars = val;
            return this;
        }

        public Builder setNumDifficultyVotes(int val) {
            this.numDifficultyVotes = val;
            return this;
        }

        public Builder setTotalDifficulty(int val) {
            this.totalDifficulty = val;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }
    
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
