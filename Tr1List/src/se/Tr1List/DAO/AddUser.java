package se.Tr1List.DAO;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import se.Tr1List.ProductListActivity;
import se.Tr1List.Misc.converter;
import se.Tr1List.Model.UserModel;
import se.Tr1List.Singleton.Instance;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AddUser extends AsyncTask<Void, Integer, Boolean> {
	UserModel model;
	ProductListActivity context;
	String Inetaddress;
	private DefaultHttpClient httpclient;
	private HttpPost httppost;
	private JSONObject data;
	private HttpResponse response;
	private HttpEntity entity;
	private InputStream instream;
	private String result;

	public AddUser(Context context, UserModel model) {
		context = (ProductListActivity) context;
		this.model = model;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try {
			
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(Instance.getInstance().getInetAddress() + "/JSON/GetUser");
			data = new JSONObject();

			data.put("PhoneId", model.getPhoneId());
			data.put("Name", "TempName");
			
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
				JSONObject row = new JSONObject(result);
				UserModel user = new UserModel.Builder().setId(row.getInt("Id")).setName(row.getString("Name")).setPhoneId(row.getString("PhoneId")).build();
				Instance.getInstance().setUser(user);
				
				Log.w("...", result);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*@Override 
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		itemListActivityContext.pb.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPostExecute(ArrayList<ProductModel> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		itemListActivityContext.pb.setVisibility(View.INVISIBLE);
	}*/
}
