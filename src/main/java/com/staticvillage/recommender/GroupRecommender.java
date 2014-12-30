package com.staticvillage.recommender;

import com.staticvillage.recommender.bean.Place;
import com.staticvillage.recommender.indexer.Indexer;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.PlusAnonymousUserDataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.util.List;

/**
 * Created by joelparrish on 12/28/14.
 */
public class GroupRecommender {
    private Indexer<Place> indexer;
    private AnonymousRecommender recommender;

    public GroupRecommender(Indexer<Place> indexer, AnonymousRecommender recommender){
        this.indexer = indexer;
        this.recommender = recommender;
    };

    public List<RecommendedItem> recommend(long[] placeIds, int top) throws TasteException {
        return this.recommend(placeIds, top, null);
    }

    public List<RecommendedItem> recommend(long[] placeIds, int top, GeoPoint geoPoint) throws TasteException {
        if(recommender == null)
            return null;

        List<Place> beans = indexer.getBeans(geoPoint);
        PreferenceBag preferenceBag = new PreferenceBag();

        for(Place place : beans){
            for(String feature : place.features){
                preferenceBag.add(feature);
            }
        }

        return recommender.recommend(preferenceBag.getWeightedPreferences(
                PlusAnonymousUserDataModel.TEMP_USER_ID), top);
    }
}
