package se.Tr1List.Model;

public class ProductModel {
	private	String name;
	private	int id;
	private boolean selected;
	private String status;
	private	int productListId;

	public ProductModel(Builder builder){
		this.setName(builder.name);
		this.setId(builder.id);
		this.setSelected(builder.selected);
		this.setStatus(builder.status);
		this.setProductListId(builder.productListId);
	}
	
	public static class Builder{
		private	String name;
		private	int id;
		private boolean selected;
		private String status;
		private	int productListId;
		
		public Builder setName(String name){
			this.name = name;
			return this;			
		}
		
		public Builder setId(int id){
			this.id = id;
			return this;			
		}
		
		public Builder setSelected(boolean selected){
			this.selected = selected;
			return this;			
		}
		
		public Builder setStatus(String status){
			this.status = status;
			return this;			
		}
		
		public Builder setProductListId(int productListId){
			this.productListId = productListId;
			return this;			
		}
		
		public ProductModel build(){
			return new ProductModel(this);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getStatus() {
		if(this.status == null)
			status = "Active";
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getProductListId() {
		return productListId;
	}

	public void setProductListId(int productListId) {
		this.productListId = productListId;
	}
	
}
