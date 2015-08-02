package se.Tr1List.Class;

public class ProductListClass {
	private String product;
	private int id;
	
	public ProductListClass(Builder builder){
		this.setProduct(builder.product);
		this.setId(builder.id);
	}
	
	public static class Builder{
		private String product;
		private int id;
		
		public Builder setProduct(String product){
			this.product = product;
			return this;
		}
		
		public Builder setId(int id){
			this.id = id;
			return this;
		}
		
		public ProductListClass build(){
			return new ProductListClass(this);
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
	
}
