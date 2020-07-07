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
import java.lang.Math;

/**
* Represents only the info that is needed about a game from the main page.
*/
public class BasicGameData {
    //TODO(ldchen): delete all of the mocking once datastore is working.
    private int getRandomIntegerBetween(int left, int right) {
        int res = left + (int)(Math.random()*(right-left+1));
        if(res > right) res = right; // happens if Math.random() somehow gives exactly 1
        return res;
    }

    private String getRandomWord() {
        String[] words = {"absorption", "knowledge", "wear", "egg",
                          "befall", "staking", "light", "muddled",
                          "dynamic", "attempt", "accurate", "meal",
                          "trap", "progress", "walk", "muscle"};
        return words[getRandomIntegerBetween(0, words.length-1)];
    }

    private String gameID = "N/A";
    private String gameName = "N/A";
    private String creatorUsername = "N/A";
    private double difficulty = 2.0;
    private double stars = 2.5;
    private ArrayList<Coordinates> stageLocations = new ArrayList<>();

    /**
    * Creates a BasicGameData object that corresponds to the inputted Game object.
    * @param game a Game object that this BasicGameData will refer to.
    */
    public BasicGameData(Game game) {
        this.gameID = game.getGameID();
        this.gameName = game.getGameName();
        this.creatorUsername = getRandomWord() + getRandomWord();
        if(game.getNumDifficultyVotes() != 0) {
            this.difficulty = (double)game.getTotalDifficulty() / (double)game.getNumDifficultyVotes();
        }
        if(game.getNumStarVotes() != 0) {
            this.stars = (double)game.getTotalStars() / (double)game.getNumStarVotes();
        }
        int numStages = 1 + ((int)Math.random() * 5);
        Coordinates gameCenter = Coordinates.getRandomCoordinates();
        for(int i = 0; i < numStages; i++) {
            stageLocations.add(gameCenter.getRandomWithin(0.001));
        }
    }
}
