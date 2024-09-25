package ru.skillbox.currency.exchange.service.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
import ru.skillbox.currency.exchange.service.CurrencyService;
import ru.skillbox.currency.exchange.service.dto.CurrencyDetailsXml;
import ru.skillbox.currency.exchange.service.dto.CurrencyXml;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static ru.skillbox.currency.exchange.mapper.CurrencyMapper.CURRENCY_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class DownloaderService {

    private final CurrencyRepository currencyRepository;

    private final CurrencyService currencyService;

    private static final String URL_TO_XML_FILE = "https://cbr.ru/scripts/XML_daily.asp";

    private static final String PATH_TO_FILE = "downloads\\XML_daily.asp";

    public List<CurrencyDto> manipulateStringXMLEntity(String xml) {

        log.info("xml entity was downloaded from " + URL_TO_XML_FILE + " " + LocalDateTime.now());

        List<CurrencyDto> listToUpdateCurrenciesInXML = new ArrayList<>();
        List<CurrencyDto> listToUpdateCurrenciesInDB;

        this.byteaArrayToFile(xml);

        log.info("xml entity was saved to file on disk at" + PATH_TO_FILE + " " + LocalDateTime.now());

        try {
            log.info("xml entity was converted in list of objects at " + LocalDateTime.now());

            listToUpdateCurrenciesInXML = this.mapperToCurrencyDtoList(Objects.requireNonNull(this.unmarshal()));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        listToUpdateCurrenciesInDB = this.checkCurrenciesInDB(listToUpdateCurrenciesInXML);
        log.info("xml entity was checked in db at" + " " + LocalDateTime.now());

        return listToUpdateCurrenciesInDB;
    }

    private void byteaArrayToFile(String bytesArray) {

        try
                (FileOutputStream fos = new FileOutputStream(PATH_TO_FILE)) {
            fos.write(bytesArray.getBytes());
            log.info("xml entity was saved on disk at" + PATH_TO_FILE + " " + LocalDateTime.now());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CurrencyXml unmarshal() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(CurrencyXml.class);
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        var input = this.downloader();
        log.info("xml entity was download on disk at" + " " + LocalDateTime.now());

        return (CurrencyXml) jaxbUnmarshaller.unmarshal(new StringReader(input));
    }

    public List<CurrencyDto> mapperToCurrencyDtoList(CurrencyXml xml) {
        List<CurrencyDto> result = new ArrayList<>();
        for (CurrencyDetailsXml currency : xml.getValutes()) {
            result.add(this.mapperToCurrencyDto(currency));
        }
        log.info("xml entity was converted to objects list at" + " " + LocalDateTime.now());

        return result;
    }

    private CurrencyDto mapperToCurrencyDto(CurrencyDetailsXml currencyDetailsXml) {

        log.info("xml entity was converted to objects by mapper at" + " " + LocalDateTime.now());
        new CurrencyDto();
        return CurrencyDto
                .builder()
                .name(currencyDetailsXml.getName())
                .nominal(currencyDetailsXml.getNominal())
                .value(currencyDetailsXml.getValue())
                .isoNumCode(currencyDetailsXml.getNumCode())
                .letterIsoCode(currencyDetailsXml.getLetterISOCode())
                .build();
    }

    private List<CurrencyDto> checkCurrenciesInDB(List<CurrencyDto> listToUpdateCurrenciesInXML) {

        List<CurrencyDto> result = new ArrayList<>();
        for (CurrencyDto currency : listToUpdateCurrenciesInXML) {
            Currency currencyToCheck = CURRENCY_MAPPER.ToEntity(currency);

            if (currencyRepository.exists(Example.of(currencyToCheck))) {
                log.info("entity was in db, started update at" + " " + LocalDateTime.now());
                result.add(currencyService.update(currency));
            } else {
                log.info("entity was not in db, started create at" + " " + LocalDateTime.now());
                result.add(currencyService.create(currency));
            }
        }

        return result;
    }

    public String downloader() {
        return this.download();
    }

    private String download() {

        try {

            return this.getter();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private String getter() {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_TO_XML_FILE))
                .GET()
                .build();

        try {

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}