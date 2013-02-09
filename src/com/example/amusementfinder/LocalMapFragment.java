package com.example.amusementfinder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class LocalMapFragment extends SupportMapFragment implements OnMapClickListener
																	  , OnMarkerClickListener
																	  , OnInfoWindowClickListener
																	  , OnCameraChangeListener
																	  , OnInitializedListener{

	private GetAmusementTask mTask;

	@Override
	public void onInflate(Activity arg0, AttributeSet arg1, Bundle arg2) {
		super.onInflate(arg0, arg1, arg2);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		GoogleMap map = getMap();
		map.setOnMapClickListener(this);
		map.setOnMarkerClickListener(this);
		map.setOnInfoWindowClickListener(this);
		map.setOnCameraChangeListener(this);
		
		// マップの初期始点の設定（今回は広島国際学院大学）
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.391185,132.455852), 18.0f));
	}

	@Override
	public void onMapClick(LatLng geo) {
		// データを取得、ピンを立てる
		mTask = new GetAmusementTask(getActivity(), getMap());
		mTask.execute(geo.latitude, geo.longitude);
	}

	@Override
	public void onPause() {
		super.onPause();
		
		// AsyncTaskのキャンセル
		if(mTask != null && !mTask.isCancelled()) {
			mTask.cancel(true);
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Log.d("TEST", "onInfoWindowClick");
		YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(getActivity());
		if(result == YouTubeInitializationResult.SUCCESS) {
			YouTubePlayerSupportFragment youtubeFragment = YouTubePlayerSupportFragment.newInstance();
			youtubeFragment.initialize("AIzaSyCssybXFtvPcAnMl40xvBWJ0cXo4bfLcvw", this);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.layout1, youtubeFragment);
			ft.addToBackStack(null);
			ft.commit();
		} else {
			Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		Log.d("TEST", "onMarkerClick");
		return false;
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		Log.d("TEST", "onCameraChange");
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		Toast.makeText(getActivity(), arg1.toString(), Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player,
			boolean arg2) {
		Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
		player.loadVideo("TwIjCQfT2L4");
		player.play();		
	}
}
