package com.simplifyOM.Interface;

import java.util.List;

import org.json.JSONException;

import com.simplifyOM.DTO.OMdetails;
import com.simplifyOM.DTO.Project;

public interface ObjectHandler {

	public Project setOMCapabilities(OMdetails omDetails) throws JSONException;

}
