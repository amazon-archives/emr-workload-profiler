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

import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class ConfigTest {
    @Test
    public void defaultConfigTest() {
        String s3BucketName = "foo";
        Config c = new Config(s3BucketName, new ArrayList<>());
        assertEquals(c.getS3Bucket(), s3BucketName);
        assertEquals(c.getEnabledProfilers().size(), 0);
    }

    @Test
    public void configWithProfilerEnabledTest() {
        String s3BucketName = "bar";
        String profilerClassName = "SPARK";
        List<String> profilerList = new ArrayList<>();
        profilerList.add(profilerClassName);


        Config c = new Config(s3BucketName, profilerList);

        assertEquals(c.getS3Bucket(), s3BucketName);
        assertEquals(c.getEnabledProfilers().size(), 1);
        assertEquals(c.getEnabledProfilers().get(0), profilerClassName);
    }

    @Test
    public void configUpdateTest() {
        String s3BucketName = "bar";
        String profilerClassName = "SPARK";
        List<String> profilerList = new ArrayList<>();
        profilerList.add(profilerClassName);


        Config c = new Config("foo", profilerList);
        c.setS3Bucket(s3BucketName);
        c.setEnabledProfilers(new ArrayList<>());

        assertEquals(c.getS3Bucket(), s3BucketName);
        assertEquals(c.getEnabledProfilers().size(), 0);
    }

    @Test
    public void parseConfigTest() throws Exception {
        File file = new File("src/resources/config.json");
        Config c = Config.parseConfig(file.getAbsolutePath()).get();
        assertEquals(c.getS3Bucket(), "workload-metrics");
        assertEquals(c.getEnabledProfilers().size(), 1);
    }

    @Test
    public void parseConfigInvalidTest() throws Exception {
        File file = new File("foo.json");
        Optional<Config> c = Config.parseConfig(file.getAbsolutePath());
        assertFalse(c.isPresent());
    }
}
