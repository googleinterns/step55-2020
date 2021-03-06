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
import com.google.sps.data.*;
import com.google.sps.managers.*;
import com.google.sps.utils.*;

import java.util.ArrayList;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Servlet that updates the games feedback.
*/
@WebServlet("/update-feedback-data")
public class UpdateFeedbackServlet extends HttpServlet {
  DatastoreManager datastoreManager = new DatastoreManager();

  @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String gameID = request.getParameter("gameID");
        int difficultyVote = Integer.parseInt(request.getParameter("difficultyVote"));
        int starVote = Integer.parseInt(request.getParameter("starVote"));

        Game game;
        try {
            game = datastoreManager.retrieveGame(gameID);
        } catch(Exception e) {
            game = null;
        }

        int totalNumStarVotes = game.getNumStarVotes();
        int totalStars = game.getTotalStars();
        if (starVote != 0) {
            totalNumStarVotes += 1;
            totalStars += starVote;
        }

        int totalNumDifficultyVotes = game.getNumDifficultyVotes();
        int totalDifficulty = game.getTotalDifficulty();
        if (difficultyVote != 0) {
            totalNumDifficultyVotes += 1;
            totalDifficulty += difficultyVote;
        }

        Game.Builder updateOldGame = new Game.Builder(gameID, game.getGameName());
        updateOldGame = updateOldGame.setGameCreator(game.getGameCreator());
        updateOldGame = updateOldGame.setStages(game.getStages());
        updateOldGame = updateOldGame.setGameDescription(game.getGameDescription());
        updateOldGame = updateOldGame.setNumTimesPlayed(game.getNumTimesPlayed());
        updateOldGame = updateOldGame.setNumTimesFinished(game.getNumTimesFinished());
        updateOldGame = updateOldGame.setNumStarVotes(totalNumStarVotes);
        updateOldGame = updateOldGame.setTotalStars(totalStars);
        updateOldGame = updateOldGame.setNumDifficultyVotes(totalNumDifficultyVotes);
        updateOldGame = updateOldGame.setTotalDifficulty(totalDifficulty);  
        Game updatedGame = updateOldGame.build();     
        datastoreManager.createOrReplaceGame(updatedGame);
    }
}

