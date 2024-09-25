package ru.skillbox.currency.exchange.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.currency.exchange.configuration.Schedule;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.service.util.DownloaderService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/download")
@RequiredArgsConstructor
public class CurrencyActualValueDownloader {

    private final DownloaderService downloaderService;

    private final Schedule schedule;

    @GetMapping("/file")
    @ResponseStatus(HttpStatus.OK)
    public List<CurrencyDto> downloaderExactCurrencyValueFromServer() {

        return downloaderService.manipulateStringXMLEntity(schedule.downloader());
    }

    @GetMapping("/manual")
    @ResponseStatus(HttpStatus.OK)
    public List<CurrencyDto> manualDownloaderExactCurrencyValueFromServer() {

        return downloaderService.manipulateStringXMLEntity(downloaderService.downloader());
    }
}
