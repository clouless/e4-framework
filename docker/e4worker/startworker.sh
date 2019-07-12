#!/bin/bash
ssh e4w 'bash -s' < startworker-local.sh $1 $2
sleep 3
ssh -t e4w 'docker logs --follow $(docker ps -q)'