#!/bin/bash
ssh -t e4w 'docker kill $(docker ps -q)'
