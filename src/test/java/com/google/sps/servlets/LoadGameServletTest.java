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
import java.util.Arrays;
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
public final class LoadGameServletTest {
    private DatastoreManager datastoreManager = new DatastoreManager();
    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;
    private LoadGameServlet servlet;

    /**
    * Sets up mocks before each test.
    */
    @Before
    public void setUp() throws Exception {
        helper.setUp();
        request = mock(HttpServletRequest.class);
        when(request.getParameter("gameID")).thenReturn("abcGameId");

        response = mock(HttpServletResponse.class);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        servlet = new LoadGameServlet();
    }

    /**
    * Gets rid of local datastore after each test.
    */
    @After
    public void tearDown() {
        helper.tearDown();
    }

    /**
    * Tests that the game information is correctly loaded.
    */
    @Test
    public void testLoadExisitingGame() throws Exception {
        Game.Builder gamebuilder = new Game.Builder("abcGameId", "abcGameName");
        gamebuilder.setGameCreator("abcUserId").setGameDescription("abcGameDescription").setStages(new ArrayList<String>(Arrays.asList("stage1id", "stage2id")));
        gamebuilder.setNumTimesPlayed(10).setNumTimesFinished(5).setNumStarVotes(12).setTotalStars(48).setNumDifficultyVotes(12).setTotalDifficulty(24);
        Game expectedGame = gamebuilder.build();
        datastoreManager.createOrReplaceGame(expectedGame);

        Stage.Builder stagebuilder = new Stage.Builder("stage1id", 1);
        stagebuilder.setKey("abc1k").setStartingHint("abc1h").setStartingLocation(new Coordinates(0.0, 0.0));
        Hint stage1hint1 = new Hint.Builder("stage1hint1id", 1).setLocation(new Coordinates(0.0, 0.0)).setText("abc11").build();
        Hint stage1hint2 = new Hint.Builder("stage1hint2id", 2).setLocation(new Coordinates(0.0, 0.0)).setText("abc12").build();
        stagebuilder.setHints(new ArrayList<Hint>(Arrays.asList(stage1hint1, stage1hint2)));
        Stage expectedStage1 = stagebuilder.build();
        datastoreManager.createOrReplaceStage(expectedStage1);

        stagebuilder = new Stage.Builder("stage2id", 2);
        stagebuilder.setKey("abc2k").setStartingHint("abc2h").setStartingLocation(new Coordinates(1.0, 1.0));
        Hint stage2hint1 = new Hint.Builder("stage2hint1id", 1).setLocation(new Coordinates(0.0, 0.0)).setText("abc21").build();
        stagebuilder.setHints(new ArrayList<Hint>(Arrays.asList(stage2hint1)));
        Stage expectedStage2 = stagebuilder.build();
        datastoreManager.createOrReplaceStage(expectedStage2);

        servlet.doPost(request, response);
        String json = responseWriter.toString();
        Type GameType = new TypeToken<Game>(){}.getType();
        Game res = new Gson().fromJson(json, GameType);
        
        Assert.assertEquals(res, expectedGame);
        Assert.assertEquals(datastoreManager.retrieveStage(res.getStages().get(0)), expectedStage1);
        Assert.assertEquals(datastoreManager.retrieveStage(res.getStages().get(1)), expectedStage2);
    }
}
