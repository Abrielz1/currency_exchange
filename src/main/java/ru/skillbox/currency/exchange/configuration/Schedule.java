package ru.skillbox.currency.exchange.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.skillbox.currency.exchange.service.util.DownloaderService;
import java.time.LocalDateTime;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Schedule {

    private final DownloaderService downloaderService;

    @Scheduled(fixedRate = 3600000)
    public void scheduleFixedDelayTask() {
        log.info("event started by schedule!" + LocalDateTime.now());
        downloaderService.manipulateStringXMLEntity(downloaderService.downloader());
    }

    public String downloader() {
        return downloaderService.downloader();
    }
}
