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

import java.util.Objects;

/**
* Represents two objects.
*/
public class Pair<Type1, Type2> {
    public Type1 first;
    public Type2 second;

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
    public Pair(Type1 first, Type2 second) {
        this.first = first;
        this.second = second;
    }

    /**
    * Checks whether two Pair objects are equal. Two Pair objects are equal if and only if
    * their corresponding firsts and seconds are equal.
    * @param other the Pair that this is being compared with.
    * @return a boolean indicating whether this is equal to other.
    */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Pair)) return false;
        Pair otherPair = (Pair) other;
        return this.first.equals(otherPair.first) && this.second.equals(otherPair.second);
    }

    /**
    * Generates the hash code for this Pair.
    * @return an int containing the hash code of this Pair.
    */
    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }
}
