package ru.skillbox.currency.exchange.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.CurrencyShortDto;
import ru.skillbox.currency.exchange.service.CurrencyService;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService service;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    List<CurrencyShortDto> getAllCurrencies(@RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {

        PageRequest pageable = PageRequest.of(from / size, size);

        return service.getAllCurrencies(pageable);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    CurrencyDto getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @GetMapping(value = "/convert")
    @ResponseStatus(HttpStatus.OK)
    Double convertValue(@RequestParam("value") Long value,
                        @RequestParam("numCode") Long numCode) {
        return service.convertValue(value, numCode);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    CurrencyDto create(@RequestBody CurrencyDto dto) {
        return service.create(dto);
    }
}
