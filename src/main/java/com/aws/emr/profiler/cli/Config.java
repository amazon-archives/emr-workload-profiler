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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

@Builder
@Data
@Log4j
public class Config {
    private String s3Bucket;
    private List<String> enabledProfilers;

    public static Optional<Config> parseConfig(String configPath)  {
        try {
            File configFile = new File(configPath);
            Gson serializer = new GsonBuilder().create();
            return Optional.of(tryParseConfig(configFile, serializer));
        } catch (Exception ex) {
            log.error(ex);
            return Optional.empty();
        }
    }

    private static Config tryParseConfig(File configFile, Gson serializer) throws Exception {
        try (   FileInputStream inputStream = new FileInputStream(configFile);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.defaultCharset());
                JsonReader reader = new JsonReader(inputStreamReader)
        ) {
            return serializer.fromJson(reader, Config.class);
        }
    }
}
