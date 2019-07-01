FROM codeclou/docker-atlassian-confluence-data-center:confluencenode-6.15.3

RUN apt-get update && apt-get -y install net-tools

COPY $E4_PROV_SMALLDS_HOME /confluence-home
COPY $E4_PROV_SMALLDS_SHARED_HOME /confluence-shared-home
COPY docker-entrypoint.sh /work-private/docker-entrypoint.sh