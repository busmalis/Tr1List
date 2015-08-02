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

import se.Tr1List.ProductActivity;
import se.Tr1List.Misc.converter;
import se.Tr1List.Model.ProductListModel;
import se.Tr1List.Model.ProductModel;
import se.Tr1List.Singleton.Instance;
import android.os.AsyncTask;

public class GetProducts extends AsyncTask<Void, Integer, ArrayList<ProductModel>> {
	ArrayList<ProductModel> arrayList = new ArrayList<ProductModel>();
	ProductActivity itemListActivityContext;
	String Inetaddress = Instance.getInstance().getInetAddress();
	private ProductListModel activeProductListModel;
	private JSONObject data;
	private HttpEntity entity;
	private InputStream instream;
	private String result;
	private JSONArray array;
	private JSONObject row;
	private ProductModel itemModel;
	private DefaultHttpClient httpclient;
	private HttpPost httppost;
	private HttpResponse response;

	public GetProducts(ProductActivity context,
			ProductListModel activeProductList) {
		itemListActivityContext = context;
		activeProductListModel = activeProductList;
	}

	@Override
	protected ArrayList<ProductModel> doInBackground(Void... params) {
		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(Inetaddress + "/JSON/GetProducts");
			data = new JSONObject();

			data.put("Name", activeProductListModel.getName());
			data.put("Id", activeProductListModel.getId());

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
					itemModel = new ProductModel.Builder()
					.setName(row.getString("Name"))
					.setId(row.getInt("Id"))
					.setSelected(false)
					.setStatus("Active")
					.build();
					
					arrayList.add(itemModel);
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
		//itemListActivityContext.pb.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPostExecute(ArrayList<ProductModel> result) {
		super.onPostExecute(result);
		//itemListActivityContext.pb.setVisibility(View.INVISIBLE);
	}
}
