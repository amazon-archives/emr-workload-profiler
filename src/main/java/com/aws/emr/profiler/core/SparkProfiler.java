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

import com.aws.emr.profiler.cli.Config;
import com.aws.emr.profiler.core.metricscollector.MetricsCollector;
import com.aws.emr.profiler.core.metricscollector.SparkMetricsCollector;
import com.aws.emr.profiler.core.persister.Persister;
import com.aws.emr.profiler.core.persister.S3Persister;

import java.time.Instant;

public class SparkProfiler implements Profiler {

    private static String FILE_NAME_FORMAT = "emr-profiler-spark-metrics-%tQ";

    private MetricsCollector metricsCollector;
    private Persister metricsPersister;

    public SparkProfiler(Config config) {
        this.metricsCollector = new SparkMetricsCollector();
        this.metricsPersister = new S3Persister(config.getS3Bucket());
    }

    public SparkProfiler(MetricsCollector metricsCollector, Persister metricsPersister) {
        this.metricsCollector = metricsCollector;
        this.metricsPersister = metricsPersister;
    }

    @Override
    public void profile() {
        String metrics = metricsCollector.getSerializedMetrics();
        String fileName = String.format(FILE_NAME_FORMAT, Instant.now().toEpochMilli());
        metricsPersister.saveMetrics(fileName, metrics);
    }
}
