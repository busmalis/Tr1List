package se.Tr1List.Singleton;

import java.util.ArrayList;

import se.Tr1List.Model.ProductListModel;
import se.Tr1List.Model.UserModel;
import android.content.Context;
import android.util.Log;

public class Instance {
	 private static Instance instance;	 	
	 private ProductListModel ActiveProductList;
	 private UserModel user;
	 private ArrayList<UserModel> sharedUsers;
	 private String InetAddress;

	// Private constructor prevents instantiation from other classes
    private Instance() {
    }

    public static void initInstance()
    {
    	if (instance == null)
    	{
    		instance = new Instance();
    	}
    }

    public static Instance getInstance() {
        return instance;
    }

    public static String getApplicationName(Context context) {
	    int stringId = context.getApplicationInfo().labelRes;
	    return context.getString(stringId);
	}

	public void setUser(UserModel user) {
		try{
			this.user = user;
		}
		catch(Exception e){
			Log.w("...", "setUser: " + e.getMessage());
		}
	}

	public UserModel getUser() {
		try{
			return this.user;
		}
		catch(Exception e){
			Log.w("...", "setUser: " + e.getMessage());
			return null;
		}		
	}
	
	public ProductListModel getActiveProductList() {
		return ActiveProductList;
	}

	public void setActiveProductList(ProductListModel activeProductList) {
		ActiveProductList = activeProductList;
	}

	public ArrayList<UserModel> getSharedUsers() {
		return sharedUsers;
	}

	public void setSharedUsers(ArrayList<UserModel> sharedUsers) {
		this.sharedUsers = sharedUsers;
	}

	public String getInetAddress() {
		return InetAddress;
	}

	public void setInetAddress(String inetAddress) {
		InetAddress = inetAddress;
	}
		
}
