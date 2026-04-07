#!/bin/bash

# Convert Render's postgres:// DATABASE_URL to jdbc:postgresql:// format
if [ -n "$DATABASE_URL" ]; then
  # Replace postgres:// with jdbc:postgresql://
  export DATABASE_URL=$(echo "$DATABASE_URL" | sed 's|postgres://|jdbc:postgresql://|' | sed 's|postgresql://|jdbc:postgresql://|')
  echo "DATABASE_URL configured for JDBC"
fi

exec java -jar app.jar
