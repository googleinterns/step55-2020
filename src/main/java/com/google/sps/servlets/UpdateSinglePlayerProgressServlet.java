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
* Servlet that updates the current progress of a user in a game.
*/
@WebServlet("/update-singleplayerprogress-data")
public class UpdateSinglePlayerProgressServlet extends HttpServlet {
    UserService userService = UserServiceFactory.getUserService();
    DatastoreManager datastoreManager = new DatastoreManager();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!userService.isUserLoggedIn()) {
            return;
        }
        String userID = userService.getCurrentUser().getUserId();
        String gameID = request.getParameter("gameID");

        SinglePlayerProgress.Builder progressBuilder = new SinglePlayerProgress.Builder(userID, gameID);
        SinglePlayerProgress oldProgress = datastoreManager.retrieveSinglePlayerProgress(userID, gameID);
        progressBuilder.setLocation(oldProgress.getLocation());
        progressBuilder.setHintsFound(oldProgress.getHintsFound());
        progressBuilder.setStageID(oldProgress.getStageID());

        int type = Integer.parseInt(request.getParameter("type"));
        if(type == 0) { // the user's location has changed
            double latitude = Double.parseDouble(request.getParameter("latitude"));
            double longitude = Double.parseDouble(request.getParameter("longitude"));
            progressBuilder.setLocation(new Coordinates(latitude, longitude));
        } else if(type == 1) { // the user has found a hint
            String hintID = request.getParameter("hintID");
            ArrayList<String> oldHints = oldProgress.getHintsFound();
            oldHints.add(hintID);
            progressBuilder.setHintsFound(oldHints);
        } else { // the user has completed a stage
            Game game = datastoreManager.retrieveGame(gameID);
            
            String currentStage = oldProgress.getStageID();
            ArrayList<String> stages = game.getStages();
            // get index of current stage
            for(int i = 0; i < stages.size(); i++) {
                if(stages.get(i).equals(currentStage)) {
                    // set the new stage id to be the next index
                    if(i == stages.size()-1) {
                        // already at last stage
                        progressBuilder.setStageID("N/A");
                    } else {
                        progressBuilder.setStageID(stages.get(i+1));
                    }
                    break;
                }
            }
        }
        datastoreManager.createOrReplaceSinglePlayerProgress(progressBuilder.build());
    }
}
