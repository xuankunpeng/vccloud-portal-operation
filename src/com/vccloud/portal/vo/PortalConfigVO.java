package com.vccloud.portal.vo;

import net.sf.json.JSONObject;

import com.vccloud.portal.db.model.TExtVidyoPortalConfig;
import com.vccloud.portal.util.CommonUtil;

public class PortalConfigVO {

	private TExtVidyoPortalConfig tExtVidyoPortalConfig;

	public TExtVidyoPortalConfig gettExtVidyoPortalConfig() {
		return tExtVidyoPortalConfig;
	}

	public void settExtVidyoPortalConfig(
			TExtVidyoPortalConfig tExtVidyoPortalConfig) {
		this.tExtVidyoPortalConfig = tExtVidyoPortalConfig;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		if (tExtVidyoPortalConfig == null) {
			return json;
		}
		json.put("id", tExtVidyoPortalConfig.getId());
		json.put("portalName", CommonUtil.null2Empty(tExtVidyoPortalConfig
				.getPortalName()));
		json.put("portalUrl", CommonUtil.null2Empty(tExtVidyoPortalConfig
				.getPortalUrl()));
		json.put("superName", CommonUtil.null2Empty(tExtVidyoPortalConfig
				.getSuperName()));
		json.put("superPassword", CommonUtil.null2Empty(tExtVidyoPortalConfig
				.getSuperPassword()));
		return json;
	}

	public JSONObject toJson4Search() {
		JSONObject json = new JSONObject();
		if (tExtVidyoPortalConfig == null) {
			return json;
		}
		json.put("id", tExtVidyoPortalConfig.getId());
		json.put("portalName", CommonUtil.null2Empty(tExtVidyoPortalConfig
				.getPortalName()));
		json.put("portalUrl", CommonUtil.null2Empty(tExtVidyoPortalConfig
				.getPortalUrl()));
		return json;
	}

}
