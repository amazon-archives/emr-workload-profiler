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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

@Data
@Builder
public class Config {
    private static Logger LOG = Logger.getLogger(Config.class);

    @Getter
    @Setter
    private String s3Bucket;

    @Getter
    @Setter
    private List<String> enabledProfilers;

    public static Optional<Config> parseConfig(String configPath)  {
        try {
            return Optional.of(tryParseConfig(configPath));
        } catch (Exception ex) {
            LOG.error(ex);
            return Optional.empty();
        }
    }

    private static Config tryParseConfig(String configPath) throws Exception {
        Gson gson = new GsonBuilder().create();
        File f = new File(configPath);
        JsonReader r = new JsonReader(new InputStreamReader(new FileInputStream(f), Charset.defaultCharset()));
        return gson.fromJson(r, Config.class);
    }
}
