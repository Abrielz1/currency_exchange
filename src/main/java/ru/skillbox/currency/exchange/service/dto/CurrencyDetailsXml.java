package ru.skillbox.currency.exchange.service.dto;

import lombok.NoArgsConstructor;
import ru.skillbox.currency.exchange.service.util.StringToDoubleAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Objects;

@NoArgsConstructor
public class CurrencyDetailsXml {

    private String id;

    private String name;

    private Long numCode;

    private  Long nominal;

    private String letterISOCode;

    private Double value;

    @XmlAttribute(name = "ID")
    public String getId() {
        return id;
    }

    @XmlElement(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "NumCode")
    public Long getNumCode() {
        return numCode;
    }

    public void setNumCode(Long numCode) {
        this.numCode = numCode;
    }

    @XmlElement(name = "CharCode")
    public String getLetterISOCode() {
        return letterISOCode;
    }

    public void setLetterISOCode(String letterISOCode) {
        this.letterISOCode = letterISOCode;
    }

    @XmlElement(name = "Value")
    @XmlJavaTypeAdapter(StringToDoubleAdapter.class)
    public Double getValue() {
        return value;
    }

    @XmlElement(name = "Nominal")
    public Long getNominal() {
        return nominal;
    }

    public void setNominal(Long nominal) {
        this.nominal = nominal;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (CurrencyDetailsXml) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.numCode, that.numCode) &&
                Objects.equals(this.letterISOCode, that.letterISOCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numCode, letterISOCode);
    }

    @Override
    public String toString() {
        return "CurrencyDetailsXml{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", numCode=" + numCode +
                ", nominal=" + nominal +
                ", letterISOCode='" + letterISOCode + '\'' +
                ", value=" + value +
                '}';
    }
}
