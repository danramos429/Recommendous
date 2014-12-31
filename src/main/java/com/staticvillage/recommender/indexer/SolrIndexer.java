package com.staticvillage.recommender.indexer;

import com.staticvillage.recommender.bean.Place;
import com.staticvillage.recommender.exception.IndexerException;
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
    public void addBean(Place place) throws IndexerException {
        try {
            solr.addBean(place);
        } catch (IOException e) {
            throw new IndexerException(e.getMessage());
        } catch (SolrServerException e) {
            throw new IndexerException(e.getMessage());
        }
    }

    @Override
    public void addBeans(List<Place> places) throws IndexerException {
        try {
            solr.addBeans(places);
        } catch (IOException e) {
            throw new IndexerException(e.getMessage());
        } catch (SolrServerException e) {

        }
    }

    @Override
    public List<Place> getBeans() throws IndexerException {
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        //query.

        try {
            QueryResponse rsp = solr.query(query);
            return rsp.getBeans(Place.class);
        } catch (SolrServerException e) {
            throw new IndexerException(e.getMessage());
        }
    }

    @Override
    public List<Place> getBeans(Object obj) {
        return null;
    }
}
