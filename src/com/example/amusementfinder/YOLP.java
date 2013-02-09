package com.example.amusementfinder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.amusementfinder.HttpRequest.Response;

public class YOLP {
	private YOLP() {
		
	}
	
	public static List<AmusementInfo> get(double lat, double lng) {
		HttpRequest req = new HttpRequest();
		StringBuilder sb = new StringBuilder();
		sb.append("http://search.olp.yahooapis.jp/OpenLocalPlatform/V1/localSearch?");
		sb.append("appid=dj0zaiZpPXk0U3JtbGt1ZkpUSSZkPVlXazliV2RDTm1JNU5tOG1jR285TUEtLSZzPWNvbnN1bWVyc2VjcmV0Jng9NzQ-");
		sb.append("&lat=" + lat + "&lon=" + lng + "&dist=1&output=json&device=mobile");
		
		List<AmusementInfo> list = new ArrayList<YOLP.AmusementInfo>();
		Response res = req.get(sb.toString());
		
		// レスポンスからデータを抽出する。
		try {
			JSONObject root = new JSONObject(res.getResponse());
			
			JSONArray feature = root.getJSONArray("Feature");
			for(int i = 0; i < feature.length(); i++) {
				AmusementInfo info = new AmusementInfo();
				
				JSONObject obj = feature.getJSONObject(i);
				String name = obj.getString("Name");
				JSONObject geometry = obj.getJSONObject("Geometry");
				String[] coodinate = geometry.getString("Coordinates").split(",");
				JSONObject property = obj.getJSONObject("Property");
				String address = property.getString("Address");
				
				info.setName(name);
				info.setLat(Double.parseDouble(coodinate[1]));
				info.setLng(Double.parseDouble(coodinate[0]));
				info.setAddress(address);
				
				list.add(info);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static class AmusementInfo {
		private String name;
		private double lat;
		private double lng;
		private String address;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getLat() {
			return lat;
		}
		public void setLat(double lat) {
			this.lat = lat;
		}
		public double getLng() {
			return lng;
		}
		public void setLng(double lng) {
			this.lng = lng;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
	}
}
