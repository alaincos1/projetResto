package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import org.bson.Document;

public class UserCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("users");

    public boolean save(Object userEntity) {
        return super.insert(Mapper.toDocument(userEntity), collection);
    }

    @Override
    public boolean update(Object o) {
        return super.update(Mapper.toDocument(o), collection);
    }

    public UserEntity getUserById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (UserEntity) Mapper.toObject(doc, UserEntity.class);
    }
}
