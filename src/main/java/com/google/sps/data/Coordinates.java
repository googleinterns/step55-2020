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
* Represents a location on Earth as a longitude and a latitude.
*/
public class Coordinates {
    private double latitude;
    private double longitude;

    /**
    * Constructor that initializes the coordinates as (0,0).
    */
    public Coordinates() {
        this.latitude = 0;
        this.longitude = 0;
    }

    /**
    * Constructor with specific coordinates.
    * @param latitude a double specifying the latitude.
    * @param longitude a double specifying the longitude.
    */
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
	
    /**
    * Sets the latitude of the coordinates.
    * @param latitude a double specifying the new latitude.
    */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
    * Sets the longitude of the coordinates.
    * @param longitude a double specifying the new longitude.
    */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
    * Get the latitude of the coordinates.
    * @return the latitude of the coordinates.
    */
    public double getLatitude() {
        return this.latitude;
    }

    /**
    * Get the longitude of the coordinates.
    * @return the longitude of the coordinates.
    */
    public double getLongitude() {
        return this.longitude;
    }
}
