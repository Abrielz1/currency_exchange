package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.dto.CurrencyDto;;
import ru.skillbox.currency.exchange.service.util.CurrencyDetailsXml;
import ru.skillbox.currency.exchange.service.util.CurrencyXml;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DownloaderService {

    private final CurrencyService currencyService;

    private static final String PATH_TO_FILE = "downloads\\XML_daily.asp";

    public List<CurrencyDto> manipulateByteArray(byte[] bytesArray) {

        List<CurrencyDto> listToUpdateCurrenciesInXML = new ArrayList<>();
        List<CurrencyDto> listToUpdateCurrenciesInDB;

        this.byteaArrayToFile(bytesArray);

        try {
           listToUpdateCurrenciesInXML = this.mapperToCurrencyDtoList(this.unmarshal());
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }

        listToUpdateCurrenciesInDB = this.checkCurrenciesInDB(listToUpdateCurrenciesInXML);

        return listToUpdateCurrenciesInDB;
    }

    private void byteaArrayToFile(byte[] bytesArray) {

        try {
            Files.write(Paths.get(PATH_TO_FILE), Objects.requireNonNull(bytesArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CurrencyXml unmarshal() throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(CurrencyXml.class);
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();

        return (CurrencyXml) jaxbUnmarshaller.unmarshal(new FileReader(PATH_TO_FILE));
    }

    public List<CurrencyDto> mapperToCurrencyDtoList(CurrencyXml xml) {
        List<CurrencyDto> result = new ArrayList<>();
        for (CurrencyDetailsXml currency : xml.getValutes()) {
            result.add(this.mapperToCurrencyDto(currency));
        }

        return result;
    }

    private CurrencyDto mapperToCurrencyDto(CurrencyDetailsXml currencyDetailsXml) {

        new CurrencyDto();
        return CurrencyDto
                .builder()
              //  .id(0L)
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
            if (currencyService.checkRecordInBD(currency).isPresent()) {
                result.add(currencyService.update(currency));
            } else {
                result.add(currencyService.create(currency));
            }
        }

        return result;
    }
}