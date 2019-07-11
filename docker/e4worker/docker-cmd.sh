E4_DIR="/tmp/e4"
function e4run {
  if [ -z "$1" ]
    then
    echo "Error: port must be given as argument"
  else
    docker run -d \
    --add-host=confluence-cluster-6153-lb:34.209.135.98 \
    --shm-size=2048m \
     -v "$E4_DIR:$E4_DIR" \
     -p "$1:$1" \
     -e E4_PORT="$1" \
     -e E4_JAR_URL='https://www.dropbox.com/s/bzbellcrvyb6a2u/e4-LATEST.jar?dl=1' \
     -e E4_OUTPUT_DIR="$E4_DIR/out/$1" \
     fgrund/e4worker:0.2
  fi
}
