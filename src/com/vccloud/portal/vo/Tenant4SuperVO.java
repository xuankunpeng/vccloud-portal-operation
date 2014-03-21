package com.vccloud.portal.vo;

import net.sf.json.JSONObject;

import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType;

public class Tenant4SuperVO {

	private SingleTenantDataType tenant;

	public SingleTenantDataType getTenant() {
		return tenant;
	}

	public void setTenant(SingleTenantDataType tenant) {
		this.tenant = tenant;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		if (tenant == null) {
			return json;
		}
		json.put("tenantId", tenant.getTenantId().getEntityID());
		json.put("tenantName", CommonUtil.null2Empty(tenant.getTenantName()));
		return json;
	}

}
