# Real-time Stock Trading Engine

A high-performance, lock-free stock trading engine built with Spring Boot that matches buy and sell orders in real-time.

## Overview

This application matches buy and sell orders for up to 1,024 stock tickers using a custom lock-free, atomic operations-based implementation.

## Key Features

- Real-time order matching (O(n) complexity)
- Supports 1,024 tickers
- Lock-free and thread-safe using atomic operations
- REST API for order management
- Simulated trading activity

## Requirements

- Java 17+
- Maven
- Spring Boot 3.2.3

## Project Structure

src/main/java/com/onymos/stocktrading/
├── StockTradingEngineApplication.java   # Main application entry point
├── controller/
│   └── OrderController.java             # REST API endpoints
├── model/
│   └── Order.java                       # Order model with atomic references
└── service/
    ├── OrderBookService.java            # Core order book operations
    └── OrderSimulationService.java      # Simulates trading activity

## Getting Started

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/Rahul2251999/stock-trading-engine.git
   cd stock-trading-engine
Build the Project:

bash
Copy
mvn clean install
Run the Application:

bash
Copy
mvn spring-boot:run
The application will start on port 8080.

API Endpoints
Add Order:
POST /api/orders
Parameters: type, ticker, quantity, price

Match Orders:
GET /api/orders/match

View Order Book:
GET /api/orders/book
