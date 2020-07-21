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
* Servlet that manages retrieving the progress of a user in game.
*/
@WebServlet("/load-singleplayerprogress-data")
public class LoadSinglePlayerProgressServlet extends HttpServlet {
    DatastoreManager datastoreManager = new DatastoreManager();
    UserVerifier userVerifier = new UserVerifier();
    String userID;
    String gameID;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        parseInput(request);
        SinglePlayerProgress res = null;
        if(userID != null) res = datastoreManager.retrieveSinglePlayerProgress(userID, gameID);
        if(res == null) res = getNewGame();
        String json = new Gson().toJson(res);
        response.getWriter().println(json);
    }

    private void parseInput(HttpServletRequest request) throws IOException {
        if(request.getParameterMap().containsKey("idToken") && request.getParameterMap().containsKey("email")) {
            userVerifier.build(request.getParameter("idToken"), request.getParameter("email"));
            userID = userVerifier.getUserID();
        }
        gameID = request.getParameter("gameID");
    }

    private SinglePlayerProgress getNewGame() {
        SinglePlayerProgress.Builder progressBuilder = new SinglePlayerProgress.Builder(userID, gameID);
        Game game = datastoreManager.retrieveGame(gameID);
        Stage firstStage = datastoreManager.retrieveStage(game.getStages().get(0));
        progressBuilder.setLocation(firstStage.getStartingLocation());
        progressBuilder.setHintsFound(new ArrayList<>());
        progressBuilder.setStageID(firstStage.getStageID());
        return progressBuilder.build();
    }
}
