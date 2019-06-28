#!/bin/bash

####################################################################################
# MIT License
# Copyright (c) 2017 Bernhard Grünewaldt
# See https://github.com/codeclou/docker-atlassian-confluence-data-center/blob/master/LICENSE
####################################################################################

set -e


####################################################################################
#
# VERSION
#
####################################################################################
# keep in sync with 'manage-confluence-cluster-6.15.3-version.txt'
MANAGEMENT_SCRIPT_VERSION=3


####################################################################################
#
# CONFIG
#
####################################################################################

CONFLUENCE_VERSION="6.15.3"
CONFLUENCE_VERSION_DOT_FREE="6153"
CONFLUENCE_LB_PUBLIC_PORT=26153
POSTGRESQL_VERSION="9.6"

####################################################################################
#
# COLORS
#
####################################################################################

export CLICOLOR=1
C_RED='\x1B[31m'
C_CYN='\x1B[96m'
C_GRN='\x1B[32m'
C_MGN='\x1B[35m'
C_RST='\x1B[39m'

####################################################################################
#
# FUNCTIONS
#
####################################################################################

# Used to be able to use pass-by-reference in bash
#
#
return_by_reference() {
    if unset -v "$1"; then
        eval $1=\"\$2\"
    fi
}

# Returns 1 if the container is running and 0 if not
#
# @usage `_is_named_container_running 'foo' $result` passed variable `result` contains return value
#
# @param $1 {string} name or name-chunk of container
# @param $2 {int} return value passByReference
function _is_named_container_running {
    local ret_value=$(docker ps --format '{{.ID}}' --filter "name=${1}" | wc -l | awk '{print $1}')

    local "$2" && return_by_reference $2 $ret_value
}

# Kill and remove an existing named container
#
# @param $1 {string} the container name or part of it
function _kill_and_remove_named_instance_if_exists {
    container_name=$1
    # KILL
    named_container_running_result=-1
    _is_named_container_running ${container_name} named_container_running_result
    if (( named_container_running_result == 1 )) # arithmetic brackets ... woohoo
    then
        echo -e $C_CYN">> docker kill ........:${C_RST}${C_GRN} Killing${C_RST}   - Named container ${container_name} is running."
        docker kill ${container_name}
    else
        echo -e $C_CYN">> docker kill ........:${C_RST}${C_MGN} Skipping${C_RST}  - Named container ${container_name} is not running."
    fi
}

# Start the loadbalancer instance
#
# @param $1 {integer} amount of confluencenodes
function start_instance_loadbalancer {
    echo -e $C_CYN">> docker run .........:${C_RST}${C_GRN} Starting${C_RST}  - Starting instance confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-lb."
    docker run \
        --rm \
        --name confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-lb \
        --net=confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE} \
        --net-alias=confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-lb \
        --env NODES=${1} \
        -p $CONFLUENCE_LB_PUBLIC_PORT:$CONFLUENCE_LB_PUBLIC_PORT \
        -d fgrund/docker-atlassian-confluence-data-center:loadbalancer-${CONFLUENCE_VERSION}
}

# Kill the loadbalancer instance
#
#
function kill_instance_loadbalancer {
    _kill_and_remove_named_instance_if_exists confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-lb
}

# Start the database instance
#
#
function start_instance_database {
    echo -e $C_CYN">> docker run .........:${C_RST}${C_GRN} Starting${C_RST}  - Starting instance confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-db."
    docker run \
        --rm \
        --name confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-db \
        --net=confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE} \
        --net-alias=confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-db \
        -e POSTGRES_PASSWORD=confluence \
        -e POSTGRES_USER=confluence \
        -d postgres:${POSTGRESQL_VERSION}
}

# Kill the database instance
#
#
function kill_instance_database {
    _kill_and_remove_named_instance_if_exists confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-db
}

# Start a confluencenode instance
#
# @param $1 {int} node ID
function start_instance_confluencenode {
    echo -e $C_CYN">> docker run .........:${C_RST}${C_GRN} Starting${C_RST}  - Starting instance confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-node${1}."
    docker run \
        --rm \
        --name confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-node${1} \
        --net=confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE} \
        --net-alias=confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-node${1} \
        --env NODE_NUMBER=${1} \
        -v confluence-shared-home-${CONFLUENCE_VERSION_DOT_FREE}:/confluence-shared-home \
        -d codeclou/docker-atlassian-confluence-data-center:confluencenode-${CONFLUENCE_VERSION}
}

# Kill a confluencenode instance
#
# @param $1 {int} node ID
function kill_instance_confluencenode {
    _kill_and_remove_named_instance_if_exists confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-node${1}
}

