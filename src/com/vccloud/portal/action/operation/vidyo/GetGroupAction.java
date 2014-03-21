package com.vccloud.portal.action.operation.vidyo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.OperationRejectedException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.vo.GroupVO;

public class GetGroupAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String groupId = params.get("groupId");
		String tenantId = params.get("tenantId");

		try {
			int groupId2 = Integer.parseInt(groupId);
			long tenantId2 = Long.parseLong(tenantId);
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			GroupVO group = vidyoService.getGroup(tenantId2, groupId2);
			JSONObject result = new JSONObject();
			result.put("result", "ok");
			if (group != null) {
				result.put("group", group.toJson3());
			}
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
		String groupId = params.get("groupId");
		String tenantId = params.get("tenantId");

		try {
			int groupId2 = Integer.parseInt(groupId);
			if (groupId2 <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
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
