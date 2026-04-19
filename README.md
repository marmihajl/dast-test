# DAST Demo

Spring Boot web application used for demonstrating **Dynamic Application Security Testing (DAST)** tools and techniques. Built as part of a RITEH software security course.

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA + H2 (in-memory database)
- Thymeleaf templates
- SpringDoc OpenAPI (Swagger UI)

## Getting Started

### Prerequisites

- Java 21 (e.g., Eclipse Adoptium Temurin)
- Maven 3.9+

### Build & Run

```bash
mvn clean package -DskipTests -s settings-local.xml
java -jar target/dast-demo-0.0.1-SNAPSHOT.jar
```

The application starts on **http://localhost:8080**.

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/` | Home page |
| GET | `/search?query=` | Search students by name |
| GET | `/greet?name=` | Greeting page |
| GET | `/files` | List available files |
| GET | `/download?file=` | Download a file by name |

### API Documentation

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Security Features

- **Content-Security-Policy**: `default-src 'self'`
- **X-Content-Type-Options**: `nosniff`
- **X-Frame-Options**: `DENY`
- **Permissions-Policy**: restricts geolocation, camera, microphone
- **Cross-Origin-Resource-Policy**: `same-origin`
- Parameterized SQL queries (SQL injection prevention)
- Input validation on file download endpoint

## DAST Testing

This project has been tested with the following DAST tools, all run via Docker in WSL Ubuntu:

### ZAP (OWASP Zed Attack Proxy)

```bash
docker run --rm \
  -v /mnt/c/projects/dast-demo:/zap/wrk/:rw \
  --add-host=host.docker.internal:host-gateway \
  zaproxy/zap-stable \
  zap-full-scan.py -t http://<HOST_IP>:8080 \
  -r zap-active-scanning-report.html \
  -J zap-active-scanning-report.json \
  -w zap-active-scanning-report.md -a
```

### Nikto

```bash
docker run --rm \
  --add-host=host.docker.internal:host-gateway \
  -v /mnt/c/projects/dast-demo:/tmp/nikto-output \
  frapsoft/nikto \
  -h http://<HOST_IP>:8080 -port 8080 \
  -Tuning x -Display V \
  -output /tmp/nikto-output/nikto-report.htm -Format htm
```

### Nuclei

```bash
docker run --rm \
  --add-host=host.docker.internal:host-gateway \
  -v /mnt/c/projects/dast-demo:/output \
  projectdiscovery/nuclei \
  -u http://<HOST_IP>:8080 -as \
  -jsonl -o /output/nuclei-report.jsonl
```

### Schemathesis

```bash
docker run --rm \
  --add-host=host.docker.internal:host-gateway \
  -v /mnt/c/projects/dast-demo:/output \
  schemathesis/schemathesis:stable \
  run http://<HOST_IP>:8080/v3/api-docs \
  --checks all --phases examples,coverage,fuzzing,stateful \
  -n 100
```

> Replace `<HOST_IP>` with the WSL host gateway IP (e.g., `172.23.224.1` from `ip route show default`).

## Project Structure

```
src/main/java/hr/riteh/dastdemo/
  DastDemoApplication.java          # Main app + OpenAPI config
  config/
    SecurityHeadersFilter.java      # Security headers filter
  controller/
    HomeController.java             # GET /
    StudentController.java          # GET /search, GET /greet
    FileController.java             # GET /files, GET /download
  model/
    Student.java                    # JPA entity
  repository/
    StudentRepository.java          # Spring Data repository
```
