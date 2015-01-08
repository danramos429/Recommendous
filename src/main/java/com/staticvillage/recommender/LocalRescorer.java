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
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.recommender.IDRescorer;

import java.util.List;

/**
 * Created by joelparrish on 12/28/14.
 */
public class LocalRescorer implements IDRescorer {
    private FastIDSet local;

    private Indexer<Place> indexer;
    private GeoPoint geoPoint;

    public LocalRescorer(Indexer<Place> indexer, GeoPoint geoPoint) throws IndexerException {
        this.indexer = indexer;
        this.geoPoint = geoPoint;

        local = getLocalIds();
    }

    @Override
    public double rescore(long placeId, double originalScore) {
        return isFiltered(placeId) ? Double.NaN : originalScore;
    }

    @Override
    public boolean isFiltered(long placeId) {
        return !local.contains(placeId);
    }

    private FastIDSet getLocalIds() throws IndexerException {
        FastIDSet fastIDSet = new FastIDSet();

        List<Place> places = indexer.getBeans(geoPoint);

        for(int i=0; i<places.size(); i++){
            long id = Long.valueOf(places.get(i).id);
            fastIDSet.add(id);
        }

        return fastIDSet;
    }
}
