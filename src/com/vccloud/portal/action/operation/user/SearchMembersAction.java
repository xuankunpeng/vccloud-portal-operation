package com.vccloud.portal.action.operation.user;

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
import com.vccloud.portal.service.UserService;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.vo.MemberVO;

public class SearchMembersAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String keyword = params.get("keyword");
		String tenantId = params.get("tenantId");
		String pageSize = params.get("pageSize");
		String pageNo = params.get("pageNo");

		try {
			long tenantId2 = 0;
			if (!CommonUtil.isNullOrEmpty(tenantId)) {
				tenantId2 = Long.parseLong(tenantId);
			}
			int pageSize2 = Constants.SearchProps.DEFAULT_PAGE_SIZE;
			if (!CommonUtil.isNullOrEmpty(pageSize)) {
				pageSize2 = Integer.parseInt(pageSize);
			}
			int pageNo2 = Constants.SearchProps.DEFAULT_PAGE_NO;
			if (!CommonUtil.isNullOrEmpty(pageNo)) {
				pageNo2 = Integer.parseInt(pageNo);
			}
			UserService userService = ServiceLocator.getUserService();
			List<MemberVO> list = userService.searchMembers(tenantId2, keyword,
					pageSize2, pageNo2);
			int count = userService.countMembers(tenantId2, keyword);
			JSONObject result = new JSONObject();
			result.put("result", "ok");
			JSONArray ja = new JSONArray();
			if (!CommonUtil.isNullOrEmpty(list)) {
				for (MemberVO item : list) {
					ja.add(item.toJson2());
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
		String tenantId = params.get("tenantId");
		String pageSize = params.get("pageSize");
		String pageNo = params.get("pageNo");

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
