package com.simplifyOM.DTO;

import java.util.HashMap;

public class OMdetails {

	private HashMap<String, String> omDetail = new HashMap<String, String>();

	public void setOMCapabilities(String OMdetailName, String value) {
		omDetail.put(OMdetailName, value);
	}

	public HashMap<String, String> getOmDetail() {
		return omDetail;
	}

	public void setOmDetail(HashMap<String, String> omDetail) {
		this.omDetail = omDetail;
	}

}
