package se.Tr1List;

import java.util.ArrayList;

import se.Tr1List.Class.ProductAdapter;
import se.Tr1List.DAO.AddUser;
import se.Tr1List.DAO.DeleteProduct;
import se.Tr1List.DAO.GetProducts;
import se.Tr1List.Model.ProductModel;
import se.Tr1List.Model.UserModel;
import se.Tr1List.Singleton.Instance;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ProductActivity extends Activity {
	/** Called when the activity is first created. */
	public ListView itemlistProduct;
	public ArrayList<ProductModel> productModelList;
	private Context productContext;
	public EditText productText;
	private ProductAdapter productAdapter;
	public EditText etProduct;
	public Button bAddProduct;
	static int selectedid;
	public ProductModel selectedItemModel;
	private ArrayList<ProductModel> deleteItemsArrayList;
	// private Instance instance;
	// private MenuItem shareItem;
	private GetProducts getTask;
	private Instance instance;
	private boolean actionModeReady;
	private ActionMode mActionMode;
	private int count;
	private LinearLayout layout;
	private MenuItem newItem;
	private MenuItem closeItem;
	private Button bEditProductList;
	private ProductModel selectedItem;
	private UserModel currentUser;
	private AddUser addUserTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);
		productContext = this.getApplicationContext();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.submenudefault, menu);
		newItem = menu.findItem(R.id.subMenuNew);
		closeItem = menu.findItem(R.id.subMenuClose);
		// shareItem = menu.findItem(R.id.subMenuShare);
		// shareItem.setVisible(false);
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

		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			NavUtils.navigateUpFromSameTask(this);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void init() {
		try {
			etProduct = (EditText) findViewById(R.id.etProduct);
			etProduct.setHint("Add an item...");
			bAddProduct = (Button) findViewById(R.id.bAddProduct);
			bEditProductList = (Button) findViewById(R.id.bEditProductList);
			// pb = (ProgressBar) findViewById(R.id.pbItemlist);
			deleteItemsArrayList = new ArrayList<ProductModel>();
			deleteItemsArrayList.clear();
			instance = Instance.getInstance();
			layout = (LinearLayout) findViewById(R.id.Header);
			this.setTitle(instance.getActiveProductList().getName());
			setActionModeReady(true);
			initList();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initList() {
		try {

			productModelList = new ArrayList<ProductModel>();
			getTask = new GetProducts(this, instance.getActiveProductList());
			getTask.execute();
			productModelList = getTask.get();
			/*
			 * productModelList.add(new
			 * ProductModel.Builder().setName("Test1").setId(1).build());
			 * productModelList.add(new
			 * ProductModel.Builder().setName("Test2").setId(2).build());
			 * productModelList.add(new
			 * ProductModel.Builder().setName("Test3").setId(3).build());
			 * productModelList.add(new
			 * ProductModel.Builder().setName("Test4").setId(4).build());
			 * productModelList.add(new
			 * ProductModel.Builder().setName("Test5").setId(5).build());
			 * productModelList.add(new
			 * ProductModel.Builder().setName("Test6").setId(6).build());
			 */
			productAdapter = new ProductAdapter(this,
					R.layout.product_item_row, productModelList);
			productAdapter.setNotifyOnChange(true);
			productAdapter.notifyDataSetChanged();
			itemlistProduct = (ListView) findViewById(R.id.itemview);

			// In case of header use for the list. Great dividers
			// View header =
			// (View)getLayoutInflater().inflate(tr1Store.se.R.layout.itemlist_header_row,
			// null);
			// itemlist.addHeaderView(header);

			itemlistProduct.setAdapter(productAdapter);

			itemlistProduct.requestFocus();

		} catch (Exception e) {
			e.fillInStackTrace();
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

	public void addItem(View view) {
		productAdapter.addItem();
	}

	public void editItem(View view) {
		productAdapter.editItem();
	}

	public void deleteSelectedItems() {
		for (ProductModel item : productModelList) {
			if (item.isSelected()) {
				DeleteProduct task = new DeleteProduct(this, item);
				task.execute();
				deleteItemsArrayList.add(item);
			}
		}
		productModelList.removeAll(deleteItemsArrayList);
		deleteItemsArrayList.clear();
		productAdapter.notifyDataSetChanged();
	}

	public boolean isActionModeReady() {
		return actionModeReady;
	}

	public void setActionModeReady(boolean actionModeReady) {
		this.actionModeReady = actionModeReady;
	}

	public void setActionBar(boolean b, int count) {
		this.count = count;

		if (isActionModeReady()) {
			mActionMode = startActionMode(new ActionBarCallBack());
			mActionMode.getMenu().findItem(R.id.subMenuShare)
			.setVisible(false);
		} else if (count == 0) {
			mActionMode.finish();
		} else {
			mActionMode.getMenu().findItem(R.id.subMenuShare).setVisible(false);
			mActionMode.setTitle(String.valueOf(count));
			if (count > 1) {
				mActionMode.getMenu().findItem(R.id.subMenuEdit)
						.setVisible(false);
			} else {
				mActionMode.getMenu().findItem(R.id.subMenuEdit)
						.setVisible(true);
			}
		}
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

				/*
				 * case R.id.subMenuShare:
				 * Toast.makeText(getApplicationContext(), "SHARE",
				 * Toast.LENGTH_LONG).show(); return true; }
				 */
			}
			return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.getMenuInflater().inflate(R.menu.submenu, menu);
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			productAdapter.selectAll(false);
			setActionModeReady(true);
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

			setActionModeReady(false);
			mode.setTitle(String.valueOf(count));
			return false;
		}

	}

	private void openEditBox(ProductModel model) {
		if (layout.getVisibility() == View.GONE)
			layout.setVisibility(View.VISIBLE);

		newItem.setVisible(false);
		closeItem.setVisible(true);

		if (model != null) {
			etProduct.setText(model.getName());
			etProduct.setSelection(etProduct.getText().length());
			bAddProduct.setVisibility(View.GONE);
			bEditProductList.setVisibility(View.VISIBLE);
		} else {
			bAddProduct.setVisibility(View.VISIBLE);
			bEditProductList.setVisibility(View.GONE);
		}

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(etProduct, InputMethodManager.SHOW_IMPLICIT);

		etProduct.requestFocus();
	}

	private void closeEditBox() {
		if (layout.getVisibility() == View.VISIBLE)
			layout.setVisibility(View.GONE);

		etProduct.setText("");
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etProduct.getWindowToken(), 0);

		newItem.setVisible(true);
		closeItem.setVisible(false);
	}

	public void closeSubMenu() {
		closeEditBox();
	}

	public ProductModel getSelectedItem() {
		if (productAdapter.getSelectedItems().size() > 0)
			selectedItem = productAdapter.getSelectedItems().get(0);
		return selectedItem;
	}
}