package com.simplifyOM.DTO;

import java.util.ArrayList;
import java.util.List;

public class Module {

	private String name = null;
	private String id = null;
	private List<Page> pages = new ArrayList<Page>();

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Page getPageByName(String pageName) {
		Page page = null;
		for (Page pg : pages)
			if (pg.getName().equalsIgnoreCase(pageName)) {
				page = pg;
				break;
			}
		return page;
	}
}
