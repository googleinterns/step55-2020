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
import com.google.sps.managers.*;

import java.util.ArrayList;
import java.lang.Math;

/**
* Represents only the info that is needed about a game on its page.
*/
public class ReducedGameData {
    private transient DatastoreManager datastoreManager = new DatastoreManager();
    public String gameID = "N/A";
    public String gameName = "N/A";
    public String gameDescription = "N/A";
    public String creatorUsername = "N/A";
    public double difficulty = 2.0;
    public double stars = 2.5;
    public ArrayList<Coordinates> stageLocations = new ArrayList<>();

    /**
    * Creates a ReducedGameData object that corresponds to the inputted Game object.
    * @param game a Game object that this ReducedGameData will refer to.
    */
    public ReducedGameData(Game game) {
        this.gameID = game.getGameID();
        this.gameName = game.getGameName();
        this.gameDescription = game.getGameDescription();
        try {
            this.creatorUsername = datastoreManager.retrieveUser(game.getGameCreator()).getUsername();
        } catch(Exception e) {
            this.creatorUsername = null;
        }
        if(game.getNumDifficultyVotes() != 0) {
            this.difficulty = (double)game.getTotalDifficulty() / (double)game.getNumDifficultyVotes();
        }
        if(game.getNumStarVotes() != 0) {
            this.stars = (double)game.getTotalStars() / (double)game.getNumStarVotes();
        }
        ArrayList<String> stageIDs = game.getStages();
        for(String stageID: stageIDs) {
            try {
                this.stageLocations.add(datastoreManager.retrieveStage(stageID).getStartingLocation());
            } catch(Exception e) {
                this.stageLocations.add(null);
            }
        }
    }
}
