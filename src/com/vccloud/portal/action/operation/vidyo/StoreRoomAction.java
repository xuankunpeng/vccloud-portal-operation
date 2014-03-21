package com.vccloud.portal.action.operation.vidyo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.DuplicateRoomExtensionException;
import com.vccloud.portal.exception.DuplicateRoomNameException;
import com.vccloud.portal.exception.OperationRejectedException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.util.CommonUtil;

public class StoreRoomAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String tenantId = params.get("tenantId");
		String roomId = params.get("roomId");
		String description = params.get("description");
		String extension = params.get("extension");
		String groupName = params.get("groupName");
		String name = params.get("name");
		String ownerName = params.get("ownerName");
		String roomType = params.get("roomType");
		String roomPIN = params.get("roomPIN");
		String moderatorPIN = params.get("moderatorPIN");

		try {
			long tenantId2 = Long.parseLong(tenantId);
			int roomId2 = 0;
			if (!CommonUtil.isNullOrEmpty(roomId)) {
				roomId2 = Integer.parseInt(roomId);
			}
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			if (roomId2 > 0) {
				vidyoService.updateRoom(tenantId2, roomId2, description,
						extension, groupName, name, ownerName, roomType);
			} else {
				vidyoService.createRoom(tenantId2, description, extension,
						groupName, name, ownerName, roomType, roomPIN,
						moderatorPIN);
			}
			return RESULT_OK;
		} catch (OperationRejectedException e) {
			return RESULT_ERR_OPERATION_REJECTED;
		} catch (DuplicateRoomNameException e) {
			return RESULT_ERR_DUPLICATE_ROOM_NAME;
		} catch (DuplicateRoomExtensionException e) {
			return RESULT_ERR_DUPLICATE_ROOM_EXTENSION;
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
		String extension = params.get("extension");
		String groupName = params.get("groupName");
		String name = params.get("name");
		String ownerName = params.get("ownerName");
		String roomType = params.get("roomType");

		try {
			long tenantId2 = Long.parseLong(tenantId);
			if (tenantId2 <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		if (!CommonUtil.isNullOrEmpty(roomId)) {
			try {
				int roomId2 = Integer.parseInt(roomId);
				if (roomId2 <= 0) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (CommonUtil.isNullOrEmpty(name)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(ownerName)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(extension)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(groupName)) {
			return false;
		}
		if (!CommonUtil.isNullOrEmpty(roomType)) {
			if (!com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.RoomType_type0.Personal
					.getValue().equalsIgnoreCase(roomType)
					&& !com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.RoomType_type0.Public
							.getValue().equalsIgnoreCase(roomType)) {
				return false;
			}
		}
		return true;
	}

	protected boolean isOperatorRoleRequired() {
		return true;
	}

}
