package se.Tr1List.Class;

public class UserClass {
	private String name;
	private int id;
	private String phoneId;
	private Boolean selected;

	public UserClass(Builder builder){
		this.setName(builder.name);
		this.setId(builder.id);
		this.setPhoneId(builder.phoneId);
		this.setSelected(builder.selected);
	}
	
	public static class Builder{
		private String name;
		private int id;
		private String phoneId;
		private Boolean selected;
		
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
		
		public UserClass build(){
			return new UserClass(this);
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

}
