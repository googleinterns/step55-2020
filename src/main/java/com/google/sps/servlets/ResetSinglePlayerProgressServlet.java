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
* Servlet that, when called, resets/creates the progress of a user on a game.
*/
@WebServlet("/reset-singleplayerprogress-data")
public class ResetSinglePlayerProgressServlet extends HttpServlet {
    DatastoreManager datastoreManager = new DatastoreManager();
    UserVerifier userVerifier = new UserVerifier();

    /**
    * If the progress for the given userID and gameID exists, it will be reset to a new game.
    * Otherwise a new progress will be created for the given userID and gameID.
    */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userVerifier.build(request.getParameter("idToken"), request.getParameter("email"));
        String userID = userVerifier.getUserID();
        String gameID = request.getParameter("gameID");
        SinglePlayerProgress.Builder progressBuilder = new SinglePlayerProgress.Builder(userID, gameID);
        Game game;
        try {
            game = datastoreManager.retrieveGame(gameID);
        } catch(Exception e) {
            game = null;
        }
        Stage firstStage;
        try {
            firstStage = datastoreManager.retrieveStage(game.getStages().get(0));
        } catch(Exception e) {
            firstStage = null;
        }
        progressBuilder.setLocation(firstStage.getStartingLocation());
        progressBuilder.setHintsFound(new ArrayList<>());
        progressBuilder.setStageID(firstStage.getStageID());
        datastoreManager.createOrReplaceSinglePlayerProgress(progressBuilder.build());
    }
}
