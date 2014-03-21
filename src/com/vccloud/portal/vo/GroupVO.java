package com.vccloud.portal.vo;

import net.sf.json.JSONObject;

import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Group;

public class GroupVO {

	private Group group;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		if (group == null) {
			return json;
		}
		json.put("groupID", group.getGroupID().getEntityID());
		json.put("name", CommonUtil.null2Empty(group.getName()));
		return json;
	}

	public JSONObject toJson2() {
		JSONObject json = new JSONObject();
		if (group == null) {
			return json;
		}
		json.put("groupID", group.getGroupID().getEntityID());
		json.put("name", CommonUtil.null2Empty(group.getName()));
		json
				.put("roomMaxUsers", CommonUtil.null2Empty(group
						.getRoomMaxUsers()));
		json.put("userMaxBandWidthIn", CommonUtil.null2Empty(group
				.getUserMaxBandWidthIn()));
		json.put("userMaxBandWidthOut", CommonUtil.null2Empty(group
				.getUserMaxBandWidthOut()));
		return json;
	}

	public JSONObject toJson3() {
		JSONObject json = new JSONObject();
		if (group == null) {
			return json;
		}
		json.put("groupID", group.getGroupID().getEntityID());
		json.put("name", CommonUtil.null2Empty(group.getName()));
		json
				.put("roomMaxUsers", CommonUtil.null2Empty(group
						.getRoomMaxUsers()));
		json.put("userMaxBandWidthIn", CommonUtil.null2Empty(group
				.getUserMaxBandWidthIn()));
		json.put("userMaxBandWidthOut", CommonUtil.null2Empty(group
				.getUserMaxBandWidthOut()));
		json.put("allowRecording", group.getAllowRecording());
		json.put("description", CommonUtil.null2Empty(group.getDescription()));
		return json;
	}

}
