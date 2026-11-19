cd /home/plte1217/Desktop/git/integra
docker compose up -d
mvn spring-boot:run
curl -X POST localhost:8080/api/messages -H 'Content-Type: application/json' -d '{"content":"hi"}'
curl localhost:8080/api/messages