# Multistage compile Frontend Application
FROM node:lts-alpine3.13 as frontend-build

WORKDIR /build/frontend

# Download Dependencies
COPY es.optsicom.lib.web/src/web/package.json /build/frontend/
COPY es.optsicom.lib.web/src/web/package-lock.json /build/frontend/
RUN npm install

# Compile Project
COPY es.optsicom.lib.web/src/web /build/frontend
RUN npx ng build --prod

####

# Multistage compile Backend Aplication
FROM maven:3.6.3-openjdk-8 as backend-build

# Download Dependencies
COPY es.optsicom.lib/pom.xml /build/backend/es.optsicom.lib/
COPY es.optsicom.lib.analysis/pom.xml /build/backend/es.optsicom.lib.analysis/
COPY es.optsicom.lib.approx/pom.xml /build/backend/es.optsicom.lib.approx/
COPY es.optsicom.lib.exact/pom.xml /build/backend/es.optsicom.lib.exact/
COPY es.optsicom.lib.npst/pom.xml /build/backend/es.optsicom.lib.npst/
COPY es.optsicom.lib.parent/pom.xml /build/backend/es.optsicom.lib.parent/
COPY es.optsicom.lib.util/pom.xml /build/backend/es.optsicom.lib.util/
COPY es.optsicom.lib.web/pom.xml /build/backend/es.optsicom.lib.web/

WORKDIR /build/backend
RUN cd es.optsicom.lib.parent \
    && mvn --batch-mode clean verify -DskipTests

# Compile Project
COPY es.optsicom.lib /build/backend/es.optsicom.lib/
COPY es.optsicom.lib.analysis /build/backend/es.optsicom.lib.analysis/
COPY es.optsicom.lib.approx /build/backend/es.optsicom.lib.approx/
COPY es.optsicom.lib.exact /build/backend/es.optsicom.lib.exact/
COPY es.optsicom.lib.npst /build/backend/es.optsicom.lib.npst/
COPY es.optsicom.lib.parent /build/backend/es.optsicom.lib.parent/
COPY es.optsicom.lib.util /build/backend/es.optsicom.lib.util/
COPY es.optsicom.lib.web /build/backend/es.optsicom.lib.web/
COPY --from=frontend-build /build/frontend/dist/optsicom-page \
    /build/backend/es.optsicom.lib.web/src/main/resources/static

WORKDIR /build/backend
RUN cd es.optsicom.lib.parent \
    && mvn --batch-mode -o package -DskipTests \
    && cd ../es.optsicom.lib.web/target \
    && mv es.optsicom.lib.web-*SNAPSHOT.jar optsicom-web.jar

###

# Optsicom Web Image
FROM openjdk:8-alpine

# Copy necessaries files
COPY es.optsicom.lib.web/docker/optsicom-web/entrypoint.sh /usr/local/bin
COPY --from=backend-build /build/backend/es.optsicom.lib.web/target/optsicom-web.jar \
    /opt/optsicom-web/

EXPOSE 5000

WORKDIR /opt/optsicom-web

CMD ["/usr/local/bin/entrypoint.sh"]
