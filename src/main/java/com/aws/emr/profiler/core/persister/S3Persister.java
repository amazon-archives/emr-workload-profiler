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

import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Optional;

public class S3Persister implements Persister {
    private static Optional<TransferManager> transferManager;
    private String s3Bucket;

    public S3Persister(String s3Bucket) {
        this.s3Bucket = s3Bucket;
        this.transferManager = Optional.of(TransferManagerBuilder.defaultTransferManager());
    }

    public S3Persister(TransferManager transferManager, String s3Bucket) {
        this.transferManager = Optional.of(transferManager);
        this.s3Bucket = s3Bucket;
    }

    @Override
    public void saveMetrics(String fileName, String metrics) {
        try {
            prepareTransferManager();
            File file = new File(fileName);
            tryWriteToFile(metrics, file);
            tryUploadFile(file.getName(), file);
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
            shutdownTransferManager();
        }
    }

    private void prepareTransferManager() {
        if (!transferManager.isPresent()) {
            this.transferManager = Optional.of(TransferManagerBuilder.defaultTransferManager());
        }
    }

    private void shutdownTransferManager() {
        transferManager.get().shutdownNow();
        transferManager = Optional.empty();
    }

    private void tryWriteToFile(String serializedData, File file) {
        try {
            Files.write(serializedData, file, Charset.forName(Charsets.UTF_8.toString()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void tryUploadFile(String fileName, File file) {
        try {
            transferManager.get().upload(s3Bucket, fileName, file).waitForCompletion();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
