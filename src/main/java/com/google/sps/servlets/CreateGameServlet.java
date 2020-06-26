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

/** Takes the created game information and puts the new game in datastore */
@WebServlet("/create-game-data")
public class CreateGameServlet extends HttpServlet {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/index.html");
    }

    private String getName(HttpServletRequest request) {
        return request.getParameter("gameName");
    }

    private String getDescription(HttpServletRequest request) {
        return request.getParameter("gameDescription");
    }

    // Below: TODO once game creation form is done
    private int getNumStages(HttpServletRequest request) {
        return -1;
    }

	private ArrayList<Coordinates> getStageSpawnLocations(HttpServletRequest request) {
        return null;
    }

    private ArrayList<String> getStageStarterHints(HttpServletRequest request) {
        return null;
    }

    private ArrayList<Integer> getNumHints(HttpServletRequest request) {
        return null;
    }

    private ArrayList<ArrayList<Coordinates>> getHintCoordinates(HttpServletRequest request) {
        return null;
    }

    private ArrayList<ArrayList<String>> getHintText(HttpServletRequest request) {
        return null;
    }
}
