# Payment Services
Built a reactive payment processing backend using Java, Spring Boot, WebFlux, MongoDB, and Kafka, implementing event-driven payment workflows, REST APIs, CI/CD automation, testing, and microservices-ready architecture for real-time transaction processing.

## Features

- Create a payment
- Retrieve a payment by ID
- Retrieve all payments or filter by customer ID
- Update payment status
- Publish Kafka events whenever a payment is created or updated
- Reactive, non-blocking REST APIs using WebFlux
- CI pipeline for build + test validation

## Project Structure

```text
src/main/java/com/xyz/payments
├── config
├── controller
├── dto
├── exception
├── messaging
├── model
├── repository
└── service
```

## API Endpoints

### Create Payment
`POST /api/v1/payments`

Sample request:
```json
{
  "orderId": "ORD-1001",
  "customerId": "CUST-901",
  "amount": 249.99,
  "currency": "USD",
  "paymentMethod": "CARD"
}
```

### Get Payment by ID
`GET /api/v1/payments/{paymentId}`

### Get All Payments
`GET /api/v1/payments`

### Get Payments by Customer
`GET /api/v1/payments?customerId=CUST-901`

### Update Payment Status
`PATCH /api/v1/payments/{paymentId}/status`

Sample request:
```json
{
  "status": "CAPTURED"
}
```

## Running Locally

### 1. Start MongoDB and Kafka
```bash
docker compose up -d
```

### 2. Run the application
```bash
mvn spring-boot:run
```

Application runs at:
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Build and Test

```bash
mvn clean test package
```
