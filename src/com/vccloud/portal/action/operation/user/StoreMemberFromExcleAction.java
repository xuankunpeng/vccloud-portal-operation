package com.vccloud.portal.action.operation.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.db.model.TExtVidyoTenant;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.DuplicateMemberDisplayNameException;
import com.vccloud.portal.exception.DuplicateMemberExtensionException;
import com.vccloud.portal.exception.DuplicateMemberNameException;
import com.vccloud.portal.exception.FileNotFoundException;
import com.vccloud.portal.exception.ImportExcleDataException;
import com.vccloud.portal.exception.OperationRejectedException;
import com.vccloud.portal.exception.RoleNameException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.UserService;
import com.vccloud.portal.util.CommonUtil;

public class StoreMemberFromExcleAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String tenantId = params.get("tenantId");
		try {
			long tenantId2 = 0;
			if (!CommonUtil.isNullOrEmpty(tenantId)) {
				tenantId2 = Long.parseLong(tenantId);
			}
			long operatorId = getOperatorId(request);
			UserService userService = ServiceLocator.getUserService();
			DiskFileItem item = CommonUtil.isNullOrEmpty(diskFiles) ? null
					: diskFiles.get(0);
			TExtVidyoTenant tenant = userService.getTenant(tenantId2);
			userService.importMember(operatorId, item, tenantId2, tenant
					.getExtensionPrefix());

			return RESULT_OK;
		} catch (OperationRejectedException e) {

			return RESULT_ERR_OPERATION_REJECTED;
		} catch (FileNotFoundException e) {
			return RESULT_ERR_FILR_NOT_FOUND;
		} catch(RoleNameException e){
			return RESULT_ERR_ROLENAME;
		}
		catch (DuplicateMemberNameException e) {
			return RESULT_ERR_DUPLICATE_MEMBER_NAME;
		} catch (DuplicateMemberDisplayNameException e) {
			return RESULT_ERR_DUPLICATE_MEMBER_DISPLAY_NAME;
		} catch (DuplicateMemberExtensionException e) {
			return RESULT_ERR_DUPLICATE_MEMBER_EXTENSION;
		} catch (CallVidyoWebServiceException e) {
			logger.error("", e);
			return RESULT_ERR_VIDYO;
		} catch (ImportExcleDataException e) {
			return RESULT_ERR_EXCLE_DATA;
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
		return true;
	}

	protected boolean isOperatorRoleRequired() {
		return true;
	}

}
