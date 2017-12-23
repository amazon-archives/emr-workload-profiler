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

package com.aws.emr.profiler.core.persister;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;

public class S3PersisterTest {

    private TransferManager transferManager;

    @BeforeMethod
    public void setUp() {
        transferManager = mock(TransferManager.class);
    }

    @Test
    public void testS3Persister() throws Exception {
        Upload upload = mock(Upload.class);
        when(upload.waitForUploadResult()).thenReturn(new UploadResult());
        TransferManager transferManager = mock(TransferManager.class);
        when(transferManager.upload(anyString(), anyString(), any())).thenReturn(upload);

        S3Persister s3Persister = new S3Persister(transferManager, "foo");
        s3Persister.saveMetrics("foo", "bar");

        verify(transferManager, times(1)).upload(anyString(), anyString(), any());
        verify(transferManager, times(1)).shutdownNow();
        verifyNoMoreInteractions(transferManager);
        verify(upload, times(1)).waitForCompletion();
        verifyNoMoreInteractions(upload);
        assertFalse(new File("foo").exists());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testPersistApplicationDataException() throws Exception {
        TransferManager transferManager = mock(TransferManager.class);
        when(transferManager.upload(anyString(), anyString(), any()))
                .thenThrow(new AmazonServiceException(""));

        S3Persister s3Persister = new S3Persister(transferManager, "foo");

        try {
            s3Persister.saveMetrics("foo", "bar");
        } finally {
            assertFalse(new File("foo").exists());
        }
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testPersistApplicationNullException() throws Exception {
        Upload upload = mock(Upload.class);
        when(upload.waitForUploadResult()).thenReturn(new UploadResult());
        TransferManager transferManager = mock(TransferManager.class);
        when(transferManager.upload(anyString(), anyString(), any())).thenReturn(upload);

        S3Persister s3Persister = new S3Persister(transferManager, "foo");

        try {
            s3Persister.saveMetrics("foo", null);
        } finally {
            assertFalse(new File("foo").exists());
        }
    }
}
