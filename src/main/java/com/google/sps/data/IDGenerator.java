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

import java.util.UUID;

/**
* A simple class that generates random ID strings that are extremely
* likely to be unique. These IDs are used for users, games, stages, hints,
* etc.
*/
public class IDGenerator {
    /**
    * Generates a random (and unique) ID.
    * @return a String containing a random ID.
    */
    public static String gen() {
        return UUID.randomUUID().toString();
    }
}
