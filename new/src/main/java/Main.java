/**
 * Created by SarathKumar on 1/5/2016.
 */
//package com.mongodb;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Main {
    public static void main(String[] args) {
        Spark.get("/",new Route() {
            @Override
            public Object handle(final Request request,
                                 final Response response) {
                return "Hello World From Spark\n";
            }
        });


    }
}

