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
* Represents two objects.
*/
public class Pair<T1, T2> {
    public T1 first;
    public T2 second;
    
    /**
    * Constructor that creates a new pair of null.
    */
    public Pair() {
        this.first = null;
        this.second = null;
    }

    /**
    * Constructor that creates a new pair with specified first and second.
    * @param first the first object of the pair.
    * @param second the second object of the pair.
    */
    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
}
