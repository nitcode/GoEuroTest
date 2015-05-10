package com.test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonToCsv {
	
	public static JsonArray ReadJson(String cityName) throws IOException {	
		String userURL = "http://api.goeuro.com/api/v2/position/suggest/en/"+cityName;
		URL url = new URL(userURL);
	    HttpURLConnection request = (HttpURLConnection) url.openConnection();
	    request.connect();
	    JsonParser jParser = new JsonParser(); 
	    JsonElement jRoot = jParser.parse(new InputStreamReader((InputStream) request.getContent()));     
	    JsonArray jArray=jRoot.getAsJsonArray();
	    return jArray;
	}
	
	public static void writeCSV(JsonArray array) throws IOException {
		FileWriter fileWriter = new FileWriter("data.csv");
	    String header="Id,Name,Type,Country,Latitude,Longitude";
	    fileWriter.append(header);
	    fileWriter.append("\n");  
	    JsonObject jObject=null;
	    JsonObject jCoordinates=null;
	    for (int i = 0; i < array.size(); i++) {
	    	jObject = array.get(i).getAsJsonObject();
	        String _id = jObject.get("_id").getAsString();
	        fileWriter.append(_id);
	        fileWriter.append(",");
	        String name = jObject.get("name").getAsString();
	        fileWriter.append(name);
	        fileWriter.append(",");
	        String type = jObject.get("type").getAsString();
	        fileWriter.append(type);
	        fileWriter.append(",");
	        String country = jObject.get("country").getAsString();
	        fileWriter.append(country);
	        fileWriter.append(",");
	        jCoordinates = jObject.get("geo_position").getAsJsonObject();
	        String latitude=jCoordinates.get("latitude").getAsString();
	        fileWriter.append(latitude);
	        fileWriter.append(",");
	        String longitude=jCoordinates.get("longitude").getAsString();
	        fileWriter.append(longitude);
	        fileWriter.append(",");
	        fileWriter.append('\n');
	    }
	    fileWriter.flush();
	    fileWriter.close();		
	}

	public static void main(String[] args) {
		String cityName=args[0];
		JsonArray jArray = null;
		try {
			jArray = ReadJson(cityName);
			if(jArray.size()==0) {
		    	System.out.println("Enter valid city name");
		    }
		    else {
		    	try {
					writeCSV(jArray);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
		    	System.out.println("CSV created");
		    }
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}