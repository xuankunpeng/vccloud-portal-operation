package com.vccloud.portal.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.db.model.TExtVidyoMember;
import com.vccloud.portal.db.model.TExtVidyoMemberExample;
import com.vccloud.portal.db.model.TExtVidyoOperationAccount;
import com.vccloud.portal.db.model.TExtVidyoOperationAccountExample;
import com.vccloud.portal.db.model.TExtVidyoPortal;
import com.vccloud.portal.db.model.TExtVidyoTenant;
import com.vccloud.portal.db.model.TExtVidyoTenantExample;
import com.vccloud.portal.db.model.TPortalInfo;
import com.vccloud.portal.db.model.TPortalInfoExample;
import com.vccloud.portal.exception.AuthnFailedException;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.DuplicateMemberDisplayNameException;
import com.vccloud.portal.exception.DuplicateMemberEmailException;
import com.vccloud.portal.exception.DuplicateMemberExtensionException;
import com.vccloud.portal.exception.DuplicateMemberNameException;
import com.vccloud.portal.exception.FileNotFoundException;
import com.vccloud.portal.exception.ImportExcleDataException;
import com.vccloud.portal.exception.OperationRejectedException;
import com.vccloud.portal.exception.PasswordNotMatchesException;
import com.vccloud.portal.exception.RoleNameException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.UserService;
import com.vccloud.portal.util.AnalyExcleUtil;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.vo.MemberVO;
import com.vccloud.portal.vo.TenantAndPortalInfoVO;
import com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceUtil;

public class UserServiceImpl extends ServiceDefault implements UserService {

