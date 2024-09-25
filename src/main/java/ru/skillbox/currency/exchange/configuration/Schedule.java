package ru.skillbox.currency.exchange.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.skillbox.currency.exchange.service.DownloaderService;

import java.util.Collections;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Schedule {

    private final DownloaderService downloaderService;

    private final RestTemplateBuilder templateBuilder;

    private static final String URL_TO_XML_FILE = "https://cbr.ru/scripts/XML_daily.asp";

    @Scheduled(fixedRate = 3600000)
    public void scheduleFixedDelayTask() {

        downloaderService.manipulateByteArray(download());
    }

    public byte[] downloader() {
        return download();
    }

    private byte[] download() {
        byte[] arr = new byte[10];
        try {

        return this.getter();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return arr;
    }

    private byte[] getter() {

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> response = templateBuilder
                .build()
                .exchange(
                        URL_TO_XML_FILE,
                        HttpMethod.GET,
                        entity, byte[].class);

        return response.getBody();
    }
}
