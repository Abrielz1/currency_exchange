package ru.skillbox.currency.exchange.service.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringToDoubleAdapter extends XmlAdapter<String, Double> {

    @Override
    public Double unmarshal(String value) throws Exception {

        return Double.parseDouble(value.replace(",", "."));
    }

    @Override
    public String marshal(Double value) throws Exception {

        return String.valueOf(value);
    }
}
