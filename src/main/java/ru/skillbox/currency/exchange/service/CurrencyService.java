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
import ru.skillbox.currency.exchange.mapper.CurrencyMapperManual;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
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

    public CurrencyDto update(CurrencyDto currency) {

        if (currency == null) {
            return null;
        }

        Currency inBDCurrency;

        if (this.checkRecordInBD(currency).isPresent()) {
            inBDCurrency = this.checkRecordInBD(currency).get();
        } else {
            inBDCurrency = CurrencyMapperManual.toEntity(currency);
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

        Currency toBd = repository.saveAndFlush(inBDCurrency);

        return CURRENCY_MAPPER.convertToDto(toBd);
    }

    @Transactional
    public CurrencyDto create(CurrencyDto dto) {
        log.info("CurrencyService method create executed");
        Currency toBd = repository.saveAndFlush(CurrencyMapperManual.toEntity(dto));

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
}
