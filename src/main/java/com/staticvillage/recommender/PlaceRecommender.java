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
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.util.Collection;
import java.util.List;

/**
 * Created by joelparrish on 12/28/14.
 */
public class PlaceRecommender implements Recommender {
    private Recommender delegate;
    private DataModel model;

    private Indexer<Place> indexer;
    private GeoPoint geoPoint;

    public PlaceRecommender(DataModel model) throws TasteException {
        this(model, 5);
    }

    public PlaceRecommender(DataModel model, int neighborhoodSize) throws TasteException {
        this(model, neighborhoodSize, null, null);
    }

    public PlaceRecommender(DataModel model, int neighborhoodSize, Indexer<Place> indexer, GeoPoint geoPoint) throws TasteException {
        this.model = model;
        this.indexer = indexer;
        this.geoPoint = geoPoint;

        UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighborhoodSize, similarity, model);

        delegate = new GenericUserBasedRecommender(model, neighborhood, similarity);
    }

    @Override
    public List<RecommendedItem> recommend(long placeId, int howMany) throws TasteException {
        if(indexer == null || geoPoint == null)
            return delegate.recommend(placeId, howMany);

        LocalRescorer rescorer = null;
        try {
            rescorer = new LocalRescorer(indexer, geoPoint);
        } catch (IndexerException e) {
            e.printStackTrace();
            throw new TasteException("Error occurred configuring rescorer");
        }

        return recommend(placeId, howMany, rescorer);
    }

    @Override
    public List<RecommendedItem> recommend(long placeId, int howMany, IDRescorer idRescorer) throws TasteException {
        return delegate.recommend(placeId, howMany, idRescorer);
    }

    @Override
    public float estimatePreference(long placeId, long featureId) throws TasteException {
        return 0;
    }

    @Override
    public void setPreference(long placeId, long featureId, float value) throws TasteException {
        delegate.setPreference(placeId, featureId, value);
    }

    @Override
    public void removePreference(long placeId, long featureId) throws TasteException {
        delegate.removePreference(placeId, featureId);
    }

    @Override
    public DataModel getDataModel() {
        return delegate.getDataModel();
    }

    @Override
    public void refresh(Collection<Refreshable> collection) {
        delegate.refresh(collection);
    }
}
