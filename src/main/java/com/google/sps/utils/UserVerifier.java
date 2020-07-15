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

package com.google.sps.utils;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.util.Collections;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

/**
* Class that parses an idToken given by the client.
*/
public class UserVerifier {
    private final String CLIENT_ID = "683964064238-ccubt4o7k5oc9pml8n72id8q1p1phukl.apps.googleusercontent.com";
    private GoogleIdToken idToken;
    private GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new UrlFetchTransport(), new JacksonFactory()).setAudience(Collections.singletonList(CLIENT_ID)).build();

    public UserVerifier(String idTokenString) {
        idToken = verifier.verify(idTokenString);
    }

    public boolean isValid() {
        return (idToken != null);
    }
}
