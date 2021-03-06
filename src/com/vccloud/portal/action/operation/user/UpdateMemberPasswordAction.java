package com.vccloud.portal.action.operation.user;

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
import com.vccloud.portal.service.UserService;
import com.vccloud.portal.util.CommonUtil;

public class UpdateMemberPasswordAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String memberId = params.get("memberId");
		String newPassword = params.get("newPassword");

		try {
			int memberId2 = Integer.parseInt(memberId);
			UserService userService = ServiceLocator.getUserService();
			userService.updateMemberPasswordByOperator(memberId2, newPassword);
			return RESULT_OK;
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
		String memberId = params.get("memberId");
		String newPassword = params.get("newPassword");

		try {
			long memberId2 = Long.parseLong(memberId);
			if (memberId2 <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(newPassword)) {
			return false;
		}
		return true;
	}

	protected boolean isOperatorRoleRequired() {
		return true;
	}

}
