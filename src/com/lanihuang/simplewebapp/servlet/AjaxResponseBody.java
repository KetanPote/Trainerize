package com.lanihuang.simplewebapp.servlet;

public class AjaxResponseBody
{
	private String code,name,price;

	public AjaxResponseBody(String code, String name, String price) {
		super();
		this.code = code;
		this.name = name;
		this.price = price;
	}

	public AjaxResponseBody() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
}
