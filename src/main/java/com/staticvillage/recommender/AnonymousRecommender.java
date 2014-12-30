package com.staticvillage.recommender;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.PlusAnonymousUserDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.util.List;

/**
 * Created by joelparrish on 12/28/14.
 */
public class AnonymousRecommender extends PlaceRecommender {
    private PlusAnonymousUserDataModel model;

    public AnonymousRecommender(DataModel model) throws TasteException {
        super(model);
    }

    public AnonymousRecommender(DataModel model, int neighborhoodSize) throws TasteException {
        super(new PlusAnonymousUserDataModel(model), neighborhoodSize);

        model = (PlusAnonymousUserDataModel) getDataModel();
    }

    public synchronized List<RecommendedItem> recommend(PreferenceArray prefs, int howMany) throws TasteException {
        model.setTempPrefs(prefs);

        List<RecommendedItem> recommendations = recommend(PlusAnonymousUserDataModel.TEMP_USER_ID, howMany, null);
        model.clearTempPrefs();

        return recommendations;
    }
}
