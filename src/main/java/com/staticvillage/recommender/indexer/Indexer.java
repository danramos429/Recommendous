package com.staticvillage.recommender.indexer;

import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;

/**
 * Created by joelparrish on 12/28/14.
 */
public interface Indexer<E> {
    public void addBean(E bean) throws IOException, SolrServerException;
    public void addBeans(List<E> beans) throws IOException, SolrServerException;
    public List<E> getBeans() throws SolrServerException;
    public List<E> getBeans(Object obj);
}
