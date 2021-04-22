package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.BookingEntity;
import org.bson.Document;

public class BookingCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("bookings");

    public boolean save(Object bookingEntity) {
        return super.insert(Mapper.toDocument(bookingEntity), collection);
    }

    public BookingEntity getBookingById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (BookingEntity) Mapper.toObject(doc, BookingEntity.class);
    }
}
