package com.staticvillage.recommender.bean;

import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

/**
 * Created by joelparrish on 12/28/14.
 */
public class Place {
    @Field
    public String id;

    @Field
    public List<String> features;

    @Field
    public List<String> values;

    @Field("lat")
    public String latitude;

    @Field("lng")
    public String longitude;
}
