package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.MenuEntity;
import org.bson.Document;

public class MenuCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("menus");

    public boolean save(Object menuEntity) {
        return super.insert(Mapper.toDocument(menuEntity), collection);
    }

    public MenuEntity getMenuById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (MenuEntity) Mapper.toObject(doc, MenuEntity.class);
    }
}
