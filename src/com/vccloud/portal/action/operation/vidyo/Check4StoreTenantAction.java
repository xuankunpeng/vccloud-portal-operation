package com.vccloud.portal.action.operation.vidyo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.util.CommonUtil;

public class Check4StoreTenantAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String tenantId = params.get("tenantId");
		String tenantName = params.get("tenantName");
		String tenantURL = params.get("tenantURL");
		String extensionPrefix = params.get("extensionPrefix");
		String portalURL = params.get("portalURL");

		try {
			long tenantId2 = 0;
			if (!CommonUtil.isNullOrEmpty(tenantId)) {
				tenantId2 = Long.parseLong(tenantId);
			}
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			List<String> list = vidyoService.check4StoreTenant(tenantId2,
					tenantName, tenantURL, extensionPrefix, portalURL);
			JSONObject result = new JSONObject();
			result.put("result", "ok");
			JSONArray ja = new JSONArray();
			if (!CommonUtil.isNullOrEmpty(list)) {
				for (String item : list) {
					ja.add(item);
				}
			}
			result.put("list", ja);
			return result;
		} catch (ServiceException e) {
			logger.error("", e);
			return RESULT_ERR_SYS;
		} catch (Exception e) {
			logger.error("", e);
			return RESULT_ERR_SYS;
		}
	}

	@Override
	protected boolean validateInputs(Map<String, String> params) {
		String tenantId = params.get("tenantId");
		String tenantName = params.get("tenantName");
		String tenantURL = params.get("tenantURL");
		String extensionPrefix = params.get("extensionPrefix");
		String portalURL = params.get("portalURL");

		if (!CommonUtil.isNullOrEmpty(tenantId)) {
			try {
				long tenantId2 = Long.parseLong(tenantId);
				if (tenantId2 <= 0) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (CommonUtil.isNullOrEmpty(tenantName)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(tenantURL)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(extensionPrefix)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(portalURL)) {
			return false;
		}
		return true;
	}

	protected boolean isOperatorRoleRequired() {
		return true;
	}

}
