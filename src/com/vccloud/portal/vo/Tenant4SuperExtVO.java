package com.vccloud.portal.vo;

import net.sf.json.JSONObject;

import com.vccloud.portal.db.model.TExtVidyoPortal;
import com.vccloud.portal.db.model.TPortalInfo;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.TenantDataExtType;

public class Tenant4SuperExtVO {

	private TenantDataExtType tenant;
	private TPortalInfo portalInfo;
	private TExtVidyoPortal vidyoPortal;

	public TenantDataExtType getTenant() {
		return tenant;
	}

	public void setTenant(TenantDataExtType tenant) {
		this.tenant = tenant;
	}

	public TPortalInfo getPortalInfo() {
		return portalInfo;
	}

	public void setPortalInfo(TPortalInfo portalInfo) {
		this.portalInfo = portalInfo;
	}

	public TExtVidyoPortal getVidyoPortal() {
		return vidyoPortal;
	}

	public void setVidyoPortal(TExtVidyoPortal vidyoPortal) {
		this.vidyoPortal = vidyoPortal;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("tenantName", "");
		json.put("tenantURL", "");
		json.put("tenantAdminName", "");
		json.put("tenantAdminPassword", "");
		json.put("extensionPrefix", "");
		json.put("dialinNumber", "");
		json.put("vidyoReplayUrl", "");
		json.put("description", "");
		json.put("numOfInstalls", 0);
		json.put("numOfSeats", 0);
		json.put("numOfLines", 0);
		json.put("numOfExecutives", 0);
		json.put("numOfPanoramas", 0);
		json.put("enableGuestLogin", true);
		json.put("allowedTenantList", new int[0]);
		json.put("vidyoManager", 0);
		json.put("vidyoProxyList", new int[0]);
		json.put("allowedVidyoGatewayList", new int[0]);
		json.put("allowedVidyoReplayRecorderList", new int[0]);
		json.put("allowedVidyoReplayList", new int[0]);
		json.put("allowedLocationTagList", new int[0]);
		json.put("vidyoMobileAllowed", true);
		json.put("ipcAllowOutbound", true);
		json.put("ipcAllowInbound", true);
		json.put("portalUrl", "");
		if (tenant != null) {
			if (tenant.getTenantName() != null) {
				json.put("tenantName", tenant.getTenantName()
						.getTenantNamePattern());
			}
			if (tenant.getExtensionPrefix() != null) {
				json.put("extensionPrefix", tenant.getExtensionPrefix()
						.getTenantExtensionPrefixPattern());
			}
			if (tenant.getDialinNumber() != null) {
				json
						.put("dialinNumber", tenant.getDialinNumber()
								.getString20());
			}
			if (tenant.getVidyoReplayUrl() != null) {
				json.put("vidyoReplayUrl", tenant.getVidyoReplayUrl()
						.getTenantUrlPattern());
			}
			json.put("description", tenant.getDescription());
			if (tenant.getNumOfInstalls() != null) {
				json.put("numOfInstalls", tenant.getNumOfInstalls()
						.getNonNegativeInt());
			}
			if (tenant.getNumOfSeats() != null) {
				json.put("numOfSeats", tenant.getNumOfSeats()
						.getNonNegativeInt());
			}
			if (tenant.getNumOfLines() != null) {
				json.put("numOfLines", tenant.getNumOfLines()
						.getNonNegativeInt());
			}
			if (tenant.getNumOfExecutives() != null) {
				json.put("numOfExecutives", tenant.getNumOfExecutives()
						.getNonNegativeInt());
			}
			if (tenant.getNumOfPanoramas() != null) {
				json.put("numOfPanoramas", tenant.getNumOfPanoramas()
						.getNonNegativeInt());
			}
			json.put("enableGuestLogin", tenant.getEnableGuestLogin());
			json.put("allowedTenantList", tenant.getAllowedTenantList());
			json.put("vidyoManager", tenant.getVidyoManager());
			json.put("vidyoProxyList", tenant.getVidyoProxyList());
			json.put("allowedVidyoGatewayList", tenant
					.getAllowedVidyoGatewayList());
			json.put("allowedVidyoReplayRecorderList", tenant
					.getAllowedVidyoReplayRecorderList());
			json.put("allowedVidyoReplayList", tenant
					.getAllowedVidyoRepalyList());
			json.put("allowedLocationTagList", tenant
					.getAllowedLocationTagList());
			json.put("vidyoMobileAllowed", tenant.getVidyoMobileAllowed());
			json.put("ipcAllowOutbound", tenant.getIpcAllowOutbound());
			json.put("ipcAllowInbound", tenant.getIpcAllowInbound());
		}
		if (portalInfo != null) {
			json.put("tenantURL", portalInfo.getPortalUrl());
			json.put("tenantAdminName", portalInfo.getAdminName());
			json.put("tenantAdminPassword", portalInfo.getAdminPassword());
		}
		if (vidyoPortal != null) {
			json.put("portalUrl", CommonUtil.null2Empty(vidyoPortal
					.getPortalurl()));
		}
		return json;
	}

}
