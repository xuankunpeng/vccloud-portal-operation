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
import com.vccloud.portal.util.CommonUtil;

public class UpdateRoomModeratorPinAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String tenantId = params.get("tenantId");
		String roomId = params.get("roomId");
		String pinCode = params.get("pin");

		try {
			long roomId2 = Long.parseLong(roomId);
			long tenantId2 = Long.parseLong(tenantId);
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			if (CommonUtil.isNullOrEmpty(pinCode)) {
				vidyoService.removeRoomModeratorPin(tenantId2, roomId2);
			} else {
				vidyoService
						.createRoomModeratorPin(tenantId2, roomId2, pinCode);
			}
			return RESULT_OK;
		} catch (CallVidyoWebServiceException e) {
			logger.error("", e);
			return RESULT_ERR_VIDYO;
		} catch (OperationRejectedException e) {
			return RESULT_ERR_OPERATION_REJECTED;
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
		String roomId = params.get("roomId");

		try {
			long tenantId2 = Long.parseLong(tenantId);
			if (tenantId2 <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		try {
			long roomId2 = Long.parseLong(roomId);
			if (roomId2 <= 0) {
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
