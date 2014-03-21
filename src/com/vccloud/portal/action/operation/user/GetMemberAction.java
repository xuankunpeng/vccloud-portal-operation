package com.vccloud.portal.action.operation.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.action.BaseAction;
import com.vccloud.portal.db.model.TExtVidyoMember;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.ServiceLocator;
import com.vccloud.portal.service.UserService;
import com.vccloud.portal.vo.MemberVO;

public class GetMemberAction extends BaseAction {

	@Override
	public JSONObject doApiAction(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			List<DiskFileItem> diskFiles) throws Throwable {
		String memberId = params.get("memberId");

		try {
			int memberId2 = Integer.parseInt(memberId);
			UserService userService = ServiceLocator.getUserService();
			TExtVidyoMember member = userService.getMember(memberId2);
			JSONObject result = new JSONObject();
			result.put("result", "ok");
			MemberVO vo = new MemberVO();
			if (member != null) {
				vo.setVidyoMember(member);
			}
			result.put("member", vo.toJson3());
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
		String memberId = params.get("memberId");

		try {
			int memberId2 = Integer.parseInt(memberId);
			if (memberId2 <= 0) {
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
