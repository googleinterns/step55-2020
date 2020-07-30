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
public final class CreateUsernameServletTest {
    private DatastoreManager datastoreManager = new DatastoreManager();
    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserVerifier userVerifier;
    private StringWriter responseWriter;
    private CreateUsernameServlet servlet;

    /**
    * Sets up mocks before each test.
    */
    @Before
    public void setUp() throws Exception {
        helper.setUp();
        request = mock(HttpServletRequest.class);
        when(request.getParameter("idToken")).thenReturn("abcIdToken");
        when(request.getParameter("email")).thenReturn("abcEmail@gmail.com");
        when(request.getParameter("userName")).thenReturn("abc");

        response = mock(HttpServletResponse.class);

        userVerifier = mock(UserVerifier.class);
        doNothing().when(userVerifier).build(any(String.class), any(String.class));
        when(userVerifier.getUserID()).thenReturn("abcUserId");

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        servlet = new CreateUsernameServlet();
        servlet.userVerifier = userVerifier;
    }

    /**
    * Removes the local datastore after each test.
    */
    @After
    public void tearDown() {
        helper.tearDown();
    }

    /**
    * Tests the creation of a valid username that is not taken. Makes sure
    * that the username is updated correctly in datastore after the servlet
    * is run.
    */
    @Test
    public void testValidUsername() throws Exception {
        User user = new User.Builder("abcUserId").build();
        datastoreManager.createOrReplaceUser(user);

        servlet.doPost(request, response);

        String username = datastoreManager.retrieveUser("abcUserId").getUsername();
        Assert.assertTrue(username.equals("abc"));
    }

    // /**
    // * Simulates user with id 'abcUserId' trying to change their username from 'usernameB' to 'usernameA',
    // * which is already taken by the user with id 'UserA'.
    // */
    @Test
    public void testTakenUsername() throws Exception {
        User userA = new User.Builder("UserA").setUsername("usernameA").build();
        User userB = new User.Builder("abcUserId").setUsername("usernameB").build();
        datastoreManager.createOrReplaceUser(userA);
        datastoreManager.createOrReplaceUser(userB);
        when(request.getParameter("userName")).thenReturn("usernameA");

        servlet.doPost(request, response);

        String usernameA = datastoreManager.retrieveUser("UserA").getUsername();
        Assert.assertTrue(usernameA.equals("usernameA"));
        String usernameB = datastoreManager.retrieveUser("abcUserId").getUsername();
        Assert.assertTrue(usernameB.equals("usernameB"));
    }

    /**
    * Tests a user attempting to create a username of just the empty string.
    * An error should be thrown.
    */
    @Test(expected=IOException.class)
    public void testEmptyUsername() throws Exception {
        User user = new User.Builder("abcUserId").build();
        datastoreManager.createOrReplaceUser(user);
        when(request.getParameter("userName")).thenReturn("");

        servlet.doPost(request, response);
    }

    /**
    * Tests a user attempting to create a username with more than 20 characters.
    * An error should be thrown.
    */
    @Test(expected=IOException.class)
    public void testTooLongUsername() throws Exception {
        User user = new User.Builder("abcUserId").build();
        datastoreManager.createOrReplaceUser(user);
        when(request.getParameter("userName")).thenReturn("012345678901234567890");

        servlet.doPost(request, response);
    }

    /**
    * Tests a user attempting to create a username containing unallowed special characters.
    * An error should be thrown.
    */
    @Test(expected=IOException.class)
    public void testUsernameWithSpecialCharacters() throws Exception {
        User user = new User.Builder("abcUserId").build();
        datastoreManager.createOrReplaceUser(user);
        when(request.getParameter("userName")).thenReturn("abc!");

        servlet.doPost(request, response);
    }

    /**
    * Tests a user attmepting to create a username containing allowed special characters
    * (underscores and periods). This should be allowed (no error should occur).
    */
    public void testUsernameWithUnderscoresAndPeriods() throws Exception {
        User user = new User.Builder("abcUserId").build();
        datastoreManager.createOrReplaceUser(user);
        when(request.getParameter("userName")).thenReturn("abc_abc.abc");

        servlet.doPost(request, response);
    }
}
