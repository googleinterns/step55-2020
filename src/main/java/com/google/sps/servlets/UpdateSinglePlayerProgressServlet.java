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
* Servlet that updates the current progress of a user in a game.
*/
@WebServlet("/update-singleplayerprogress-data")
public class UpdateSinglePlayerProgressServlet extends HttpServlet {
    UserService userService = UserServiceFactory.getUserService();
    DatastoreManager datastoreManager = new DatastoreManager();
    Gson gson = new Gson();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!userService.isUserLoggedIn()) {
            return;
        }
        String userID = userService.getCurrentUser().getUserId();
        String gameID = getGameID(request);
        Coordinates location = getLocation(request);
        ArrayList<String> hintsFound = getHintsFound(request);
        String stageID = getStageID(request);

        SinglePlayerProgress.Builder progressBuilder = new SinglePlayerProgress.Builder(userID, gameID);
        progressBuilder.setLocation(location);
        progressBuilder.setHintsFound(hintsFound);
        progressBuilder.setStageID(stageID);

        datastoreManager.createOrReplaceSinglePlayerProgress(progressBuilder.build());
    }

    private String getGameID(HttpServletRequest request) {
        return request.getParameter("gameID");
    }

    private Coordinates getLocation(HttpServletRequest request) {
        String json = request.getParameter("location");
        Type coordinatesType = new TypeToken<Coordinates>(){}.getType();
        Coordinates res = gson.fromJson(json, coordinatesType);
        return res;
    }

    private ArrayList<String> getHintsFound(HttpServletRequest request) {
        String json = request.getParameter("hintsFound");
        Type stringListType = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> res = gson.fromJson(json, stringListType);
        return res;
    }

    private String getStageID(HttpServletRequest request) {
        return request.getParameter("stageID");
    }
}
