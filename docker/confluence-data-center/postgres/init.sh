#!/bin/bash

# Given by --env: $CONFLUENCE_VERSION
CONFLUENCE_VERSION_DOT_FREE=${CONFLUENCE_VERSION//\./}

if [ -f /e4prov/$E4_PROV_KEY/confluencedb.tar.gz ];
then
  echo ">> postgres-init: Dump file found. Restore: /e4prov/$E4_PROV_KEY/confluencedb.tar.gz"
  pg_restore -U confluence -d confluence /e4prov/$E4_PROV_KEY/confluencedb.tar.gz
else
  echo ">> postgres-init: No dump file found starting from scratch."
fi
