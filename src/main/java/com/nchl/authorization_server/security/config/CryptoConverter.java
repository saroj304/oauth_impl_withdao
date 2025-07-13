package com.nchl.authorization_server.security.config;


import com.nchl.authorization_server.Utility.Utilities;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Converter
@AllArgsConstructor
public class CryptoConverter implements AttributeConverter<String, String> {
private final Utilities utilities;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String convertToDatabaseColumn(String arg0) {
        try {
            return utilities.encrypt(arg0);
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String arg0) {
        try {
            return utilities.decrypt(arg0);
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

}
