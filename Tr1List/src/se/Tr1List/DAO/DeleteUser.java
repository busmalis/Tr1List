package se.Tr1List.DAO;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import se.Tr1List.ProductListActivity;
import se.Tr1List.UserActivity;
import se.Tr1List.Misc.converter;
import se.Tr1List.Model.ProductListModel;
import se.Tr1List.Model.UserModel;
import se.Tr1List.Singleton.Instance;
import android.content.Context;
import android.os.AsyncTask;

public class DeleteUser extends
		AsyncTask<Void, Integer, ArrayList<UserModel>> {
	UserModel userModel;
	UserActivity userContext;
	String Inetaddress = Instance.getInstance().getInetAddress();
	private DefaultHttpClient httpclient;
	private HttpPost httppost;
	private JSONObject data;
	private HttpResponse response;
	private HttpEntity entity;
	private InputStream instream;
	private String result;

	public DeleteUser(Context context, UserModel itemmodel) {
		userContext = (UserActivity) context;
		userModel = itemmodel;
	}

	@Override
	protected ArrayList<UserModel> doInBackground(Void... params) {
		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(Inetaddress
					+ "/JSON/DeleteUser");

			data = new JSONObject();
			data.put("Id", Instance.getInstance().getUser().getId());

			httppost.setEntity(new ByteArrayEntity(data.toString().getBytes(
					"UTF8")));
			httppost.addHeader("Content-Type",
					"application/json; charset=utf-8");
			httppost.setHeader("json", data.toString());

			response = httpclient.execute(httppost);
			entity = response.getEntity();
			if (entity != null) {
				instream = entity.getContent();
				result = converter.convertStreamToString(instream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/*@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(ArrayList<UserModel> result) {
		super.onPostExecute(result);
	}*/
}
