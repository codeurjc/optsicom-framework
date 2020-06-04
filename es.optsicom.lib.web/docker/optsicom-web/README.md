

## Environment variables

### Database configuration
- **OPTSICOM_DB_MODE**: [mysql | derby]
- **OPTSICOM_DB_HOST**:
- **OPTSICOM_DB_PORT**:
- **OPTSCIOM_DB_SCHEMA**:
- **OPTSCIOM_DB_USER**:
- **OPTSCIOM_DB_SECRET**:

### Web credentials
- **OPTSICOM_API_USER**:
- **OPTSICOM_API_SECRET**:

## Database MySQL

```bash
docker run -d --rm --name optsicom-database \
        -p 3306:3306 \
        -e MYSQL_USER=root \
        -e MYSQL_ROOT_PASSWORD=0pts1c0m \
        -e MYSQL_DATABASE=optsicom \
        mysql:8.0.20 --character-set-server=utf8 --collation-server=utf8_general_ci
```

## Optsicom Web

```bash
docker run -d --rm --name optsicom-web \
        --network host \
        -p 5000:5000 \
        -e OPTSICOM_DB_MODE=mysql \
        -e OPTSICOM_DB_HOST=localhost \
        -e OPTSICOM_DB_PORT=3306 \
        -e OPTSICOM_DB_SCHEMA=optsicom \
        -e OPTSICOM_DB_USER=root \
        -e OPTSCIOM_DB_SECRET=0pts1c0m \
        -e OPTSICOM_API_USER=researcher \
        -e OPTSICOM_API_SECRET=0pts1c0m \
        codeurjc/optsicom-web
```
