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

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Projections.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Random;

public class FieldSelectionTest {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> collection = db.getCollection("fieldSelectionTest");
        collection.drop();
        Random rand = new Random();

        // insert 10 documents with two random integers
        for (int i = 0; i < 10; i++) {
            collection.insertOne(
                    new Document("x", rand.nextInt(2))
                            .append("y", rand.nextInt(100))
                            .append("z", rand.nextInt(1000)));
        }

        Bson filter = Filters.eq("x",0);

        Bson projectionByDocument  = new Document("y", true);

        //or you can
        //Bson projectionByDocument = new Document("y", true).append("i",true).append("_id",false);


        Bson projectionByHelper = include("y", "i"); //will still include _id

        //or you can
        //Bson projectionByHelper = fields(include("y","i"),excludeId());
        //Bson projectionByHelper = exclude("_id","x");

        //comment and uncomment projectionValues during different runs

        Iterator itr = collection.find(filter)
                .projection(projectionByDocument).iterator();
        try {
            while (itr.hasNext()) {
                Document doc  = (Document)itr.next();
                Helper.prettyPrintJSON(doc);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            client.close();
        }
    }
}
