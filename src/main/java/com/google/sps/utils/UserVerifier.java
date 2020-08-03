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
import com.google.sps.managers.*;
import com.google.sps.data.*;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.util.Collections;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import java.io.IOException;

/**
* Class that parses an idToken given by the client.
*/
public class UserVerifier {
    private DatastoreManager datastoreManager = new DatastoreManager();
    private String CLIENT_ID;
    private GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new UrlFetchTransport(), new JacksonFactory()).setAudience(Collections.singletonList(CLIENT_ID)).build();
    private boolean isValid = false;
    private GoogleIdToken idToken;
    private Payload payload;

    /**
    * Checks the validity of the given idTokenString and email. If the token is invalid or
    * if it doesn't match the email, an error is thrown. Otherwise the payload will be initiated
    * with the corresponding user information.
    * @param idTokenString the id token string of the user.
    * @param email the email corresponding to the same id token string.
    */
    public void build(String idTokenString, String email) throws IOException {
        try {
            CLIENT_ID = datastoreManager.retrieveKeys().get("CLIENT_ID");
        } catch (Exception e) {
            throw new IOException("Client id is missing");
        }
        isValid = false;
        try {
            this.idToken = verifier.verify(idTokenString);
            this.payload = idToken.getPayload();
        } catch (Exception e) {
            throw new IOException("Invalid id token string");
        }
        if(this.payload.getEmail().equals(email)) {
            isValid = true;
        } else {
            throw new IOException("Id token string does not match email");
        }
    }

    /**
    * Retrieves the userID from datastore using the email. Throws an error if the payload
    * has not been initiated.
    * @return a String containing the userID.
    */
    public String getUserID() throws IOException {
        if(!isValid) {
            throw new IOException("UserVerifier has not been built correctly.");
        }
        String email = payload.getEmail();
        String id;
        try {
            id = datastoreManager.retrieveIdByEmail(email);
        } catch(Exception e) {
            id = null;
        }
        if(id == null) {
            id = IDGenerator.gen();
            datastoreManager.createOrReplaceIdentification(email, id);
        }
        return id;
    }
}
