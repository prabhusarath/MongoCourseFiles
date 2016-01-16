package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Author Mike Clovis
 * Date: 10/22/2015
 * Time: 6:48 PM
 */
public class DeleteTest {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> collection = db.getCollection("deleteTest");

        collection.drop();

        for(int idx = 1;idx<=8;idx++){
            collection.insertOne(new Document("_id",idx));
        }

        System.out.println("Before any deletes");
        List<Document>docs =collection.find().into(new ArrayList<Document>());
        for(Document doc:docs){
            Helper.prettyPrintJSON(doc);
        }

        // delete 2,3,4

        collection.deleteMany(and(gte("_id", 2), lt("_id", 5)));
        System.out.println("removed gte2 lt5");
        docs = collection.find().into(new ArrayList<Document>());
        for (Document doc:docs){
            Helper.prettyPrintJSON(doc);
        }

        collection.deleteOne(eq("_id",8));
        System.out.println("Removed id 8");

        docs = collection.find().into(new ArrayList<Document>());
        for (Document doc:docs){
            Helper.prettyPrintJSON(doc);
        }
    }
}
