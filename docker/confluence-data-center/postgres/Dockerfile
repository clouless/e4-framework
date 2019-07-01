FROM postgres:9.6

COPY $E4_PROV_SMALLDS_SQL /tmp/
COPY init.sh /docker-entrypoint-initdb.d/
