package com.staticvillage.recommender.indexer;

import org.junit.Test;

import static org.junit.Assert.*;

public class MongoDBIndexerTest {

    @Test
    public void testAddBean() throws Exception {
        String connString = "mongodb://testuser:test@192.168.2.136/test_indexer";
        MongoDBIndexer indexer = new MongoDBIndexer(connString, "test_indexer", "mongo_place");
    }

    @Test
    public void testAddBeans() throws Exception {

    }

    @Test
    public void testUpdateBean() throws Exception {

    }

    @Test
    public void testGetBeans() throws Exception {

    }

    @Test
    public void testGetBeans1() throws Exception {

    }
}