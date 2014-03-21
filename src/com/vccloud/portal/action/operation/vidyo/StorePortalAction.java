package com.vccloud.portal.action.operation.vidyo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.exception.DuplicatePortalNameException;
import com.vccloud.portal.exception.DuplicatePortalUrlException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.util.CommonUtil;

public class StorePortalAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String id = params.get("id");
		String portalName = params.get("portalName");
		String portalUrl = params.get("portalUrl");
		String superName = params.get("superName");
		String superPassword = params.get("superPassword");

		try {
			long id2 = 0;
			if (!CommonUtil.isNullOrEmpty(id)) {
				id2 = Long.parseLong(id);
			}
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			vidyoService.storePortal(id2, portalName, portalUrl, superName,
					superPassword);
			return RESULT_OK;
		} catch (DuplicatePortalNameException e) {
			return RESULT_ERR_DUPLICATE_PORTAL_NAME;
		} catch (DuplicatePortalUrlException e) {
			return RESULT_ERR_DUPLICATE_PORTAL_URL;
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
		String id = params.get("id");
		String portalName = params.get("portalName");
		String portalUrl = params.get("portalUrl");
		String superName = params.get("superName");
		String superPassword = params.get("superPassword");

		if (!CommonUtil.isNullOrEmpty(id)) {
			try {
				long id2 = Long.parseLong(id);
				if (id2 <= 0) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (CommonUtil.isNullOrEmpty(portalName)) {
			return false;
		}
		if (portalName.length() > 50) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(portalUrl)) {
			return false;
		}
		if (portalUrl.length() > 500) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(superName)) {
			return false;
		}
		if (superName.length() > 50) {
			return false;
		}
		if (CommonUtil.isNullOrEmpty(superPassword)) {
			return false;
		}
		if (superPassword.length() > 50) {
			return false;
		}
		return true;
	}

	protected boolean isOperatorRoleRequired() {
		return true;
	}

}
