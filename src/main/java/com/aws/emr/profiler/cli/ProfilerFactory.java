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
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j
public class ProfilerFactory {
    private static String DEFAULT_PROFILER_PACKAGE ="com.aws.emr.profiler.core.";

    public static List<Profiler> createProfilers(Config config) throws Exception {
        return config.getEnabledProfilers().parallelStream()
                .map(profiler -> createProfiler(config, profiler))
                .filter(profiler -> profiler.isPresent())
                .map(profiler -> profiler.get()).collect(Collectors.toList());
    }

    private static Optional<Profiler> createProfiler(Config config, String profilerClassName) {
        try {
            Class profilerClass = Class.forName(DEFAULT_PROFILER_PACKAGE + profilerClassName);
            Profiler profiler =  (Profiler) profilerClass.getConstructor(Config.class).newInstance(config);
            return Optional.of(profiler);
        } catch (Exception ex) {
            log.error(ex);
            return Optional.empty();
        }
    }
}
