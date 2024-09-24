package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapperManual;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
import ru.skillbox.currency.exchange.service.util.CurrencyDetailsXml;
import ru.skillbox.currency.exchange.service.util.CurrencyXml;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DownloaderService {
    private final CurrencyRepository currencyRepository;

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

        System.out.println("myArray " + new String(bytesArray, StandardCharsets.UTF_8) + " myArray!");

       try
           (FileOutputStream fos = new FileOutputStream(PATH_TO_FILE)) {
           fos.write(bytesArray, 0, bytesArray.length);
        }
            catch (IOException e) {
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
            Currency currencyToCheck = CurrencyMapperManual.toEntity(currency);
            if (currencyRepository.exists(Example.of(currencyToCheck))) {

                result.add(currencyService.update(currency));
            } else {
                result.add(currencyService.create(currency));
            }
        }

        return result;
    }
}