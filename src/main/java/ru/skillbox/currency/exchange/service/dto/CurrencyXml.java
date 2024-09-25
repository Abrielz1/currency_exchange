package ru.skillbox.currency.exchange.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "ValCurs")
@XmlType
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyXml {

    private String date;

    private List<CurrencyDetailsXml> valutes;


    @XmlAttribute(name = "Date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @XmlElement(name = "Valute")
    public List<CurrencyDetailsXml> getValutes() {
        return valutes;
    }

    public void setValutes(List<CurrencyDetailsXml> valutes) {
        this.valutes = valutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyXml that = (CurrencyXml) o;
        return Objects.equals(date, that.date) && Objects.equals(valutes, that.valutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, valutes);
    }

    @Override
    public String toString() {
        return "CurrencyXml[" +
                "date=" + date + ", " +
                "valutes=" + valutes + ']';
    }
}
