FROM postgres:9.6

COPY provision/confluence-smallds.sql /tmp/
COPY init.sh /docker-entrypoint-initdb.d/
