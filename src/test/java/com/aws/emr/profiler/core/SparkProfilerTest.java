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

import com.aws.emr.profiler.core.metricscollector.MetricsCollector;
import com.aws.emr.profiler.core.metricscollector.SparkMetricsCollector;
import com.aws.emr.profiler.core.persister.Persister;
import com.aws.emr.profiler.core.persister.S3Persister;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SparkProfilerTest {

    private MetricsCollector sparkMetricsCollector;
    private Persister s3Persister;
    private Profiler sparkProfiler;

    @BeforeMethod
    public void setUp() {
        s3Persister = mock(S3Persister.class);
        sparkMetricsCollector = mock(SparkMetricsCollector.class);
        sparkProfiler = new SparkProfiler(sparkMetricsCollector, s3Persister);
    }

    @Test
    public void collectMetricsValid() {
        when(sparkMetricsCollector.getSerializedMetrics()).thenReturn(SparkTestData.serializedSparkApplicationData);
        doNothing().when(s3Persister).saveMetrics(any(), any());

        sparkProfiler.profile();

        verify(sparkMetricsCollector, times(1)).getSerializedMetrics();
        verify(s3Persister, times(1)).saveMetrics(any(), any());
    }
}
