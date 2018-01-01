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

import com.aws.emr.profiler.core.Profiler;
import lombok.extern.log4j.Log4j;

import java.util.List;

@Log4j
public class Main {
    private static String DEFAULT_CONFIG_PATH = System.getProperty("user.home") + "/.emr-profiler/config.json";

    public static void main(String[] args) throws Exception {
        log.info("Parsing config");
        Config config = Config.parseConfig(DEFAULT_CONFIG_PATH).orElseThrow(InvalidConfigFileException::new);

        log.info("Creating profilers");
        List<Profiler> profilers = ProfilerFactory.createProfilers(config);

        log.info("Running profilers");
        profilers.parallelStream().forEach(p -> p.profile());
    }
}
