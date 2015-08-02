package se.Tr1List.DAO;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import se.Tr1List.UserActivity;
import se.Tr1List.Misc.converter;
import se.Tr1List.Model.ProductListModel;
import se.Tr1List.Model.UserModel;
import se.Tr1List.Singleton.Instance;
import android.content.Context;
import android.os.AsyncTask;

public class ShareProductList extends AsyncTask<Void, Integer, Boolean> {
	ProductListModel productListModel;
	UserActivity userContext;
	String Inetaddress = Instance.getInstance().getInetAddress();
	private DefaultHttpClient httpclient;
	private HttpPost httppost;
	private JSONObject data;
	private HttpResponse response;
	private HttpEntity entity;
	private InputStream instream;
	private String result;
	private JSONArray dataArray;

	public ShareProductList(Context context, ProductListModel productListModel) {
		userContext = (UserActivity) context;
		this.productListModel = productListModel;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(Inetaddress + "/JSON/ShareProductList");
			
			dataArray = new JSONArray();
			
			for (UserModel user : Instance.getInstance().getSharedUsers()) {
				data = new JSONObject();
				data.put("UserId", user.getId()); // CHANGE!
				data.put("Status", user.getStatus());
				data.put("ProductListId", Instance.getInstance()
						.getActiveProductList().getId());
						
			
			httppost.setEntity(new ByteArrayEntity(data.toString().getBytes(
					"UTF8")));
			httppost.addHeader("Content-Type",
					"application/json; charset=utf-8");
			//httppost.setHeader("json", data.toString());
			httppost.setHeader("json", data.toString());

			response = httpclient.execute(httppost);
			entity = response.getEntity();
			if (entity != null) {
				instream = entity.getContent();
				result = converter.convertStreamToString(instream);
				//productListModel.setId(Integer.parseInt(result.trim()));
				//return productListModel;
			}
			}
			return true;			
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
