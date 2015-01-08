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
