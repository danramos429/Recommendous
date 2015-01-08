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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {
	private Indexer<Place> indexer;
	private int buffer;

	public CSVLoader(Indexer<Place> indexer){
		this(indexer, 10);
	}
	public CSVLoader (Indexer<Place> indexer, int buffer) {
		this.indexer = indexer;
		this.buffer = buffer;
	}
	
	public int load(String filePath) throws IOException, IndexerException {
		File file = new File(filePath);
		if(!file.exists())
			return 0;

		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String read = reader.readLine();
		if(read == null)
			return 0;
			
		//todo - use regex
		String[] header = read.split(",");
		int idIndex = Integer.valueOf(header[0]);
		int nameIndex = Integer.valueOf(header[1]);
		int latIndex = Integer.valueOf(header[2]);
		int lngIndex = Integer.valueOf(header[3]);
		int featureIndex = Integer.valueOf(header[4]);
		int featureSize = Integer.valueOf(header[5]);
		
		read = reader.readLine();
		if(read == null)
			return 0;

		int updated = 0;
		String[] columnNames = read.split(",");
		ArrayList<Place>  places = new ArrayList<Place>(buffer);

		while((read = reader.readLine()) != null) {
			Place place = new Place();
			String[] entry = read.split(",");
			ArrayList<String> features = new ArrayList<String>(featureSize);
			ArrayList<Float> featureValues = new ArrayList<Float>(featureSize);
			
			place.id = entry[idIndex];
			place.name = entry[nameIndex];
			place.latitude = Double.valueOf(entry[latIndex]);
			place.longitude = Double.valueOf(entry[lngIndex]);
			
			for(int i=0; i<featureSize; i++) {
				if(entry[featureIndex + i].isEmpty())
					continue;
					
				features.add(columnNames[featureIndex + i]);
				featureValues.add(Float.valueOf(entry[featureIndex + i]));
			}
			
			place.features = features;
			place.values = featureValues;
			
			places.add(place);
			if(places.size() == buffer){
				try {
					indexer.addBeans((List<Place>)places.clone());
					updated += buffer;
					places.clear();
				} catch (IndexerException e) {
					e.printStackTrace();
					reader.close();
					throw new IndexerException("Error occurred while indexing");
				}
			}
		}

		if(places.size() > 0) {
			try {
				indexer.addBeans((List<Place>) places.clone());
				updated += places.size();
				places.clear();
			} catch (IndexerException e) {
				e.printStackTrace();
				reader.close();
				throw new IndexerException("Error occurred while indexing");
			}
		}

		reader.close();
		return updated;
	}
}
