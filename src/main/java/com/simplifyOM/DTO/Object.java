package com.simplifyOM.DTO;

import java.util.ArrayList;
import java.util.List;

public class Object {

	private List<Attributes> attributes = new ArrayList<Attributes>();

	private String name = null;

	public List<Attributes> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attributes> attributes) {
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttrByName(String attrName) {
		String attrvalue = null;
		for (Attributes attr : attributes)
			if (attr.getName().equalsIgnoreCase(attrName)) {
				attrvalue = attr.getValue();
				break;
			}
		return attrvalue;
	}
}
