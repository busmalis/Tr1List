package se.Tr1List.Class;

import java.util.ArrayList;

import se.Tr1List.ProductListActivity;
import se.Tr1List.R;
import se.Tr1List.DAO.AddProductList;
import se.Tr1List.DAO.EditProductList;
import se.Tr1List.Model.ProductListModel;
import se.Tr1List.Singleton.Instance;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ProductListAdapter extends ArrayAdapter<ProductListModel> {

	Context context;
	int layoutResourceId;
	ArrayList<ProductListModel> data = null;
	//Button bItemlistDelete;
	itemsHolder holder;
	ProductListActivity productListContext = (ProductListActivity) getContext();
	private ProductListModel productListModel;
	private View row;
	private LayoutInflater inflater;
	private AddProductList addTask;
	private EditProductList editTask;

	public ProductListAdapter(Context context, int layoutResourceId,
			ArrayList<ProductListModel> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			row = convertView;
			holder = null;

			if (row == null) {
				inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new itemsHolder();
				holder.checkboxSelect = (CheckBox) row.findViewById(R.id.PLCheckbox);
				holder.txtText = (TextView) row.findViewById(R.id.PLtxtText);		
				//holder.txtSmallCountText = (TextView) row.findViewById(R.id.PLtxtSmallCount);
				row.setTag(holder);
			} else {
				holder = (itemsHolder) row.getTag();
			}

			productListModel = data.get(position);
			holder.txtText.setText(productListModel.getName());
			holder.checkboxSelect = (CheckBox) row.findViewById(R.id.PLCheckbox);
			if(data.get(position).isSelected()){
				holder.checkboxSelect.setChecked(true);
			}
			else
			{
				holder.checkboxSelect.setChecked(false);
			}
			
			final OnClickListener checkboxClick = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						selectItem(position);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			holder.checkboxSelect.setOnClickListener(checkboxClick);
			
			row.refreshDrawableState();
			return row;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void selectItem(int position) {
		productListContext.selectedItemModel = productListContext.productListModelList
				.get(position);
		productListModel = productListContext.selectedItemModel;
		productListModel.setSelected(!productListModel.isSelected());
		Instance.getInstance().setActiveProductList(productListModel);
		
		int count = 0;
		for (ProductListModel productListModel : data) {
			if(productListModel.isSelected())
				count++;
		}
		
		if(count > 0)
			productListContext.setActionBar(true, count);
		else
			productListContext.setActionBar(false, 0);
	}
	
	public void addItem() {
		if(productListContext.etProduct.getText().length() == 0){
			Toast.makeText(productListContext, "Can't add empty item!", Toast.LENGTH_LONG).show();
		}
		else{
			try {
				productListModel = new ProductListModel.Builder().setName(
						productListContext.etProduct.getText().toString()).build();
				addTask = new AddProductList(productListContext,
						productListModel);
				AsyncTask<Void, Integer, ProductListModel> product = addTask.execute();			
				productListContext.productListModelList.add(product.get());
				//productListContext.etProduct.setText("");
				productListContext.selectedItemModel.setName(productListModel.getName());
				notifyDataSetChanged();
				productListContext.closeSubMenu();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		productListContext.closeSubMenu();
	}
	
	public void editItem() {
		if(productListContext.etProduct.getText().equals(null)){
			Toast.makeText(productListContext, "Can't edit empty item!", Toast.LENGTH_LONG).show();
		}
		else{
			try {
				productListModel = productListContext.getSelectedItem();
				productListModel.setName(productListContext.etProduct.getText().toString());
				editTask = new EditProductList(productListContext, productListModel);
				//AsyncTask<Void, Integer, ProductListModel> productList = 
				editTask.execute();
				notifyDataSetChanged();
				productListContext.closeSubMenu();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
	}
	
	public void selectAll(boolean bool){
		for (ProductListModel productListModel : data) {
			productListModel.setSelected(bool);
			notifyDataSetChanged();
		}
		
	}
	
	public ArrayList<ProductListModel> getSelectedItems(){
		ArrayList<ProductListModel> selectedItems = new ArrayList<ProductListModel>();
		for (ProductListModel productListModel : data) {
			if(productListModel.isSelected())
				selectedItems.add(productListModel);
		}
		return selectedItems;		
	}
			
	static class itemsHolder {
		CheckBox checkboxSelect;
		TextView txtText;
		//TextView txtSmallCountText;
	}	
}