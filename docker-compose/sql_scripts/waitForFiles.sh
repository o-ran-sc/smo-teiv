#!/bin/sh
FILE_PATH="/docker-entrypoint-initdb.d/00_init-teiv-exposure-model.sql"
FILE_PATH1="/docker-entrypoint-initdb.d/01_init-teiv-exposure-data.sql"
FILE_PATH2="/docker-entrypoint-initdb.d/02_init-teiv-exposure-consumer-data.sql"

echo "Waiting for sql files to be available..."

while [ ! -f "$FILE_PATH" ] || [ ! -f "$FILE_PATH1" ] || [ ! -f "$FILE_PATH2" ]; do
  echo "Waiting for sql files to be available..."
  sleep 1
done

echo "Files found! Starting Postgres..."
docker-entrypoint.sh postgres