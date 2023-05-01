package niffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import niffler.model.UserJson;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.io.IOException;

public class ClasspathUserUpdateConverter implements ArgumentConverter {
    private ClassLoader cl = ClasspathUserUpdateConverter.class.getClassLoader();
    private static ObjectMapper om = new ObjectMapper();
    UserJson userJson;

    @Override
    public UserJson convert(Object source, ParameterContext context)
            throws ArgumentConversionException {
        if (source instanceof String stringSource) {
            try {
                userJson = new UserJson();
                userJson.setUsername(om.readValue(cl.getResourceAsStream(stringSource), UserJson.class).getUsername());
                userJson.setFirstname(om.readValue(cl.getResourceAsStream(stringSource), UserJson.class).getFirstname());
                userJson.setSurname(om.readValue(cl.getResourceAsStream(stringSource), UserJson.class).getSurname());
                userJson.setCurrency(om.readValue(cl.getResourceAsStream(stringSource), UserJson.class).getCurrency());
                return userJson;
            } catch (IOException e) {
                throw new ArgumentConversionException(e.getMessage());
            }
        } else {
            throw new ArgumentConversionException("Only string source supported!");
        }
    }
}

