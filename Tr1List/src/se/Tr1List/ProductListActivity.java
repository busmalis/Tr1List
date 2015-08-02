package se.Tr1List;

import java.util.ArrayList;

import se.Tr1List.Class.ProductListAdapter;
import se.Tr1List.DAO.AddUser;
import se.Tr1List.DAO.DeleteProductList;
import se.Tr1List.DAO.GetProductList;
import se.Tr1List.Model.ProductListModel;
import se.Tr1List.Model.UserModel;
import se.Tr1List.Singleton.Instance;
import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

public class ProductListActivity extends Activity {
	/** Called when the activity is first created. */
	private ListView itemlistProductList;
	public ArrayList<ProductListModel> productListModelList;
	private ProductListAdapter productListAdapter;
	public Button bAddProductList;
	static int selectedid;
	public ProductListModel selectedItemModel;
	private ArrayList<ProductListModel> deleteItemsArrayList;
	public EditText etProduct;
	private GetProductList getTask;
	protected Instance instance;
	private Animation animAlpha2;
	private ActionMode mActionMode;
	private int count;
	private boolean actionModeReady;
	private Context context;
	private LinearLayout layout;
	// private MenuItem shareItem;
	private MenuItem newItem;
	private MenuItem closeItem;
	private Button bEditProductList;
	private ProductListModel selectedItem;
	private UserModel currentUser;
	private AddUser addUserTask;
	private ShareActionProvider mShareActionProvider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productlist);
		context = this.getApplicationContext();
		animAlpha2 = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
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
			etProduct = (EditText) findViewById(R.id.PLetProductList);
			etProduct.setHint("Add an item...");
			bAddProductList = (Button) findViewById(R.id.PLbAddProductList);
			bEditProductList = (Button) findViewById(R.id.PLbEditProductList);
			deleteItemsArrayList = new ArrayList<ProductListModel>();
			deleteItemsArrayList.clear();
			instance = Instance.getInstance();
			layout = (LinearLayout) findViewById(R.id.PLHeader);
			getActionBar().setBackgroundDrawable(
					new ColorDrawable(color.transparent));
			setActionModeReady(true);
			checkUser();
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
		// shareItem = menu.findItem(R.id.subMenuShare);
		newItem = menu.findItem(R.id.subMenuNew);
		closeItem = menu.findItem(R.id.subMenuClose);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.subMenuNew:
			openEditBox(null);
			return true;
		case R.id.subMenuClose:
			closeSubMenu();
			return true;

		case R.id.subMenuSettings:
			openSettings();
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

			productListModelList = new ArrayList<ProductListModel>();

			getTask = new GetProductList(this);
			getTask.execute();
			productListModelList = getTask.get();
			productListAdapter = new ProductListAdapter(this,
					R.layout.productlist_item_row, productListModelList);
			productListAdapter.setNotifyOnChange(true);
			productListAdapter.notifyDataSetChanged();
			itemlistProductList = (ListView) findViewById(R.id.PLproductlistview);

			// In case of header use for the list. Great dividers
			// View header =
			// (View)getLayoutInflater().inflate(tr1Store.se.R.layout.itemlist_header_row,
			// null);
			// itemlist.addHeaderView(header);
			itemlistProductList.setAdapter(productListAdapter);

			itemlistProductList
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						private ProductListModel ProductListModel;

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							ProductListModel = productListModelList.get(arg2);
							instance.setActiveProductList(ProductListModel);

							/* ANIMATION */
							arg1.startAnimation(animAlpha2);
							startActivity(new Intent(
									"android.intent.action.ITEMLISTACTIVITY"));
						}
					});

			itemlistProductList.requestFocus();

		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}

	public void addItem(View view) {
		productListAdapter.addItem();
	}

	public void editItem(View view) {
		productListAdapter.editItem();
	}

	public void setActionBar(boolean b, int count) {
		this.count = count;

		if (isActionModeReady()) {
			mActionMode = startActionMode(new ActionBarCallBack());
		} else if (count == 0) {
			mActionMode.finish();
		} else {
			mActionMode.setTitle(String.valueOf(count));
			if (count > 1) {
				mActionMode.getMenu().findItem(R.id.subMenuEdit)
						.setVisible(false);
				mActionMode.getMenu().findItem(R.id.subMenuShare)
						.setVisible(false);
			}

			else {
				mActionMode.getMenu().findItem(R.id.subMenuEdit)
						.setVisible(true);
				mActionMode.getMenu().findItem(R.id.subMenuShare)
						.setVisible(true);
			}
		}
	}

	public void deleteSelectedItems() {
		for (ProductListModel item : productListModelList) {
			if (item.isSelected()) {
				DeleteProductList task = new DeleteProductList(this, item);
				task.execute();
				deleteItemsArrayList.add(item);
			}
		}
		productListModelList.removeAll(deleteItemsArrayList);
		deleteItemsArrayList.clear();
		productListAdapter.notifyDataSetChanged();
	}

	public boolean isActionModeReady() {
		return actionModeReady;
	}

	public void setActionModeReady(boolean actionModeReady) {
		this.actionModeReady = actionModeReady;
	}

	class ActionBarCallBack implements ActionMode.Callback {
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.subMenuDelete:
				deleteSelectedItems();
				mode.finish();
				return true;

			case R.id.subMenuEdit:
				openEditBox(getSelectedItem());
				mode.finish();
				return true;

			case R.id.subMenuShare:
				startActivity(new Intent("android.intent.action.USERACTIVITY"));
				mode.finish();
				return true;
			}
			return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			mode.getMenuInflater().inflate(R.menu.submenu, menu);
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO Auto-generated method stub
			setActionModeReady(true);
			productListAdapter.selectAll(false);
			// mode.finish();
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub

			setActionModeReady(false);
			mode.setTitle(String.valueOf(count));
			return false;
		}
	}

	private void openEditBox(ProductListModel model) {
		Log.w("...", "openEditBox");
		if (layout.getVisibility() == View.GONE) {
			layout.setVisibility(View.VISIBLE);
			layout.animate().alpha(1.0f);
		}

		// shareItem.setVisible(false);
		newItem.setVisible(false);
		closeItem.setVisible(true);

		if (model != null) {
			etProduct.setText(model.getName());
			etProduct.setSelection(etProduct.getText().length());
			bAddProductList.setVisibility(View.GONE);
			bEditProductList.setVisibility(View.VISIBLE);
		} else {
			bAddProductList.setVisibility(View.VISIBLE);
			bEditProductList.setVisibility(View.GONE);
		}

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(etProduct, InputMethodManager.SHOW_IMPLICIT);

		etProduct.requestFocus();

		getActionBar().setBackgroundDrawable(
				new ColorDrawable(color.darker_gray));

	}

	private void closeEditBox() {
		Log.w("...", "closeEditBox");
		if (layout.getVisibility() == View.VISIBLE) {
			layout.setVisibility(View.GONE);
		}

		etProduct.setText("");
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etProduct.getWindowToken(), 0);

		// shareItem.setVisible(true);
		newItem.setVisible(true);
		closeItem.setVisible(false);

		getActionBar().setBackgroundDrawable(
				new ColorDrawable(color.transparent));
	}

	public void closeSubMenu() {
		closeEditBox();
	}

	public ProductListModel getSelectedItem() {
		if (productListAdapter.getSelectedItems().size() > 0)
			selectedItem = productListAdapter.getSelectedItems().get(0);
		return selectedItem;
	}

	private void checkUser() {
		try {
			if (instance.getUser() != null)
				return;

			instance.setUser(new UserModel.Builder().setPhoneId(getPhoneId())
					.build());
			addUserTask = new AddUser(this, instance.getUser());
			addUserTask.execute();
			if (addUserTask.get())
				Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Log.d("...", "checkUser: " + e.getMessage());
		}

	}
	
	private void openSettings() {
		startActivity(new Intent("android.intent.action.SETTINGSACTIVITY"));
	}

	private String getPhoneId() {
		String androidId = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);

		Log.d("...", "getPhoneId: " + androidId);
		return androidId;
	}

	private ArrayList<ProductListModel> getProductListsToShare() {
		ArrayList<ProductListModel> lists = new ArrayList<ProductListModel>();
		for (ProductListModel productListModel : productListAdapter
				.getSelectedItems()) {
			lists.add(productListModel);
		}
		return lists;
	}

}