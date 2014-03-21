package com.vccloud.portal.action.operation.vidyo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.db.model.TExtVidyoTenant;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.OperationRejectedException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.UserService;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.vo.GroupVO;
import com.vccloud.portal.vo.MemberVO;

public class Prepare4StoreRoomAction extends BaseAction {

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
			List<MemberVO> members = userService.searchMembers(tenantId2, null,
					Integer.MAX_VALUE, 0);
			List<GroupVO> groups = vidyoService.getGroupsByTenantId(tenantId2);
			JSONArray members2 = new JSONArray();
			JSONArray groups2 = new JSONArray();
			if (tenant != null) {
				JSONObject tenant2 = new JSONObject();
				tenant2.put("id", 0);
				tenant2.put("name", tenant.getAdminName());
				tenant2.put("displayname", tenant.getAdminName());
				members2.add(tenant2);
			}
			if (!CommonUtil.isNullOrEmpty(members)) {
				for (MemberVO item : members) {
					members2.add(item.toJson());
				}
			}
			if (!CommonUtil.isNullOrEmpty(groups)) {
				for (GroupVO item : groups) {
					groups2.add(item.toJson());
				}
			}
			JSONObject result = new JSONObject();
			result.put("result", "ok");
			result.put("extensionPrefix", tenant == null ? "" : CommonUtil
					.null2Empty(tenant.getExtensionPrefix()));
			result.put("members", members2);
			result.put("groups", groups2);
			return result;
		} catch (OperationRejectedException e) {
			return RESULT_ERR_OPERATION_REJECTED;
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
