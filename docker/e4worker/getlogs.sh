#!/bin/bash
ssh -t $1 'docker logs  $(docker ps -q)' > "confluence-log-$(date +%s).log"
ssh -t awse4 'rm e4.log'
ssh -t awse4 'docker cp $(docker ps -qf "name=confluence-cluster-6153-lb"):/var/www/logs/e4.log .'
scp awse4:e4.log "access-log-$(date +%s).log"
scp e4w:/tmp/e4/out/$2/e4\*.sqlite .
grep /rest access-log-*.log > access-log-rest.log
