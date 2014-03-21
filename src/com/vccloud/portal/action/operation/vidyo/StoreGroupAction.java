package com.vccloud.portal.action.operation.vidyo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.DuplicateGroupNameException;
import com.vccloud.portal.exception.OperationRejectedException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.util.CommonUtil;

public class StoreGroupAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String groupId = params.get("groupId");
		String tenantId = params.get("tenantId");
		String name = params.get("name");
		String description = params.get("description");
		String roomMaxUsers = params.get("roomMaxUsers");
		String userMaxBandWidthIn = params.get("userMaxBandWidthIn");
		String userMaxBandWidthOut = params.get("userMaxBandWidthOut");
		String allowRecording = params.get("allowRecording");

		try {
			int groupId2 = 0;
			if (!CommonUtil.isNullOrEmpty(groupId)) {
				groupId2 = Integer.parseInt(groupId);
			}
			long tenantId2 = Long.parseLong(tenantId);
			boolean allowRecording2 = Boolean.parseBoolean(allowRecording);
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			if (groupId2 > 0) {
				vidyoService.updateGroup(tenantId2, groupId2, name,
						description, roomMaxUsers, userMaxBandWidthIn,
						userMaxBandWidthOut, allowRecording2);
			} else {
				vidyoService.createGroup(tenantId2, name, description,
						roomMaxUsers, userMaxBandWidthIn, userMaxBandWidthOut,
						allowRecording2);
			}
			return RESULT_OK;
		} catch (OperationRejectedException e) {
			return RESULT_ERR_OPERATION_REJECTED;
		} catch (DuplicateGroupNameException e) {
			return RESULT_ERR_DUPLICATE_GROUP_NAME;
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
		String name = params.get("name");
		String roomMaxUsers = params.get("roomMaxUsers");
		String userMaxBandWidthIn = params.get("userMaxBandWidthIn");
		String userMaxBandWidthOut = params.get("userMaxBandWidthOut");
		String allowRecording = params.get("allowRecording");

		if (!CommonUtil.isNullOrEmpty(groupId)) {
			try {
				int groupId2 = Integer.parseInt(groupId);
				if (groupId2 <= 0) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		try {
			long tenantId2 = Long.parseLong(tenantId);
			if (tenantId2 <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(name)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(roomMaxUsers)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(userMaxBandWidthIn)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(userMaxBandWidthOut)) {
			return false;
		}
		try {
			Boolean.parseBoolean(allowRecording);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	protected boolean isOperatorRoleRequired() {
		return true;
	}

}
