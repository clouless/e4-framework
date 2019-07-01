FROM codeclou/docker-atlassian-confluence-data-center:confluencenode-6.15.3

RUN apt-get update && apt-get -y install net-tools

COPY provision/confluence-home-smallds /confluence-home
COPY provision/confluence-shared-home-smallds /confluence-shared-home
COPY docker-entrypoint.sh /work-private/docker-entrypoint.sh
