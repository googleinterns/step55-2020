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
* Servlet that when given a hintID, serves the corresponding Hint.
*/
@WebServlet("/load-hint-data")
public class LoadHintServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String hintID = request.getParameter("hintID");
        Hint hint = null;
        if(hintID.equals("hint1id")) {
            hint = getHint1();
        } else if(hintID.equals("hint2id")) {
            hint = getHint2();
        } else if(hintID.equals("hint3id")) {
            hint = getHint3();
        } else if(hintID.equals("hint4id")) {
            hint = getHint4();
        } else if(hintID.equals("hint5id")) {
            hint = getHint5();
        } else {
            hint = getHint6();
        }
        String json = new Gson().toJson(hint);
        response.getWriter().println(json);
    }

    Hint getHint1() {
        Hint.Builder res = new Hint.Builder("hint1id", 1);
        res.setLocation(new Coordinates(40.444155, -79.942854));
        res.setText("I need a break from all of this dra[m]a.");
        return res.build();
    }

    Hint getHint2() {
        Hint.Builder res = new Hint.Builder("hint2id", 2);
        res.setLocation(new Coordinates(40.443132, -79.943448));
        res.setText("Why is ther[e] so much paint on this?!");
        return res.build();
    }

    Hint getHint3() {
        Hint.Builder res = new Hint.Builder("hint3id", 3);
        res.setLocation(new Coordinates(40.442228, -79.943445));
        res.setText("Face south, then keep following the rightmost path unti[l] you reach a turtle-shaped building.");
        return res.build();
    }

    Hint getHint4() {
        Hint.Builder res = new Hint.Builder("hint4id", 4);
        res.setLocation(new Coordinates(40.442396, -79.945995));
        res.setText("Nada[l] would want to be here.");
        return res.build();
    }

    Hint getHint5() {
        Hint.Builder res = new Hint.Builder("hint5id", 5);
        res.setLocation(new Coordinates(40.442084, -79.942227));
        res.setText("How is this a r[o]tunda with no dome?");
        return res.build();
    }

    Hint getHint6() {
        Hint.Builder res = new Hint.Builder("hint6id", 6);
        res.setLocation(new Coordinates(40.441847, -79.941526));
        res.setText("This is the final hint, [n]ow please enter the key.");
        return res.build();
    }
}
