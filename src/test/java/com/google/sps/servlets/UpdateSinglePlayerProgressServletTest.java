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
public final class UpdateSinglePlayerProgressServletTest {
    private DatastoreManager datastoreManager = new DatastoreManager();
    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserVerifier userVerifier;
    private StringWriter responseWriter;
    private UpdateSinglePlayerProgressServlet servlet;

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        request = mock(HttpServletRequest.class);
        when(request.getParameter("idToken")).thenReturn("abcIdToken");
        when(request.getParameter("email")).thenReturn("abcEmail@gmail.com");
        when(request.getParameter("gameID")).thenReturn("gameID");
        when(request.getParameter("location")).thenReturn("{\"latitude\": 0.0,\"longitude\": 0.0}");
        when(request.getParameter("hintsFound")).thenReturn("[1,3,4]");
        when(request.getParameter("stageID")).thenReturn("stageID");

        response = mock(HttpServletResponse.class);

        userVerifier = mock(UserVerifier.class);
        doNothing().when(userVerifier).build(any(String.class), any(String.class));
        when(userVerifier.getUserID()).thenReturn("abcUserId");

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        servlet = new UpdateSinglePlayerProgressServlet();
        servlet.userVerifier = userVerifier;
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testNormalValidProgress() throws Exception {
        SinglePlayerProgress progress = new SinglePlayerProgress.Builder("abcUserId", "gameID").setLocation(new Coordinates(1,1)).setHintsFound(new ArrayList<>()).setStageID("oldStageID").build();
        datastoreManager.createOrReplaceSinglePlayerProgress(progress);

        servlet.doPost(request, response);

        SinglePlayerProgress newProgress = datastoreManager.retrieveSinglePlayerProgress("abcUserId", "gameID");
        Assert.assertTrue(newProgress.getUserID().equals("abcUserId"));
        Assert.assertTrue(newProgress.getGameID().equals("gameID"));
        Assert.assertTrue(newProgress.getLocation().equals(new Coordinates(0,0)));
        ArrayList<Integer> hintsFound = newProgress.getHintsFound();
        Assert.assertTrue(hintsFound.size() == 3);
        // TODO(ldchen): figure out why hintsFound contains Longs
        /*Assert.assertTrue(hintsFound.get(0).equals(new Integer(1)));
        Assert.assertTrue(hintsFound.get(1) == 3);
        Assert.assertTrue(hintsFound.get(3) == 4);*/
        Assert.assertTrue(newProgress.getStageID().equals("stageID"));
    }

    @Test
    public void testFinishedLastStage() throws Exception {
        SinglePlayerProgress progress = new SinglePlayerProgress.Builder("abcUserId", "gameID").setLocation(new Coordinates(1,1)).setHintsFound(new ArrayList<>()).setStageID("oldStage").build();
        datastoreManager.createOrReplaceSinglePlayerProgress(progress);
        when(request.getParameter("stageID")).thenReturn("N/A");

        servlet.doPost(request, response);

        SinglePlayerProgress newProgress = datastoreManager.retrieveSinglePlayerProgress("abcUserId", "gameID");
        Assert.assertTrue(newProgress == null);
    }
}
