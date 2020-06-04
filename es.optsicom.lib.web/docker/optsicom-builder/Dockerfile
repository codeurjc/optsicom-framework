FROM ubuntu:18.04

# Locales UTF-8
RUN apt-get update \
    && apt-get install -y --no-install-recommends debconf \
    locales \
    locales-all \
    && apt-get clean \
    && apt-get autoclean \
    && rm -Rf /tmp/* \
    && rm -Rf /var/lib/apt/lists/*

ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8

# Install necessaries software
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    wget \
    git \
    build-essential \
    maven \
    apt-transport-https \
    ca-certificates \
    gnupg-agent \
    software-properties-common \
    openjdk-8-jdk \
    && apt-get clean \
    && apt-get autoclean \
    && rm -Rf /tmp/* \
    && rm -Rf /var/lib/apt/lists/*

RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add - \
    && apt-key fingerprint 0EBFCD88 \
    && add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
    $(lsb_release -cs) \
    stable" \
    && apt-get update \
    && apt-get install -y --no-install-recommends docker-ce \
    docker-ce-cli \
    containerd.io \
    && apt-get clean \
    && apt-get autoclean \
    && rm -Rf /tmp/* \
    && rm -Rf /var/lib/apt/lists/*

# Installing last node version
RUN curl -sL https://deb.nodesource.com/setup_10.x | bash - && \
    apt-get install -y --no-install-recommends nodejs \
    && apt-get clean \
    && apt-get autoclean \
    && rm -Rf /tmp/* \
    && rm -Rf /var/lib/apt/lists/*

# Create workdir width user permissions
RUN mkdir /workdir
RUN chown 1000:1000 /workdir

# Define enviroment variables
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Define workdir
WORKDIR /workdir
