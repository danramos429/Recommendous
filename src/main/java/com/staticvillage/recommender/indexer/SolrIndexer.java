package com.staticvillage.recommender.indexer;

import com.staticvillage.recommender.bean.Place;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.List;

/**
 * Created by joelparrish on 12/29/14.
 */
public class SolrIndexer implements Indexer<Place> {
    private HttpSolrServer solr;
    private String solrUrl;

    public SolrIndexer(String solrUrl){
        this.solrUrl = solrUrl;

        solr = new HttpSolrServer(solrUrl);
    }

    @Override
    public void addBean(Place place) throws IOException, SolrServerException {
        solr.addBean(place);
    }

    @Override
    public void addBeans(List<Place> places) throws IOException, SolrServerException {
        solr.addBeans(places);
    }

    @Override
    public List<Place> getBeans() throws SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        //query.

        QueryResponse rsp = solr.query(query);
        return rsp.getBeans(Place.class);
    }

    @Override
    public List<Place> getBeans(Object obj) {
        return null;
    }
}
