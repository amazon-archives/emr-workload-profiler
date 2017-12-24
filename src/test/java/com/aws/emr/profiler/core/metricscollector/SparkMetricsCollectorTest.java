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

package com.aws.emr.profiler.core.metricscollector;

import com.aws.emr.profiler.core.SparkTestData;
import com.aws.emr.profiler.core.requestprocessor.HTTPRequestProcessor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class SparkMetricsCollectorTest {

    private HTTPRequestProcessor mockHTTPRequestProcessor;

    @BeforeMethod
    public void setUp() {
        mockHTTPRequestProcessor = mock(HTTPRequestProcessor.class);
    }

    @Test
    public void collectMetricsValid() {
        MetricsCollector collector = new SparkMetricsCollector(mockHTTPRequestProcessor);
        when(mockHTTPRequestProcessor
                .makeHttpGetRequest(anyString())).thenReturn(SparkTestData.serializedSparkApplicationData);
        String result = collector.getSerializedMetrics();
        assertEquals(result, SparkTestData.serializedSparkApplicationData);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void collectMetricsInvalid() {
        MetricsCollector collector = new SparkMetricsCollector(mockHTTPRequestProcessor);
        when(mockHTTPRequestProcessor.makeHttpGetRequest(anyString())).thenThrow(RuntimeException.class);
        collector.getSerializedMetrics();
    }
}




