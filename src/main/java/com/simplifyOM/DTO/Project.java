package com.simplifyOM.DTO;

import java.util.ArrayList;
import java.util.List;

public class Project {
	private String id = null;
	private List<Module> modules = new ArrayList<Module>();

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Module getModuleByName(String ModuleName) {
		Module module = null;
		for (Module mod : modules)
			if (mod.getName().equalsIgnoreCase(ModuleName)) {
				module = mod;
				break;
			}
		return module;
	}

}
