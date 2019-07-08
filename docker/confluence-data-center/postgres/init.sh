#!/bin/bash

# Given by --env: $CONFLUENCE_VERSION
CONFLUENCE_VERSION_DOT_FREE=${CONFLUENCE_VERSION//\./}

if [ -f /e4prov/conf${CONFLUENCE_VERSION_DOT_FREE}/confluence.sql ];
then
  echo ">> postgres-init: Dump file found. Restore: /e4prov/conf${CONFLUENCE_VERSION_DOT_FREE}/confluence.sql"
  psql -U confluence -d confluence < /e4prov/conf${CONFLUENCE_VERSION_DOT_FREE}/confluence.sql
else
  echo ">> postgres-init: No dump file found starting from scratch."
fi
