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
import java.util.Objects;
import java.util.Arrays;

@RunWith(PowerMockRunner.class)
public final class CreateGameServletTest {
    private DatastoreManager datastoreManager = new DatastoreManager();
    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserVerifier mockUserVerifier;
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

        mockUserVerifier = mock(UserVerifier.class);
        doNothing().when(mockUserVerifier).build(any(String.class), any(String.class));
        when(mockUserVerifier.getUserID()).thenReturn("abcUserId");

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
        servlet.userVerifier = mockUserVerifier;
        servlet.doPost(request, response);

        String json = responseWriter.toString();
        Type StringType = new TypeToken<String>(){}.getType();
        String id = new Gson().fromJson(json, StringType);

        Game.Builder gamebuilder = new Game.Builder("expectedGameId", "abcGameName");
        gamebuilder.setGameCreator("abcUserId").setGameDescription("abcGameDescription").setStages(new ArrayList<String>(Arrays.asList("stage1id", "stage2id")));
        Game expectedGame = gamebuilder.build();
        Game game = datastoreManager.retrieveGame(id);
        Assert.assertEquals(expectedGame, game);

        Stage.Builder stagebuilder = new Stage.Builder("stage1id", 1);
        stagebuilder.setKey("abc1k").setStartingHint("abc1h").setStartingLocation(new Coordinates(0.0, 0.0));
        Hint stage1hint1 = new Hint.Builder("stage1hint1id", 1).setLocation(new Coordinates(0.0, 0.0)).setText("abc11").build();
        Hint stage1hint2 = new Hint.Builder("stage1hint2id", 2).setLocation(new Coordinates(0.0, 0.0)).setText("abc12").build();
        stagebuilder.setHints(new ArrayList<Hint>(Arrays.asList(stage1hint1, stage1hint2)));
        Stage expectedStage1 = stagebuilder.build();
        Assert.assertEquals(datastoreManager.retrieveStage(game.getStages().get(0)), expectedStage1);

        stagebuilder = new Stage.Builder("stage2id", 2);
        stagebuilder.setKey("abc2k").setStartingHint("abc2h").setStartingLocation(new Coordinates(1.0, 1.0));
        Hint stage2hint1 = new Hint.Builder("stage2hint1id", 1).setLocation(new Coordinates(0.0, 0.0)).setText("abc21").build();
        stagebuilder.setHints(new ArrayList<Hint>(Arrays.asList(stage2hint1)));
        Stage expectedStage2 = stagebuilder.build();
        Assert.assertEquals(datastoreManager.retrieveStage(game.getStages().get(1)), expectedStage2);
    }

    @Test(expected=IOException.class)
    public void testBadIdToken() throws Exception {
        servlet = new CreateGameServlet();
        servlet.doPost(request, response);
    }

    @Test(expected=IOException.class)
    public void testStageListLengthsDontMatch() throws Exception {
        servlet = new CreateGameServlet();
        servlet.userVerifier = mockUserVerifier;
        when(request.getParameter("stageSpawnLocations")).thenReturn("[]");
        servlet.doPost(request, response);
    }
}
