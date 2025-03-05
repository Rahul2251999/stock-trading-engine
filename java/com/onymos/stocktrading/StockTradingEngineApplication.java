package com.onymos.stocktrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockTradingEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockTradingEngineApplication.class, args);
    }
}
