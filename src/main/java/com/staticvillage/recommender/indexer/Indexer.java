package com.staticvillage.recommender.indexer;

import com.staticvillage.recommender.exception.IndexerException;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;

/**
 * Created by joelparrish on 12/28/14.
 */
public interface Indexer<E> {
    public void addBean(E bean) throws IndexerException;
    public void addBeans(List<E> beans) throws IndexerException;
    public List<E> getBeans() throws IndexerException;
    public List<E> getBeans(Object obj);
}
