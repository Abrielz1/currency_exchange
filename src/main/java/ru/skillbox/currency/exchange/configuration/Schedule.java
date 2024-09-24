package ru.skillbox.currency.exchange.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.skillbox.currency.exchange.controller.CurrencyActualValueDownloader;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Schedule {

    CurrencyActualValueDownloader downloader;

    @Scheduled(fixedRate = 3600000)
    public void scheduleFixedDelayTask() {

        downloader.downloaderExactCurrencyValueFromServer();
    }
}
