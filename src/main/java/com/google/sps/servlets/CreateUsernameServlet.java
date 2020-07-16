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
* Servlet that gets the username and stores it in datastore.
*/
@WebServlet("/create-username-data")
public class CreateUsernameServlet extends HttpServlet {
    DatastoreManager datastoreManager = new DatastoreManager();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("userName");
        boolean doesUsernameExist = datastoreManager.doesUsernameExist(userName);

        if (!doesUsernameExist) {
            UserVerifier userVerifier = new UserVerifier(request.getParameter("idToken"));
            String userId = userVerifier.getUserID();
            User.Builder user = new User.Builder(userId).setUsername(userName);
            User oldUser = datastoreManager.retrieveUser(userId);

            user.setFirstName(oldUser.getFirstName()).setLastName(oldUser.getLastName()).setProfilePictureUrl(oldUser.getProfilePictureUrl());
            user.setGamesCreated(oldUser.getGamesCreated()).setGamesCompletedWithTime(oldUser.getGamesCompletedWithTime());
            datastoreManager.createOrReplaceUser(user.build());

            String json = new Gson().toJson(userName);
            response.getWriter().println(json);
      } else {
            String json = new Gson().toJson(null);
            response.getWriter().println(json);
        }
        response.sendRedirect("/profilePage.html");
    }
}
