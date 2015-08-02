package se.Tr1List.DAO;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import se.Tr1List.ProductListActivity;
import se.Tr1List.UserActivity;
import se.Tr1List.Misc.converter;
import se.Tr1List.Model.ProductListModel;
import se.Tr1List.Model.UserModel;
import se.Tr1List.Singleton.Instance;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetUsers extends	AsyncTask<Void, Integer, ArrayList<UserModel>> {
	ArrayList<UserModel> arrayList = new ArrayList<UserModel>();
	UserActivity context;
	String Inetaddress = Instance.getInstance().getInetAddress();
	private HttpResponse response;
	private DefaultHttpClient httpclient;
	private HttpPost httppost;
	private JSONObject data;
	private HttpEntity entity;
	private InputStream instream;
	private String result;
	private JSONArray array;
	private JSONObject row;
	private UserModel userModel;

	public GetUsers(Context context) {
		this.context = (UserActivity) context;
	}

	@Override
	protected ArrayList<UserModel> doInBackground(Void... params) {
		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(Inetaddress
					+ "/JSON/GetUsers");
			data = new JSONObject();
			
			data.put("UserId", Instance.getInstance().getUser().getId());
			data.put("ProductListId", Instance.getInstance().getActiveProductList().getId());
			
			httppost.setEntity(new ByteArrayEntity(data.toString().getBytes(
					"UTF8")));

			httppost.addHeader("Content-Type",
					"application/json; charset=utf-8");
			httppost.setHeader("json", data.toString());

			// Routine for POSTing JSON objects to the server
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			if (entity != null) {
				instream = entity.getContent();
				result = converter.convertStreamToString(instream);
				array = new JSONArray(result);
				for (int i = 0; i < array.length(); i++) {
					row = array.getJSONObject(i);
					boolean selected;
					if(row.getString("Status").equals("Included")){
						selected = true;
					}
					else{
						selected = false;
					}
					userModel = new UserModel.Builder()
					.setName(row.getString("Name"))
					.setId(row.getInt("Id"))
					.setPhoneId(row.getString("PhoneId"))
					.setSelected(selected)
					.setStatus(row.getString("Status"))
					.build();
					arrayList.add(userModel);
					publishProgress(i);
				}
				return arrayList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("...", "getUsers: " + e.getMessage());
		}
		return arrayList;
	}
/*
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(ArrayList<ProductListModel> result) {
		super.onPostExecute(result);
	}*/
}
