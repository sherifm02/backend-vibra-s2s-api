#!/bin/bash

##
## Script to set the Maven project version based on different execution contexts.
##
## Usage:
## - Local: `./mvnSetVersion.sh`
## - GitHub Actions:
##   - `./mvnSetVersion.sh TAG SEM.VER.TAG`
##   - `./mvnSetVersion.sh master FINAL_VERSION`
##   - `./mvnSetVersion.sh SNAPSHOT gh GITHUB_RUN_NUMBER FINAL_VERSION`
##

# Assign script arguments to named variables for better readability
VERSION_TYPE=$1           # master, SNAPSHOT, or TAG
GH_FLAG=$2                # 'gh' if running in GitHub Actions
GITHUB_RUN_NUMBER=$3      # GitHub Actions run number (only for SNAPSHOT)
FINAL_VERSION=${4:-$2}    # Base version from pipeline (major.minor.patch) for master, TAG and SNAPSHOT (defaults to $2 if $4 is empty)

# Get the current date
DATE=$(date +'%Y-%m-%d')

# Extract Quarkus version from pom.xml
POM_FILE="./pom.xml"
MF_QUARKUS_VERSION=$(xmllint --xpath "/*[local-name()='project']/*[local-name()='properties']/*[local-name()='quarkus.platform.version']/text()" "${POM_FILE}" 2>/dev/null || echo "")

if [[ -n "$MF_QUARKUS_VERSION" ]]; then
    QUARKUS_VERSION_TAG=$(echo "$MF_QUARKUS_VERSION" | sed 's/\./-/g')
    echo "DEBUG: Quarkus Version Found: $MF_QUARKUS_VERSION"
else
    QUARKUS_VERSION_TAG=""
    echo "DEBUG: Quarkus Version Not Found."
fi

# Determine the final version format based on execution context
if [[ -n "$VERSION_TYPE" ]]; then
    case "$VERSION_TYPE" in
        master)
            if [[ -n "$QUARKUS_VERSION_TAG" ]]; then
                MF_VERSION="$FINAL_VERSION-$QUARKUS_VERSION_TAG-$DATE"
            else
                MF_VERSION="$FINAL_VERSION-$DATE"
            fi
            ;;
        SNAPSHOT)
            if [[ -n "$QUARKUS_VERSION_TAG" ]]; then
                MF_VERSION="$GITHUB_RUN_NUMBER-$FINAL_VERSION-$QUARKUS_VERSION_TAG-$DATE-SNAPSHOT"
            else
                MF_VERSION="$GITHUB_RUN_NUMBER-$FINAL_VERSION-$DATE-SNAPSHOT"
            fi
            ;;
        TAG)
            if [[ -n "$QUARKUS_VERSION_TAG" ]]; then
                MF_VERSION="$FINAL_VERSION-$QUARKUS_VERSION_TAG-$DATE"
            else
                MF_VERSION="$FINAL_VERSION-$DATE"
            fi
            ;;
        *)
            echo "ERROR: Invalid version type specified. Allowed values: master, SNAPSHOT, TAG."
            exit 1
            ;;
    esac
else
    # Local execution: Use current date and time, and include Quarkus version at the beginning if available
    TIMESTAMP=$(date +%H-%M)
    if [[ -n "$QUARKUS_VERSION_TAG" ]]; then
        MF_VERSION="$QUARKUS_VERSION_TAG-$(date +%Y%m%d)-$TIMESTAMP-LOCAL"
    else
        MF_VERSION="$(date +%Y%m%d)-$TIMESTAMP-LOCAL"
    fi
fi

# Replace '/' with '-' in the version string
MF_VERSION=${MF_VERSION//\//-}

echo "Setting version to: ${MF_VERSION}"

# Set the Maven version
if [[ "$GH_FLAG" == "gh" ]]; then
    mvn -DnewVersion=$MF_VERSION versions:set -s settings.xml && \
    mvn versions:commit -s settings.xml
else
    mvn -DnewVersion=$MF_VERSION versions:set && \
    mvn versions:commit
fi

# Export MF_VERSION to be available for subsequent GitHub Actions steps
if [[ -n "$GITHUB_ENV" ]]; then
    echo "MF_VERSION=${MF_VERSION}" >> "$GITHUB_ENV"
else
    echo "GITHUB_ENV not set, proceeding without exporting MF_VERSION."
fi
