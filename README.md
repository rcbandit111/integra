# integra-service

Quarkus microservice with PostgreSQL, Flyway, and jOOQ.

## Run

```bash
cd /home/plte1217/Desktop/git/integra
# Optional: start Postgres manually (dev mode uses Quarkus Dev Services by default)
docker compose up -d
mvn quarkus:dev
```

Prod profile (uses docker-compose Postgres):

```bash
mvn quarkus:dev -Dquarkus.profile=prod
```


## API docs

- Swagger UI: http://localhost:8080/q/swagger-ui
- OpenAPI spec: http://localhost:8080/q/openapi

## Try it

```bash
curl -X POST localhost:8080/api/partnership-requests -H 'Content-Type: application/json' -d '{"partnerType":"reseller","firstName":"Ada","lastName":"Lovelace","email":"ada@example.com","companyName":"Analytical Engines"}'
curl localhost:8080/api/partnership-requests
curl -X POST localhost:8080/api/sales-requests -H 'Content-Type: application/json' -d '{"firstName":"Ada","lastName":"Lovelace","email":"ada@example.com","companyName":"Analytical Engines"}'
curl localhost:8080/api/sales-requests
curl -X POST localhost:8080/api/signups -H 'Content-Type: application/json' -d '{"email":"ada@example.com","password":"secret123","confirmPassword":"secret123","firstName":"Ada","lastName":"Lovelace","company":"Analytical Engines"}'
curl localhost:8080/api/signups
curl localhost:8080/api/hello
curl localhost:8080/q/health
```
