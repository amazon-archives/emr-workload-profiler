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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProfilerFactory {
    private static String DEFAULT_PROFILER_CLASS ="com.aws.emr.profiler.core.";

    public static List<Profiler> createProfilers(Config c) throws Exception {
        return c.getEnabledProfilers().parallelStream().map(p -> createProfiler(c, p)).filter(p -> p.isPresent())
                .map(p -> p.get()).collect(Collectors.toList());
    }

    private static Optional<Profiler> createProfiler(Config c, String profilerClassName) {
        try {
            Class profilerClass = Class.forName(DEFAULT_PROFILER_CLASS + profilerClassName);
            Profiler p =  (Profiler) profilerClass.getConstructor(Config.class).newInstance(c);
            return Optional.of(p);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
