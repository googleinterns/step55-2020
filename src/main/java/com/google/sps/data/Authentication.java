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

package com.google.sps.data;

/**
* Represents the authentication data of a user.
*/
public class Authentication {
    private boolean loggedIn = false;
    private String loginUrl = "N/A";
    private String logoutUrl = "N/A";
    
    /**
    * Constructor for an Authentication object.
    * @param loggedIn a boolean representing whether the user is logged in.
    */
    public Authentication(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
    * Sets the login url of the user.
    * @param loginUrl a url that the user can navigate to in order to log in.
    */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    /**
    * Sets the logout url of the user.
    * @param logoutUrl a url that the user can navigate to in order to log out.
    */
    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }
}
