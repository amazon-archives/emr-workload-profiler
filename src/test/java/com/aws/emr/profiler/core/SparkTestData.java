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

package com.aws.emr.profiler.core;

public class SparkTestData {
    public final static String serializedSparkApplicationData =
            "{\n" +
                    "  \"id\" : \"application_1476167072801_0002\",\n" +
                    "  \"name\" : \"Spark Pi\",\n" +
                    "  \"attempts\" : [ {\n" +
                    "    \"attemptId\" : \"1\",\n" +
                    "    \"startTime\" : \"2016-10-11T06:35:26.219GMT\",\n" +
                    "    \"endTime\" : \"2016-10-11T06:35:37.960GMT\",\n" +
                    "    \"lastUpdated\" : \"2016-10-11T06:35:38.020GMT\",\n" +
                    "    \"duration\" : 11741,\n" +
                    "    \"sparkUser\" : \"hadoop\",\n" +
                    "    \"completed\" : true,\n" +
                    "    \"endTimeEpoch\" : 1476167737960,\n" +
                    "    \"startTimeEpoch\" : 1476167726219,\n" +
                    "    \"lastUpdatedEpoch\" : 1476167738020\n" +
                    "  } ]\n" +
                    "}";
}
