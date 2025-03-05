# Real-time Stock Trading Engine

A high-performance, lock-free stock trading engine built with Spring Boot that matches buy and sell orders in real-time.

## Overview

This application implements a real-time stock trading engine that efficiently matches buy and sell orders for up to 1,024 different stock tickers. It features a custom lock-free implementation to handle concurrent operations without using traditional locks, making it highly performant in multi-threaded environments.

## Key Features

- Real-time order matching with O(n) time complexity
- Support for up to 1,024 stock tickers
- Thread-safe, lock-free implementation using atomic operations
- RESTful API for order management
- Automated simulation of trading activity
- Custom data structures (no use of dictionaries, maps, or equivalent data structures)

## Technical Requirements

- Java 17 or higher
- Maven 3.6 or higher
- Spring Boot 3.2.3

## Project Structure

```
src/main/java/com/onymos/stocktrading/
├── StockTradingEngineApplication.java   # Main application entry point
├── controller/
│   └── OrderController.java             # REST API endpoints
├── model/
│   └── Order.java                       # Order model with atomic references
└── service/
    ├── OrderBookService.java            # Core order book operations
    └── OrderSimulationService.java      # Simulates trading activity
```

## Getting Started

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/stock-trading-engine.git
cd stock-trading-engine
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on port 8080.

## API Documentation

### Add a New Order

```
POST /api/orders
```

Parameters:
- `type`: Order type (BUY or SELL)
- `ticker`: Stock ticker symbol
- `quantity`: Number of shares
- `price`: Price per share

Example:
```bash
curl -X POST "http://localhost:8080/api/orders?type=BUY&ticker=AAPL&quantity=100&price=150.50"
```

Response:
- Returns the order ID

### Trigger Order Matching

```
GET /api/orders/match
```

Example:
```bash
curl -X GET "http://localhost:8080/api/orders/match"
```

Response:
- Returns a success message

### View Order Book

```
GET /api/orders/book
```

Example:
```bash
curl -X GET "http://localhost:8080/api/orders/book"
```

Response:
- Returns a text representation of the current order book

## Implementation Details

### Order Matching Algorithm

The order matching follows these steps:
1. Find the highest priced buy order and lowest priced sell order for each ticker
2. Match orders when the buy price ≥ sell price
3. Update or remove orders based on matched quantities
4. Repeat until no more matches are possible

### Thread Safety

The application uses atomic references and compare-and-set operations to ensure thread safety without traditional locks:
- `AtomicReference` for managing order lists
- Compare-and-set operations for updating references
- No blocking synchronization

### Custom Data Structures

Instead of using built-in maps or dictionaries, the application uses:
- Arrays indexed by a custom hash function for ticker lookup
- Custom linked list implementation using atomic references

## Automated Simulation

The application includes a simulation service that:
- Generates random buy and sell orders every 200ms
- Processes order matching every 500ms
- Logs the state of the order book every 5 seconds

## Performance Considerations

- O(n) time complexity for order matching
- Lock-free operations for maximum concurrency
- Efficient memory usage through custom data structures

## Extending the Application

To add new features:
1. Add new controller endpoints in `OrderController.java`
2. Extend the `OrderBookService.java` with additional functionality
3. Create new service classes for specialized operations

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
