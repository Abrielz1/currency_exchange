package ru.skillbox.currency.exchange.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.service.DownloaderService;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/download")
@RequiredArgsConstructor
public class CurrencyActualValueDownloader {

    private final DownloaderService downloaderService;

    private final RestTemplateBuilder templateBuilder;

    private static final String URL_TO_XML_FILE = "https://cbr.ru/scripts/XML_daily.asp";


    @GetMapping("/file")
    @ResponseStatus(HttpStatus.OK)
    public List<CurrencyDto> downloaderExactCurrencyValueFromServer() {

        return downloaderService.manipulateByteArray(this.downloadXMLFile());
    }

    private byte[] downloadXMLFile() {

        try {

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



        private void timer() {
        // todo сделать while() {}
        //  и там сначала запускать метод скачки парсинга и сохранения валют в базе и потом в сон thread.sleep(3600_000)
    }
}
