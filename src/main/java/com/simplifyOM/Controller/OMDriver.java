package com.simplifyOM.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplifyOM.DTO.Attributes;
import com.simplifyOM.DTO.Module;
import com.simplifyOM.DTO.OMdetails;
import com.simplifyOM.DTO.Object;
import com.simplifyOM.DTO.Page;
import com.simplifyOM.DTO.Project;
import com.simplifyOM.HttpUtility.ApiUtil;
import com.simplifyOM.Interface.ObjectHandler;

public class OMDriver implements ObjectHandler {

	private Logger logger = LoggerFactory.getLogger(OMDriver.class);
	private OMdetails omDetails;
	private String url = "http://172.104.33.173:4000/api/v1/project";
	private HashMap<String, String> header = new HashMap<String, String>();

	private Project getProject() throws JSONException {
		try {
			if (this.omDetails.getOmDetail().containsKey("projectId")) {
				Project project = new Project();
				project.setId(this.omDetails.getOmDetail().get("projectId"));
				JSONObject res = ApiUtil.get(
						url + File.separator + this.omDetails.getOmDetail().get("projectId") + "/modules", null,
						header);
				for (int i = 0; i < res.getJSONArray("data").length(); i++) {
					Module mod = new Module();
					mod.setId(res.getJSONArray("data").getJSONObject(i).getString("id"));
					mod.setName(res.getJSONArray("data").getJSONObject(i).getString("name"));
					mod.setPages(getpages(mod.getName(), project.getId()));
					project.getModules().add(mod);
				}

				return project;
			} else {
				this.logger.info("Please provide ProjectId");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<Page> getpages(String modulename, String projectId) {
		try {
			List<Page> pages = new ArrayList<Page>();
			Map<String, String> qureparam = new HashMap<String, String>();
			qureparam.put("module", modulename);
			JSONObject res = ApiUtil.get(url + File.separator + this.omDetails.getOmDetail().get("projectId") + "/page",
					qureparam, header);
			for (int i = 0; i < res.getJSONArray("data").length(); i++) {
				Page mod = new Page();
				mod.setId(res.getJSONArray("data").getJSONObject(i).getString("id"));
				mod.setName(res.getJSONArray("data").getJSONObject(i).getString("name"));
				mod.setObjects(getObjects(modulename, mod.getName(), projectId));
				pages.add(mod);
			}
			return pages;
		} catch (Exception e) {
			return null;
		}
	}

	private List<Object> getObjects(String modulename, String pageName, String projectId) {
		try {
			List<Object> object = new ArrayList<Object>();
			Map<String, String> qureparam = new HashMap<String, String>();
			qureparam.put("module", modulename);
			qureparam.put("page", pageName);
			JSONObject res = ApiUtil.get(
					url + File.separator + this.omDetails.getOmDetail().get("projectId") + "/objects", qureparam,
					header);
			for (int i = 0; i < res.getJSONArray("data").length(); i++) {
				Object mod = new Object();
				mod.setName(res.getJSONArray("data").getJSONObject(i).getString("name"));
				for (int j = 0; j < res.getJSONArray("data").getJSONObject(i).getJSONArray("attributes")
						.length(); j++) {
					Attributes attr = new Attributes();
					attr.setName(res.getJSONArray("data").getJSONObject(i).getJSONArray("attributes").getJSONObject(j)
							.getString("name"));
					attr.setValue(res.getJSONArray("data").getJSONObject(i).getJSONArray("attributes").getJSONObject(j)
							.getString("value"));
					mod.getAttributes().add(attr);
				}
				object.add(mod);
			}
			return object;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Project setOMCapabilities(OMdetails omDetails) throws JSONException {
		this.omDetails = omDetails;
		Project project = new Project();
		project.setId(this.omDetails.getOmDetail().get("projectId"));
		if (this.omDetails.getOmDetail().containsKey("token")) {
			header.put("content-type", "application/json");
			header.put("private_key", this.omDetails.getOmDetail().get("token"));
			if (!this.omDetails.getOmDetail().containsKey("moduleName")
					&& !this.omDetails.getOmDetail().containsKey("pageName"))
				project = this.getProject();
			else if (this.omDetails.getOmDetail().containsKey("moduleName")
					&& !this.omDetails.getOmDetail().containsKey("pageName")) {
				Module mod = new Module();
				mod.setName(this.omDetails.getOmDetail().get("moduleName"));
				mod.setPages(getpages(this.omDetails.getOmDetail().get("moduleName"),
						this.omDetails.getOmDetail().get("projectId")));
				project.getModules().add(mod);

			} else if (this.omDetails.getOmDetail().containsKey("moduleName")
					&& this.omDetails.getOmDetail().containsKey("pageName")) {
				Module mod = new Module();
				Page page = new Page();
				mod.setName(this.omDetails.getOmDetail().get("moduleName"));
				page.setName(this.omDetails.getOmDetail().get("pageName"));
				page.setObjects(getObjects(this.omDetails.getOmDetail().get("moduleName"),
						this.omDetails.getOmDetail().get("pageName"), this.omDetails.getOmDetail().get("projectId")));
				mod.getPages().add(page);
				project.getModules().add(mod);
			}
			return project;
		} else {
			this.logger.info("Please provide token");
			return null;
		}
	}

	public static void main(String[] args) {
		OMdetails om = new OMdetails();
		OMDriver driver = new OMDriver();
		om.setOMCapabilities("token", "390b280ae6628b850a74ceccc4275efa980eb7b7d151a59b");
		om.setOMCapabilities("projectId", "136");
		try {
			System.out.println(new ObjectMapper().writeValueAsString(driver.setOMCapabilities(om).getModuleByName("adf").getPageByName("Maven Repository: org.json » json » 20090211 - artifact-org.json")));
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
