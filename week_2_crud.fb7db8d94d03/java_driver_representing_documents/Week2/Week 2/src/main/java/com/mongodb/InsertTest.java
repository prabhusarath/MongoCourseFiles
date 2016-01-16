/*
 * Copyright (c) 2008 - 2015 MongoDB Inc. <http://mongodb.com>
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

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class InsertTest {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();     //defaults to localhost:27017
        try {

            MongoDatabase courseDB = client.getDatabase("course");
            MongoCollection<Document> collection = courseDB.getCollection("insertTest");

            collection.drop();

            Document doc = new Document("x",1).append("y",2);

            Helper.prettyPrintJSON(doc);

            collection.insertOne(doc);
            Helper.prettyPrintJSON(doc);

            ArrayList<Document> docs = collection.find().into(new ArrayList<Document>());

            for(Document d:docs){
                System.out.println(d);

            }

            collection.drop();

            Document smith = new Document("name","smith")
                    .append("age", 30).append("profession","programmer");

            Document jones = new Document("name","jones")
                    .append("age", 34).append("profession","hacker");

            collection.insertMany(Arrays.asList(smith,jones));

            System.out.println(collection.count());

            List<Document> docs2 = collection.find().into(new ArrayList<Document>());

            for(Document d:docs2){
                Helper.prettyPrintJSON(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}
