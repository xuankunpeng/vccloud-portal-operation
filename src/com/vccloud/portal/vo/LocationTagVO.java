package com.vccloud.portal.vo;

import net.sf.json.JSONObject;

import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.LocationTag;

public class LocationTagVO {

	private LocationTag locationTag;

	public LocationTag getLocationTag() {
		return locationTag;
	}

	public void setLocationTag(LocationTag locationTag) {
		this.locationTag = locationTag;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		if (locationTag == null) {
			return json;
		}
		json.put("locationTagID", locationTag.getLocationTagID());
		json.put("locationTagName", CommonUtil.null2Empty(locationTag
				.getLocationTagName()));
		return json;
	}

}
