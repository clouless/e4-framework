FROM openjdk:8u171

VOLUME /tmp/e4

RUN apt-get update && apt-get install -y \
    software-properties-common \
    unzip \
    curl \
    xvfb

# Chrome browser to run the tests
RUN curl https://dl-ssl.google.com/linux/linux_signing_key.pub -o /tmp/google.pub \
    && cat /tmp/google.pub | apt-key add -; rm /tmp/google.pub \
    && echo 'deb http://dl.google.com/linux/chrome/deb/ stable main' > /etc/apt/sources.list.d/google.list \
    && mkdir -p /usr/share/desktop-directories \
    && apt-get -y update && apt-get install -y google-chrome-stable
# Disable the SUID sandbox so that chrome can launch without being in a privileged container
RUN dpkg-divert --add --rename --divert /opt/google/chrome/google-chrome.real /opt/google/chrome/google-chrome \
    && echo "#!/bin/bash\nexec /opt/google/chrome/google-chrome.real --no-sandbox --disable-setuid-sandbox \"\$@\"" > /opt/google/chrome/google-chrome \
    && chmod 755 /opt/google/chrome/google-chrome

# Chrome Driver
RUN mkdir -p /opt/selenium \
    && curl http://chromedriver.storage.googleapis.com/2.45/chromedriver_linux64.zip -o /opt/selenium/chromedriver_linux64.zip \
    && cd /opt/selenium; unzip /opt/selenium/chromedriver_linux64.zip; rm -rf chromedriver_linux64.zip; ln -fs /opt/selenium/chromedriver /usr/local/bin/chromedriver;

## E4

CMD mkdir -p $E4_OUTPUT_DIR && mkdir -p $E4_INPUT_DIR \
 && java -jar $E4_INPUT_DIR/e4.jar -p $E4_PORT --worker-only --output-dir $E4_OUTPUT_DIR --input-dir $E4_INPUT_DIR