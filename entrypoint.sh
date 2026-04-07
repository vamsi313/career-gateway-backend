#!/bin/bash

# Parse Render's DATABASE_URL and convert to Spring Boot format
# Render format: postgres://user:password@host:port/dbname
# Spring Boot needs: jdbc:postgresql://host:port/dbname (with separate user/pass)

if [ -n "$DATABASE_URL" ]; then
  # Remove postgres:// or postgresql:// prefix
  DB_STRING=$(echo "$DATABASE_URL" | sed 's|^postgres://||' | sed 's|^postgresql://||')
  
  # Extract user:password and host:port/dbname
  USERPASS=$(echo "$DB_STRING" | cut -d'@' -f1)
  HOSTDB=$(echo "$DB_STRING" | cut -d'@' -f2)
  
  # Extract individual components
  export DATABASE_USERNAME=$(echo "$USERPASS" | cut -d':' -f1)
  export DATABASE_PASSWORD=$(echo "$USERPASS" | cut -d':' -f2)
  
  # Build JDBC URL without credentials
  export DATABASE_URL="jdbc:postgresql://${HOSTDB}"
  
  # Also set Spring's native properties
  export SPRING_DATASOURCE_URL="$DATABASE_URL"
  export SPRING_DATASOURCE_USERNAME="$DATABASE_USERNAME"
  export SPRING_DATASOURCE_PASSWORD="$DATABASE_PASSWORD"
  
  echo "Database configured: jdbc:postgresql://${HOSTDB}"
fi

exec java -jar app.jar
