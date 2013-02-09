package com.example.amusementfinder;

import java.util.List;

import com.example.amusementfinder.YOLP.AmusementInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class GetAmusementTask extends AsyncTask<Double, Void, List<AmusementInfo>> {

	private ProgressDialog mDialog;
	private GoogleMap mMap;
	
	public GetAmusementTask(Context c, GoogleMap map) {
		mDialog = new ProgressDialog(c);
		mDialog.setMessage("データ検索中...");
		mDialog.setIndeterminate(true);
		mDialog.setCanceledOnTouchOutside(false);
		
		mMap = map;
	}
	
	@Override
	protected List<AmusementInfo> doInBackground(Double... params) {
		List<AmusementInfo> list = YOLP.get(params[0], params[1]);
		
		return list;
	}

	@Override
	protected void onPostExecute(List<AmusementInfo> result) {
		super.onPostExecute(result);
		
		if(mMap != null) {
			mMap.clear();
			for(AmusementInfo info : result) {
				LatLng geo = new LatLng(info.getLat(), info.getLng());
				mMap.addMarker(new MarkerOptions().position(geo).title(info.getName()).snippet(info.getAddress()));
			}
		}
		
		mDialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog.show();
	}

}
