package com.example.liveplaylist;

public class Company {
	
	private String company_id;
	private String company_name;
	private String company_description;
	private String company_logo;
	private String company_adress;
	private String company_phone;
	private String category_name;
	
	public Company(String id, String name, String description, String logo, String adres, String phone, String category) {
		// TODO Auto-generated constructor stub
		this.setCompany_id(id);
		this.setCategory_name(name);
		this.setCompany_description(description);
		this.setCompany_logo(logo);
		this.setCompany_adress(adres);
		this.setCompany_phone(phone);
		this.setCategory_name(category);
	}

	public String getCompany_logo() {
		return company_logo;
	}

	public void setCompany_logo(String company_logo) {
		this.company_logo = company_logo;
	}

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getCompany_description() {
		return company_description;
	}

	public void setCompany_description(String company_description) {
		this.company_description = company_description;
	}

	public String getCompany_adress() {
		return company_adress;
	}

	public void setCompany_adress(String company_adress) {
		this.company_adress = company_adress;
	}

	public String getCompany_phone() {
		return company_phone;
	}

	public void setCompany_phone(String company_phone) {
		this.company_phone = company_phone;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
	
	
	
}
