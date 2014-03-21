package com.vccloud.portal.action.operation.vidyo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.constant.Constants;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.vo.CdrVO;

public class SearchCdrsAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String portalId = params.get("portalId");
		String tenantId = params.get("tenantId");
		String memberId = params.get("memberId");
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");
		String pageSize = params.get("pageSize");
		String pageNo = params.get("pageNo");

		try {
			long portalId2 = 0;
			if (!CommonUtil.isNullOrEmpty(portalId)) {
				portalId2 = Long.parseLong(portalId);
			}
			long tenantId2 = 0;
			if (!CommonUtil.isNullOrEmpty(tenantId)) {
				tenantId2 = Long.parseLong(tenantId);
			}
			long memberId2 = 0;
			if (!CommonUtil.isNullOrEmpty(memberId)) {
				memberId2 = Long.parseLong(memberId);
			}
			Date startTime2 = null;
			if (!CommonUtil.isNullOrEmpty(startTime)) {
				startTime2 = CommonUtil.getStart(CommonUtil.getDate(startTime,
						"yyyy-MM-dd"));
			}
			Date endTime2 = null;
			if (!CommonUtil.isNullOrEmpty(endTime)) {
				endTime2 = CommonUtil.getEnd(CommonUtil.getDate(endTime,
						"yyyy-MM-dd"));
			}
			int pageSize2 = Constants.SearchProps.DEFAULT_PAGE_SIZE;
			if (!CommonUtil.isNullOrEmpty(pageSize)) {
				pageSize2 = Integer.parseInt(pageSize);
			}
			int pageNo2 = Constants.SearchProps.DEFAULT_PAGE_NO;
			if (!CommonUtil.isNullOrEmpty(pageNo)) {
				pageNo2 = Integer.parseInt(pageNo);
			}
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			List<CdrVO> list = vidyoService.searchCdrs(portalId2, tenantId2,
					memberId2, startTime2, endTime2, pageSize2, pageNo2);
			int count = vidyoService.countCdrs(portalId2, tenantId2, memberId2,
					startTime2, endTime2);
			JSONObject result = new JSONObject();
			result.put("result", "ok");
			JSONArray ja = new JSONArray();
			if (!CommonUtil.isNullOrEmpty(list)) {
				for (CdrVO item : list) {
					ja.add(item.toJson());
				}
			}
			result.put("list", ja);
			result.put("count", count);
			result.put("pageSize", pageSize2);
			result.put("pageNo", pageNo2);
			return result;
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
		String memberId = params.get("memberId");
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");
		String pageSize = params.get("pageSize");
		String pageNo = params.get("pageNo");

		if (!CommonUtil.isNullOrEmpty(portalId)) {
			try {
				long portalId2 = Long.parseLong(portalId);
				if (portalId2 <= 0) {
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
		if (!CommonUtil.isNullOrEmpty(memberId)) {
			try {
				long memberId2 = Long.parseLong(memberId);
				if (memberId2 <= 0) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (!CommonUtil.isNullOrEmpty(startTime)) {
			Date startTime2 = CommonUtil.getDate(startTime, "yyyy-MM-dd");
			if (startTime2 == null) {
				return false;
			}
		}
		if (!CommonUtil.isNullOrEmpty(endTime)) {
			Date endTime2 = CommonUtil.getDate(endTime, "yyyy-MM-dd");
			if (endTime2 == null) {
				return false;
			}
		}
		if (!CommonUtil.isNullOrEmpty(pageSize)) {
			try {
				int pageSize2 = Integer.parseInt(pageSize);
				if (pageSize2 <= 0) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (!CommonUtil.isNullOrEmpty(pageNo)) {
			try {
				int pageNo2 = Integer.parseInt(pageNo);
				if (pageNo2 < 0) {
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
