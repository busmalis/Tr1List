package se.Tr1List.DAO;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import se.Tr1List.Misc.converter;
import se.Tr1List.Model.UserModel;
import se.Tr1List.Singleton.Instance;
import android.os.AsyncTask;

public class EditUser extends AsyncTask<Void, Integer, UserModel> {
	UserModel model;
	//SettingsActivity settingsContext;
	String Inetaddress = Instance.getInstance().getInetAddress();
	private DefaultHttpClient httpclient;
	private HttpPost httppost;
	private JSONObject data;
	private HttpResponse response;
	private InputStream instream;

	public EditUser(UserModel model) {//Context context, UserModel model) {
		//settingsContext = (SettingsActivity) context;
		this.model = model;
	}

	@Override
	protected UserModel doInBackground(Void... params) {

		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(Inetaddress + "/JSON/EditUser");

			data = new JSONObject();
			data.put("Name", model.getName());
			data.put("PhoneId", Instance.getInstance().getUser().getPhoneId());
			data.put("Id", Instance.getInstance().getUser().getId());

			httppost.setEntity(new ByteArrayEntity(data.toString().getBytes(
					"UTF8")));
			httppost.addHeader("Content-Type",
					"application/json; charset=utf-8");
			httppost.setHeader("json", data.toString());

			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				instream = entity.getContent();
				String result = converter.convertStreamToString(instream);				
				return model;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	/*@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(ProductListModel result) {
		super.onPostExecute(result);
	}*/
}
