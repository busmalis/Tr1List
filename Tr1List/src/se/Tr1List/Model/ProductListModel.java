package se.Tr1List.Model;

public class ProductListModel {
	private String name;
	private int id;
	private boolean selected;
	
	public ProductListModel(Builder builder){
		this.setName(builder.name);
		this.setId(builder.id);
		this.setSelected(builder.selected);
	}
	
	public static class Builder{
		private String name;
		private int id;
		private boolean selected;
		
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
		
		public ProductListModel build(){
			return new ProductListModel(this);
		}
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
