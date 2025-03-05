# stock-trading-engine

A real-time stock trading engine that matches buy and sell orders using lock-free data structures.

## Features

- **Add Orders:**  
  Accepts Order Type (BUY/SELL), Ticker Symbol, Quantity, and Price.
- **Simulation:**  
  Randomly generates orders to simulate live trading.
- **Order Matching:**  
  Matches orders when the highest BUY price is ≥ the lowest SELL price (O(n) time complexity).
- **REST API Endpoints:**
  - `POST /api/orders/add` — Add a new order.
  - `GET /api/orders/match` — Trigger order matching.
  - `GET /api/orders/snapshot` — View the current order book snapshot.

## Setup

1. **Build the project:**
   ```bash
   mvn clean package
Run the application:
bash
Copy
mvn spring-boot:run
Access the API:
Use a REST client or browser to interact with the endpoints.
Requirements
Java 17
Maven
Spring Boot 3.x
