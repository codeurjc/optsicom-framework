FROM openjdk:8

RUN useradd -m optsicom
USER optsicom
COPY entrypoint.sh /home/optsicom
COPY optsicomweb.jar /home/optsicom
COPY wait-for-it.sh /home/optsicom
WORKDIR /home/optsicom

ENTRYPOINT ["/home/optsicom/entrypoint.sh"]
CMD ["java", "-jar", "/home/optsicom/optsicomweb.jar"]