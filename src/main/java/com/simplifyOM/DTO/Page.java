package com.simplifyOM.DTO;

import java.util.ArrayList;
import java.util.List;

public class Page {

	private String name = null;
	private String id = null;
	private List<Object> objects = new ArrayList<Object>();

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

	public List<Object> getObjects() {
		return objects;
	}

	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}

	public Object getObjectByName(String objectName) {
		Object object = null;
		for (Object obj : objects)
			if (obj.getName().equalsIgnoreCase(objectName)) {
				object = obj;
				break;
			}
		return object;
	}
}
