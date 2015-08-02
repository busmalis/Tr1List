package se.Tr1List.Model;


public class UserModel {
	private String name;
	private int id;
	private String phoneId;
	private Boolean selected;
	private String Status;

	public UserModel(Builder builder){
		this.setName(builder.name);
		this.setId(builder.id);
		this.setPhoneId(builder.phoneId);
		this.setSelected(builder.selected);
		this.setStatus(builder.status);
	}
	
	public static class Builder{
		private String name;
		private int id;
		private String phoneId;
		private Boolean selected;
		private String status;
		
		public Builder setName(String name){
			this.name = name;
			return this;
		}
		
		public Builder setId(int id){
			this.id = id;
			return this;
		}
		
		public Builder setPhoneId(String phoneId){
			this.phoneId = phoneId;
			return this;
		}
		
		public Builder setSelected(Boolean selected){
			this.selected = selected;
			return this;
		}
		
		public Builder setStatus(String status){
			this.status = status;
			return this;
		}
		
		public UserModel build(){
			return new UserModel(this);
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

	public String getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}

	public Boolean isSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		
		this.selected = selected;
	}

	public String getStatus() {
		if(this.selected){
			return "Included";
		}
		return "Excluded";
	}

	public void setStatus(String status) {
		Status = status;
	}
}
