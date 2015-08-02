package se.Tr1List;

import java.util.Timer;
import java.util.TimerTask;

import se.Tr1List.Singleton.Instance;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

public class Splash extends Activity {
	protected boolean lBoolean;
	private TextView splashText;
	private TextView splashTextVersion;
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	private Instance instance;
	private String version;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		init();
	}

	private void init() {
		Instance.initInstance();
		instance = Instance.getInstance();
		splashText = (TextView) findViewById(R.id.splashText);
		splashText.setText("Connecting to server...");
		splashTextVersion = (TextView) findViewById(R.id.splashTextVersion);
		splashTextVersion.setText(getVersion());

		Timer t = new Timer();
		boolean checkConnection = checkConnection();
		if (checkConnection) {
			t.schedule(new splash(), 1500);
		} else {
			splashText.setText("Server not found...");
		}

	}

	private String getVersion() {
		try{
			
		PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		version = pInfo.versionName;
		}
		catch(Exception e){
			splashTextVersion.setText("Cannot present version...");
		}
		return "V." + version;
	}

	class splash extends TimerTask {

		@Override
		public void run() {
			Intent i = new Intent(Splash.this, ProductListActivity.class);
			finish();
			startActivity(i);
		}
	}

	private boolean checkConnection() {
		boolean flag = false;
		splashText.setText("Checking connection...");
		try {
			connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			splashText.setText("Getting network information...");
			info = connectivityManager.getActiveNetworkInfo();
			splashText.setText("Checking network connection");

			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				instance.setInetAddress("http://192.168.1.3:8080/tr1Store");
				splashText.setText("Server over WiFi found!");
				flag = true;
			}
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				instance.setInetAddress("http://busalli.ddns.net:8080/tr1Store");
				splashText.setText("Server found!");
				flag = true;
			}
		} catch (Exception exception) {
			System.out.println("Exception at network connection....."
					+ exception);
		}
		return flag;
	}
}