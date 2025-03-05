package com.onymos.stocktrading.service;

import com.onymos.stocktrading.model.OrderType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OrderSimulationService {
    private final OrderBookService orderBookService;
    private final Random random = new Random();
    private static final String[] TICKERS = {"AAPL", "MSFT", "GOOGL", "AMZN", "TSLA"};

    public OrderSimulationService(OrderBookService orderBookService) {
        this.orderBookService = orderBookService;
    }

    // Randomly generate and add a new order every 200ms.
    @Scheduled(fixedRate = 200)
    public void generateRandomOrder() {
        OrderType type = random.nextBoolean() ? OrderType.BUY : OrderType.SELL;
        String ticker = TICKERS[random.nextInt(TICKERS.length)];
        int quantity = 1 + random.nextInt(100);
        double price = 50 + (450 * random.nextDouble());
        price = Math.round(price * 100) / 100.0;
        orderBookService.addOrder(type, ticker, quantity, price);
    }

    // Match orders across all tickers every 500ms.
    @Scheduled(fixedRate = 500)
    public void matchOrders() {
        orderBookService.matchOrders();
    }
}
