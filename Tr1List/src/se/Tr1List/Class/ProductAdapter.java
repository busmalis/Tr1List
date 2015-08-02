package se.Tr1List.Class;

import java.util.ArrayList;

import se.Tr1List.ProductActivity;
import se.Tr1List.R;
import se.Tr1List.DAO.AddProduct;
import se.Tr1List.DAO.EditProduct;
import se.Tr1List.Model.ProductModel;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ProductAdapter extends ArrayAdapter<ProductModel> {

	Context context;
	int layoutResourceId;
	ArrayList<ProductModel> data = null;
	Button bItemDelete;
	itemsHolder holder;
	ProductActivity productContext = (ProductActivity) getContext();
	private ProductModel productModel;
	private View row;
	private LayoutInflater inflater;
	private AddProduct addTask;
	private EditProduct editTask;

	public ProductAdapter(Context context, int layoutResourceId,
			ArrayList<ProductModel> data) {
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
				holder.txtText = (TextView) row.findViewById(R.id.txtText);
				holder.checkboxSelect = (CheckBox) row.findViewById(R.id.Checkbox);
				row.setTag(holder);
			} else {
				holder = (itemsHolder) row.getTag();
			}

			productModel = data.get(position);
			holder.txtText.setText(productModel.getName());
			if(data.get(position).isSelected()){
				holder.checkboxSelect.setChecked(true);
			}
			else
			{
				holder.checkboxSelect.setChecked(false);
			}
						
			final OnClickListener checkboxSelectedListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try{
						selectItem(position);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			};
			holder.checkboxSelect.setOnClickListener(checkboxSelectedListener);
			
			row.refreshDrawableState();
			return row;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void selectItem(int position) {
		productContext.selectedItemModel = productContext.productModelList
				.get(position);
		productModel = productContext.selectedItemModel;
		productModel.setSelected(!productModel.isSelected());
		
		int count = 0;
		for (ProductModel productModel : data) {
			if(productModel.isSelected())
				count++;
		}
		
		if(count > 0)
			productContext.setActionBar(true, count);
		else
			productContext.setActionBar(false, 0);
	}

	static class itemsHolder {
		TextView txtText;
		CheckBox checkboxSelect;
	}

	public void addItem() {
		if(productContext.etProduct.getText().length() == 0){
			Toast.makeText(productContext, "Can't add empty item!", Toast.LENGTH_LONG).show();
		}
		else{
			try {
				productModel = new ProductModel.Builder()
						.setName(
								productContext.etProduct.getText()
										.toString()).setStatus("Active").build();
				addTask = new AddProduct(productContext, productModel);
				AsyncTask<Void, Integer, ProductModel> product = addTask.execute();
				productContext.productModelList.add(product.get());
				productContext.etProduct.setText("");
				notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void editItem() {
		if(productContext.etProduct.getText().equals(null)){
			Toast.makeText(productContext, "Can't edit empty item!", Toast.LENGTH_LONG).show();
		}
		else{
			try {
				productModel = productContext.getSelectedItem();
				productModel.setName(productContext.etProduct.getText().toString());
				editTask = new EditProduct(productContext, productModel);
				//AsyncTask<Void, Integer, ProductModel> productList = 
				editTask.execute();
				notifyDataSetChanged();
				productContext.closeSubMenu();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
	}

	public void setBackgroundOnItems() {
		int count;
		ProductModel model;

		count = getCount();
		for (int i = 1; i < count; i++) {
			model = (ProductModel) productContext.itemlistProduct
					.getItemAtPosition(i);
			if (model.isSelected()) {
				getChildAt(i).setBackgroundColor(Color.CYAN);
			} else {
				getChildAt(i).setBackgroundColor(Color.WHITE);
			}
		}
	}

	public void selectAll(boolean bool){
		for (ProductModel productModel : data) {
			productModel.setSelected(bool);
			notifyDataSetChanged();
		}
		
	}
	
	public ArrayList<ProductModel> getSelectedItems(){
		ArrayList<ProductModel> selectedItems = new ArrayList<ProductModel>();
		for (ProductModel productModel : data) {
			if(productModel.isSelected())
				selectedItems.add(productModel);
		}
		return selectedItems;		
	}
	
	private View getChildAt(int i) {
		return row;
	}

}