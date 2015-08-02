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
import se.Tr1List.Misc.converter;
import se.Tr1List.Model.ProductListModel;
import se.Tr1List.Singleton.Instance;
import android.content.Context;
import android.os.AsyncTask;

public class GetProductList extends	AsyncTask<Void, Integer, ArrayList<ProductListModel>> {
	ArrayList<ProductListModel> arrayList = new ArrayList<ProductListModel>();
	ProductListActivity productListContext;
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
	private ProductListModel productListModel;

	public GetProductList(Context context) {
		productListContext = (ProductListActivity) context;
	}

	@Override
	protected ArrayList<ProductListModel> doInBackground(Void... params) {
		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(Inetaddress
					+ "/JSON/GetProductList");
			data = new JSONObject();

			data.put("Name", Instance.getInstance().getUser().getName());
			data.put("Id", Instance.getInstance().getUser().getId());
			data.put("PhoneId", Instance.getInstance().getUser().getPhoneId());
			
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
					productListModel = new ProductListModel.Builder()
					.setName(row.getString("Name"))
					.setId(row.getInt("Id"))
					.build();
					arrayList.add(productListModel);
					publishProgress(i);
				}
				return arrayList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(ArrayList<ProductListModel> result) {
		super.onPostExecute(result);
	}
}
