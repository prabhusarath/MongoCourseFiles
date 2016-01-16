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


import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FindTest {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();
//        this is the way to accomplish task using Driver version 2.x
//        while this methodology is still supported, it is better
//        to use newer version (Driver 3.x or better

//        DB db = client.getDB("course");
//        DBCollection collection = db.getCollection("findTest");

//        This is the newer way to do it.
        MongoDatabase db =client.getDatabase("course");
        MongoCollection <Document> collection = db.getCollection("findTest");
        collection.drop();

        // insert 10 documents with a random integer as the value of field "x"
        for (int i = 0; i < 10; i++) {

            collection
                    .insertOne(new Document("x",
                            new Random().nextInt(100)));
        }

        System.out.println("Find one:");

        Document one = collection.find().first();
        System.out.println(one);

        // or just the x value
        System.out.println("x: "+one.getInteger("x"));

        System.out.println("\nFind all with into (collection)");

        List<Document> myDocs = collection.find().into(new ArrayList<Document>());

        System.out.println("\nCount: ");
        System.out.println(myDocs.size());

        for(Document d:myDocs){
            Helper.prettyPrintJSON(d);
        }



        System.out.println("\nFind all with iteration of FindIterable ");

        FindIterable<Document> docs = collection.find();
        Iterator itr = docs.iterator();

        System.out.println("\nCount:");
        long count = collection.count();
        System.out.println(count);

        while (itr.hasNext()) {
            Document  cur = (Document) itr.next();
            System.out.println(cur);
        }


        System.out.println("\nFind all with iteration of cursor ");
        MongoCursor<Document> cursor = collection.find().iterator();


        count = 0;

        try {
            while(cursor.hasNext()){
                Document doc = cursor.next();
                Helper.prettyPrintJSON(doc);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        System.out.println("\nCount: ");
        System.out.println(count);

        client.close();

    }
}
