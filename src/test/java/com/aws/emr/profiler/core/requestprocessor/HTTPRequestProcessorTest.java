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
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

public class HTTPRequestProcessorTest {
    private MockWebServer mockWebServer;

    @BeforeMethod
    public void setup() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8089);
    }

    @AfterMethod
    public void shutdown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void testGetResponseBodyValid() throws Exception {
        mockWebServer.enqueue(new MockResponse().setBody("localhost"));
        HTTPRequestProcessor httpRequestProcessor = new HTTPRequestProcessor(mockWebServer.getHostName() + ":" +
                String.valueOf(mockWebServer.getPort()));
        String response = httpRequestProcessor.makeHttpGetRequest(mockWebServer.url("/foo").toString());
        assertEquals(response, "localhost");
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals(recordedRequest.getPath(), "/foo");
        assertEquals(mockWebServer.getRequestCount(), 1);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testMakeHttpGetRequestIOException() throws Exception {
        mockWebServer.enqueue(new MockResponse().setBody("test"));
        OkHttpClient httpClient = mock(OkHttpClient.class, Mockito.RETURNS_DEEP_STUBS);
        when(httpClient.newCall(any()).execute()).thenThrow(new IOException());
        HTTPRequestProcessor httpRequestProcessor = new HTTPRequestProcessor(httpClient);
        httpRequestProcessor.makeHttpGetRequest(mockWebServer.url("/biz").toString());
    }
}
