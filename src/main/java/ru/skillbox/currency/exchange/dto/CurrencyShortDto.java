package ru.skillbox.currency.exchange.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyShortDto {

    private String name;

    private Integer value;
}
