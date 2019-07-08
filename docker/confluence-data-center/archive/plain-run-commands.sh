
#
# NETWORK
#
create_network

#
# DATABASE
#
docker run \
    --sysctl kernel.shmmax=100663296 \
    --rm \
    --name confluence-cluster-6153-db \
    --net=confluence-cluster-6153 \
    --net-alias=confluence-cluster-6153-db \
    -e POSTGRES_PASSWORD=confluence \
    -e POSTGRES_USER=confluence \
    -it fgrund/postgres:9.6-smalldataset -c max_connections=300 -c shared_buffers=80MB

#
# LOADBALANCER
#
docker run \
    --rm \
    --name confluence-cluster-6153-lb \
    --net=confluence-cluster-6153 \
    --net-alias=confluence-cluster-6153-lb \
    --env NODES=1 \
    -v $(pwd)/loadbalancer:/e4work \
    --entrypoint /e4work/docker-entrypoint.sh \
    -p $CONFLUENCE_LB_PUBLIC_PORT:$CONFLUENCE_LB_PUBLIC_PORT \
    -d fgrund/docker-atlassian-confluence-data-center:loadbalancer-6.15.3

#
# CONFLUENCE NODE
#

docker run \
    --rm \
    --name confluence-cluster-6153-node1 \
    --net=confluence-cluster-6153 \
    --net-alias=confluence-cluster-6153-node1 \
    --env NODE_NUMBER=1 \
    -v confluence-shared-home-6153:/confluence-shared-home \
    -p 5005:5005 \
    -p 4330:4330 \
    -v $(pwd)/confluencenode:/work-my \
    --entrypoint /work-my/docker-entrypoint.sh \
    -it codeclou/docker-atlassian-confluence-data-center:confluencenode-6.15.3
