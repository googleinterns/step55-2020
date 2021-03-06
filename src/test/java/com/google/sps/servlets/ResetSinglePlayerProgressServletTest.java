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
import java.util.Arrays;

@RunWith(PowerMockRunner.class)
public final class ResetSinglePlayerProgressServletTest {
    private DatastoreManager datastoreManager = new DatastoreManager();
    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserVerifier userVerifier;
    private StringWriter responseWriter;
    private ResetSinglePlayerProgressServlet servlet;

    /**
    * Sets up mocks before each test.
    */
    @Before
    public void setUp() throws Exception {
        helper.setUp();
        request = mock(HttpServletRequest.class);
        when(request.getParameter("idToken")).thenReturn("abcIdToken");
        when(request.getParameter("email")).thenReturn("abcEmail@gmail.com");
        when(request.getParameter("gameID")).thenReturn("gameID");

        response = mock(HttpServletResponse.class);

        userVerifier = mock(UserVerifier.class);
        doNothing().when(userVerifier).build(any(String.class), any(String.class));
        when(userVerifier.getUserID()).thenReturn("abcUserId");

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        servlet = new ResetSinglePlayerProgressServlet();
        servlet.userVerifier = userVerifier;
    }

    /**
    * Removes local datastore after each test.
    */
    @After
    public void tearDown() {
        helper.tearDown();
    }

    /**
    * Tests reseting a progress successfully when there is already a progress object.
    */
    @Test
    public void testReplaceProgress() throws Exception {
        Game game = new Game.Builder("gameID", "gameName").setStages(new ArrayList<String>(Arrays.asList("stage1id"))).build();
        datastoreManager.createOrReplaceGame(game);
        Stage stage = new Stage.Builder("stage1id", 1).setStartingLocation(new Coordinates(0,0)).build();
        datastoreManager.createOrReplaceStage(stage);
        SinglePlayerProgress progress = new SinglePlayerProgress.Builder("abcUserId", "gameID")
                                            .setLocation(new Coordinates(123,123))
                                            .setHintsFound(new ArrayList<Integer>(Arrays.asList(1, 3)))
                                            .setStageID("stage2id")
                                            .build();
        datastoreManager.createOrReplaceSinglePlayerProgress(progress);
        
        servlet.doPost(request, response);

        SinglePlayerProgress newprogress = datastoreManager.retrieveSinglePlayerProgress("abcUserId", "gameID");
        Assert.assertTrue(newprogress.getUserID().equals("abcUserId"));
        Assert.assertTrue(newprogress.getGameID().equals("gameID"));
        Assert.assertTrue(newprogress.getLocation().equals(new Coordinates(0,0)));
        Assert.assertTrue(newprogress.getHintsFound().size() == 0);
        Assert.assertTrue(newprogress.getStageID().equals("stage1id"));
    }

    /**
    * Tests creating a new progress when there is no current progress.
    */
    @Test
    public void testNewProgress() throws Exception {
        Game game = new Game.Builder("gameID", "gameName").setStages(new ArrayList<String>(Arrays.asList("stage1id"))).build();
        datastoreManager.createOrReplaceGame(game);
        Stage stage = new Stage.Builder("stage1id", 1).setStartingLocation(new Coordinates(0,0)).build();
        datastoreManager.createOrReplaceStage(stage);

        servlet.doPost(request, response);

        SinglePlayerProgress newprogress = datastoreManager.retrieveSinglePlayerProgress("abcUserId", "gameID");
        Assert.assertTrue(newprogress.getUserID().equals("abcUserId"));
        Assert.assertTrue(newprogress.getGameID().equals("gameID"));
        Assert.assertTrue(newprogress.getLocation().equals(new Coordinates(0,0)));
        Assert.assertTrue(newprogress.getHintsFound().size() == 0);
        Assert.assertTrue(newprogress.getStageID().equals("stage1id"));
    }
}
