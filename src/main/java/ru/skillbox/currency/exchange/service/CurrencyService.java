package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.CurrencyShortDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static ru.skillbox.currency.exchange.mapper.CurrencyMapper.CURRENCY_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyMapper mapper;

    private final CurrencyRepository repository;

    public List<CurrencyShortDto> getAllCurrencies(PageRequest pageable) {

        return repository.findAll(pageable)
                .stream()
                .map(CURRENCY_MAPPER::convertToShortDto)
                .collect(Collectors.toList());
    }

    public CurrencyDto getById(Long id) {
        log.info("CurrencyService method getById executed");
        Currency currency = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Currency not found with id: " + id));

        return mapper.convertToDto(currency);
    }

    public Double convertValue(Long value, Long numCode) {
        log.info("CurrencyService method convertValue executed");
        Currency currency = repository.findByIsoNumCode(numCode);

        return value * currency.getValue();
    }

    public Currency update(CurrencyDto currency) {

        if (currency == null) {
            return null;
        }

        return this.updateEntity(currency);
    }

    public List<Currency> update(List<CurrencyDto> currency) {

        List<CurrencyDto> updatedCurrencyList = new ArrayList<>();

        for (CurrencyDto dto : updatedCurrencyList) {
            Currency toUpdate = this.update(dto);
            updatedCurrencyList.add(CURRENCY_MAPPER.convertToDto(toUpdate));
        }

        return updatedCurrencyList
                .stream()
                .map(CURRENCY_MAPPER::ToEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CurrencyDto create(CurrencyDto dto) {
        log.info("CurrencyService method create executed");
        Currency toBd = repository.saveAndFlush(CURRENCY_MAPPER.ToEntity(dto));

        return  mapper.convertToDto(toBd);
    }

    public Optional<Currency> checkRecordInBD(CurrencyDto currencyDto) {

        Currency currency = Currency.builder()
                .name(currencyDto.getName())
                .nominal(currencyDto.getNominal())
                .value(currencyDto.getValue())
                .isoNumCode(currencyDto.getIsoNumCode())
                .letterIsoCode(currencyDto.getLetterIsoCode())
                .build();

        return repository.findByCurrencyNameAndIsoCodeAndCharCode(
                        currency.getName(),
                        currency.getIsoNumCode(),
                        currency.getLetterIsoCode());
    }

    private Currency updateEntity(CurrencyDto currency) {

        Currency inBDCurrency;

        if (this.checkRecordInBD(currency).isPresent()) {
            inBDCurrency = this.checkRecordInBD(currency).get();
        } else {
            return null;
        }

        if (currency.getName() != null) {
            inBDCurrency.setName(currency.getName());
        }

        if (currency.getNominal() != null) {
            inBDCurrency.setNominal(currency.getNominal());
        }

        if (currency.getValue() != null) {
            inBDCurrency.setValue(currency.getValue());
        }

        if (currency.getIsoNumCode() != null) {
            inBDCurrency.setIsoNumCode(currency.getIsoNumCode());
        }

        if (currency.getLetterIsoCode() != null) {
            inBDCurrency.setLetterIsoCode(currency.getLetterIsoCode());
        }

        return inBDCurrency;
    }
}
