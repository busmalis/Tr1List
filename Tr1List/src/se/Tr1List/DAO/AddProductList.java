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
import se.Tr1List.Model.ProductListModel;
import se.Tr1List.Singleton.Instance;
import android.content.Context;
import android.os.AsyncTask;

public class AddProductList extends	AsyncTask<Void, Integer, ProductListModel> {
	ProductListModel productListModel;
	ProductListActivity productListContext;
	String Inetaddress = Instance.getInstance().getInetAddress();
	private DefaultHttpClient httpclient;
	private HttpPost httppost;
	private JSONObject data;
	private HttpResponse response;
	private HttpEntity entity;
	private InputStream instream;
	private String result;

	public AddProductList(Context context, ProductListModel itemmodel) {
		productListContext = (ProductListActivity) context;
		productListModel = itemmodel;
	}

	@Override
	protected ProductListModel doInBackground(Void... params) {

		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(Inetaddress
					+ "/JSON/CreateProductList");
			data = new JSONObject();
			
			data.put("Name", productListModel.getName());
			data.put("UserId", Instance.getInstance().getUser().getId());
			
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
				/*array = new JSONArray(result);
				for (int i = 0; i < array.length(); i++) {
					
					row = new getJSONObject();
					productListModel = new ProductListModel.Builder()
					.setName(row.getString("Name"))
					.setId(row.getInt("Id"))
					.build();
				}*/
				productListModel.setId(Integer.parseInt(result.trim()));
				return productListModel;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productListModel;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(ProductListModel result) {
		super.onPostExecute(result);
	}
}
