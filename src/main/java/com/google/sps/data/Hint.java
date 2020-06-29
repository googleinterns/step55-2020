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
* Represents the static data of a single hint of a stage.
*/
public class Hint {
    private final String hintID;
    private final int hintNumber;
    private final Coordinates location;
    private final String text;

    /**
    * Builder class used to create Hint objects.
    */
    public static class Builder {
        // Required parameters
        private final String hintID;
        private final int hintNumber;

        // Optional parameters - initialized to default values
        private Coordinates location;
        private String text;

        /**
        * Constructor that sets the hint ID and the hint number.
        * @param hintID the unique code used to identify this specific hint.
        * @param hintNumber a int representing the hint number.
        */
        public Builder(String hintID, int hintNumber) {
            this.hintID = hintID;
            this.hintNumber = hintNumber;
        }

        /**
        * Sets the longitude and latitude where this hint is located.
        * @param val a Coordinates object representing the location.
        * @return a Builder object with the new location.
        * @see com.google.sps.data.Coordinates
        */
        public Builder setLocation(Coordinates val) {
            this.location = val;
            return this;
        }

        /**
        * Sets the text that appears when this hint is found.
        * @param val a String representing the text of the hint.
        * @return a Builder object with the new text.
        */
        public Builder setText(String val) {
            this.text = val;
            return this;
        }

        /**
        * Gets the Hint object created by this Builder.
        * @return a Hint object with the properties specified in the Builder.
        */
        public Hint build() {
            return new Hint(this);
        }
    }
    
    /**
    * Constructor that converts a Builder into a Hint.
    * @param builder the Builder object to be converted.
    * @see Builder#build()
    */
    private Hint(Builder builder) {
        this.hintID = builder.hintID;
        this.hintNumber = builder.hintNumber;
        this.location = builder.location;
        this.text = builder.text;
    }

    /**
    * Retrieves the id of this hint.
    * @return a String representing the hintID of this hint.
    */
    public String getHintID() {
        return this.hintID;
    }

    /**
    * Retrieves this hint's number.
    * @return an int representing the number of this int.
    */
    public int getHintNumber() {
        return this.hintNumber;
    }

    /**
    * Retrieves this hint's location.
    * @return a Coordinates representing the location of this int.
    */
    public Coordinates getLocation() {
        return this.location;
    }

    /**
    * Retrieves this hint's text.
    * @return a String representing the text of this int.
    */
    public String getText() {
        return this.text;
    }
}
