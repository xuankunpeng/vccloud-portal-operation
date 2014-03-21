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
import com.vccloud.portal.vo.RoomVO;

public class GetRoomAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String tenantId = params.get("tenantId");
		String roomId = params.get("roomId");

		try {
			long tenantId2 = Long.parseLong(tenantId);
			int roomId2 = Integer.parseInt(roomId);
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			RoomVO room = vidyoService.getRoom(tenantId2, roomId2);
			JSONObject result = new JSONObject();
			result.put("result", "ok");
			result.put("room", room.toJson3());
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
			int roomId2 = Integer.parseInt(roomId);
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
