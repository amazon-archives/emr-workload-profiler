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

package com.aws.emr.profiler.core.requestprocessor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HTTPRequestProcessor {
    private static OkHttpClient httpClient = new OkHttpClient();
    private static String INSTANCE_METADATA_ENDPOINT = "169.254.169.254";
    private static String INSTANCE_METADATA_URI = "http://%s/latest/meta-data/local-hostname";

    public HTTPRequestProcessor() {
        this.INSTANCE_METADATA_URI = String.format(INSTANCE_METADATA_URI, INSTANCE_METADATA_ENDPOINT);
    }

    public HTTPRequestProcessor(String host) {
        this.INSTANCE_METADATA_URI = String.format(INSTANCE_METADATA_URI, host);
    }

    public HTTPRequestProcessor(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        this.INSTANCE_METADATA_URI = String.format(INSTANCE_METADATA_URI, INSTANCE_METADATA_ENDPOINT);
    }

    public String makeHttpGetRequest(String url) {
        Request request = new Request.Builder().get().url(url).build();
        Response response = tryMakeRequest(request);
        return tryGetResponseBody(response);
    }

    private Response tryMakeRequest(Request request) {
        try {
            return httpClient.newCall(request).execute();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String tryGetResponseBody(Response response) {
        try {
            return response.body().string();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getLocalHostName() {
        Request request = new Request.Builder().get().url(INSTANCE_METADATA_URI).build();
        Response response = tryMakeRequest(request);
        return tryGetResponseBody(response);
    }
}
