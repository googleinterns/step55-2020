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
        private String key = null;
        private String startingHint = null;
        private Coordinates startingLocation = null;
        private ArrayList<Hint> hints = null;

        /**
        * Constructor that sets the stage ID and the stage number.
        * @param stageID the unique code used to identify this specific stage.
        * @param stageNumber a int representing the stage number.
        */
        public Builder(String stageID, int stageNumber) {
            this.stageID = stageID;
            this.stageNumber = stageNumber;
        }

        /**
        * Sets the key used to beat this stage.
        * @param val the key used to beat this stage.
        * @return a Builder object with the new key.
        */
        public Builder setKey(String val) {
            this.key = val;
            return this;
        }

        /**
        * Sets the starting hint given at the beginning of this stage.
        * @param val the starting hint.
        * @return a Builder object with the new starting hint.
        */
        public Builder setStartingHint(String val) {
            this.startingHint = val;
            return this;
        }

        /**
        * Sets the longitude and latitude where the user spawns when this stage begins.
        * @param val a Coordinates object representing the starting location.
        * @return a Builder object with the new starting location.
        * @see com.google.sps.data.Coordinates
        */
        public Builder setStartingLocation(Coordinates val) {
            this.startingLocation = val;
            return this;
        }

        /**
        * Specifies which hints are in the game.
        * @param val an ArrayList of Hint representing the list of hints in this stage.
        * @return a Builder object with the new list of hints.
        */
        public Builder setHints(ArrayList<Hint> val) {
            this.hints = val;
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
}
