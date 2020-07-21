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
import static org.mockito.Mockito.*;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.io.IOException;

@RunWith(PowerMockRunner.class)
public final class CreateGameServletTest {
    private DatastoreManager datastoreManager = new DatastoreManager();
    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserVerifier userVerifier;
    private StringWriter responseWriter;
    private CreateGameServlet servlet;

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        request = mock(HttpServletRequest.class);
        when(request.getParameter("idToken")).thenReturn("abcIdToken");
        when(request.getParameter("email")).thenReturn("abcEmail@gmail.com");
        when(request.getParameter("gameName")).thenReturn("abcGameName");
        when(request.getParameter("gameDescription")).thenReturn("abcGameDescription");
        when(request.getParameter("stageKeys")).thenReturn("[\"abc1k\",\"abc2k\"]");
        when(request.getParameter("stageSpawnLocations")).thenReturn("[{\"latitude\": 0.0,\"longitude\": 0.0},{\"latitude\": 1.0,\"longitude\": 1.0}]");
        when(request.getParameter("stageStarterHints")).thenReturn("[\"abc1h\",\"abc2h\"]");
        when(request.getParameter("hintLocations")).thenReturn("[[{\"latitude\": 0.0,\"longitude\": 0.0},{\"latitude\": 0.0,\"longitude\": 0.0}],[{\"latitude\": 0.0,\"longitude\": 0.0}]]");
        when(request.getParameter("hintTexts")).thenReturn("[[\"abc11\",\"abc12\"],[\"abc21\"]]");

        response = mock(HttpServletResponse.class);

        userVerifier = mock(UserVerifier.class);
        doNothing().when(userVerifier).build("abcIdToken", "abcEmail@gmail.com");
        when(userVerifier.getUserID()).thenReturn("abcUserId");

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testValidGame() throws Exception {
        servlet = new CreateGameServlet();
        servlet.userVerifier = userVerifier;
        servlet.doPost(request, response);

        String json = responseWriter.toString();
        Type StringType = new TypeToken<String>(){}.getType();
        String id = new Gson().fromJson(json, StringType);

        Game game = datastoreManager.retrieveGame(id);
        Assert.assertTrue(game.getGameName().equals("abcGameName"));
        Assert.assertTrue(game.getGameCreator().equals("abcUserId"));
        Assert.assertTrue(game.getGameDescription().equals("abcGameDescription"));
        ArrayList<String> stages = game.getStages();
        Assert.assertTrue(stages.size() == 2);
        Stage stage1 = datastoreManager.retrieveStage(stages.get(0));
        Assert.assertTrue(stage1.getStageNumber() == 1);
        Assert.assertTrue(stage1.getKey().equals("abc1k"));
        Assert.assertTrue(stage1.getStartingHint().equals("abc1h"));
        Assert.assertTrue(stage1.getStartingLocation().equals(new Coordinates(0, 0)));
        ArrayList<Hint> hints = stage1.getHints();
        Assert.assertTrue(hints.size() == 2);
        Assert.assertTrue(hints.get(0).getHintNumber() == 1);
        Assert.assertTrue(hints.get(0).getLocation().equals(new Coordinates(0, 0)));
        Assert.assertTrue(hints.get(0).getText().equals("abc11"));
        Assert.assertTrue(hints.get(1).getHintNumber() == 2);
        Assert.assertTrue(hints.get(1).getLocation().equals(new Coordinates(0, 0)));
        Assert.assertTrue(hints.get(1).getText().equals("abc12"));
        Stage stage2 = datastoreManager.retrieveStage(stages.get(1));
        Assert.assertTrue(stage2.getStageNumber() == 2);
        Assert.assertTrue(stage2.getKey().equals("abc2k"));
        Assert.assertTrue(stage2.getStartingHint().equals("abc2h"));
        Assert.assertTrue(stage2.getStartingLocation().equals(new Coordinates(1, 1)));
        hints = stage2.getHints();
        Assert.assertTrue(hints.size() == 1);
        Assert.assertTrue(hints.get(0).getHintNumber() == 1);
        Assert.assertTrue(hints.get(0).getLocation().equals(new Coordinates(0, 0)));
        Assert.assertTrue(hints.get(0).getText().equals("abc21"));
    }

    @Test(expected=IOException.class)
    public void testBadIdToken() throws Exception {
        servlet = new CreateGameServlet();
        servlet.doPost(request, response);
    }

    @Test(expected=IOException.class)
    public void testStageListLengthsDontMatch() throws Exception {
        servlet = new CreateGameServlet();
        servlet.userVerifier = userVerifier;
        when(request.getParameter("stageSpawnLocations")).thenReturn("[]");
        servlet.doPost(request, response);
    }
}
