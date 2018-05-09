package com.example.demo.domain;

public class Base {

	private int rows;
	
	private int page;
	
	private int start;

	

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getStart() {
		int begin=(this.page-1)*this.rows;
		return begin;
	}

	public void setStart(int start) {
		this.start = start;
	}
	
	
}
