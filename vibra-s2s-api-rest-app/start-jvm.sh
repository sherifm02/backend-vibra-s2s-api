#!/bin/bash

# Set environment variables
export MF_SERVICE_NAME=vibra-s2s
export MF_ENVIRONMENT_NAME=local-jvm-"$USER"
export MF_OTEL_COLLECTOR_HOST=localhost
export MF_CONFIG_ENV=dev

# Function to run Quarkus in dev mode
run_quarkus_dev() {
    mvn quarkus:dev
}

run_quarkus_dev