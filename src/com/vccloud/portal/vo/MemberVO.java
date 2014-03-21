package com.vccloud.portal.vo;

import net.sf.json.JSONObject;

import com.vccloud.portal.db.model.TExtVidyoMember;
import com.vccloud.portal.util.CommonUtil;

public class MemberVO {

	private TExtVidyoMember vidyoMember;

	public TExtVidyoMember getVidyoMember() {
		return vidyoMember;
	}

	public void setVidyoMember(TExtVidyoMember vidyoMember) {
		this.vidyoMember = vidyoMember;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		if (vidyoMember == null) {
			return json;
		}
		json.put("id", vidyoMember.getId());
		json.put("name", CommonUtil.null2Empty(vidyoMember.getName()));
		json.put("displayname", CommonUtil.null2Empty(vidyoMember
				.getDisplayname()));
		return json;
	}

	public JSONObject toJson2() {
		JSONObject json = new JSONObject();
		if (vidyoMember == null) {
			return json;
		}
		json.put("id", vidyoMember.getId());
		json.put("name", CommonUtil.null2Empty(vidyoMember.getName()));
		json.put("displayname", CommonUtil.null2Empty(vidyoMember
				.getDisplayname()));
		json
				.put("extension", CommonUtil.null2Empty(vidyoMember
						.getExtension()));
		json
				.put("groupName", CommonUtil.null2Empty(vidyoMember
						.getGroupname()));
		return json;
	}

	public JSONObject toJson3() {
		JSONObject json = new JSONObject();
		if (vidyoMember == null) {
			return json;
		}
		json.put("id", vidyoMember.getId());
		json.put("name", CommonUtil.null2Empty(vidyoMember.getName()));
		json.put("displayname", CommonUtil.null2Empty(vidyoMember
				.getDisplayname()));
		json
				.put("extension", CommonUtil.null2Empty(vidyoMember
						.getExtension()));
		json
				.put("groupName", CommonUtil.null2Empty(vidyoMember
						.getGroupname()));
		json.put("allowcalldirect", vidyoMember.getAllowcalldirect());
		json.put("allowpersonalmeeting", vidyoMember.getAllowpersonalmeeting());
		json.put("description", CommonUtil.null2Empty(vidyoMember
				.getDescription()));
		json.put("email", CommonUtil.null2Empty(vidyoMember.getEmail()));
		json.put("language", CommonUtil.null2Empty(vidyoMember.getLanguage()));
		json.put("locationtag", CommonUtil.null2Empty(vidyoMember
				.getLocationtag()));
		json
				.put("proxyname", CommonUtil.null2Empty(vidyoMember
						.getProxyname()));
		json.put("rolename", CommonUtil.null2Empty(vidyoMember.getRolename()));
		json.put("tenantId", vidyoMember.getTenantId());
		return json;
	}

}
