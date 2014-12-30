package com.staticvillage.recommender;

import com.staticvillage.recommender.bean.Place;
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

    public LocalRescorer(Indexer<Place> indexer, GeoPoint geoPoint){
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

    private FastIDSet getLocalIds(){
        FastIDSet fastIDSet = new FastIDSet();

        List<Place> places = indexer.getBeans(geoPoint);

        for(int i=0; i<places.size(); i++){
            long id = Long.valueOf(places.get(i).id);
            fastIDSet.add(id);
        }

        return fastIDSet;
    }
}
