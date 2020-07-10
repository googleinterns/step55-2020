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
* Servlet that processes a game once it has been created.
*/
@WebServlet("/create-game-data")
public class CreateGameServlet extends HttpServlet {
    DatastoreManager datastoreManager = new DatastoreManager();
    Gson gson = new Gson();
    String gameID;
    String userID;
    String gameName;
    String gameDescription;
    ArrayList<String> stageKeys;
    ArrayList<Coordinates> stageSpawnLocations;
    ArrayList<String> stageStarterHints;
    ArrayList<ArrayList<Coordinates>> hintLocations;
    ArrayList<ArrayList<String>> hintTexts;

    /**
    * Creates a game with the given fetch parameters.
    */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        parseInput(request);
        if(!isInputValid()) {
            System.out.println("Input json is not valid");
            System.exit(1);
        }

        gameID = IDGenerator.gen();

        Game.Builder gameBuilder = new Game.Builder(gameID, gameName);
        gameBuilder.setGameCreator(userID);
        gameBuilder.setGameDescription(gameDescription);
        ArrayList<String> stages = new ArrayList<>();
        for(int i = 0; i < stageKeys.size(); i++) {
            Stage stage = buildStage(i);
            stages.add(stage.getStageID());
            datastoreManager.createOrReplaceStage(stage);
        }
        gameBuilder.setStages(stages);
        gameBuilder.setNumTimesPlayed(0);
        gameBuilder.setNumTimesFinished(0);
        gameBuilder.setNumStarVotes(0);
        gameBuilder.setTotalStars(0);
        gameBuilder.setNumDifficultyVotes(0);
        gameBuilder.setTotalDifficulty(0);
        Game game = gameBuilder.build();
        datastoreManager.createOrReplaceGame(game);
        System.out.println(gameID);
        response.getWriter().println(gameID);
    }

    /**
    * Creates a Hint object given the stage index and the hint index.
    * stageIndex = 0 corresponds to the 1st stage, and hintIndex = 0
    * corresponds to the 1st hint.
    * @param stageIndex the stage index.
    * @param hintIndex the hint index within the stage.
    * @return a Hint object corresponding to the stage and hint indices.
    */
    Hint buildHint(int stageIndex, int hintIndex) {
        String hintID = IDGenerator.gen();
        Hint.Builder res = new Hint.Builder(hintID, hintIndex+1);
        res.setLocation(hintLocations.get(stageIndex).get(hintIndex));
        res.setText(hintTexts.get(stageIndex).get(hintIndex));
        return res.build();
    }

    /**
    * Creates a Stage object given the stage index.
    * stageIndex = 0 corresponds to the 1st stage.
    * @param stageIndex the stage index.
    * @return a Stage object corresponding to the stage index.
    */
    Stage buildStage(int stageIndex) {
        String stageID = IDGenerator.gen();
        Stage.Builder res = new Stage.Builder(stageID, stageIndex+1);
        res.setKey(stageKeys.get(stageIndex));
        res.setStartingHint(stageStarterHints.get(stageIndex));
        res.setStartingLocation(stageSpawnLocations.get(stageIndex));
        ArrayList<Hint> hints = new ArrayList<>();
        for(int i = 0; i < hintLocations.get(stageIndex).size(); i++) {
            hints.add(buildHint(stageIndex, i));
        }
        res.setHints(hints);
        return res.build();
    }

    /**
    * Parses all the inputs of the request and sets the corresponding variables.
    * @param request the HttpServletRequest of the doPost.
    */
    void parseInput(HttpServletRequest request) {
        userID = getUserID(request);
        gameName = getGameName(request);
        gameDescription = getGameDescription(request);
        stageKeys = getStageKeys(request);
        stageSpawnLocations = getStageSpawnLocations(request);
        stageStarterHints = getStageStarterHints(request);
        hintLocations = getHintLocations(request);
        hintTexts = getHintTexts(request);
    }

    /**
    * Checks that the sizes of stageSpawnLocations, stageStarterHints,
    * hintLocations, and hintTexts all are consistent with one another.
    * @return a boolean marking whether or not the values are consistent.
    */
    boolean isInputValid() {
        int numStages = stageSpawnLocations.size();
        if(stageStarterHints.size() != numStages) return false;
        if(stageKeys.size() != numStages) return false;

        for(int i = 0; i < numStages; i++) {
            int numHints = hintLocations.get(i).size();
            if(hintTexts.get(i).size() != numHints) return false;
        }
        return true;
    }

    /**
    * Retrieves the userID of the creator of the game.
    * @param request the HttpServletRequest of the doPost.
    * @return a String representing the userID.
    */
    private String getUserID(HttpServletRequest request) {
        // TODO(ldchen): actually get the user ID once authentication works.
        return IDGenerator.gen();
    }

    /**
    * Retrieves the game's name.
    * @param request the HttpServletRequest of the doPost.
    * @return a String representing the name/title of the game.
    */
    private String getGameName(HttpServletRequest request) {
        return request.getParameter("gameName");
    }

    /**
    * Retrieves the game's description.
    * @param request the HttpServletRequest of the doPost.
    * @return a String representing the description of the game.
    */
    private String getGameDescription(HttpServletRequest request) {
        return request.getParameter("gameDescription");
    }

    /**
    * Retrieves the key for each stage.
    * @param request the HttpServletRequest of the doPost.
    * @return an ArrayList, where the ith element is a String containing the
    *         key for the ith stage.
    */
    private ArrayList<String> getStageKeys(HttpServletRequest request) {
        String json = request.getParameter("stageKeys");
        Type stringListType = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> res = gson.fromJson(json, stringListType);
        return res;
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
