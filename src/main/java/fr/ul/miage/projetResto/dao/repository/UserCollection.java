package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;

import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    
    public List<UserEntity> getAll() {
        return collection.find().into(new ArrayList<Document>()).stream()
                .map(doc -> (UserEntity) Mapper.toObject(doc, UserEntity.class))
                .collect(Collectors.toList());
    }
}
