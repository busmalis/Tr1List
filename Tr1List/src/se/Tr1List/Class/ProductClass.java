package se.Tr1List.Class;

public class ProductClass {
	private String product;
	private int id;
	private boolean selected;

	public ProductClass(Builder builder){
		this.setProduct(builder.product);
		this.setId(builder.id);
		this.setSelected(builder.selected);
	}
	
	public static class Builder{
		private String product;
		private int id;
		private boolean selected;
		
		public Builder setProduct(String product){
			this.product = product;
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
		
		public ProductClass build(){
			return new ProductClass(this);
		}
		
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
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

}
