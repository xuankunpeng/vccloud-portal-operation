package com.vccloud.portal.vo;

import net.sf.json.JSONObject;

import com.vccloud.portal.db.model.TExtVidyoPortalConfig;
import com.vccloud.portal.db.model.TExtVidyoTenant;
import com.vccloud.portal.util.CommonUtil;

public class TenantAndPortalConfigVO {

	private TExtVidyoTenant tenant;
	private TExtVidyoPortalConfig portalConfig;

	public TExtVidyoTenant getTenant() {
		return tenant;
	}

	public void setTenant(TExtVidyoTenant tenant) {
		this.tenant = tenant;
	}

	public TExtVidyoPortalConfig getPortalConfig() {
		return portalConfig;
	}

	public void setPortalConfig(TExtVidyoPortalConfig portalConfig) {
		this.portalConfig = portalConfig;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("tenantID", 0L);
		json.put("name", "");
		json.put("url", "");
		json.put("extensionPrefix", "");
		json.put("description", "");
		json.put("portalName", "");
		json.put("portalId", 0L);
		if (tenant != null) {
			json.put("tenantID", tenant.getId());
			json.put("name", CommonUtil.null2Empty(tenant.getName()));
			json.put("url", CommonUtil.null2Empty(tenant.getUrl()));
			json.put("extensionPrefix", CommonUtil.null2Empty(tenant
					.getExtensionPrefix()));
			json.put("description", CommonUtil.null2Empty(tenant
					.getDescription()));
		}
		if (portalConfig != null) {
			json.put("portalName", CommonUtil.null2Empty(portalConfig
					.getPortalName()));
			json.put("portalId", portalConfig.getId());
		}
		return json;
	}

	public JSONObject toJson2() {
		JSONObject json = new JSONObject();
		json.put("tenantID", 0L);
		json.put("url", "");
		json.put("description", "");
		if (tenant != null) {
			json.put("tenantID", tenant.getId());
			json.put("url", CommonUtil.null2Empty(tenant.getUrl()));
			json.put("description", CommonUtil.null2Empty(tenant
					.getDescription()));
		}
		return json;
	}

}
