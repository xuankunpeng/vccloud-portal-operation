package com.vccloud.portal.vo;

import net.sf.json.JSONObject;

import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.ComponentData;

public class ComponentDataVO {

	private ComponentData componentData;

	public ComponentData getComponentData() {
		return componentData;
	}

	public void setComponentData(ComponentData componentData) {
		this.componentData = componentData;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		if (componentData == null) {
			return json;
		}
		json.put("componentType", CommonUtil.null2Empty(componentData
				.getComponentType().getValue()));
		json.put("identifier", CommonUtil.null2Empty(componentData
				.getIdentifier()));
		json.put("displayName", CommonUtil.null2Empty(componentData
				.getDisplayName()));
		return json;
	}

}
