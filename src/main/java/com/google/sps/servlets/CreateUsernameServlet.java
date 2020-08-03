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
    UserVerifier userVerifier = new UserVerifier();

    /**
    * Sets the username of the given user if it's valid.
    */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("userName");
        if(username.length() == 0) {
            throw new IOException("Username must be at least 1 character");
        }
        if(username.length() > 20) {
            throw new IOException("Username cannot be more than 20 characters");
        }
        String pattern = "^[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz._0123456789]*$";
        if(!username.matches(pattern)) {
            throw new IOException("Only letters, digits, underscores, and periods are allowed");
        }

        boolean doesUsernameExist = datastoreManager.doesUsernameExist(username.toLowerCase());
        if (!doesUsernameExist) {
            userVerifier.build(request.getParameter("idToken"), request.getParameter("email"));
            String userId = userVerifier.getUserID();
            User.Builder user = new User.Builder(userId).setUsername(username);
            User oldUser;
            try {
                oldUser = datastoreManager.retrieveUser(userId);
            } catch(Exception e) {
                oldUser = null;
            }

            user.setFirstName(oldUser.getFirstName()).setLastName(oldUser.getLastName()).setProfilePictureUrl(oldUser.getProfilePictureUrl());
            user.setGamesCreated(oldUser.getGamesCreated()).setGamesCompletedWithTime(oldUser.getGamesCompletedWithTime());
            datastoreManager.createOrReplaceUser(user.build());
        }
    }
}
