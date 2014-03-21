package com.vccloud.portal.action.operation.vidyo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.constant.Constants;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.vo.RoomVO;

public class SearchRoomsAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String tenantId = params.get("tenantId");
		String pageSize = params.get("pageSize");
		String pageNo = params.get("pageNo");

		try {
			long tenantId2 = Long.parseLong(tenantId);
			int pageSize2 = Constants.SearchProps.DEFAULT_PAGE_SIZE;
			if (!CommonUtil.isNullOrEmpty(pageSize)) {
				pageSize2 = Integer.parseInt(pageSize);
			}
			int pageNo2 = Constants.SearchProps.DEFAULT_PAGE_NO;
			if (!CommonUtil.isNullOrEmpty(pageNo)) {
				pageNo2 = Integer.parseInt(pageNo);
			}
			VidyoService vidyoService = ServiceLocator.getVidyoService();
			List<RoomVO> list = vidyoService.searchRooms(tenantId2);
			JSONObject result = new JSONObject();
			result.put("result", "ok");
			JSONArray ja = new JSONArray();
			if (!CommonUtil.isNullOrEmpty(list)) {
				int fromIndex = pageNo2 * pageSize2;
				int toIndex = (pageNo2 + 1) * pageSize2 < list.size() ? (pageNo2 + 1)
						* pageSize2
						: list.size();
				if (fromIndex <= toIndex) {
					List<RoomVO> subList = list.subList(fromIndex, toIndex);
					for (RoomVO item : subList) {
						ja.add(item.toJson2());
					}
				}
			}
			result.put("list", ja);
			result.put("count", CommonUtil.isNullOrEmpty(list) ? 0 : list
					.size());
			result.put("pageSize", pageSize2);
			result.put("pageNo", pageNo2);
			result.put("tenantId", tenantId2);
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
		String tenantId = params.get("tenantId");
		String pageSize = params.get("pageSize");
		String pageNo = params.get("pageNo");

		try {
			long tenantId2 = Long.parseLong(tenantId);
			if (tenantId2 <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
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
