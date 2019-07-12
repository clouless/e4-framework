#!/bin/bash
ssh -t e4w 'docker logs --follow $(docker ps -q)'
