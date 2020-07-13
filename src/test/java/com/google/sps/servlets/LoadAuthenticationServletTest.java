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

@RunWith(JUnit4.class)
public final class LoadAuthenticationServletTest {
    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig());
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;
    private LoadAuthenticationServlet servlet;

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        servlet = new LoadAuthenticationServlet();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testNotLoggedIn() throws Exception { // should return an Authentication object with loggedIn = false and give a login url
        responseWriter.getBuffer().setLength(0); // clear the writer

        helper.setEnvIsLoggedIn(false);
        
        servlet.doGet(request, response);
        String json = responseWriter.toString();
        Type AuthenticationType = new TypeToken<Authentication>(){}.getType();
        Authentication res = new Gson().fromJson(json, AuthenticationType);

        Assert.assertEquals(res.getLogoutUrl(), "N/A");
        Assert.assertEquals(res.getLoggedIn(), false);
        Assert.assertFalse(res.getLoginUrl().equals("N/A"));
    }

    @Test
    public void testLoggedIn() throws Exception { // loggedIn should be true and should give a logout url
        responseWriter.getBuffer().setLength(0); // clear the writer

        helper.setEnvIsLoggedIn(true).setEnvEmail("abcdefg@google.com").setEnvIsAdmin(false);
        
        servlet.doGet(request, response);
        String json = responseWriter.toString();
        Type AuthenticationType = new TypeToken<Authentication>(){}.getType();
        Authentication res = new Gson().fromJson(json, AuthenticationType);

        Assert.assertEquals(res.getLoginUrl(), "N/A");
        Assert.assertEquals(res.getLoggedIn(), true);
        Assert.assertFalse(res.getLogoutUrl().equals("N/A"));
    }
}
