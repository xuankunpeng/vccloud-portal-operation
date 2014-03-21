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
import com.vccloud.portal.vo.TenantAndPortalConfigVO;

public class GetTenantsByPortalConfigIdAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String portalConfigId = params.get("portalConfigId");

		try {
			long portalConfigId2 = Long.parseLong(portalConfigId);
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			List<TenantAndPortalConfigVO> tenants = vidyoService
					.getTenantsByPortalConfigId(portalConfigId2);
			JSONArray tenants2 = new JSONArray();
			if (!CommonUtil.isNullOrEmpty(tenants)) {
				for (TenantAndPortalConfigVO item : tenants) {
					tenants2.add(item.toJson2());
				}
			}
			JSONObject result = new JSONObject();
			result.put("result", "ok");
			result.put("tenants", tenants2);
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
		String portalConfigId = params.get("portalConfigId");

		try {
			long portalConfigId2 = Long.parseLong(portalConfigId);
			if (portalConfigId2 <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean isAuthnRequired() {
		return false;
	}

}