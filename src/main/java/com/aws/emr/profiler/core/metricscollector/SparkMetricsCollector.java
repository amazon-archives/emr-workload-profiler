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

import com.aws.emr.profiler.core.requestprocessor.HTTPRequestProcessor;

public class SparkMetricsCollector implements MetricsCollector {

    private static final String SPARK_HISTORY_SERVER = "http://%s:18080/api/v1/applications";

    private HTTPRequestProcessor httpRequestProcessor;

    public SparkMetricsCollector() {
        this.httpRequestProcessor = new HTTPRequestProcessor();
    }

    public SparkMetricsCollector(HTTPRequestProcessor httpRequestProcessor) {
        this.httpRequestProcessor = httpRequestProcessor;
    }

    @Override
    public String getSerializedMetrics() {
        return httpRequestProcessor.makeHttpGetRequest(getSparkRESTAPIEndpoint());
        // todo: add metrics for job, tasks, stages, executors, events, etc.
    }

    private String getSparkRESTAPIEndpoint() {
        return String.format(SPARK_HISTORY_SERVER, httpRequestProcessor.getLocalHostName());
    }
}
