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
* Servlet that creates a userID.
*/
@WebServlet("/create-userid-data")
public class CreateUserIdServlet extends HttpServlet {
    DatastoreManager datastoreManager = new DatastoreManager();
    UserVerifier userVerifier = new UserVerifier();

    /**
    * Given an id token and an email, creates the user id for this user.
    */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userVerifier.build(request.getParameter("idToken"), request.getParameter("email"));
        String userId = userVerifier.getUserID();
        User currentUser;
        try {
            currentUser = datastoreManager.retrieveUser(userId);
        } catch(Exception e) {
            currentUser = null;
        }

        if (currentUser !=  null) {
            return;
        }
        
        String randomUserName = generateRandomUsername();
        while (datastoreManager.doesUsernameExist(randomUserName)) {
            randomUserName = generateRandomUsername();
        }

        User user = new User.Builder(userId).setUsername(randomUserName).build();
        datastoreManager.createOrReplaceUser(user);   
    }

    private String generateRandomUsername() {
        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz._0123456789";
        int userNameLen = 4 + (int) (Math.random() * 16);
        String result = "";
        for (int i = 0; i < userNameLen; i++) {
            int index = (int)(Math.random() * validCharacters.length());
            char letterToAdd = validCharacters.charAt(index);
            String letterAsString = Character.toString(letterToAdd);
            result = result.concat(letterAsString);
        }
        return result;
    } 
}
