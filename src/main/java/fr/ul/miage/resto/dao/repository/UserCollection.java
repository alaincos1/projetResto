package fr.ul.miage.resto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.model.entity.UserEntity;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("users");

    public boolean save(Object userEntity) {
        UserEntity user = (UserEntity) userEntity;
        if (user.getRole() == Role.DIRECTOR && collection.countDocuments(new Document("role", Role.DIRECTOR.toString())) >= 1 ||
                user.getRole() == Role.BUTLER && collection.countDocuments(new Document("role", Role.BUTLER.toString())) >= 1) {
            return false;
        }

        return super.insert(Mapper.toDocument(userEntity), collection);
    }

    @Override
    public boolean update(Object o) {
        UserEntity user = (UserEntity) o;
        if (user.getRole() == Role.DIRECTOR && getUserById(user.get_id()).getRole() != Role.DIRECTOR && collection.countDocuments(new Document("role", Role.DIRECTOR.toString())) >= 1 ||
                user.getRole() == Role.BUTLER && getUserById(user.get_id()).getRole() != Role.BUTLER && collection.countDocuments(new Document("role", Role.BUTLER.toString())) >= 1) {
            return false;
        }
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

    public boolean delete(UserEntity user, boolean forceDelete) {
        if ((user.getRole() == Role.DIRECTOR || user.getRole() == Role.BUTLER) && !forceDelete) {
            return false;
        }

        Document doc = new Document("_id", user.get_id());
        return collection.deleteOne(doc).getDeletedCount() == 1;
    }

    public UserEntity getDirectionUser(Role role) {
        Document doc = new Document("role", role.toString());
        Document user = collection.find(doc).first();
        return user == null ? null : (UserEntity) Mapper.toObject(user, UserEntity.class);
    }
}
