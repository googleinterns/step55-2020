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
* Servlet that checks if the user's desired username is available. If yes,
* a new User entity will be put into datastore. If no, the response will 
* indicate so.
*/
@WebServlet("/create-username-data")
public class CreateUsernameServlet extends HttpServlet {
    MockDatastoreManager datastoreManager = new MockDatastoreManager();
    UserService userService = UserServiceFactory.getUserService();
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String res = null;
        if(!datastoreManager.doesUsernameExist(username)) {
            res = username;

            User.Builder userBuilder = new User.Builder(userService.getCurrentUser().getUserId());
            userBuilder.setUsername(username);
            datastoreManager.createOrReplaceUser(userBuilder.build());
        }
        String json = new Gson().toJson(res);
        response.getWriter().println(json);
    }
}
