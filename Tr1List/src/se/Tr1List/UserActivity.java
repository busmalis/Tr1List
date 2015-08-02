package se.Tr1List;

import java.util.ArrayList;

import se.Tr1List.Class.UserAdapter;
import se.Tr1List.DAO.AddUser;
import se.Tr1List.DAO.GetUsers;
import se.Tr1List.DAO.ShareProductList;
import se.Tr1List.Model.ProductListModel;
import se.Tr1List.Model.UserModel;
import se.Tr1List.Singleton.Instance;
import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

public class UserActivity extends Activity {
	/** Called when the activity is first created. */
	private ListView usersList;
	public ArrayList<UserModel> userModelList;
	private UserAdapter userAdapter;
	public Button bAddProductList;
	static int selectedid;
	public UserModel selectedItemModel;
	private ArrayList<UserModel> deleteItemsArrayList;
	public EditText etProduct;
	private GetUsers getTask;
	protected Instance instance;
	private Animation animAlpha2;
	private ActionMode mActionMode;
	private int count;
	private boolean actionModeReady;
	private Context context;
	private LinearLayout layout;
	//private MenuItem shareItem;
	private MenuItem newItem;
	private MenuItem closeItem;
	private Button bEditProductList;
	private UserModel selectedUser;
	private UserModel currentUser;
	private AddUser addUserTask;
	private ShareActionProvider mShareActionProvider;
	ShareProductList shareTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user);
		context = this.getApplicationContext();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		init();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void init() {
		try {
			initList();			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.submenudefault, menu);
		newItem = menu.findItem(R.id.subMenuNew);
	//	closeItem = menu.findItem(R.id.subMenuClose);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle presses on the action bar items
		switch (item.getItemId()) {		
		case R.id.subMenuNew:
			try{
				ProductListModel selectedProductList = Instance.getInstance().getActiveProductList();
				shareTask = new ShareProductList(this, selectedProductList);
				shareTask.execute();
				if(!shareTask.get()){
					Toast.makeText(this, "Error during sharing", Toast.LENGTH_LONG).show();
				}	
				else{
					Toast.makeText(this, "Success during sharing", Toast.LENGTH_LONG).show();
				}
			}
			catch(Exception e){
				Log.d("...", "subMenuNew: " + e.getMessage());
			}
			finish();
			return true;
		/*case R.id.subMenuClose:
			closeSubMenu();
			return true;
	*/	case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	public void synchronize() {
		try {
			Toast.makeText(this, "Synchronizing...", Toast.LENGTH_SHORT).show();
			initList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initList() {
		try {

			userModelList = new ArrayList<UserModel>();

			getTask = new GetUsers(this);
			getTask.execute();
			userModelList = getTask.get();
			userAdapter = new UserAdapter(this,
					R.layout.user_item_row, userModelList);
			userAdapter.setNotifyOnChange(true);
			userAdapter.notifyDataSetChanged();
			usersList = (ListView) findViewById(R.id.Uproductlistview);

			// In case of header use for the list. Great dividers
			// View header =
			// (View)getLayoutInflater().inflate(tr1Store.se.R.layout.itemlist_header_row,
			// null);
			// itemlist.addHeaderView(header);
			usersList.setAdapter(userAdapter);

			//itemlistProductList
				//	.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					//	private UserModel userModel;

			//		@Override
			//		public void onItemClick(AdapterView<?> arg0, View arg1,
			//				int arg2, long arg3) {
			//				userModel = userModelList.get(arg2);
							//instance.setActiveProductList(userModel);

							/* ANIMATION */
			//				arg1.startAnimation(animAlpha2);
							//				startActivity(new Intent(
			//						"android.intent.action.ITEMLISTACTIVITY"));
			//			}
			//		});

			usersList.requestFocus();

		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}
	
}