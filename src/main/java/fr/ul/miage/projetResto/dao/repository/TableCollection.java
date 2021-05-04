package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.model.entity.BookingEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

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
        return collection.find(or(eq("idServer", user), eq("idHelper", user))).into(new ArrayList<Document>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
    }

    public List<TableEntity> getAllTableByServerOrHelperAndState(String user, TableState state) {
        return collection.find(and(eq("tableState", state.toString()), or(eq("idServer", user), eq("idHelper", user)))).into(new ArrayList<Document>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
    }

    public List<TableEntity> getAllTableByState(TableState state) {
        return collection.find(eq("tableState", state.toString())).into(new ArrayList<Document>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
    }

    public boolean delete(String idTable) {
        Bson doc = Filters.eq("_id", idTable);
        DeleteResult dr = collection.deleteOne(doc);
        return dr.getDeletedCount() == 1;
    }

    public List<TableEntity> getAllRemovableTables() {
        Bson filter = Filters.eq("tableState", TableState.Free.toString());
        List<TableEntity> tables = collection.find(filter).into(new ArrayList<Document>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
        BookingCollection bookingCollection = new BookingCollection();
        List<TableEntity> resultTables = new ArrayList<>();
        List<BookingEntity> bookings = bookingCollection.getAll();
        for (TableEntity table : tables) {
            if (bookings.stream().noneMatch(bookingEntity -> table.get_id().equals(bookingEntity.getIdTable()))) {
                resultTables.add(table);
            }
        }
        return resultTables;
    }
}
