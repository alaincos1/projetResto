package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import org.bson.Document;

public class TableCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("tables");

    public boolean save(Object tableEntity) {
        return super.insert(Mapper.toDocument(tableEntity), collection);
    }

    @Override
    public boolean update(Object o) {
        return super.update(Mapper.toDocument(o), collection);
    }

    public TableEntity getTableById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (TableEntity) Mapper.toObject(doc, TableEntity.class);
    }
}
