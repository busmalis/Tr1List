package se.Tr1List.Class;

import java.util.ArrayList;

import se.Tr1List.ProductListActivity;
import se.Tr1List.R;
import se.Tr1List.UserActivity;
import se.Tr1List.DAO.AddProductList;
import se.Tr1List.DAO.EditProductList;
import se.Tr1List.Model.ProductListModel;
import se.Tr1List.Model.UserModel;
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

public class UserAdapter extends ArrayAdapter<UserModel> {

	Context context;
	int layoutResourceId;
	ArrayList<UserModel> data = null;
	itemsHolder holder;
	UserActivity userContext = (UserActivity) getContext();
	private UserModel userModel;
	private View row;
	private LayoutInflater inflater;
	private AddProductList addTask;
	private EditProductList editTask;

	public UserAdapter(Context context, int layoutResourceId,
			ArrayList<UserModel> data) {
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
				holder.checkboxSelect = (CheckBox) row.findViewById(R.id.UCheckbox);
				holder.txtText = (TextView) row.findViewById(R.id.UtxtText);		
				//holder.txtSmallCountText = (TextView) row.findViewById(R.id.PLtxtSmallCount);
				row.setTag(holder);
			} else {
				holder = (itemsHolder) row.getTag();
			}

			userModel = data.get(position);
			holder.txtText.setText(userModel.getId() + ": "+ userModel.getName());
			//holder.checkboxSelect = (CheckBox) row.findViewById(R.id.PLCheckbox);
			if(data.get(position).isSelected() || data.get(position).getStatus().equals("Included")){
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
		userContext.selectedItemModel = userContext.userModelList
				.get(position);
		userContext.selectedItemModel = userContext.selectedItemModel;
		userContext.selectedItemModel.setSelected(!userContext.selectedItemModel.isSelected());		
		Instance.getInstance().setSharedUsers(getSelectedItems());
			
	}
			
	public ArrayList<UserModel> getSelectedItems(){
		ArrayList<UserModel> selectedItems = new ArrayList<UserModel>();
		for (UserModel userModel : data) {
		//	if(userModel.isSelected())
				selectedItems.add(userModel);
		}
		return selectedItems;		
	}
			
	static class itemsHolder {
		CheckBox checkboxSelect;
		TextView txtText;
	}	
}