package com.staticvillage.recommender;

import com.staticvillage.recommender.bean.Place;
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

        LocalRescorer rescorer = new LocalRescorer(indexer, geoPoint);
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
