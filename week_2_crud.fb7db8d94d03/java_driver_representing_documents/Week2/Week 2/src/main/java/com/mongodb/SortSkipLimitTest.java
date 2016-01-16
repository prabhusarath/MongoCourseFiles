/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import static com.mongodb.client.model.Sorts.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SortSkipLimitTest {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();
        try {
            MongoDatabase db = client.getDatabase("course");
            MongoCollection<Document> collection = db.getCollection("SortSkipLimitTest");

            collection.drop();

            System.out.println("Without using inner documents (keeping it simple)");

            for (int i = 1;i<=10;i++){
                for (int j=1;j<=10;j++){
                    collection.insertOne( new Document("i",i).append("j",j));
                }
            }

            Bson filter = eq("i", "3");
            Bson projection = excludeId();

            Bson sortByDocument = new Document("j",1); // ascending
            // or same thing by builder
            Bson sortByHelper = ascending("j");

            System.out.println("\nNumber in collection where i is 3 (should be 10)");
            System.out.println(collection.count(filter));



            List <Document> docs = collection.find(filter)
                    .projection(projection)
                    .sort(sortByDocument).into(new ArrayList<Document>());

            System.out.println("Only i == 3 , j ascending");

            for(Document doc:docs){
                Helper.prettyPrintJSON(doc);
            }

            System.out.println();

            sortByDocument = new Document("i",1).append("j",-1);
            //or
            sortByHelper = orderBy(ascending("i"),descending("j"));

            System.out.println("All docs , i ascending and within j desc limiting by 50");

            docs = collection.find().projection(projection)
                    .sort(sortByHelper).limit(50)  //first fifty
                    .into(new ArrayList<Document>());

            for(Document doc:docs){
                Helper.prettyPrintJSON(doc);
            }

            System.out.println();


            System.out.println("All docs , i ascending and within j desc skipping 85");

            docs = collection.find().projection(projection)
                    .sort(sortByHelper).skip(85)  //first fifty
                    .into(new ArrayList<Document>());

            for(Document doc:docs){
                Helper.prettyPrintJSON(doc);
            }

            System.out.println();

            System.out.println("All docs , i ascending and within j desc skipping 85 limit5");

            docs = collection.find().projection(projection)
                    .sort(sortByHelper).skip(85).limit(5)
                    .into(new ArrayList<Document>());

            for(Document doc:docs){
                Helper.prettyPrintJSON(doc);
            }

            System.out.println();

            System.out.println("All docs , i ascending and within j desc skip 85 limit 50 ");
            System.out.println("Do you get error if your limit runs over??");

            docs = collection.find().projection(projection)
                    .sort(sortByHelper).skip(85).limit(50)
                    .into(new ArrayList<Document>());

            for(Document doc:docs){
                Helper.prettyPrintJSON(doc);
            }

            System.out.println();

            System.out.println();

            System.out.println("All docs , i ascending and within j desc skip 105 limit 50 ");
            System.out.println("Do you get error if your skip runs over??");

            docs = collection.find().projection(projection)
                    .sort(sortByHelper).skip(105).limit(50)
                    .into(new ArrayList<Document>());

            for(Document doc:docs){
                Helper.prettyPrintJSON(doc);
            }








            Random rand = new Random();

            // insert 10 lines with random start and end points
            for (int i = 0; i < 10; i++) {
                collection.insertOne(
                        new Document("_id", i)
                                .append("start",
                                        new Document("x", rand.nextInt(2))
                                                .append("y", rand.nextInt(90) + 10)
                                )
                                .append("end",
                                        new Document("x", rand.nextInt(2))
                                                .append("y", rand.nextInt(90) + 10)
                                )
                );
            }

            FindIterable<Document> results = collection.find()
                    .sort(new Document("start.x", 1).append("start.y", -1))
                    .skip(2).limit(5);

//            Iterator itr = results.iterator();
//
//
//                while (itr.hasNext()) {
//                    Document doc  = (Document) itr.next();
//                    System.out.println(doc);
//                }

            // or

            docs = results.into(new ArrayList<Document>());

            int ctr=0;
            for(Document doc:docs){
                System.out.println("Document # "+ ctr++);
                Helper.prettyPrintJSON(doc);
            }

            System.out.println("Total docs "+ctr);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
