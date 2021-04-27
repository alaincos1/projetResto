package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import fr.ul.miage.projetResto.model.entity.BookingEntity;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("bookings");

    public boolean save(Object bookingEntity) {
        return super.insert(Mapper.toDocument(bookingEntity), collection);
    }

    @Override
    public boolean update(Object o) {
        return super.update(Mapper.toDocument(o), collection);
    }

    public BookingEntity getBookingById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (BookingEntity) Mapper.toObject(doc, BookingEntity.class);
    }
}
