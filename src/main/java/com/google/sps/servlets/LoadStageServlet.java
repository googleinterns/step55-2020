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
        // Currently ignores the input and just returns a fixed Stage.
        String stageID = "stage1id";

        Stage.Builder stageBuilder = new Stage.Builder(stageID, 1);
        stageBuilder.setKey("mellon");
        stageBuilder.setStartingHint("Look for the people who defy gravity");
        stageBuilder.setStartingLatitude(40.444240);
        stageBuilder.setStartingLongitude(-79.942013);
        ArrayList<String> hints = new ArrayList<String>();
        hints.add("hint1id"); hints.add("hint2id"); hints.add("hint3id"); hints.add("hint4id"); hints.add("hint5id"); hints.add("hint6id");
        stageBuilder.setHints(hints);
        Stage stage = stageBuilder.build();
        
        String json = new Gson().toJson(stage);
        response.getWriter().println(json);
    }
}
