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
* Servlet that when given a stageID, serves the corresponding Stage.
*/
@WebServlet("/load-stage-data")
public class LoadStageServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stageID = getStageID(request);
        int idx = -1;
        if(stageID.equals("stage1id")) {
            idx = 1;
        } else {
            idx = 2;
        }
        Stage stage = getStage(stageID, idx);
        
        String json = new Gson().toJson(stage);
        response.getWriter().println(json);
    }

    Stage getStage(String stageID, int idx) {
        if(idx == 1) {
            Stage.Builder stageBuilder = new Stage.Builder(stageID, 1);
            stageBuilder.setKey("mellon");
            stageBuilder.setStartingHint("Look for the people who defy gravity");
            stageBuilder.setStartingLocation(new Coordinates(40.444240, -79.942013));
            ArrayList<Hint> hints = new ArrayList<Hint>();
            for(int i = 1; i <= 6; i++) hints.add(getHint(i));
            stageBuilder.setHints(hints);
            return stageBuilder.build();
        } else {
            Stage.Builder stageBuilder = new Stage.Builder(stageID, 2);
            stageBuilder.setKey("test");
            stageBuilder.setStartingHint("testing multiple stages. the key is 'test'");
            stageBuilder.setStartingLocation(new Coordinates(33.748439, -84.415932));
            ArrayList<Hint> hints = new ArrayList<Hint>();
            stageBuilder.setHints(hints);
            return stageBuilder.build();
        }
    }

    Hint getHint(int idx) {
        Hint.Builder res = new Hint.Builder("hint" + String.valueOf(idx) + "id", idx);
        if(idx == 1) {
            res.setLocation(new Coordinates(40.444155, -79.942854));
            res.setText("I need a break from all of this dra[m]a.");
        } else if(idx == 2) {
            res.setLocation(new Coordinates(40.443132, -79.943448));
            res.setText("Why is ther[e] so much paint on this?!");
        } else if(idx == 3) {
            res.setLocation(new Coordinates(40.442228, -79.943445));
            res.setText("Face south, then keep following the rightmost path unti[l] you reach a turtle-shaped building.");
        } else if(idx == 4) {
            res.setLocation(new Coordinates(40.442396, -79.945995));
            res.setText("Nada[l] would want to be here.");
        } else if(idx == 5) {
            res.setLocation(new Coordinates(40.442084, -79.942227));
            res.setText("How is this a r[o]tunda with no dome?");
        } else if(idx == 6) {
            res.setLocation(new Coordinates(40.441847, -79.941526));
            res.setText("This is the final hint, [n]ow please enter the key.");
        }
        return res.build();
    }

    /**
    * Retrieves the stageID.
    * @param request the HttpServletRequest of the doPost.
    * @return a String representing the stageID.
    */
    private String getStageID(HttpServletRequest request) {
        return request.getParameter("stageID");
    }
}
