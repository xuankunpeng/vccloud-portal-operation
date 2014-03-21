package com.vccloud.portal.action.operation.vidyo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.db.model.TExtVidyoTenant;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.UserService;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.vo.ComponentDataVO;
import com.vccloud.portal.vo.LocationTagVO;
import com.vccloud.portal.vo.Tenant4SuperVO;

public class Prepare4StoreTenantAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String portalId = params.get("portalId");
		String tenantId = params.get("tenantId");

		try {
			long portalId2 = Long.parseLong(portalId);
			long tenantId2 = 0;
			if (!CommonUtil.isNullOrEmpty(tenantId)) {
				tenantId2 = Long.parseLong(tenantId);
			}
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			Map<String, List<ComponentDataVO>> serviceComponentsDatas = vidyoService
					.getServiceComponentsData(portalId2);
			Map<String, String> licenseDatas = vidyoService.getLicenseData(
					portalId2, tenantId2);
			List<LocationTagVO> locationTags = vidyoService
					.getLocationTags(portalId2);
			List<Tenant4SuperVO> tenants = vidyoService
					.getListOfTenants(portalId2);
			UserService userService = ServiceLocator.getUserService();
			TExtVidyoTenant tenant = null;
			if (tenantId2 > 0) {
				tenant = userService.getTenant(tenantId2);
			}
			JSONObject result = new JSONObject();
			result.put("result", "ok");
			List<ComponentDataVO> vidyoManager = serviceComponentsDatas
					.get("VidyoManager");
			List<ComponentDataVO> vidyoProxy = serviceComponentsDatas
					.get("VidyoProxy");
			List<ComponentDataVO> vidyoGateway = serviceComponentsDatas
					.get("VidyoGateway");
			List<ComponentDataVO> vidyoRecorder = serviceComponentsDatas
					.get("VidyoRecorder");
			List<ComponentDataVO> vidyoReplay = serviceComponentsDatas
					.get("VidyoReplay");
			JSONArray vidyoManager2 = new JSONArray();
			JSONArray vidyoProxy2 = new JSONArray();
			JSONArray vidyoGateway2 = new JSONArray();
			JSONArray vidyoRecorder2 = new JSONArray();
			JSONArray vidyoReplay2 = new JSONArray();
			if (!CommonUtil.isNullOrEmpty(vidyoManager)) {
				for (ComponentDataVO item : vidyoManager) {
					vidyoManager2.add(item.toJson());
				}
			}
			if (!CommonUtil.isNullOrEmpty(vidyoProxy)) {
				for (ComponentDataVO item : vidyoProxy) {
					vidyoProxy2.add(item.toJson());
				}
			}
			if (!CommonUtil.isNullOrEmpty(vidyoGateway)) {
				for (ComponentDataVO item : vidyoGateway) {
					vidyoGateway2.add(item.toJson());
				}
			}
			if (!CommonUtil.isNullOrEmpty(vidyoRecorder)) {
				for (ComponentDataVO item : vidyoRecorder) {
					vidyoRecorder2.add(item.toJson());
				}
			}
			if (!CommonUtil.isNullOrEmpty(vidyoReplay)) {
				for (ComponentDataVO item : vidyoReplay) {
					vidyoReplay2.add(item.toJson());
				}
			}
			result.put("vidyoManager", vidyoManager2);
			result.put("vidyoProxyList", vidyoProxy2);
			result.put("allowedVidyoGatewayList", vidyoGateway2);
			result.put("allowedVidyoReplayRecorderList", vidyoRecorder2);
			result.put("allowedVidyoReplayList", vidyoReplay2);
			String installs = licenseDatas.get("Installs");
			String seats = licenseDatas.get("Seats");
			String ports = licenseDatas.get("Ports");
			String limitTypeExecutiveSystem = licenseDatas
					.get("LimitTypeExecutiveSystem");
			String limitTypePanoramaSystem = licenseDatas
					.get("LimitTypePanoramaSystem");
			result.put("numOfInstalls", installs);
			result.put("numOfSeats", seats);
			result.put("numOfLines", ports);
			result.put("numOfExecutives", limitTypeExecutiveSystem);
			result.put("numOfPanoramas", limitTypePanoramaSystem);
			JSONArray locationTags2 = new JSONArray();
			if (!CommonUtil.isNullOrEmpty(locationTags)) {
				for (LocationTagVO item : locationTags) {
					locationTags2.add(item.toJson());
				}
			}
			result.put("allowedLocationTagList", locationTags2);
			JSONArray tenants2 = new JSONArray();
			if (!CommonUtil.isNullOrEmpty(tenants)) {
				for (Tenant4SuperVO item : tenants) {
					if (tenant != null
							&& tenant.getName().equalsIgnoreCase(
									item.getTenant().getTenantName())) {
						continue;
					}
					tenants2.add(item.toJson());
				}
			}
			result.put("allowedTenantList", tenants2);
			return result;
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
		String portalId = params.get("portalId");
		String tenantId = params.get("tenantId");

		if (CommonUtil.isNullOrEmpty(portalId)) {
			return false;
		}
		try {
			long portalId2 = Long.parseLong(portalId);
			if (portalId2 <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
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
		return true;
	}

	protected boolean isOperatorRoleRequired() {
		return true;
	}

}
