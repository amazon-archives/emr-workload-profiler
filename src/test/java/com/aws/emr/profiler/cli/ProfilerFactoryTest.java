/*
 * Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 *
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file.
 * This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aws.emr.profiler.cli;

import java.util.List;

import com.aws.emr.profiler.core.Profiler;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.assertEquals;

public class ProfilerFactoryTest {
    @Test
    public void invalidProfilerTest() throws Exception {
        List<String> profilerClassNames = new ArrayList<>();
        profilerClassNames.add("FOOO");
        Config c = new Config("test", profilerClassNames);
        List<Profiler> profilers = ProfilerFactory.createProfilers(c);
        assertEquals(profilers.size(), 0);
    }

    @Test
    public void sparkEnabledTest() throws Exception {
        List<String> profilerClassNames = new ArrayList<>();
        profilerClassNames.add("SparkProfiler");
        Config c = new Config("test", profilerClassNames);
        List<Profiler> profilers = ProfilerFactory.createProfilers(c);
        assertEquals(profilers.size(), 1);
    }

}
