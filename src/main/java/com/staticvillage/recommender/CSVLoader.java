package com.staticvillage.recommender;

public class CSVLoader {
	private Indexer<Place> indexer;
	
	public CSVLoader (Indexer<Place> indexer) {
		this.indexer = indexer;
	}
	
	public int load(String filePath) {
		File file = new File(filePath);
		if(!file.exists())
			return 0;
			
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String read = reader.readLine();
		if(read == null)
			return 0;
			
		//todo - use regex
		String header = read.split(",");
		int idIndex = read[0]
		int nameIndex = read[1];
		int latIndex = read[2];
		int lngIndex = read[3];
		int featureIndex = read[4];
		int featureSize = Integer.valueOf(read[5]);
		
		read = reader.readLine();
		if(read == null)
			return 0;
			
		String columnNames = read.split(",");
		
		while((read = reader.readLine()) != null) {
			Place place = new Place();
			String[] entry = read.spli(",");
			ArrayList<String> features = new ArrayList<String>(featureSize);
			ArrayList<Float> featureValues = new ArrayList<Float>(featureSize);
			
			place.id = entry[idIndex];
			place.name = entry[nameIndex];
			place.latitude = Double.valueOf(entry[latIndex]);
			place.longitude = Double.valueOf(entry[lngIndex]);
			
			for(int i=0; i<featureSize; i++) {
				if(entry[featureIndex + i] = String.empty())
					continue;
					
				features.add(columnNames[featureIndex + i]);
				featureValues.add(entry[featureIndex + i]);
			}
			
			place.features = features;
			place.values = featureValues;
			
			indexer.addBean(place);
		}
	}
}
