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
* Represents the static data of a single stage of a game.
*/
public class Stage {
    private final String stageID;
    private final int stageNumber;
    private final String key;
    private final String startingHint;
    private final Coordinates startingLocation;
    private final ArrayList<Hint> hints;

    /**
    * Builder class used to create Stage objects.
    */
    public static class Builder {
        // Required parameters
        private final String stageID;
        private final int stageNumber;

        // Optional parameters - initialized to default values
        private String key = "N/A";
        private String startingHint = "N/A";
        private Coordinates startingLocation = new Coordinates();
        private ArrayList<Hint> hints = new ArrayList<>();

        /**
        * Constructor that sets the stage ID and the stage number.
        * @param stageID the unique code used to identify this specific stage.
        * @param stageNumber a int containing the stage number.
        */
        public Builder(String stageID, int stageNumber) {
            this.stageID = stageID;
            this.stageNumber = stageNumber;
        }

        /**
        * Sets the key used to beat this stage.
        * @param key the key used to beat this stage.
        * @return a Builder object with the new key.
        */
        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        /**
        * Sets the starting hint given at the beginning of this stage.
        * @param startingHint the starting hint.
        * @return a Builder object with the new starting hint.
        */
        public Builder setStartingHint(String startingHint) {
            this.startingHint = startingHint;
            return this;
        }

        /**
        * Sets the longitude and latitude where the user spawns when this stage begins.
        * @param startingLocation a Coordinates object containing the starting location.
        * @return a Builder object with the new starting location.
        * @see com.google.sps.data.Coordinates
        */
        public Builder setStartingLocation(Coordinates startingLocation) {
            this.startingLocation = startingLocation;
            return this;
        }

        /**
        * Specifies which hints are in the game.
        * @param hints an ArrayList of Hint containing the list of hints in this stage.
        * @return a Builder object with the new list of hints.
        */
        public Builder setHints(ArrayList<Hint> hints) {
            this.hints = hints;
            return this;
        }

        /**
        * Gets the Stage object created by this Builder.
        * @return a Stage object with the properties specified in the Builder.
        */
        public Stage build() {
            return new Stage(this);
        }
    }
    
    /**
    * Constructor that converts a Builder into a Stage.
    * @param builder the Builder object to be converted.
    * @see Builder#build()
    */
    private Stage(Builder builder) {
        this.stageID = builder.stageID;
        this.stageNumber = builder.stageNumber;
        this.key = builder.key;
        this.startingHint = builder.startingHint;
        this.startingLocation = builder.startingLocation;
        this.hints = builder.hints;
    }

    /**
    * Retrieves the id of this stage.
    * @return a String containing the stageID of this stage.
    */
    public String getStageID() {
        return this.stageID;
    }

    /**
    * Retrieves the number of this stage.
    * @return an int containing the stageNumber of this stage.
    */
    public int getStageNumber() {
        return this.stageNumber;
    }

    /**
    * Retrieves the key for this stage.
    * @return a String containing the key for this stage.
    */
    public String getKey() {
        return this.key;
    }

    /**
    * Retrieves the starting hint of this stage.
    * @return a String containing the starting hint of this stage.
    */
    public String getStartingHint() {
        return this.startingHint;
    }

    /**
    * Retrieves the starting coordinates of this stage.
    * @return a Coordinates containing the starting location of this stage.
    */
    public Coordinates getStartingLocation() {
        return this.startingLocation;
    }

    /**
    * Retrieves the list of hints in this stage.
    * @return an ArrayList of Hint containing the hints that are in this stage.
    */
    public ArrayList<Hint> getHints() {
        return this.hints;
    }

    /**
    * Tests equality of this Stage with another object. Equality is determined
    * by whether all fields except for the stageID are the same.
    * @param other the other object.
    * @return a boolean indicating whether this is equal to the other object.
    */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Stage)) return false;
        Stage otherStage = (Stage) other;
        if(this.stageNumber != otherStage.stageNumber) return false;
        if(!this.key.equals(otherStage.key)) return false;
        if(!this.startingHint.equals(otherStage.startingHint)) return false;
        if(!this.startingLocation.equals(otherStage.startingLocation)) return false;
        if(this.hints.size() != otherStage.hints.size()) return false;
        for(int i = 0; i < this.hints.size(); i++) {
            Hint thisHint = this.hints.get(i);
            Hint otherHint = otherStage.hints.get(i);
            if(!thisHint.equals(otherHint)) return false;
        }
        return true;
    }
}
