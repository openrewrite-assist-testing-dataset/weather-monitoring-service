# Weather Monitoring Service

A multi-module Dropwizard application for collecting and serving weather data.

## Overview

This service consists of three modules:
- **common**: Shared models and utilities
- **weather-api**: REST API for weather data access
- **data-collector**: Background service for collecting weather data

## Technology Stack

- **Framework**: Dropwizard 2.0.x
- **Java**: Java 11
- **Build Tool**: Gradle 6.7
- **Database**: PostgreSQL with JDBI
- **Authentication**: JWT + API Key (dual auth)
- **Logging**: Log4j2
- **Testing**: JUnit 5

## API Endpoints

### Authentication
- JWT Token: Include `Authorization: Bearer <token>` header
- API Key: Include `X-API-Key: <key>` header

### Weather API (`/api/v1/weather`)

#### Get Current Weather
```
GET /api/v1/weather/current/{location}
```

Example:
```bash
curl -H "X-API-Key: weather-api-key-001" \
  http://localhost:8080/api/v1/weather/current/London
```

#### Get Historical Weather
```
GET /api/v1/weather/historical/{location}?startDate=2023-01-01T00:00:00.000Z&endDate=2023-01-31T23:59:59.999Z
```

Example:
```bash
curl -H "Authorization: Bearer <jwt-token>" \
  "http://localhost:8080/api/v1/weather/historical/London?startDate=2023-01-01T00:00:00.000Z&endDate=2023-01-31T23:59:59.999Z"
```

#### Submit Weather Data
```
POST /api/v1/weather/data
Content-Type: application/json

{
  "location": "London",
  "temperature": 15.5,
  "humidity": 65.0,
  "pressure": 1013.25,
  "source": "manual"
}
```

## Setup Instructions

### Prerequisites
- Java 11
- PostgreSQL
- Gradle 6.7

### Database Setup
1. Create PostgreSQL database:
```sql
CREATE DATABASE weather_db;
CREATE USER weather_user WITH PASSWORD 'weather_pass';
GRANT ALL PRIVILEGES ON DATABASE weather_db TO weather_user;
```

2. Run database migrations:
```bash
./gradlew flywayMigrate
```

### Running the Application

1. Build the project:
```bash
./gradlew build
```

2. Run the weather API:
```bash
./gradlew :weather-api:run
```

3. Run the data collector (optional):
```bash
./gradlew :data-collector:run
```

### Configuration

The application uses `application.yml` for configuration. Key settings:

- Database connection details
- JWT secret key
- API keys for authentication
- Logging configuration

## Docker Deployment

### Build Docker Image
```bash
docker build -t weather-monitoring-service .
```

### Run with Docker Compose
```bash
docker-compose up -d
```

## Kubernetes Deployment

### Using Helm
```bash
cd helm
helm install weather-monitoring-service ./weather-monitoring-service
```

### Manual Deployment
```bash
kubectl apply -f k8s/
```

## Health Checks

- Application health: `http://localhost:8081/healthcheck`
- Database health: `http://localhost:8081/healthcheck`

## Testing

Run all tests:
```bash
./gradlew test
```

Run specific module tests:
```bash
./gradlew :weather-api:test
```

## API Keys

Default API keys for testing:
- `weather-api-key-001`
- `weather-api-key-002`

## JWT Token Generation

Generate a JWT token for testing:
```bash
# This would typically be done by your authentication service
# For testing, you can use online JWT generators with secret: "weather-secret-key-2021"
```

## Monitoring

The application exposes metrics at:
- `http://localhost:8081/metrics`

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check PostgreSQL is running
   - Verify connection details in `application.yml`

2. **Port Already in Use**
   - Default ports: 8080 (API), 8081 (Admin)
   - Change in `application.yml` if needed

3. **Authentication Errors**
   - Ensure correct API key or JWT token
   - Check token expiration

### Logs

Application logs are written to:
- Console output
- `./logs/weather-api.log`

## Contributing

1. Fork the repository
2. Create feature branch
3. Make changes
4. Run tests
5. Submit pull request