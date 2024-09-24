package ru.skillbox.currency.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skillbox.currency.exchange.entity.Currency;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByIsoNumCode(Long isoNumCode);

    @Query(value = """
                SELECT *
                FROM currency
                WHERE currency.name like :name
                AND  currency.iso_num_code = :iso_num_code
                AND  currency.letter_iso_code like :letter_iso_code
                   """, nativeQuery = true)
    Optional<Currency> findByCurrencyNameAndIsoCodeAndCharCode(@Param("name") String name,
                                                               @Param("iso_num_code") Long iso_num_code,
                                                               @Param("letter_iso_code") String letter_iso_code);

    boolean existsByNameAndAndIsoNumCodeAndAndLetterIsoCode(String name, Long isoNumCode, String letterIsoCode);
}
