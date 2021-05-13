package fr.ul.miage.resto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.model.entity.BillEntity;
import fr.ul.miage.resto.utils.DateDto;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

public class BillCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("bills");

    public boolean save(Object billEntity) {
        return super.insert(Mapper.toDocument(billEntity), collection);
    }

    @Override
    public boolean update(Object o) {
        return super.update(Mapper.toDocument(o), collection);
    }

    public BillEntity getBillById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (BillEntity) Mapper.toObject(doc, BillEntity.class);
    }

    public List<BillEntity> getBillsByPeriodAndMealType(DateDto period, MealType mealType) {
        Bson filter = and(gte("date", period.getStartDate()), lte("date", period.getEndDate()));
        if (mealType != null) {
            filter = and(filter, eq("mealType", mealType.toString()));
        }

        return collection.find(filter).into(new ArrayList<>()).stream()
                .map(document -> document == null ? null : (BillEntity) Mapper.toObject(document, BillEntity.class))
                .filter(billEntity -> billEntity != null)
                .collect(Collectors.toList());
    }
}
