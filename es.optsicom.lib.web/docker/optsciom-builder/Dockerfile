FROM ubuntu:18.04

RUN apt-get update && \
    apt-get install -y curl \
    wget \
    git \
    build-essential \
    maven \
    openjdk-8-jdk

# Installing locales UTF-8
RUN apt-get install -y locales locales-all
ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8

# Installing last version node
RUN curl -sL https://deb.nodesource.com/setup_10.x | bash - && \
    apt-get install -y nodejs

# Create workdir width user permissions
RUN mkdir /workdir
RUN chown 1000:1000 /workdir

# Cleaning the house
RUN apt-get clean && \
    apt-get autoclean && \
    rm -Rf /tmp/* && \
    rm -Rf /var/lib/apt/lists/*

# Define workdir
WORKDIR /workdir
