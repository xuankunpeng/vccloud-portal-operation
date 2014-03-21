package com.vccloud.portal.action.operation.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.constant.Constants;
import com.vccloud.portal.db.model.TExtVidyoTenant;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.UserService;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.vo.ComponentDataVO;
import com.vccloud.portal.vo.GroupVO;
import com.vccloud.portal.vo.LocationTagVO;

public class Prepare4StoreMemberAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String tenantId = params.get("tenantId");

		try {
			long tenantId2 = Long.parseLong(tenantId);
			UserService userService = ServiceLocator.getUserService();
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			TExtVidyoTenant tenant = userService.getTenant(tenantId2);
			List<GroupVO> groups = vidyoService.getGroupsByTenantId(tenantId2);
			List<ComponentDataVO> proxyList = vidyoService
					.getVidyoProxyListByTenantId(tenantId2);
			List<LocationTagVO> locationTags = vidyoService
					.getLocationTagsByTenantId(tenantId2);
			List<String> roleNames = Constants.RoleName.getAllExcept4Legacy();
			JSONArray groups2 = new JSONArray();
			JSONArray proxyList2 = new JSONArray();
			JSONArray locationTags2 = new JSONArray();
			JSONArray roleNames2 = new JSONArray();
			if (!CommonUtil.isNullOrEmpty(groups)) {
				for (GroupVO item : groups) {
					groups2.add(item.toJson());
				}
			}
			if (!CommonUtil.isNullOrEmpty(proxyList)) {
				for (ComponentDataVO item : proxyList) {
					proxyList2.add(item.toJson());
				}
			}
			if (!CommonUtil.isNullOrEmpty(locationTags)) {
				for (LocationTagVO item : locationTags) {
					locationTags2.add(item.toJson());
				}
			}
			if (!CommonUtil.isNullOrEmpty(roleNames)) {
				for (String item : roleNames) {
					roleNames2.add(item);
				}
			}
			JSONObject result = new JSONObject();
			result.put("result", "ok");
			result.put("extensionPrefix", tenant == null ? "" : CommonUtil
					.null2Empty(tenant.getExtensionPrefix()));
			result.put("groups", groups2);
			result.put("proxyList", proxyList2);
			result.put("locationTags", locationTags2);
			result.put("roleNames", roleNames2);
			return result;
		} catch (CallVidyoWebServiceException e) {
			logger.error("", e);
			return RESULT_ERR_VIDYO;
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

		try {
			long tenantId2 = Long.parseLong(tenantId);
			if (tenantId2 <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	protected boolean isOperatorRoleRequired() {
		return true;
	}

}
