FROM codeclou/docker-atlassian-confluence-data-center:confluencenode-6.15.3

RUN apt-get update && apt-get -y install net-tools

COPY provision/confluence-home-smallds /confluence-home-smallds
COPY provision/confluence-shared-home-smallds /confluence-shared-home-smallds
COPY docker-entrypoint.sh /work-private/docker-entrypoint.sh
