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

import java.util.ArrayList;

/**
* Represents the static data of a user.
*/
public class User {
    private final String userID;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String profilePicture;
    private final ArrayList<String> gamesCreated;
    private final int numGamesFinished;
    private final int numGamesCreated;
    private final ArrayList<Pair<String, Long>> gamesCompletedWithTime;

    /**
    * Builder class used to create User objects.
    */
    public static class Builder {
        // Required parameters
        private final String userID;

        // Optional parameters - initialized to default values
        private String username = null;
        private String firstName = null;
        private String lastName = null;
        private String profilePicture = null;
        private ArrayList<String> gamesCreated = null;
        private int numGamesFinished = -1;
        private int numGamesCreated = -1;
        private ArrayList<Pair<String, Long>> gamesCompletedWithTime = null;

        /**
        * Constructor for a new User Builder with the given user ID.
        * @param userID the userID to initiate this user with.
        */
        public Builder(String userID) {
            this.userID = userID;
        }

        /**
        * Sets the username of this user.
        * @param val a String representing the new username.
        * @return a Builder with the modified username.
        */
        public Builder setUsername(String val) {
            this.username = val;
            return this;
        }

        /**
        * Sets the first name of this user.
        * @param val a String representing the new first name.
        * @return a Builder with the modified first name.
        */
        public Builder setFirstName(String val) {
            this.firstName = val;
            return this;
        }

        /**
        * Sets the last name of this user.
        * @param val a String representing the new last name.
        * @return a Builder with the modified last name.
        */
        public Builder setLastName(String val) {
            this.lastName = val;
            return this;
        }

        /**
        * Sets the profile picture url for this user.
        * @param val a String representing the new profile picture url.
        * @return a Builder with the modified profile picture url.
        */
        public Builder setProfilePicture(String val) {
            this.profilePicture = val;
            return this;
        }

        /**
        * Sets the list of games this user has created.
        * @param val an ArrayList of String representing a list of gameIDs of the games this user has created.
        * @return a Builder with the modified list of games created.
        */
        public Builder setGamesCreated(ArrayList<String> val) {
            this.gamesCreated = val;
            return this;
        }

        /**
        * Sets the number of games this user has finished (won).
        * @param val an int representing how many games this user has finished.
        * @return a Builder with modified number of games finished.
        */
        public Builder setNumGamesFinished(int val) {
            this.numGamesFinished = val;
            return this;
        }

        /**
        * Sets the number of games this user has created.
        * @param val an int representing how many games this user has created.
        * @return a Builder with modified number of games created.
        */
        public Builder setNumGamesCreated(int val) {
            this.numGamesCreated = val;
            return this;
        }

        /**
        * Sets the list of games the user has finished, along with the times it took for them to finish them.
        * @param val an ArrayList of pairs of String and Long, where the first element of the pair contains the
        * gameID, and the second element contains the amount of time in milliseconds it took for them to
        * complete the game.
        * @return a Builder with modified list of games completed and their times.
        */
        public Builder setGamesCompletedWithTime(ArrayList<Pair<String, Long>> val) {
            this.gamesCompletedWithTime = val;
            return this;
        }

        /**
        * Gets the User object created by this Builder.
        * @return a User object with the properties specified in the Builder.
        */
        public User build() {
            return new User(this);
        }
    }

    /**
    * Constructor that converts a Builder into a User.
    * @param builder the Builder object to be converted.
    * @see Builder#build()
    */
    private User(Builder builder) {
        this.userID = builder.userID;
        this.username = builder.username;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.profilePicture = builder.profilePicture;
        this.gamesCreated = builder.gamesCreated;
        this.numGamesFinished = builder.numGamesFinished;
        this.numGamesCreated = builder.numGamesCreated;
        this.gamesCompletedWithTime = builder.gamesCompletedWithTime;
    }

    /**
    * Retrieves the id of this user.
    * @return a String representing the userID of this user.
    */
    public String getUserID() {
        return this.userID;
    }

    /**
    * Retrieves the username of this user.
    * @return a String representing the username of this user.
    */
    public String getUsername() {
        return this.username;
    }

    /**
    * Retrieves the first name of this user.
    * @return a String representing the first name of this user.
    */
    public String getFirstName() {
        return this.firstName;
    }

    /**
    * Retrieves the last name of this user.
    * @return a String representing the last name of this user.
    */
    public String getLastName() {
        return this.lastName;
    }

    /**
    * Retrieves the profile picture of this user.
    * @return a String representing the profile picture of this user.
    */
    public String getProfilePicture() {
        return this.profilePicture;
    }

    /**
    * Retrieves the list of games created by this user.
    * @return an ArrayList of String containing the gameIDs of the games created by this user.
    */
    public ArrayList<String> getGamesCreated() {
        return this.gamesCreated;
    }

    /**
    * Retrieves the number of games this user has finished (won).
    * @return an int representing the number of games this user has finished (won).
    */
    public int getNumGamesFinished() {
        return this.numGamesFinished;
    }

    /**
    * Retrieves the number of games this user has created.
    * @return an int representing the number of games this user has created.
    */
    public int getNumGamesCreated() {
        return this.numGamesCreated;
    }

    /**
    * Retrieves the list of games the user has finished, along with the times it took for them to finish them.
    * @return an ArrayList of pairs of String and Long, where the first element of each pair contains the
    * gameID, and the second element contains the amount of time in milliseconds it took for them to
    * complete the game.
    */
    public ArrayList<Pair<String, Long>> getGamesCompletedWithTime() {
        return this.gamesCompletedWithTime;
    }
}
