#!/bin/bash

# Define default values
pattern="^mf\.conf\.cached-service\.[^.]*\.generation="
path_filter=""
edit_mode=false
new_value=""

# Function to display usage
usage() {
    echo "Usage: $0 [-d <path_filter>] [-e <new_value>]"
    echo "  -d <path_filter>  : Filter files by path containing this pattern"
    echo "  -e <new_value>    : Edit matching keys to set new value"
    echo "Example             : ./edit-cache-generation.sh -d \"/src/main/resources/\" -e 20250319_1"
    exit 1
}

# Parse arguments
while [[ $# -gt 0 ]]; do
    case "$1" in
        -d)
            path_filter="$2"
            shift 2
            ;;
        -e)
            edit_mode=true
            new_value="$2"
            shift 2
            ;;
        *)
            usage
            ;;
    esac
done

# Find all application.properties files recursively
find . -type f -name "application.properties" | while read -r file; do
    # Apply path filtering if specified
    if [[ -n "$path_filter" && "$file" != *"$path_filter"* ]]; then
        continue
    fi

    if [[ "$edit_mode" == "true" ]]; then
        # Create a temp file for modification
        awk -v new_value="$new_value" '
        BEGIN { OFS=FS="="; changed=0 }
        /^mf\.conf\.cached-service\.[^.]*\.generation=/ { $2=new_value; changed=1 }
        { print }
        END { exit changed ? 0 : 1 }
        ' "$file" > "$file.tmp"

        # Only replace the file if changes were made (avoids unnecessary writes)
        if [[ $? -eq 0 ]]; then
            mv "$file.tmp" "$file"
            echo "Updated values in $file"
        else
            rm "$file.tmp"  # Clean up temp file if no change
        fi
    else
        # Print matching lines
        grep -E "$pattern" "$file" && echo "Found in $file"
    fi
done