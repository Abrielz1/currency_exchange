package ru.skillbox.currency.exchange.mapper;

import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.entity.Currency;

public class CurrencyMapperManual {

    public static Currency toEntity(CurrencyDto dto) {
        new Currency();

        return Currency.builder()
                .name(dto.getName())
                .nominal(dto.getNominal())
                .value(dto.getValue())
                .isoNumCode(dto.getIsoNumCode())
                .letterIsoCode(dto.getLetterIsoCode())
                .build();
    }
}
