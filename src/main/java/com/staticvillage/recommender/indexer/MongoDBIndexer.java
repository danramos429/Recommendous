/*
Copyright 2015 Joel Parrish

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
 */
package com.staticvillage.recommender.indexer;

import com.mongodb.*;
import com.staticvillage.recommender.GeoPoint;
import com.staticvillage.recommender.bean.Place;
import com.staticvillage.recommender.exception.IndexerException;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joelparrish on 1/2/15.
 */
public class MongoDBIndexer implements Indexer<Place> {
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_FEATURES = "features";
    public static final String KEY_VALUES = "values";
    public static final String KEY_LOCATION = "loc";
    public static final String KEY_LOCATION_TYPE = "type";
    public static final String KEY_LOCATION_COORDINATES = "coordinates";

    private String connectionString;
    private String database;
    private String collection;
    private MongoClient mongoClient;
    private DB instanceDB;

    public MongoDBIndexer(String connectionString, String database, String collection) throws UnknownHostException {
        this.connectionString = connectionString;
        this.database = database;
        this.collection = collection;

        MongoClientURI uri = new MongoClientURI(connectionString);
        mongoClient = new MongoClient(uri);
        instanceDB = mongoClient.getDB(database);
    }

    @Override
    public void addBean(Place bean) throws IndexerException {
        DBCollection dbCollection = instanceDB.getCollection(collection);
        dbCollection.insert(toDBObject(bean), WriteConcern.NORMAL);
    }

    @Override
    public void addBeans(List<Place> beans) throws IndexerException {
        DBCollection dbCollection = instanceDB.getCollection(collection);
        BulkWriteOperation builder = dbCollection.initializeUnorderedBulkOperation();

        for(Place place : beans){
            builder.insert(toDBObject(place));
        }

        builder.execute();
    }
	
	@Override
	public boolean updateBean(Place bean){
		DBCollection dbCollection = instanceDB.getCollection(collection);
        BasicDBObject item = new BasicDBObject("id", bean.id);

        WriteResult writeResult = dbCollection.update(item, toDBObject(bean));
        return writeResult.isUpdateOfExisting();
    }

    @Override
    public List<Place> getBeans() throws IndexerException {
        DBCollection dbCollection = instanceDB.getCollection(collection);
        DBCursor cursor = dbCollection.find();

        ArrayList<Place> places = new ArrayList<Place>(cursor.count());
        try {
            while (cursor.hasNext()) {
                places.add(fromDBObject(cursor.next()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }

        return places;
    }

    @Override
    public List<Place> getBeans(Object obj) throws IndexerException {
        GeoPoint geoPoint = (GeoPoint)obj;

        BasicDBObject near = new BasicDBObject();
        near.put("geometry", toGeoJSON(geoPoint.getLatitude(), geoPoint.getLongitude()));
        near.put("$maxDistance", 1609);

        BasicDBObject query = new BasicDBObject();
        query.put("$near", near);

        DBCollection dbCollection = instanceDB.getCollection(collection);
        DBCursor cursor = dbCollection.find(query);

        ArrayList<Place> places = new ArrayList<Place>(cursor.count());
        try {
            while (cursor.hasNext()) {
                places.add(fromDBObject(cursor.next()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }

        return places;
    }

    private DBObject toGeoJSON(double latitude, double longitude){
        BasicDBObject locObject = new BasicDBObject();
        locObject.put(KEY_LOCATION_TYPE, "Point");
        locObject.put(KEY_LOCATION_COORDINATES, new Double[]{ latitude, longitude });

        return locObject;
    }

    private DBObject toDBObject(Place place){
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put(KEY_ID, place.id);
        dbObject.put(KEY_NAME, place.name);
        dbObject.put(KEY_FEATURES, place.features);
        dbObject.put(KEY_VALUES, place.values);
        dbObject.put(KEY_LOCATION, toGeoJSON(place.latitude, place.longitude));

        return dbObject;
    }

    private Place fromDBObject(DBObject obj){
        Place place = new Place();

        place.id = (String)obj.get(KEY_ID);
        place.name = (String)obj.get(KEY_NAME);
        place.features = (List<String>)obj.get(KEY_FEATURES);
        place.values = (List<Float>)obj.get(KEY_VALUES);
        place.latitude = (Double)obj.get(KEY_LOCATION_COORDINATES);
        place.longitude = (Double)obj.get(KEY_ID);

        return place;
    }
}
