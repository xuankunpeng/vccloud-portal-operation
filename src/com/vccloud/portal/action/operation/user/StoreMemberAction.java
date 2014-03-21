package com.vccloud.portal.action.operation.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.constant.Constants;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.DuplicateMemberDisplayNameException;
import com.vccloud.portal.exception.DuplicateMemberEmailException;
import com.vccloud.portal.exception.DuplicateMemberExtensionException;
import com.vccloud.portal.exception.DuplicateMemberNameException;
import com.vccloud.portal.exception.OperationRejectedException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.UserService;
import com.vccloud.portal.util.CommonUtil;

public class StoreMemberAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String memberId = params.get("memberId");
		String tenantId = params.get("tenantId");
		String name = params.get("name");
		String password = params.get("password");
		String displayName = params.get("displayName");
		String email = params.get("email");
		String extension = params.get("extension");
		String groupName = params.get("groupName");
		String proxyName = params.get("proxyName");
		String locationTag = params.get("locationTag");
		String language = params.get("language");
		String description = params.get("description");
		String roleName = params.get("roleName");

		try {
			int memberId2 = 0;
			if (!CommonUtil.isNullOrEmpty(memberId)) {
				memberId2 = Integer.parseInt(memberId);
			}
			long tenantId2 = 0;
			if (!CommonUtil.isNullOrEmpty(tenantId)) {
				tenantId2 = Long.parseLong(tenantId);
			}
			UserService userService = ServiceLocator.getUserService();
			if (memberId2 > 0) {
				userService.updateMember(memberId2, displayName, email,
						extension, groupName, proxyName, locationTag, language,
						description, roleName);
			} else {
				userService.createMember(tenantId2, name, password,
						displayName, email, extension, groupName, proxyName,
						locationTag, language, description, roleName);
			}
			return RESULT_OK;
		} catch (OperationRejectedException e) {
			return RESULT_ERR_OPERATION_REJECTED;
		} catch (DuplicateMemberNameException e) {
			return RESULT_ERR_DUPLICATE_MEMBER_NAME;
		} catch (DuplicateMemberDisplayNameException e) {
			return RESULT_ERR_DUPLICATE_MEMBER_DISPLAY_NAME;
		} catch (DuplicateMemberEmailException e) {
			return RESULT_ERR_DUPLICATE_MEMBER_EMAIL;
		} catch (DuplicateMemberExtensionException e) {
			return RESULT_ERR_DUPLICATE_MEMBER_EXTENSION;
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
		String tenantId = params.get("tenantId");
		String name = params.get("name");
		String password = params.get("password");
		String displayName = params.get("displayName");
		String email = params.get("email");
		String extension = params.get("extension");
		String groupName = params.get("groupName");
		String proxyName = params.get("proxyName");
		String locationTag = params.get("locationTag");
		String language = params.get("language");
		String description = params.get("description");
		String roleName = params.get("roleName");

		if (!CommonUtil.isNullOrEmpty(memberId)) {
			try {
				int memberId2 = Integer.parseInt(memberId);
				if (memberId2 <= 0) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
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
		if (!CommonUtil.isNullOrEmpty(name)) {
			if (name.length() > 255) {
				return false;
			}
		}
		if (!CommonUtil.isNullOrEmpty(password)) {
			if (password.length() > 255) {
				return false;
			}
		}
		if (CommonUtil.isNullOrEmpty(displayName)) {
			return false;
		}
		if (displayName.length() > 255) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(email)) {
			return false;
		}
		if (email.length() > 255) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(extension)) {
			return false;
		}
		if (extension.length() > 255) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(groupName)) {
			return false;
		}
		if (groupName.length() > 255) {
			return false;
		}
		if (!CommonUtil.isNullOrEmpty(proxyName)) {
			if (proxyName.length() > 255) {
				return false;
			}
		}
		if (CommonUtil.isNullOrEmpty(locationTag)) {
			return false;
		}
		if (locationTag.length() > 255) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(language)) {
			return false;
		}
		if (language.length() > 255) {
			return false;
		}
		if (!CommonUtil.isNullOrEmpty(description)) {
			if (description.length() > 255) {
				return false;
			}
		}
		if (CommonUtil.isNullOrEmpty(roleName)) {
			return false;
		}
		if (!Constants.RoleName.getAllExcept4Legacy().contains(roleName)) {
			return false;
		}
		return true;
	}

	protected boolean isOperatorRoleRequired() {
		return true;
	}

}
