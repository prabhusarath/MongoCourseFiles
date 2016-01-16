package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import org.bson.Document;
import org.bson.conversions.Bson;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author Mike Clovis
 * Date: 10/22/2015
 * Time: 2:36 PM
 */
public class FindWithFilterTest {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");

        MongoCollection<Document> collection = db.getCollection("findWithFilterTest");

        collection.drop();

        //insert 10 documents with random x and y

        for(int idx = 0;idx<10;idx++){
            collection.insertOne(new Document("x",new Random().nextInt(2))
            .append("y",new Random().nextInt(100)));
        }

        //put all into a list
        List<Document> docs = collection.find().into(new ArrayList<Document>());

        for(Document doc:docs){
            Helper.prettyPrintJSON(doc);
        }

        long count = collection.count();

        System.out.println("\nCount: ");
        System.out.println(count);

        Bson filterXByDocument = new Document("x",0);
        // or this is the same
        Bson filterXByHelper = eq("x", 0);       //static import of Filters

        System.out.println("\nCount of x==0");
        System.out.println(collection.count(filterXByDocument));

        System.out.println("Let's limit by filter what is returned");


        List<Document> filteredDocs =
                collection.find(filterXByHelper).into(new ArrayList<Document>());

        System.out.println("\nCount returned: ");
        System.out.println(filteredDocs.size());
        System.out.println("");
        for(Document doc:filteredDocs){
            Helper.prettyPrintJSON(doc);
        }

        // y is greater than 10

        Bson filterYByDocument = new Document("y",
                new Document("$gt",10));
        //or equivalent

        Bson filterYByHelper = gt("y",10);// again using static import of filters

        System.out.println("\nCount of docs that match y greater than 10");
        System.out.println(collection.count(filterYByHelper));

        filteredDocs = collection.find(filterYByDocument).into(new ArrayList<Document>());

        System.out.println("\nActual number returned");
        System.out.println(filteredDocs.size());

        System.out.println();
        for(Document doc:filteredDocs){
           Helper.prettyPrintJSON(doc);
        }

        // create an interval for y  between 10 and 90

        filterYByDocument  = new Document("y",
                new Document("$gt",10).append("$lt",90)); //implicit and
        filterYByHelper = and(gt("y", 10), lt("y", 90));

        System.out.println("\nCount of documents in collection that match: ");
        System.out.println(collection.count(filterYByDocument));

        filteredDocs = collection.find(filterYByHelper).into(new ArrayList<Document>());
        System.out.println("\nNumber of documents returned: ");
        System.out.println(filteredDocs.size());

        System.out.println("\nAnd the docs are: ");
        for(Document doc:filteredDocs){
            Helper.prettyPrintJSON(doc);
        }













    }
}
