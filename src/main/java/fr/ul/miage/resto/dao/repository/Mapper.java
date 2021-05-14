package fr.ul.miage.resto.dao.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ul.miage.resto.view.GeneralView;
import org.bson.Document;

public class Mapper {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Mapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static Document toDocument(Object o) {
        String json = null;
        try {
            json = OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            GeneralView generalView = new GeneralView();
            generalView.displayMessage("Problème de parsing en Document: "+ e.getMessage());
        }
        return Document.parse(json);
    }

    public static Object toObject(Document doc, Class clazz) {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return OBJECT_MAPPER.readValue(doc.toJson(), clazz);
        } catch (JsonProcessingException e) {
            GeneralView generalView = new GeneralView();
            generalView.displayMessage("Problème de parsing en Objet: "+ e.getMessage());
        }
        return null;
    }
}
