package fr.ul.miage.resto.dao.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@DisplayName("MongoAccess")
@ExtendWith(MockitoExtension.class)
class MongoAccessTest {
    @Spy
    OrderCollection concrete = new OrderCollection();

    @Test
    @DisplayName("Le document existe déjà")
    void testInsertFalse(@Mock MongoCollection<Document> collection) {
        Document doc = new Document("_id", "id");
        doReturn(true).when(concrete).alreadyExist(anyString(), any());

        assertFalse(concrete.insert(doc, collection));
    }

    @Test
    @DisplayName("Le document est inséré")
    void testInsertTrue(@Mock MongoCollection<Document> collection) {
        Document doc = new Document("_id", "id");
        doReturn(false).when(concrete).alreadyExist(anyString(), any());

        assertTrue(concrete.insert(doc, collection));
    }

    @Test
    @DisplayName("Le document est mis à jour")
    void testUpdateTrue(@Mock MongoCollection<Document> collection) {
        Document doc = new Document("_id", "id");
        doReturn(true).when(concrete).alreadyExist(anyString(), any());

        assertTrue(concrete.update(doc, collection));
    }

    @Test
    @DisplayName("Le document ne peut pas être à jour il n'existe pas")
    void testUpdateFalse(@Mock MongoCollection<Document> collection) {
        Document doc = new Document("_id", "id");
        doReturn(false).when(concrete).alreadyExist(anyString(), any());

        assertFalse(concrete.update(doc, collection));
    }

    @Test
    @DisplayName("Le document n'existe pas")
    void testAlreadyExistFalse(@Mock MongoCollection<Document> collection) {

        doReturn(null).when(concrete).getDocumentById(anyString(), any());

        assertFalse(concrete.alreadyExist("id", collection));
    }

    @Test
    @DisplayName("Le document existe")
    void testAlreadyExistTrue(@Mock MongoCollection<Document> collection) {
        Document doc = new Document("_id", "id");
        doReturn(doc).when(concrete).getDocumentById(anyString(), any());

        assertTrue(concrete.alreadyExist("id", collection));
    }
}
