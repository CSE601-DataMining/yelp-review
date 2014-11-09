package edu.buffalo.cse.yelp.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.*;
public class DataLoader {
	
	public class Business
	{
		public final String id;
		public Business next;
		
		public Business(String id) {
			this.id = id;
			this.next = null;
		}
	}
	
	private static DataLoader _dataLoader = null;
	public ArrayList<JSONObject> businesses;
	public HashMap<String,Business> reviews = new HashMap<String, DataLoader.Business>();
	public ArrayList<JSONObject> users;
	public ArrayList<JSONObject> checkins;
	public ArrayList<JSONObject> tips;
	
	private DataLoader() {
		loadBusinesses();
		loadReviews();
		loadUsers();
		loadTips();
		loadCheckins();
	}
	
	public static DataLoader GetInstance() 
	{
		if (_dataLoader == null)
			_dataLoader = new DataLoader();
		return _dataLoader;
	}

	
	private void loadBusinesses() 
	{
		businesses = readFile("yelpdata/yelp_dataset_challenge_academic_dataset/business.txt",false);
	}
	private void loadReviews() 
	{
		String file = "yelpdata/yelp_dataset_challenge_academic_dataset/review.txt";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line;
		
		try {
			while ((line = br.readLine()) != null) {
				JSONObject jsonObject = new JSONObject(line);
				String user_id = (String) jsonObject.get("user_id");
				if (reviews.containsKey(user_id))
					reviews.get(user_id).next = new Business((String) jsonObject.get("business_id"));
				else
					reviews.put(user_id, new Business((String) jsonObject.get("business_id")));
			}
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void loadUsers() 
	{
		users = readFile("yelpdata/yelp_dataset_challenge_academic_dataset/user.txt",false);
	}
	private void loadTips() 
	{
		tips = readFile("yelpdata/yelp_dataset_challenge_academic_dataset/tip.txt",false);
	}
	private void loadCheckins() 
	{
		checkins = readFile("yelpdata/yelp_dataset_challenge_academic_dataset/checkin.txt",false);
	}

	
	private ArrayList<JSONObject> readFile(String file, Boolean ignoreText)
	{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line;
		ArrayList<JSONObject> jsonArray = new ArrayList<JSONObject>();
		
		
		try {
			while ((line = br.readLine()) != null) {
				JSONObject jsonObject = new JSONObject(line);
				if (ignoreText)
					jsonObject.remove("text");
				jsonArray.add(jsonObject);
			}
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonArray;
	}


}
