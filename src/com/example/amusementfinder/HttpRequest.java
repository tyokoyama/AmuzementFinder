package com.example.amusementfinder;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpRequest {

	public Response get(String url) {
		HttpGet param = new HttpGet(url);
		
		return httpRequest(param);
	}
	
	private Response httpRequest(HttpUriRequest param) {
		HttpClient client = new DefaultHttpClient();
		HttpEntity entity = null;
		Response result = new Response();

		try {
			HttpResponse res = client.execute(param);
			
			result.setStatusCode(res.getStatusLine().getStatusCode());
			entity = res.getEntity();
			result.setResponse(EntityUtils.toString(entity));
			
		} catch (UnknownHostException uhe) {
			result.setResponse("Unknown Host");
		} catch (Exception e) {
			result.setResponse("System Error");
		} finally {
			try {
				if (entity != null)
					entity.consumeContent();
			} catch (IOException e) {

			}
			client.getConnectionManager().shutdown();
		}
		return result;
	}

	public class Response {
		private int statusCode;
		private String response;

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public String getResponse() {
			return response;
		}

		public void setResponse(String response) {
			this.response = response;
		}
		
		public boolean isOk() {
			return this.statusCode == HttpStatus.SC_OK;
		}
	}
}
