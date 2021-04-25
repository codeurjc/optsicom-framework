
# Optsicom Web

## Environment variables

Optsicom Web is available the following enviroment variables. This variables are necessary for configure the Database and User/Password credentials.

### Database configuration
- **OPTSICOM_DB_MODE**: [mysql | derby] This variable define the Databse that use the Optsicom Web. If you selected mysql mode you need configure "OPTSICOM_DB_HOST, OPTSICOM_DB_PORT, OPTSICOM_DB_SCHEMA, OPTSICOM_DB_USER, OPTSICOM_DB_SECRET" for the MySQL Database. If you selected derby mode is not need to configure any more variables.
- **OPTSICOM_DB_HOST**: This variable define the IP of MySQL
- **OPTSICOM_DB_PORT**: This variable define the PORT of MySQL
- **OPTSICOM_DB_SCHEMA**: This variable define the Schema of MySQL
- **OPTSICOM_DB_USER**: This variable define the default USER of MySQL
- **OPTSICOM_DB_SECRET**: This variable define the PASSWORD of MySQL

### Web credentials
- **OPTSICOM_API_USER**: This variable configures the USER to be use in Optsicom Web.
- **OPTSICOM_API_SECRET**: This variable configures the PASSWORD to be use in Optsicom Web.

# Execution Example

The current version of Optsicom Web can be launched in MySQL mode or Derby mode. If you selected the MySQL mode you need a database with utf8 activated by default. In the next section we can observe a simple execution of a MySQL database in version 8.0.20 using docker.

## Database MySQL

```bash
docker run -d --rm --name optsicom-database \
        -p 3306:3306 \
        -e MYSQL_USER=root \
        -e MYSQL_ROOT_PASSWORD=MY_SECRET \
        -e MYSQL_DATABASE=optsicom \
        mysql:8.0.20 --character-set-server=utf8 --collation-server=utf8_general_ci
```

## Optsicom Web

For launched Optsicom Web using docker use the following command.

```bash
docker run -d --rm --name optsicom-web \
        --network host \
        -p 5000:5000 \
        -e OPTSICOM_DB_MODE=mysql \
        -e OPTSICOM_DB_HOST=localhost \
        -e OPTSICOM_DB_PORT=3306 \
        -e OPTSICOM_DB_SCHEMA=optsicom \
        -e OPTSICOM_DB_USER=root \
        -e OPTSICOM_DB_SECRET=MY_SECRET \
        -e OPTSICOM_API_USER=researcher \
        -e OPTSICOM_API_SECRET=MY_SECRET \
        codeurjc/optsicom-web
```

If the application has time problems when answering we can pass the following variable to the JVM. References:

- https://docs.oracle.com/cd/E37116_01/install.111210/e23737/configuring_jvm.htm#OUDIG00058
- https://cwiki.apache.org/confluence/display/TOMCAT/HowTo+FasterStartUp#HowToFasterStartUp-EntropySource
- https://blog.longyb.com/2019/06/09/tomcat_hang_creation_of_securerandom_instance_for_sessionid_english/

```bash
JAVA_OPTIONS=-Djava.security.egd=file:/dev/./urandom
```
