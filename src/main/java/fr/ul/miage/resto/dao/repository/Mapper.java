package fr.ul.miage.resto.dao.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;

public class Mapper {
    public static ObjectMapper mapper = new ObjectMapper();

    public static Document toDocument(Object o) {
        String json = null;
        try {
            json = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Document.parse(json);
    }

    public static Object toObject(Document doc, Class clazz) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return mapper.readValue(doc.toJson(), clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
