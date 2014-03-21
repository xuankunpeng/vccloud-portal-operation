package com.vccloud.portal.action.operation.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.db.model.TExtVidyoOperationAccount;
import com.vccloud.portal.exception.AuthnFailedException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.UserService;
import com.vccloud.portal.util.CommonUtil;

public class SignInAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String username = params.get("username");
		String password = params.get("password");

		try {
			UserService userService = ServiceLocator.getUserService();
			TExtVidyoOperationAccount account = userService.signInByOperator(
					username, password);
			if (account != null) {
				setLogon(request);
				setOperator(request, account.getId());
				return RESULT_OK;
			} else {
				return RESULT_ERR_AUTHN_FAILED;
			}
		} catch (AuthnFailedException e) {
			return RESULT_ERR_AUTHN_FAILED;
		} catch (ServiceException e) {
			logger.error("", e);
			return RESULT_ERR_SYS;
		} catch (Exception e) {
			logger.error("", e);
			return RESULT_ERR_SYS;
		}
	}

	@Override
	protected boolean isAuthnRequired() {
		return false;
	}

	@Override
	protected boolean validateInputs(Map<String, String> params) {
		String username = params.get("username");
		String password = params.get("password");

		if (CommonUtil.isNullOrEmpty(username)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(password)) {
			return false;
		}
		return true;
	}

	private void setLogon(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		session.setAttribute("isLogon", true);
	}

	private void setOperator(HttpServletRequest request, long operatorId) {
		HttpSession session = request.getSession(true);
		session.setAttribute("operatorId", operatorId);
	}

}
