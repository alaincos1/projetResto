package fr.ul.miage.resto.dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.model.entity.BookingEntity;
import fr.ul.miage.resto.model.entity.TableEntity;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

public class TableCollection extends MongoAccess {
    private static final String SERVER = "idServer";
    private static final String HELPER = "idHelper";
    private static final String TABLE_STATE = "tableState";
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
        return collection.find().into(new ArrayList<>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
    }

    public List<TableEntity> getAllTableByServerOrHelper(String user) {
        return collection.find(or(eq(SERVER, user), eq(HELPER, user))).into(new ArrayList<>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
    }

    public List<TableEntity> getAllTableByServerOrHelperAndState(String user, TableState state) {
        return collection.find(and(eq(TABLE_STATE, state.toString()), or(eq(SERVER, user), eq(HELPER, user)))).into(new ArrayList<>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
    }

    public List<TableEntity> getAllTableByState(TableState state) {
        return collection.find(eq(TABLE_STATE, state.toString())).into(new ArrayList<>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
    }

    public boolean delete(String idTable) {
        Bson doc = eq("_id", idTable);
        DeleteResult dr = collection.deleteOne(doc);
        return dr.getDeletedCount() == 1;
    }

    public List<TableEntity> getAllRemovableTables() {
        Bson filter = eq(TABLE_STATE, TableState.FREE.toString());
        List<TableEntity> tables = collection.find(filter).into(new ArrayList<>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
        BookingCollection bookingCollection = new BookingCollection();
        List<TableEntity> resultTables = new ArrayList<>();
        List<BookingEntity> bookings = bookingCollection.getAll();
        for (TableEntity table : tables) {
            if (bookings.stream().noneMatch(bookingEntity -> table.getId().equals(bookingEntity.getIdTable()))) {
                resultTables.add(table);
            }
        }
        return resultTables;
    }

    public List<TableEntity> getTablesReadyToOrderByServer(String userId) {
        Bson filterServer = null;
        if (!userId.isEmpty()) {
            filterServer = eq(SERVER, userId);
        }
        Bson filterTableState = and(not(eq(TABLE_STATE, TableState.FREE.toString())),
                not(eq(TABLE_STATE, TableState.DIRTY.toString())));
        Bson filters;
        if (filterServer == null) {
            filters = filterTableState;
        } else {
            filters = and(filterServer, filterTableState);
        }
        return collection.find(filters).into(new ArrayList<>()).stream()
                .map(doc -> (TableEntity) Mapper.toObject(doc, TableEntity.class))
                .collect(Collectors.toList());
    }
}
