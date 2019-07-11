E4_DIR="/tmp/e4"
function e4run {
	if [ "$#" -ne 2 ]; then
    echo "Usage: e4run WORKER_PORT TARGET_SYSTEM_IP"
  else
    docker run -d \
      --add-host="confluence-cluster-6153-lb:$2" \
      --shm-size=2048m \
      -v "$E4_DIR:$E4_DIR" \
      -p "$1:$1" \
      -e E4_PORT="$1" \
      -e E4_JAR_URL='https://www.dropbox.com/s/t6r02bteq63naf2/e4-LATEST.jar?dl=1' \
      -e E4_OUTPUT_DIR="$E4_DIR/out/$1" \
      -e E4_INPUT_DIR="$E4_DIR/in" \
      -e E4_LICENSE_CONF="AAABLw0ODAoPeNp9kF9PgzAUxd/7KZr4og8sg8mYS0g0QJQEmJHpky933WU2YWXpn2V8ewt1mZroW3tue37nnquyE7SEngYR9RfLMFqGM5qkaxpM/TuSdEID0xXsMW6w5afJThqxvVcMxJZ3ky0S1olmYt/wI8ZaGiTPRrIPUJiCxnhw8aahFyxIwRkKhdnpwGX/bTj3gugMykrg7b8k+xESFBqlo9Vmo5jkB8074RRrYccCBPuDNfpUZr9BuWpeFUoVe75Taw1ysG6gVXhOnKdxkad1VnmFP5tHt5G/IPYW/1RWcgeCKxiD1C41fdxvnkgicVR/FzISvxjr/oBjy8mqLLOXJH8oSOtGbzbg4BmQFC+r2sKa1qBdkl4PnVBXys37kmZHaM1IJJej6+YTcDek9jAsAhQMjndzQwNXokcsfeEbtiQJn5ZfSwIUaMVmEklmaIX9V0zou8i5649ihAg=X02f7" \
      -e E4_LICENSE_LIVELY_THEME="AAABVw0ODAoPeNqVkTtPwzAUhXf/CkssMCRKnfJoJUugNAKkpkUkZWJxnZvWkuNEflTk3+OkCQIxdfBwfexzPh9fFUeHM9ZhEuNosYwflnOCk1WBSTRboI2r96C31c6ANjSYoRUYrkVrRaPoWpxAdrg4Qg24ajROGlVJB4r/G69z0CfQN3inpKiFhRIPlp9Lf6quQXPBJF4LDsoASjSwPmHFLNCeI4huAzJH3tAybjesBpqfQOGcH5m1oCclzZiQ1HgpNKP0aDhTpWjCElB6YtINzrRi0gfJc+CHJ+k3CfL3lQXFPHP61QrdjQiEDAgx2uoDU8KcTfKzNX6u9y+ohHCK4j8PD1vpDkKZUA5dBbbvKvSkfqRWO0B5uqF+BbN5RKL7u8UlPuBZdauFGb3G/n6Ru6nuSSu6Fob+km2Wpe/J69P6ksTcMu0zx/renPYtG/j7TzH6BksJ0UIwLQIVAI/gRQnCqAOX30r3aZb+N1fXz7ENAhQzYxdt+B+ssfV84G0aaF51bLxEDA==X02h1" \
      fgrund/e4worker:0.2
  fi
}
