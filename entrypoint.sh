#!/bin/bash

# Parse Render's DATABASE_URL and set Spring Boot properties
# Render may provide: postgres://user:password@host:port/dbname
# Or it may already be: jdbc:postgresql://host/dbname (if manually set)

if [ -n "$DATABASE_URL" ]; then
  # Check if it's already in JDBC format
  if echo "$DATABASE_URL" | grep -q "^jdbc:"; then
    # Already JDBC format, use as-is
    export SPRING_DATASOURCE_URL="$DATABASE_URL"
    echo "Database URL already in JDBC format"
  else
    # Render format: postgres://user:password@host:port/dbname
    DB_STRING=$(echo "$DATABASE_URL" | sed 's|^postgres://||' | sed 's|^postgresql://||')
    
    USERPASS=$(echo "$DB_STRING" | cut -d'@' -f1)
    HOSTDB=$(echo "$DB_STRING" | cut -d'@' -f2)
    
    export SPRING_DATASOURCE_USERNAME=$(echo "$USERPASS" | cut -d':' -f1)
    export SPRING_DATASOURCE_PASSWORD=$(echo "$USERPASS" | cut -d':' -f2)
    export SPRING_DATASOURCE_URL="jdbc:postgresql://${HOSTDB}"
    
    echo "Database configured: jdbc:postgresql://${HOSTDB}"
  fi
fi

# Ensure Spring Boot uses the correct values
if [ -n "$DATABASE_USERNAME" ]; then
  export SPRING_DATASOURCE_USERNAME="$DATABASE_USERNAME"
fi
if [ -n "$DATABASE_PASSWORD" ]; then
  export SPRING_DATASOURCE_PASSWORD="$DATABASE_PASSWORD"
fi

echo "Starting application..."
exec java -jar app.jar
