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
* Servlet that processes a game once it has been created.
*/
@WebServlet("/create-game-data")
public class CreateGameServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/index.html");
    }

    /**
    * Retrieves the userID of the creator of the game.
    * @param request the HttpServletRequest of the doPost.
    * @return a String representing the userID.
    */
    private String getUserID(HttpServletRequest request) {
        return request.getParameter("userID");
    }

    /**
    * Retrieves the game's name.
    * @param request the HttpServletRequest of the doPost.
    * @return a String representing the name/title of the game.
    */
    private String getName(HttpServletRequest request) {
        return request.getParameter("gameName");
    }

    /**
    * Retrieves the game's description.
    * @param request the HttpServletRequest of the doPost.
    * @return a String representing the description of the game.
    */
    private String getDescription(HttpServletRequest request) {
        return request.getParameter("gameDescription");
    }

    /**
    * Retrieves the number of stages in the game.
    * @param request the HttpServletRequest of the doPost.
    * @return an int representing the number of stages.
    */
    private int getNumStages(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("numStages"));
    }

    /**
    * Retrieves the starting location of each stage.
    * @param request the HttpServletRequest of the doPost.
    * @return an ArrayList, where the ith element is a Coordinates representing
    *         the starting location of the ith stage.
    * @see com.google.sps.data.Coordinates
    */
	private ArrayList<Coordinates> getStageSpawnLocations(HttpServletRequest request) {
        String json = request.getParameter("stageSpawnLocations");
        Type coordinatesListType = new TypeToken<ArrayList<Coordinates>>(){}.getType();
        ArrayList<Coordinates> res = gson.fromJson(json, coordinatesListType);
        return res;
    }

    /**
    * Retrieves the starting hint for each stage.
    * @param request the HttpServletRequest of the doPost.
    * @return an ArrayList, where the ith element is a String representing
    *         the starting hint for the ith stage.
    */
    private ArrayList<String> getStageStarterHints(HttpServletRequest request) {
        String json = request.getParameter("stageStarterHints");
        Type stringListType = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> res = gson.fromJson(json, stringListType);
        return res;
    }

    /**
    * Retrieves the number of hints (excluding the starter hint) in each stage.
    * @param request the HttpServletRequest of the doPost.
    * @return an ArrayList, where the ith element is an int representing
    *         the number of hints in the ith stage.
    */
    private ArrayList<Integer> getNumHints(HttpServletRequest request) {
        String json = request.getParameter("numHints");
        Type integerListType = new TypeToken<ArrayList<Integer>>(){}.getType();
        ArrayList<Integer> res = gson.fromJson(json, integerListType);
        return res;
    }

    /**
    * Retrieves the coordinates of each hint in each stage.
    * @param request the HttpServletRequest of the doPost.
    * @return an ArrayList, where the ith element is a list of
    *         Coordinates of the hints on the ith stage. The jth
    *         element of this list represents the Coordinates of
    *         the jth hint of the ith stage.
    * @see com.google.sps.data.Coordinates
    */
    private ArrayList<ArrayList<Coordinates>> getHintLocations(HttpServletRequest request) {
        String json = request.getParameter("hintLocations");
        Type coordinatesListListType = new TypeToken<ArrayList<ArrayList<Coordinates>>>(){}.getType();
        ArrayList<ArrayList<Coordinates>> res = gson.fromJson(json, coordinatesListListType);
        return res;
    }

    /**
    * Retrieves the text of each hint in each stage.
    * @param request the HttpServletRequest of the doPost.
    * @return an ArrayList, where the ith element is an inner list of
    *         Strings of the hints on the ith stage. The jth
    *         element of this inner list represents the text of
    *         the jth hint of the ith stage.
    */
    private ArrayList<ArrayList<String>> getHintTexts(HttpServletRequest request) {
        String json = request.getParameter("hintTexts");
        Type stringListListType = new TypeToken<ArrayList<ArrayList<String>>>(){}.getType();
        ArrayList<ArrayList<String>> res = gson.fromJson(json, stringListListType);
        return res;
    }
}
