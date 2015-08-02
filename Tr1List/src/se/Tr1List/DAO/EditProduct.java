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

public class EditProduct extends AsyncTask<Void, Integer, ProductModel> {
	ProductModel productModel;
	ProductActivity productContext;
	String Inetaddress = Instance.getInstance().getInetAddress();
	private DefaultHttpClient httpclient;
	private HttpPost httppost;
	private JSONObject data;
	private HttpResponse response;
	private InputStream instream;

	public EditProduct(Context context, ProductModel itemmodel) {
		productContext = (ProductActivity) context;
		productModel = itemmodel;
	}

	@Override
	protected ProductModel doInBackground(Void... params) {

		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(Inetaddress + "/JSON/EditProduct");

			data = new JSONObject();
			data.put("Name", productModel.getName());
			data.put("Id", productModel.getId());
			data.put("Status", productModel.getStatus());

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
				
				productModel.setId(Integer.parseInt(result.trim()));
				return productModel;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productModel;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(ProductModel result) {
		super.onPostExecute(result);
	}
}
