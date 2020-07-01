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

/**
* Servlet that when given a gameID, serves the corresponding Game.
*/
@WebServlet("/load-game-data")
public class LoadGameServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String gameID = getGameID(request);

        Game.Builder gameBuilder = new Game.Builder(gameID, "Demo");
        gameBuilder.setGameCreator("usernameid12345");
        gameBuilder.setGameDescription("Demo game for testing");
        ArrayList<String> stages = new ArrayList<String>();
        stages.add("stage1id");
        stages.add("stage2id");
        gameBuilder.setStages(stages);
        Game game = gameBuilder.build();
        
        String json = new Gson().toJson(game);
        response.getWriter().println(json);
    }
    
    /**
    * Retrieves the gameID.
    * @param request the HttpServletRequest of the doPost.
    * @return a String representing the gameID.
    */
    private String getGameID(HttpServletRequest request) {
        return request.getParameter("gameID");
    }
}
