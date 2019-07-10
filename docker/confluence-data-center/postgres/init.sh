#!/bin/bash

# Given by --env: $E4_PROV_KEY

if [ -f /e4prov/$E4_PROV_KEY/confluencedb.tar.gz ];
then
  echo ">> postgres-init: Dump file found. Restore: /e4prov/$E4_PROV_KEY/confluencedb.tar.gz"
  pg_restore -U confluence -d confluence /e4prov/$E4_PROV_KEY/confluencedb.tar.gz
else
  echo ">> postgres-init: No dump file found starting from scratch."
fi
