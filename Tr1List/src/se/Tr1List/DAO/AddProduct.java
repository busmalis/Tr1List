package se.Tr1List.DAO;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import se.Tr1List.ProductActivity;
import se.Tr1List.Misc.converter;
import se.Tr1List.Model.ProductModel;
import se.Tr1List.Singleton.Instance;
import android.content.Context;
import android.os.AsyncTask;

public class AddProduct extends AsyncTask<Void, Integer, ProductModel> {
	ProductModel productModel;
	ProductActivity itemListActivityContext;
	String Inetaddress = Instance.getInstance().getInetAddress();
	private DefaultHttpClient httpclient;
	private HttpPost httppost;
	private JSONObject data;
	private HttpResponse response;
	private HttpEntity entity;
	private InputStream instream;
	private String result;

	public AddProduct(Context context, ProductModel itemmodel) {
		itemListActivityContext = (ProductActivity) context;
		productModel = itemmodel;
	}

	@Override
	protected ProductModel doInBackground(Void... params) {

		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(Inetaddress + "/JSON/AddProduct");
			data = new JSONObject();

			data.put("Name", productModel.getName());
			data.put("Status", productModel.getStatus());
			data.put("ProductListId", Instance.getInstance()
					.getActiveProductList().getId());
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
				productModel.setId(Integer.parseInt(result.trim()));
				return productModel;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productModel;
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