# Cleans the confluence shared-home
#
#
function clean_confluencenode_shared_home {
    local volume_name=confluence-shared-home-${CONFLUENCE_VERSION_DOT_FREE}
    shared_home_exists=$(docker volume ls --filter "name=${volume_name}" --format '{{.Name}}' | wc -l | awk '{print $1}')
    if (( shared_home_exists == 1 )) # arithmetic brackets ... woohoo
    then
        echo -e $C_CYN">> clean shared home ..:${C_RST}${C_GRN} Deleting${C_RST}  - Deleting existing volume ${volume_name}"
        docker volume rm --force ${volume_name}
    fi
    echo -e $C_CYN">> clean shared home ..:${C_RST}${C_GRN} Creating${C_RST}  - Creating volume ${volume_name}"
    docker volume create ${volume_name}
}


# Creates a network for cluster
#
#
function create_network {
    local network_name=confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}
    network_exists=$(docker network ls --filter "name=${network_name}" --format '{{.Name}}' | wc -l | awk '{print $1}')
    if (( network_exists == 1 )) # arithmetic brackets ... woohoo
    then
        echo -e $C_CYN">> docker network .....:${C_RST}${C_MGN} Skipping${C_RST}  - Network ${network_name} exists already."
    else
        echo -e $C_CYN">> docker network .....:${C_RST}${C_GRN} Creating${C_RST}  - Creating network ${network_name}."
        docker network create ${network_name}
    fi
}

# Returns the count of running confluencenode instances
#
# @usage
#     result=-1
#     get_running_confluencenode_count result
#
# @param $1 {int} return value passByReference
function get_running_confluencenode_count {
    local ret_value=-1
    _is_named_container_running "confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-node" ret_value

    local "$1" && return_by_reference $1 $ret_value
}

# Returns an array of docker image names of running confluencenode instances
#
# @usage
#     result=""
#     get_running_confluencenode_name_array result
#
# @param $1 {string} return value passByReference in form of "node1 node2 node"
function get_running_confluencenode_name_array {
    local instance_names_string_newlines=$(docker ps --format '{{.Names}}' --filter "name=confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-node")
    local instance_names_string_oneline=$(echo $instance_names_string_newlines | tr "\n" " ")
    local ret_value=$instance_names_string_oneline

    local "$1" && return_by_reference $1 "$ret_value"
}

# Kills and removes all confluence-cluster-node* instances if present
#
#
function kill_all_running_confluencenodes {
    # NOTE: We must get all names to kill them, since e.g. node1,node4,node5 could be running
    #       so we cannot just use a dumb counter starting from 1!
    local running_confluencenode_count=0
    get_running_confluencenode_count running_confluencenode_count
    if (( running_confluencenode_count > 0 )) # arithmetic brackets ... woohoo
    then
        echo -e $C_CYN">> docker kill nodes ..:${C_RST}${C_GRN} Killing${C_RST}   - Killing all running confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-node* instances."
        local running_instance_names=""
        get_running_confluencenode_name_array running_instance_names
        local running_instance_names_array=($running_instance_names)
        for running_instance_name in "${running_instance_names_array[@]}"
        do
           _kill_and_remove_named_instance_if_exists ${running_instance_name}
        done
    else
        echo -e $C_CYN">> docker kill nodes ..:${C_RST}${C_MGN} Skipping${C_RST}  - No running confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-node* instances present."
    fi
}

# Removes all images with confluence-cluster-node* name
#
#
function remove_all_dangling_confluencenodes {
    echo -e $C_CYN">> docker rm images ...:${C_RST}${C_GRN} Removing${C_RST}  - Removing dangling confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-node* images."
    local dangling_ids=$(docker images | grep confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-node | awk '{ print $3 }')
    for dangling_id in $dangling_ids
    do
        docker rm $dangling_id
    done
}

# Prints info
#
#
function print_cluster_ready_info {
    echo -e $C_CYN">> ----------------------------------------------------------------------------------------------------"$C_RST
    echo -e $C_CYN">> info ...............:${C_RST}${C_GRN} Ready${C_RST}     - Wait for Confluence nodes to startup, might take some minutes."
    echo -e $C_CYN">> info ...............:${C_RST}${C_GRN} http://confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-lb:${CONFLUENCE_LB_PUBLIC_PORT}${C_RST} "
    echo -e $C_CYN">> info ...............:${C_RST} Do not forget to:"
    echo -e $C_CYN">> info ...............:${C_RST}   [1] put '127.0.0.1 confluence-cluster-${CONFLUENCE_VERSION_DOT_FREE}-lb' to /etc/hosts."
    echo -e $C_CYN">> info ...............:${C_RST}   [2] enable IP Forwarding to support multicast."
    echo -e $C_CYN">> ----------------------------------------------------------------------------------------------------"$C_RST
}

create_network
kill_all_running_confluencenodes
remove_all_dangling_confluencenodes
clean_confluencenode_shared_home
kill_instance_database
start_instance_database
kill_instance_loadbalancer
start_instance_loadbalancer 1
sleep 10
kill_instance_confluencenode 1
start_instance_confluencenode 1
print_cluster_ready_info