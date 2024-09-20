package ru.skillbox.currency.exchange.service.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@Component
@NoArgsConstructor
public class XMLManipulator {

    public CurrencyXml toObject(String xml) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CurrencyXml.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (CurrencyXml) jaxbUnmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