	@SuppressWarnings("unchecked")
	public TExtVidyoOperationAccount signInByOperator(String name,
			String password) throws ServiceException {
		TExtVidyoOperationAccountExample example = new TExtVidyoOperationAccountExample();
		example.createCriteria().andUserNameEqualTo(name);
		List<TExtVidyoOperationAccount> accounts = (List<TExtVidyoOperationAccount>) tExtVidyoOperationAccountDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(accounts)) {
			for (TExtVidyoOperationAccount account : accounts) {
				if (password.equals(account.getUserPassword())) {
					return account;
				}
			}
		}
		throw new AuthnFailedException("");
	}

	@SuppressWarnings("unchecked")
	public TenantAndPortalInfoVO signInByTenant(String adminName,
			String adminPassword, String portalUrl) throws ServiceException {
		TPortalInfoExample example = new TPortalInfoExample();
		example.createCriteria().andAdminNameEqualTo(adminName);
		List<TPortalInfo> portalInfos = (List<TPortalInfo>) tPortalInfoDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(portalInfos)) {
			TPortalInfo portalInfo = null;
			for (TPortalInfo item : portalInfos) {
				if (adminPassword.equals(item.getAdminPassword())
						&& portalUrl.equalsIgnoreCase(item.getPortalUrl())) {
					portalInfo = item;
					break;
				}
			}
			if (portalInfo != null) {
				TExtVidyoTenantExample example2 = new TExtVidyoTenantExample();
				example2.createCriteria().andUrlEqualTo(portalUrl);
				List<TExtVidyoTenant> tenants = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
						.selectByExample(example2);
				if (!CommonUtil.isNullOrEmpty(tenants)) {
					TExtVidyoTenant tenant = tenants.get(0);
					TenantAndPortalInfoVO vo = new TenantAndPortalInfoVO();
					vo.setPortalInfo(portalInfo);
					vo.setTenant(tenant);
					return vo;
				}
			}
		}
		throw new AuthnFailedException("");
	}

	@SuppressWarnings("unchecked")
	public TExtVidyoMember signInByMember(String name, String password,
			String portalUrl) throws ServiceException {
		TExtVidyoMemberExample example = new TExtVidyoMemberExample();
		example.createCriteria().andNameEqualTo(name);
		List<TExtVidyoMember> members = (List<TExtVidyoMember>) tExtVidyoMemberDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(members)) {
			TExtVidyoMember member = null;
			for (TExtVidyoMember item : members) {
				if (password.equals(item.getPassword())) {
					member = item;
					break;
				}
			}
			if (member != null) {
				TExtVidyoTenant tenant = tExtVidyoTenantDAO
						.selectByPrimaryKey(member.getTenantId());
				if (tenant != null) {
					if (portalUrl.equalsIgnoreCase(tenant.getUrl())) {
						return member;
					}
				}
			}
		}
		throw new AuthnFailedException("");
	}

	public List<MemberVO> searchMembers(long tenantId, String name,
			int pageSize, int pageNo) throws ServiceException {
		List<MemberVO> result = new ArrayList<MemberVO>();
		int startIndex = pageNo * pageSize;
		List<TExtVidyoMember> list = tExtVidyoMemberDAO.searchMembers(tenantId,
				name, startIndex, pageSize);
		if (CommonUtil.isNullOrEmpty(list)) {
			return result;
		}
		for (TExtVidyoMember item : list) {
			MemberVO vo = new MemberVO();
			vo.setVidyoMember(item);
			result.add(vo);
		}
		return result;
	}

	public int countMembers(long tenantId, String name) throws ServiceException {
		return tExtVidyoMemberDAO.countMembers(tenantId, name);
	}

	public boolean updateMemberPasswordByTenant(long tenantId, long memberId,
			String newPassword) throws ServiceException {
		Integer memberId2 = Integer.valueOf(memberId + "");
		TExtVidyoMember member = tExtVidyoMemberDAO
				.selectByPrimaryKey(memberId2);
		if (member == null || member.getTenantId() == null) {
			throw new OperationRejectedException("");
		}
		if (tenantId != member.getTenantId().longValue()) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenant tenant = tExtVidyoTenantDAO.selectByPrimaryKey(member
				.getTenantId());
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		member.setPassword(newPassword);
		tExtVidyoMemberDAO.updateByPrimaryKeySelective(member);

		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member[] members = VidyoPortalAdminServiceUtil
				.getMembers(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword(), null);
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member member2 = getMatchedMember(
				members, member);
		boolean b = VidyoPortalAdminServiceUtil.updateMemberPassword(portalURL,
				tenant.getAdminName(), tenant.getAdminPassword(), newPassword,
				member2);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean updateMemberDisplayNameByTenant(long tenantId,
			long memberId, String displayName) throws ServiceException {
		Integer memberId2 = Integer.valueOf(memberId + "");
		TExtVidyoMember member = tExtVidyoMemberDAO
				.selectByPrimaryKey(memberId2);
		if (member == null || member.getTenantId() == null) {
			throw new OperationRejectedException("");
		}
		if (tenantId != member.getTenantId().longValue()) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenant tenant = tExtVidyoTenantDAO.selectByPrimaryKey(member
				.getTenantId());
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		member.setDisplayname(displayName);
		tExtVidyoMemberDAO.updateByPrimaryKeySelective(member);
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member[] members = VidyoPortalAdminServiceUtil
				.getMembers(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword(), null);
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member member2 = getMatchedMember(
				members, member);
		boolean b = VidyoPortalAdminServiceUtil.updateMemberDisplayName(
				portalURL, tenant.getAdminName(), tenant.getAdminPassword(),
				displayName, member2);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return b;
	}

	public boolean updateMemberPassword(long memberId, String password,
			String newPassword) throws ServiceException {
		Integer memberId2 = Integer.valueOf(memberId + "");
		TExtVidyoMember member = tExtVidyoMemberDAO
				.selectByPrimaryKey(memberId2);
		if (member == null || member.getTenantId() == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenant tenant = tExtVidyoTenantDAO.selectByPrimaryKey(member
				.getTenantId());
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		if (!password.equals(member.getPassword())) {
			throw new PasswordNotMatchesException("");
		}
		member.setPassword(newPassword);
		tExtVidyoMemberDAO.updateByPrimaryKeySelective(member);

		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member[] members = VidyoPortalAdminServiceUtil
				.getMembers(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword(), null);
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member member2 = getMatchedMember(
				members, member);
		boolean b = VidyoPortalAdminServiceUtil.updateMemberPassword(portalURL,
				tenant.getAdminName(), tenant.getAdminPassword(), newPassword,
				member2);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean updateMemberDisplayName(long memberId, String displayName)
			throws ServiceException {
		Integer memberId2 = Integer.valueOf(memberId + "");
		TExtVidyoMember member = tExtVidyoMemberDAO
				.selectByPrimaryKey(memberId2);
		if (member == null || member.getTenantId() == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenant tenant = tExtVidyoTenantDAO.selectByPrimaryKey(member
				.getTenantId());
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		member.setDisplayname(displayName);
		tExtVidyoMemberDAO.updateByPrimaryKeySelective(member);
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member[] members = VidyoPortalAdminServiceUtil
				.getMembers(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword(), null);
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member member2 = getMatchedMember(
				members, member);
		boolean b = VidyoPortalAdminServiceUtil.updateMemberDisplayName(
				portalURL, tenant.getAdminName(), tenant.getAdminPassword(),
				displayName, member2);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean updateTenantPassword(long tenantId, String password,
			String newPassword) throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null || CommonUtil.isNullOrEmpty(tenant.getUrl())) {
			throw new OperationRejectedException("");
		}
		TPortalInfoExample example = new TPortalInfoExample();
		example.createCriteria().andPortalUrlEqualTo(tenant.getUrl());
		List<TPortalInfo> portalInfos = (List<TPortalInfo>) tPortalInfoDAO
				.selectByExample(example);
		if (CommonUtil.isNullOrEmpty(portalInfos)) {
			throw new OperationRejectedException("");
		}
		TPortalInfo portalInfo = portalInfos.get(0);
		if (!password.equals(portalInfo.getAdminPassword())) {
			throw new PasswordNotMatchesException("");
		}
		Date now = new Date();
		portalInfo.setAdminPassword(newPassword);
		portalInfo.setUpdateTime(now);
		tPortalInfoDAO.updateByPrimaryKeySelective(portalInfo);
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean updateTenantDisplayName(long tenantId, String displayName)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null || CommonUtil.isNullOrEmpty(tenant.getUrl())) {
			throw new OperationRejectedException("");
		}
		TPortalInfoExample example = new TPortalInfoExample();
		example.createCriteria().andPortalUrlEqualTo(tenant.getUrl());
		List<TPortalInfo> portalInfos = (List<TPortalInfo>) tPortalInfoDAO
				.selectByExample(example);
		if (CommonUtil.isNullOrEmpty(portalInfos)) {
			throw new OperationRejectedException("");
		}
		TPortalInfo portalInfo = portalInfos.get(0);
		Date now = new Date();
		portalInfo.setDisplayName(displayName);
		portalInfo.setUpdateTime(now);
		tPortalInfoDAO.updateByPrimaryKeySelective(portalInfo);
		return true;
	}

	public TExtVidyoMember getMember(long memberId) throws ServiceException {
		return tExtVidyoMemberDAO.selectByPrimaryKey(new Long(memberId)
				.intValue());
	}

	public TExtVidyoTenant getTenant(long tenantId) throws ServiceException {
		return tExtVidyoTenantDAO.selectByPrimaryKey(tenantId);
	}

	@SuppressWarnings("unchecked")
	public boolean createMember(long tenantId, String name, String password,
			String displayName, String email, String extension,
			String groupName, String proxyName, String locationTag,
			String language, String description, String roleName)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoMemberExample example = new TExtVidyoMemberExample();
		example.createCriteria().andTenantIdEqualTo(tenantId).andNameEqualTo(
				name);
		List<TExtVidyoMember> test = (List<TExtVidyoMember>) tExtVidyoMemberDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			throw new DuplicateMemberNameException("");
		}
		example = new TExtVidyoMemberExample();
		example.createCriteria().andTenantIdEqualTo(tenantId)
				.andDisplaynameEqualTo(displayName);
		test = (List<TExtVidyoMember>) tExtVidyoMemberDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			throw new DuplicateMemberDisplayNameException("");
		}
		example = new TExtVidyoMemberExample();
		example.createCriteria().andTenantIdEqualTo(tenantId).andEmailEqualTo(
				email);
		test = (List<TExtVidyoMember>) tExtVidyoMemberDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			throw new DuplicateMemberEmailException("");
		}
		example = new TExtVidyoMemberExample();
		example.createCriteria().andTenantIdEqualTo(tenantId)
				.andExtensionEqualTo(extension);
		test = (List<TExtVidyoMember>) tExtVidyoMemberDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			throw new DuplicateMemberExtensionException("");
		}

		TExtVidyoMember member = new TExtVidyoMember();
		member.setAllowcalldirect(true);
		member.setAllowpersonalmeeting(true);
		member.setDescription(description);
		member.setDisplayname(displayName);
		member.setEmail(email);
		member.setExtension(extension);
		member.setGroupname(groupName);
		member.setLanguage(language);
		member.setLocationtag(locationTag);
		member.setName(name);
		member.setPassword(password);
		member.setProxyname(proxyName);
		member.setRolename(roleName);
		member.setTenantId(tenantId);
		tExtVidyoMemberDAO.insertSelective(member);

		String portalURL = getRealPortalURL(tenant.getPortalId());
		boolean b = VidyoPortalAdminServiceUtil.addMember(portalURL, tenant
				.getAdminName(), tenant.getAdminPassword(), name, password,
				displayName, email, extension, groupName, proxyName,
				locationTag, language, description, roleName);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean updateMember(int memberId, String displayName, String email,
			String extension, String groupName, String proxyName,
			String locationTag, String language, String description,
			String roleName) throws ServiceException {
		TExtVidyoMember member = tExtVidyoMemberDAO
				.selectByPrimaryKey(memberId);
		if (member == null) {
			throw new OperationRejectedException("");
		}
		Long tenantId = member.getTenantId();
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoMemberExample example = new TExtVidyoMemberExample();
		example.createCriteria().andTenantIdEqualTo(tenantId)
				.andDisplaynameEqualTo(displayName);
		List<TExtVidyoMember> test = (List<TExtVidyoMember>) tExtVidyoMemberDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			if (test.size() > 1) {
				throw new DuplicateMemberDisplayNameException("");
			} else if (test.get(0).getId() != memberId) {
				throw new DuplicateMemberDisplayNameException("");
			}
		}
		example = new TExtVidyoMemberExample();
		example.createCriteria().andTenantIdEqualTo(tenantId).andEmailEqualTo(
				email);
		test = (List<TExtVidyoMember>) tExtVidyoMemberDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			if (test.size() > 1) {
				throw new DuplicateMemberEmailException("");
			} else if (test.get(0).getId() != memberId) {
				throw new DuplicateMemberEmailException("");
			}
		}
		example = new TExtVidyoMemberExample();
		example.createCriteria().andTenantIdEqualTo(tenantId)
				.andExtensionEqualTo(extension);
		test = (List<TExtVidyoMember>) tExtVidyoMemberDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			if (test.size() > 1) {
				throw new DuplicateMemberExtensionException("");
			} else if (test.get(0).getId() != memberId) {
				throw new DuplicateMemberExtensionException("");
			}
		}

		member.setDescription(description);
		member.setDisplayname(displayName);
		member.setEmail(email);
		member.setExtension(extension);
		member.setGroupname(groupName);
		member.setLanguage(language);
		member.setLocationtag(locationTag);
		member.setProxyname(proxyName);
		member.setRolename(roleName);
		tExtVidyoMemberDAO.updateByPrimaryKeySelective(member);

		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member[] members = VidyoPortalAdminServiceUtil
				.getMembers(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword(), null);
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member member2 = getMatchedMember(
				members, member);
		if (member2 == null) {
			throw new OperationRejectedException("");
		}
		boolean b = VidyoPortalAdminServiceUtil.updateMember(portalURL, tenant
				.getAdminName(), tenant.getAdminPassword(), displayName, email,
				extension, groupName, proxyName, locationTag, language,
				description, roleName, member2);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean deleteMember(int memberId) throws ServiceException {
		TExtVidyoMember member = tExtVidyoMemberDAO
				.selectByPrimaryKey(memberId);
		if (member == null) {
			throw new OperationRejectedException("");
		}
		Long tenantId = member.getTenantId();
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}

		tExtVidyoMemberDAO.deleteByPrimaryKey(memberId);

		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member[] members = VidyoPortalAdminServiceUtil
				.getMembers(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword(), null);
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member member2 = getMatchedMember(
				members, member);
		if (member2 == null) {
			throw new OperationRejectedException("");
		}
		boolean b = VidyoPortalAdminServiceUtil.deleteMember(portalURL, tenant
				.getAdminName(), tenant.getAdminPassword(), member2
				.getMemberID().getEntityID());
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean updateMemberPasswordByOperator(int memberId,
			String newPassword) throws ServiceException {
		TExtVidyoMember member = tExtVidyoMemberDAO
				.selectByPrimaryKey(memberId);
		if (member == null || member.getTenantId() == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenant tenant = tExtVidyoTenantDAO.selectByPrimaryKey(member
				.getTenantId());
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		member.setPassword(newPassword);
		tExtVidyoMemberDAO.updateByPrimaryKeySelective(member);

		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member[] members = VidyoPortalAdminServiceUtil
				.getMembers(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword(), null);
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member member2 = getMatchedMember(
				members, member);
		if (member2 == null) {
			throw new OperationRejectedException("");
		}
		boolean b = VidyoPortalAdminServiceUtil.updateMemberPassword(portalURL,
				tenant.getAdminName(), tenant.getAdminPassword(), newPassword,
				member2);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	private String getRealPortalURL(long portalId) throws ServiceException {
		TExtVidyoPortal portal = tExtVidyoPortalDAO
				.selectByPrimaryKey(portalId);
		if (portal == null) {
			return null;
		}
		return portal.getPortalurl();
	}

	private com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member getMatchedMember(
			com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member[] members,
			TExtVidyoMember member) {
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member result = null;
		if (!CommonUtil.isNullOrEmpty(members)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member item : members) {
				if (member.getName().equalsIgnoreCase(item.getName())) {
					result = item;
					break;
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public void importMember(long operatorId, DiskFileItem item, long tenantId,
			String extensionPrefix) throws ServiceException {
		String dirPath = AnalyExcleUtil.getPath();
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
		String str = sdf.format(d);
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dirPath + "\\" + str);
		System.out.println(file.getAbsolutePath());
		if (item.get().length == 0) {
			throw new FileNotFoundException("");
		}
		try {
			// System.out.println(item.get().length);
			item.write(file);

		} catch (Exception e) {
			e.printStackTrace();
		}
		TExtVidyoMember[] member = AnalyExcleUtil.analyExcle(file);
		if (member.length == 0) {
			throw new FileNotFoundException("");
		}
		Set set1 = new HashSet();
		List li = new ArrayList();
		for (TExtVidyoMember mer : member) {
			System.out.println(mer.getRolename());
			if (mer.getRolename().equals("Admin")
					|| mer.getRolename().equals("Operator")
					|| mer.getRolename().equals("Normal")
					|| mer.getRolename().equals("VidyoRoom")
					|| mer.getRolename().equals("Executive")
					|| mer.getRolename().equals("VidyoPanorama")) {

				if (mer.getExtension() != "" && mer.getDisplayname() != ""
						&& mer.getName() != "") {
					set1.add(mer.getExtension());
					set1.add(mer.getDisplayname());
					set1.add(mer.getName());
				}
				if (mer.getPassword() != "" && mer.getLanguage() != ""
						&& mer.getEmail() != "" && mer.getGroupname() != ""
						&& mer.getLocationtag() != ""
						&& mer.getRolename() != "") {
					li.add(mer.getPassword());
					li.add(mer.getLanguage());
					li.add(mer.getEmail());
					li.add(mer.getGroupname());
					li.add(mer.getLocationtag());
					li.add(mer.getRolename());
				}
			} else {
				throw new RoleNameException("");
			}

		}
		if (member.length * 3 == set1.size() && member.length * 6 == li.size()) {
			for (TExtVidyoMember mer : member) {
				TExtVidyoTenant tenant = tExtVidyoTenantDAO
						.selectByPrimaryKey(tenantId);
				if (tenant == null) {
					throw new OperationRejectedException("");
				}
				TExtVidyoMemberExample example = new TExtVidyoMemberExample();
				example.createCriteria().andTenantIdEqualTo(tenantId)
						.andNameEqualTo(mer.getName());
				List<TExtVidyoMember> test = (List<TExtVidyoMember>) tExtVidyoMemberDAO
						.selectByExample(example);
				if (!CommonUtil.isNullOrEmpty(test)) {
					throw new DuplicateMemberNameException("");
				}
				example = new TExtVidyoMemberExample();
				example.createCriteria().andTenantIdEqualTo(tenantId)
						.andDisplaynameEqualTo(mer.getDisplayname());
				test = (List<TExtVidyoMember>) tExtVidyoMemberDAO
						.selectByExample(example);
				if (!CommonUtil.isNullOrEmpty(test)) {
					throw new DuplicateMemberDisplayNameException("");
				}
				example = new TExtVidyoMemberExample();
				example.createCriteria().andTenantIdEqualTo(tenantId)
						.andExtensionEqualTo(mer.getExtension());
				test = (List<TExtVidyoMember>) tExtVidyoMemberDAO
						.selectByExample(example);
				if (!CommonUtil.isNullOrEmpty(test)) {
					throw new DuplicateMemberExtensionException("");
				}
				TExtVidyoMember Member = new TExtVidyoMember();
				Member.setAllowcalldirect(true);
				Member.setAllowpersonalmeeting(true);
				Member.setDescription(mer.getDescription());
				Member.setDisplayname(mer.getDisplayname());
				Member.setEmail(mer.getEmail());
				Member.setExtension(extensionPrefix+mer.getExtension());
				Member.setGroupname(mer.getGroupname());
				Member.setLanguage(mer.getLanguage());
				Member.setLocationtag(mer.getLocationtag());
				Member.setName(mer.getName());
				Member.setPassword(mer.getPassword());
				Member.setProxyname(mer.getProxyname());
				Member.setRolename(mer.getRolename());
				Member.setTenantId(tenantId);
				System.out.println("开始插数据了");
				tExtVidyoMemberDAO.insertSelective(Member);

				String portalURL = getRealPortalURL(tenant.getPortalId());
				boolean b = VidyoPortalAdminServiceUtil.addMember(portalURL,
						tenant.getAdminName(), tenant.getAdminPassword(), mer
								.getName(), mer.getPassword(), mer
								.getDisplayname(), mer.getEmail(), extensionPrefix+mer
								.getExtension(), mer.getGroupname(), mer
								.getProxyname(), mer.getLocationtag(), mer
								.getLanguage(), mer.getDescription(), mer
								.getRolename());
				if (!b) {
					throw new CallVidyoWebServiceException("");
				}
			}
		} else {
			throw new ImportExcleDataException("");
		}
	}

}
