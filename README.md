# mega-prod-java (code only)

**Big production-style Spring Boot app**, designed so you can build all DevOps around it.
Includes:
- **Auth (JWT)** with signup/login, password hashing, stateless security
- **Incidents** with comments, **file attachments** (pluggable storage via interface; local FS by default)
- **Outbox → Kafka** publisher (scheduled) for reliable event delivery
- **Email notifications** (Spring Mail; GreenMail in tests)
- **Scheduled reports** (simulated summary job)
- **Flyway migrations** (schema-first) with `ddl-auto: validate`
- **Caching** with Caffeine
- **Actuator** & **Prometheus** metrics
- **OpenAPI** at `/swagger-ui.html`
- **JSON logging**
- **Unit tests** + **Integration tests** (random port) — no Docker required

No Docker/K8s/CI/Ansible files included — you build them.

## Run
```bash
mvn -q -DskipTests=false verify
mvn spring-boot:run
```

## Try
```bash
# signup
curl -s -X POST localhost:8080/api/v1/auth/signup -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","password":"pw"}'

# login
curl -s -X POST localhost:8080/api/v1/auth/login -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","password":"pw"}'

# create incident (replace TOKEN)
curl -s -X POST localhost:8080/api/v1/incidents -H 'Authorization: Bearer TOKEN' -H 'Content-Type: application/json' \
  -d '{"title":"Latency spike","description":"p95>2s","severity":"HIGH"}'

# metrics & health
curl -s localhost:8080/actuator/health/readiness
curl -s localhost:8080/actuator/prometheus | head

# swagger
http://localhost:8080/swagger-ui.html
```

## Prod DB
Switch H2 → Postgres by overriding props (env or `application-prod.yml`):
```
spring.datasource.url=jdbc:postgresql://host:5432/megadb
spring.datasource.username=app
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.profiles.active=prod
```

## Where you (DevOps) plug in
- Build container, push to registry
- K8s Deployment, Service, Ingress, HPA
- Externalize config/secrets (DB, JWT secret, SMTP creds, Kafka brokers)
- Observability: scrape metrics; ship JSON logs; dashboards/alerts
- CI: tests + coverage gates, SAST/DAST, SBOM
- CD: blue/green or canary; database migration promotion flow

### Notes
- Email: app.yml points to `127.0.0.1:3025` (works with GreenMail in tests). Set real SMTP in prod.
- Kafka: `spring-kafka` included; outbox scheduler publishes to `${app.kafka.topic:events}`.
- Storage: uses local filesystem under `${app.storage.root}`. Replace with S3 by implementing `StorageService` and swapping bean.

Happy building! 🚀


## Enable Postgres (prod) and S3 (code-only)
Use Spring profiles and environment variables — no code changes required.

### Postgres profile
Run with `--spring.profiles.active=prod` and set:
- `SPRING_DATASOURCE_URL` (e.g., `jdbc:postgresql://db:5432/megadb`)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `APP_JWT_SECRET` (64+ hex chars recommended)
- `KAFKA_BOOTSTRAP_SERVERS`
- SMTP: `SMTP_HOST`, `SMTP_PORT`, `SMTP_USER`, `SMTP_PASS`

### S3 storage
Add profile `s3` too: `--spring.profiles.active=prod,s3` and set:
- `S3_BUCKET`
- `AWS_REGION`
- Credentials via AWS default chain:
  - `AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY` (env), or
  - `~/.aws/credentials`, or
  - IAM Role (preferred in cloud)

**Example run:**
```
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/megadb \
SPRING_DATASOURCE_USERNAME=app \
SPRING_DATASOURCE_PASSWORD=secret \
APP_JWT_SECRET=0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF \
S3_BUCKET=my-bucket AWS_REGION=ap-south-1 \
KAFKA_BOOTSTRAP_SERVERS=broker:9092 \
java -jar target/mega-prod-java-1.0.0.jar --spring.profiles.active=prod,s3
```
