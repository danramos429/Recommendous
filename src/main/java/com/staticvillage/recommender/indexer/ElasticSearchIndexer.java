package com.staticvillage.recommender.indexer;

import com.staticvillage.recommender.bean.Place;

import java.util.List;

/**
 * Created by joelparrish on 12/29/14.
 */
public class ElasticSearchIndexer implements Indexer<Place> {
    @Override
    public void addBean(Place bean) {

    }

    @Override
    public void addBeans(List<Place> beans) {

    }

    @Override
    public List<Place> getBeans() {
        return null;
    }

    @Override
    public List<Place> getBeans(Object obj) {
        return null;
    }
}
