# Amazon EMR Workload Profiler

The Amazon EMR Workload profiler collects metrics from big data
applications and frameworks to provide insight into system
performance.

## Build and Run tests

```
$> mvn clean install
```

## Set-up config file

A config file needs to be created in the following location.

```
~/.emr-profiler/config.json
```

The file should be JSON as shown below.
```
{
  "s3Bucket": "workload-metrics",
  "enabledProfilers": ["SparkProfiler", "PigProfiler", "YARNProfiler"]
}
```

## Run collection

``` java -jar emr-workload-profiler-0.1.jar ```

# Standards

- [commit messages](https://chris.beams.io/posts/git-commit/)
- ...

# License

This library is licensed under the Apache 2.0 License. 
