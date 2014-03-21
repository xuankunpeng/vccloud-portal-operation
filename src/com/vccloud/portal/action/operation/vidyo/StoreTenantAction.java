package com.vccloud.portal.action.operation.vidyo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.DuplicateExtensionPrefixException;
import com.vccloud.portal.exception.DuplicatePortalUrlException;
import com.vccloud.portal.exception.DuplicateTenantNameException;
import com.vccloud.portal.exception.DuplicateTenantURLException;
import com.vccloud.portal.exception.OperationRejectedException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.util.CommonUtil;

public class StoreTenantAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String tenantId = params.get("tenantId");
		String portalId = params.get("portalId");
		String tenantName = params.get("tenantName");
		String tenantURL = params.get("tenantURL");
		String tenantAdminName = params.get("tenantAdminName");
		String tenantAdminPassword = params.get("tenantAdminPassword");
		String extensionPrefix = params.get("extensionPrefix");
		String dialinNumber = params.get("dialinNumber");
		String vidyoReplayUrl = params.get("vidyoReplayUrl");
		String description = params.get("description");
		String numOfInstalls = params.get("numOfInstalls");
		String numOfSeats = params.get("numOfSeats");
		String numOfLines = params.get("numOfLines");
		String numOfExecutives = params.get("numOfExecutives");
		String numOfPanoramas = params.get("numOfPanoramas");
		String enableGuestLogin = params.get("enableGuestLogin");
		String allowedTenantList = params.get("allowedTenantList");
		String vidyoManager = params.get("vidyoManager");
		String vidyoProxyList = params.get("vidyoProxyList");
		String allowedVidyoGatewayList = params.get("allowedVidyoGatewayList");
		String allowedVidyoReplayRecorderList = params
				.get("allowedVidyoReplayRecorderList");
		String allowedVidyoReplayList = params.get("allowedVidyoReplayList");
		String allowedLocationTagList = params.get("allowedLocationTagList");
		String vidyoMobileAllowed = params.get("vidyoMobileAllowed");
		String ipcAllowOutbound = params.get("ipcAllowOutbound");
		String ipcAllowInbound = params.get("ipcAllowInbound");
		String portalURL = params.get("portalURL");
		String acl = params.get("acl");
		try {
			long tenantId2 = 0;
			long portalId2 = 0;
			int numOfInstalls2 = 0;
			int numOfSeats2 = 0;
			int numOfLines2 = 0;
			int numOfExecutives2 = 0;
			int numOfPanoramas2 = 0;
			boolean enableGuestLogin2 = true;
			List<Integer> allowedTenantList2 = new ArrayList<Integer>();
			int vidyoManager2 = 0;
			List<Integer> vidyoProxyList2 = new ArrayList<Integer>();
			List<Integer> allowedVidyoGatewayList2 = new ArrayList<Integer>();
			List<Integer> allowedVidyoReplayRecorderList2 = new ArrayList<Integer>();
			List<Integer> allowedVidyoReplayList2 = new ArrayList<Integer>();
			List<Integer> allowedLocationTagList2 = new ArrayList<Integer>();
			boolean vidyoMobileAllowed2 = true;
			boolean ipcAllowOutbound2 = true;
			boolean ipcAllowInbound2 = true;

			if (!CommonUtil.isNullOrEmpty(tenantId)) {
				tenantId2 = Long.parseLong(tenantId);
			}
			portalId2 = Long.parseLong(portalId);
			numOfInstalls2 = Integer.parseInt(numOfInstalls);
			numOfSeats2 = Integer.parseInt(numOfSeats);
			numOfLines2 = Integer.parseInt(numOfLines);
			numOfExecutives2 = Integer.parseInt(numOfExecutives);
			numOfPanoramas2 = Integer.parseInt(numOfPanoramas);
			if (!CommonUtil.isNullOrEmpty(enableGuestLogin)) {
				enableGuestLogin2 = Boolean.parseBoolean(enableGuestLogin);
			}
			if (!CommonUtil.isNullOrEmpty(allowedTenantList)) {
				String[] arr = allowedTenantList.split(",");
				for (String item : arr) {
					int id = Integer.parseInt(item);
					allowedTenantList2.add(id);
				}
			}
			vidyoManager2 = Integer.parseInt(vidyoManager);
			if (!CommonUtil.isNullOrEmpty(vidyoProxyList)) {
				String[] arr = vidyoProxyList.split(",");
				for (String item : arr) {
					int id = Integer.parseInt(item);
					vidyoProxyList2.add(id);
				}
			}
			if (!CommonUtil.isNullOrEmpty(allowedVidyoGatewayList)) {
				String[] arr = allowedVidyoGatewayList.split(",");
				for (String item : arr) {
					int id = Integer.parseInt(item);
					allowedVidyoGatewayList2.add(id);
				}
			}
			if (!CommonUtil.isNullOrEmpty(allowedVidyoReplayRecorderList)) {
				String[] arr = allowedVidyoReplayRecorderList.split(",");
				for (String item : arr) {
					int id = Integer.parseInt(item);
					allowedVidyoReplayRecorderList2.add(id);
				}
			}
			if (!CommonUtil.isNullOrEmpty(allowedVidyoReplayList)) {
				String[] arr = allowedVidyoReplayList.split(",");
				for (String item : arr) {
					int id = Integer.parseInt(item);
					allowedVidyoReplayList2.add(id);
				}
			}
			String[] arr = allowedLocationTagList.split(",");
			for (String item : arr) {
				int id = Integer.parseInt(item);
				allowedLocationTagList2.add(id);
			}
			if (!CommonUtil.isNullOrEmpty(vidyoMobileAllowed)) {
				vidyoMobileAllowed2 = Boolean.parseBoolean(vidyoMobileAllowed);
			}
			if (!CommonUtil.isNullOrEmpty(ipcAllowOutbound)) {
				ipcAllowOutbound2 = Boolean.parseBoolean(ipcAllowOutbound);
			}
			if (!CommonUtil.isNullOrEmpty(ipcAllowInbound)) {
				ipcAllowInbound2 = Boolean.parseBoolean(ipcAllowInbound);
			}
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			if (tenantId2 > 0) {
				vidyoService.updateTenant(tenantId2, portalId2, tenantName,
						tenantURL, tenantAdminName, tenantAdminPassword,
						extensionPrefix, dialinNumber, vidyoReplayUrl,
						description, numOfInstalls2, numOfSeats2, numOfLines2,
						numOfExecutives2, numOfPanoramas2, enableGuestLogin2,
						allowedTenantList2, vidyoManager2, vidyoProxyList2,
						allowedVidyoGatewayList2,
						allowedVidyoReplayRecorderList2,
						allowedVidyoReplayList2, allowedLocationTagList2,
						vidyoMobileAllowed2, ipcAllowOutbound2,
						ipcAllowInbound2, portalURL,acl);
			} else {
				vidyoService.createTenant(portalId2, tenantName, tenantURL,
						tenantAdminName, tenantAdminPassword, extensionPrefix,
						dialinNumber, vidyoReplayUrl, description,
						numOfInstalls2, numOfSeats2, numOfLines2,
						numOfExecutives2, numOfPanoramas2, enableGuestLogin2,
						allowedTenantList2, vidyoManager2, vidyoProxyList2,
						allowedVidyoGatewayList2,
						allowedVidyoReplayRecorderList2,
						allowedVidyoReplayList2, allowedLocationTagList2,
						vidyoMobileAllowed2, ipcAllowOutbound2,
						ipcAllowInbound2, portalURL,acl);
			}
			return RESULT_OK;
		} catch (OperationRejectedException e) {
			return RESULT_ERR_OPERATION_REJECTED;
		} catch (DuplicateTenantNameException e) {
			return RESULT_ERR_DUPLICATE_TENANT_NAME;
		} catch (DuplicateTenantURLException e) {
			return RESULT_ERR_DUPLICATE_TENANT_URL;
		} catch (DuplicateExtensionPrefixException e) {
			return RESULT_ERR_DUPLICATE_EXTENSION_PREFIX;
		} catch (DuplicatePortalUrlException e) {
			return RESULT_ERR_DUPLICATE_PORTAL_URL;
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
		String portalId = params.get("portalId");
		String tenantName = params.get("tenantName");
		String tenantURL = params.get("tenantURL");
		String tenantAdminName = params.get("tenantAdminName");
		String tenantAdminPassword = params.get("tenantAdminPassword");
		String extensionPrefix = params.get("extensionPrefix");
		String numOfInstalls = params.get("numOfInstalls");
		String numOfSeats = params.get("numOfSeats");
		String numOfLines = params.get("numOfLines");
		String numOfExecutives = params.get("numOfExecutives");
		String numOfPanoramas = params.get("numOfPanoramas");
		String enableGuestLogin = params.get("enableGuestLogin");
		String allowedTenantList = params.get("allowedTenantList");
		String vidyoManager = params.get("vidyoManager");
		String vidyoProxyList = params.get("vidyoProxyList");
		String allowedVidyoGatewayList = params.get("allowedVidyoGatewayList");
		String allowedVidyoReplayRecorderList = params
				.get("allowedVidyoReplayRecorderList");
		String allowedVidyoReplayList = params.get("allowedVidyoReplayList");
		String allowedLocationTagList = params.get("allowedLocationTagList");
		String vidyoMobileAllowed = params.get("vidyoMobileAllowed");
		String ipcAllowOutbound = params.get("ipcAllowOutbound");
		String ipcAllowInbound = params.get("ipcAllowInbound");
		String portalURL = params.get("portalURL");
		String acl = params.get("acl");
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
		try {
			long portalId2 = Long.parseLong(portalId);
			if (portalId2 <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(tenantName)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(tenantURL)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(tenantAdminName)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(tenantAdminPassword)) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(extensionPrefix)) {
			return false;
		}
		try {
			int numOfInstalls2 = Integer.parseInt(numOfInstalls);
			if (numOfInstalls2 < 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		try {
			int numOfSeats2 = Integer.parseInt(numOfSeats);
			if (numOfSeats2 < 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		try {
			int numOfLines2 = Integer.parseInt(numOfLines);
			if (numOfLines2 < 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		try {
			int numOfExecutives2 = Integer.parseInt(numOfExecutives);
			if (numOfExecutives2 < 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		try {
			int numOfPanoramas2 = Integer.parseInt(numOfPanoramas);
			if (numOfPanoramas2 < 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		if (!CommonUtil.isNullOrEmpty(enableGuestLogin)) {
			try {
				Boolean.parseBoolean(enableGuestLogin);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (!CommonUtil.isNullOrEmpty(allowedTenantList)) {
			try {
				String[] arr = allowedTenantList.split(",");
				for (String item : arr) {
					int id = Integer.parseInt(item);
					if (id < 0) {
						return false;
					}
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		try {
			Integer.parseInt(vidyoManager);
		} catch (NumberFormatException e) {
			return false;
		}
		if (!CommonUtil.isNullOrEmpty(vidyoProxyList)) {
			try {
				String[] arr = vidyoProxyList.split(",");
				for (String item : arr) {
					int id = Integer.parseInt(item);
					if (id < 0) {
						return false;
					}
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (!CommonUtil.isNullOrEmpty(allowedVidyoGatewayList)) {
			try {
				String[] arr = allowedVidyoGatewayList.split(",");
				for (String item : arr) {
					int id = Integer.parseInt(item);
					if (id < 0) {
						return false;
					}
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (!CommonUtil.isNullOrEmpty(allowedVidyoReplayRecorderList)) {
			try {
				String[] arr = allowedVidyoReplayRecorderList.split(",");
				for (String item : arr) {
					int id = Integer.parseInt(item);
					if (id < 0) {
						return false;
					}
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (!CommonUtil.isNullOrEmpty(allowedVidyoReplayList)) {
			try {
				String[] arr = allowedVidyoReplayList.split(",");
				for (String item : arr) {
					int id = Integer.parseInt(item);
					if (id < 0) {
						return false;
					}
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		try {
			String[] arr = allowedLocationTagList.split(",");
			for (String item : arr) {
				int id = Integer.parseInt(item);
				if (id < 0) {
					return false;
				}
			}
		} catch (NumberFormatException e) {
			return false;
		}
		if (!CommonUtil.isNullOrEmpty(vidyoMobileAllowed)) {
			try {
				Boolean.parseBoolean(vidyoMobileAllowed);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (!CommonUtil.isNullOrEmpty(ipcAllowOutbound)) {
			try {
				Boolean.parseBoolean(ipcAllowOutbound);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (!CommonUtil.isNullOrEmpty(ipcAllowInbound)) {
			try {
				Boolean.parseBoolean(ipcAllowInbound);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (CommonUtil.isNullOrEmpty(portalURL)) {
			return false;
		}
		if (portalURL.length() > 255) {
			return false;
		}
		if (!CommonUtil.isNullOrEmpty(acl)) {
			try {
				Boolean.parseBoolean(acl);
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
