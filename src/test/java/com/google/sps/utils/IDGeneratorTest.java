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

import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class IDGeneratorTest {
    /**
    * Generates 100,000 ids and checks that they are all unique.
    */
    @Test
    public void testUniqueness() {
        HashSet<String> ids = new HashSet<String>();
        for(int i = 0; i < 100000; i++) {
            ids.add(IDGenerator.gen());
        }
        Assert.assertEquals(ids.size(), 100000);
    }
}
