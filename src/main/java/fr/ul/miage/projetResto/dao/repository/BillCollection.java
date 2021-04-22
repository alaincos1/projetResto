package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.BillEntity;
import org.bson.Document;

public class BillCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("bills");

    public boolean save(Object billEntity) {
        return super.insert(Mapper.toDocument(billEntity), collection);
    }

    public BillEntity getBillById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (BillEntity) Mapper.toObject(doc, BillEntity.class);
    }
}
