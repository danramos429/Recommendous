/*
Copyright 2015 Joel Parrish

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
 */
package com.staticvillage.recommender;

import com.staticvillage.recommender.bean.Place;
import com.staticvillage.recommender.exception.IndexerException;
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

    public List<RecommendedItem> recommend(long[] placeIds, int top) throws TasteException, IndexerException {
        return this.recommend(placeIds, top, null);
    }

    public List<RecommendedItem> recommend(long[] placeIds, int top, GeoPoint geoPoint) throws TasteException, IndexerException {
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
