package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

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

    public List<TableEntity> getAll() {
        return collection.find().into(new ArrayList<Document>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
    }

    public List<TableEntity> getAllTableByServerOrHelper(String user) {
        return collection.find(or(eq("idServer",user), eq("idHelper",user))).into(new ArrayList<Document>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
    }
}
