package com.vccloud.portal.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.log4j.Logger;

import com.vccloud.portal.constant.Constants;
import com.vccloud.portal.db.model.Conferencecall2;
import com.vccloud.portal.db.model.TExtVidyoMember;
import com.vccloud.portal.db.model.TExtVidyoMemberExample;
import com.vccloud.portal.db.model.TExtVidyoPortal;
import com.vccloud.portal.db.model.TExtVidyoPortalConfig;
import com.vccloud.portal.db.model.TExtVidyoPortalConfigExample;
import com.vccloud.portal.db.model.TExtVidyoPortalExample;
import com.vccloud.portal.db.model.TExtVidyoTenant;
import com.vccloud.portal.db.model.TExtVidyoTenantExample;
import com.vccloud.portal.db.model.TLegacy;
import com.vccloud.portal.db.model.TLegacyExample;
import com.vccloud.portal.db.model.TPortalInfo;
import com.vccloud.portal.db.model.TPortalInfoExample;
import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.exception.DuplicateExtensionPrefixException;
import com.vccloud.portal.exception.DuplicateGroupNameException;
import com.vccloud.portal.exception.DuplicateLegacyNameException;
import com.vccloud.portal.exception.DuplicatePortalNameException;
import com.vccloud.portal.exception.DuplicatePortalUrlException;
import com.vccloud.portal.exception.DuplicateRoomExtensionException;
import com.vccloud.portal.exception.DuplicateRoomNameException;
import com.vccloud.portal.exception.DuplicateTenantNameException;
import com.vccloud.portal.exception.DuplicateTenantURLException;
import com.vccloud.portal.exception.FileSizeExceededException;
import com.vccloud.portal.exception.FileTypeInvalidException;
import com.vccloud.portal.exception.HasMembersLeftException;
import com.vccloud.portal.exception.HasTenantsLeftException;
import com.vccloud.portal.exception.LogoSizeExceededException;
import com.vccloud.portal.exception.OperationRejectedException;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.service.VidyoService;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.util.UploadUtil;
import com.vccloud.portal.util.UploadUtil.IMAGE_TYPE;
import com.vccloud.portal.vo.CdrVO;
import com.vccloud.portal.vo.ComponentDataVO;
import com.vccloud.portal.vo.ConferenceCall2VO;
import com.vccloud.portal.vo.GroupVO;
import com.vccloud.portal.vo.LegacyVO;
import com.vccloud.portal.vo.LocationTagVO;
import com.vccloud.portal.vo.PortalConfigVO;
import com.vccloud.portal.vo.RoomProfileVO;
import com.vccloud.portal.vo.RoomVO;
import com.vccloud.portal.vo.Tenant4SuperExtVO;
import com.vccloud.portal.vo.Tenant4SuperVO;
import com.vccloud.portal.vo.TenantAndPortalConfigVO;
import com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceUtil;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceUtil;

public class VidyoServiceImpl extends ServiceDefault implements VidyoService {

	private Logger logger = Logger.getLogger(VidyoServiceImpl.class);

	public List<ConferenceCall2VO> searchConferenceCall2s(String tenantName,
			String callerName, Date startTime, Date endTime,
			String orderByClause, int pageSize, int pageNo)
			throws ServiceException {
		List<ConferenceCall2VO> result = new ArrayList<ConferenceCall2VO>();
		int startIndex = pageNo * pageSize;
		List<Conferencecall2> list = conferencecall2DAO.searchConferenceCall2s(
				tenantName, callerName, startTime, endTime, orderByClause,
				startIndex, pageSize);
		if (CommonUtil.isNullOrEmpty(list)) {
			return result;
		}
		for (Conferencecall2 item : list) {
			ConferenceCall2VO vo = new ConferenceCall2VO();
			vo.setConferencecall2(item);
			result.add(vo);
		}
		return result;
	}

	public int countConferenceCall2s(String tenantName, String callerName,
			Date startTime, Date endTime) throws ServiceException {
		return conferencecall2DAO.countConferenceCall2s(tenantName, callerName,
				startTime, endTime);
	}

	public void storePortalInfo(String portalUrl, String welcomeWord,
			DiskFileItem item) throws ServiceException {
		String suffix = "";
		if (item != null && item.getSize() > 0) {
			String fileName = item.getName();
			if (CommonUtil.isNullOrEmpty(fileName)) {
				throw new FileTypeInvalidException("");
			}
			String[] arr = fileName.split("\\.");
			if (arr.length > 1) {
				suffix = "." + arr[arr.length - 1].toLowerCase();
			} else {
				throw new FileTypeInvalidException("");
			}
			/**
			 * <pre>
			 * if (!UploadUtil.checkLogoFileSuffix(suffix)) {
			 * 	throw new FileTypeInvalidException(&quot;&quot;);
			 * }
			 * </pre>
			 */
			if (!UploadUtil.checkLogoFileSize(item.getSize())) {
				throw new FileSizeExceededException("");
			}
		}

		TPortalInfo test = getPortalInfo(portalUrl);
		Date now = new Date();
		long id = 0;
		if (test == null) {
			TPortalInfo portalInfo = new TPortalInfo();
			portalInfo.setPortalUrl(portalUrl);
			portalInfo.setWelcomeWord(welcomeWord);
			portalInfo.setCreateTime(now);
			portalInfo.setUpdateTime(now);
			id = tPortalInfoDAO.insertSelective(portalInfo);
		} else {
			TPortalInfo portalInfo = new TPortalInfo();
			portalInfo.setId(test.getId());
			portalInfo.setPortalUrl(portalUrl);
			portalInfo.setWelcomeWord(welcomeWord);
			portalInfo.setUpdateTime(now);
			tPortalInfoDAO.updateByPrimaryKeySelective(portalInfo);
			id = test.getId();
		}
		test = tPortalInfoDAO.selectByPrimaryKey(id);

		if (item != null && item.getSize() > 0) {
			uploadLogoFile(item, id, suffix);
			String logoUrl = UploadUtil.getLogoFileName(id, suffix,
					IMAGE_TYPE.ORIGINAL);
			TPortalInfo portalInfo = new TPortalInfo();
			portalInfo.setId(id);
			portalInfo.setLogoUrl(logoUrl);
			portalInfo.setUpdateTime(now);
			tPortalInfoDAO.updateByPrimaryKeySelective(portalInfo);
		}
	}

	@SuppressWarnings("unchecked")
	public TPortalInfo getPortalInfo(String portalUrl) throws ServiceException {
		TPortalInfoExample example = new TPortalInfoExample();
		example.createCriteria().andPortalUrlEqualTo(portalUrl);
		List<TPortalInfo> list = (List<TPortalInfo>) tPortalInfoDAO
				.selectByExample(example);
		if (CommonUtil.isNullOrEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	public List<RoomVO> searchRooms(long tenantId) throws ServiceException {
		List<RoomVO> result = new ArrayList<RoomVO>();
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			return result;
		}
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room[] rooms = VidyoPortalAdminServiceUtil
				.getRooms(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword());
		if (!CommonUtil.isNullOrEmpty(rooms)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room room : rooms) {
				RoomVO vo = new RoomVO();
				vo.setRoom(room);
				result.add(vo);
			}
		}
		return result;
	}

	public List<RoomVO> searchPublicRooms(long tenantId)
			throws ServiceException {
		List<RoomVO> result = new ArrayList<RoomVO>();
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			return result;
		}
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room[] rooms = VidyoPortalAdminServiceUtil
				.getRooms(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword());
		if (!CommonUtil.isNullOrEmpty(rooms)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room room : rooms) {
				if ("Public".equalsIgnoreCase(room.getRoomType().getValue())) {
					RoomVO vo = new RoomVO();
					vo.setRoom(room);
					result.add(vo);
				}
			}
		}
		return result;
	}

	public List<RoomVO> searchPersonalRooms(long memberId)
			throws ServiceException {
		List<RoomVO> result = new ArrayList<RoomVO>();
		int memberId2 = new Long(memberId).intValue();
		TExtVidyoMember member = tExtVidyoMemberDAO
				.selectByPrimaryKey(memberId2);
		if (member == null) {
			return result;
		}
		TExtVidyoTenant tenant = tExtVidyoTenantDAO.selectByPrimaryKey(member
				.getTenantId());
		if (tenant == null) {
			return result;
		}
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room[] rooms = VidyoPortalAdminServiceUtil
				.getRooms(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword());
		if (!CommonUtil.isNullOrEmpty(rooms)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room room : rooms) {
				if ("Personal".equalsIgnoreCase(room.getRoomType().getValue())
						&& member.getName().equalsIgnoreCase(
								room.getOwnerName())) {
					RoomVO vo = new RoomVO();
					vo.setRoom(room);
					result.add(vo);
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public String getVidyoPortalURL(String userPortalURL)
			throws ServiceException {
		if (CommonUtil.isNullOrEmpty(userPortalURL)) {
			return null;
		}
		TExtVidyoTenantExample example = new TExtVidyoTenantExample();
		example.createCriteria().andUrlEqualTo(userPortalURL);
		List<TExtVidyoTenant> tenants = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(example);
		if (CommonUtil.isNullOrEmpty(tenants)) {
			return null;
		}
		TExtVidyoTenant tenant = tenants.get(0);
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			return null;
		}
		return portal.getPortalurl();
	}

	public boolean updateRoomName(long tenantId, long roomId, String name)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		int roomId2 = new Long(roomId).intValue();
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room room = null;
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room[] rooms = VidyoPortalAdminServiceUtil
				.getRooms(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword());
		if (!CommonUtil.isNullOrEmpty(rooms)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room item : rooms) {
				if (name.equalsIgnoreCase(item.getName())
						&& roomId != item.getRoomID().getEntityID()) {
					throw new DuplicateRoomNameException("");
				}
				if (roomId2 == item.getRoomID().getEntityID()) {
					room = item;
				}
			}
		}
		if (room == null) {
			throw new OperationRejectedException("");
		}
		boolean b = VidyoPortalAdminServiceUtil.updateRoomName(portalURL,
				tenant.getAdminName(), tenant.getAdminPassword(), name, room);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean removeRoomPin(long tenantId, long roomId)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		int roomId2 = new Long(roomId).intValue();
		String portalURL = getRealPortalURL(tenant.getPortalId());
		boolean b = VidyoPortalAdminServiceUtil.removeRoomPIN(portalURL, tenant
				.getAdminName(), tenant.getAdminPassword(), roomId2);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean createRoomPin(long tenantId, long roomId, String pinCode)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		int roomId2 = new Long(roomId).intValue();
		String portalURL = getRealPortalURL(tenant.getPortalId());
		boolean b = VidyoPortalAdminServiceUtil.createRoomPIN(portalURL, tenant
				.getAdminName(), tenant.getAdminPassword(), roomId2, pinCode);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean clearRoom(long tenantId, long roomId)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		int roomId2 = new Long(roomId).intValue();
		String portalURL = getRealPortalURL(tenant.getPortalId());

		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Entity_type0[] participants = VidyoPortalAdminServiceUtil
				.getParticipants(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword(), roomId2);
		if (!CommonUtil.isNullOrEmpty(participants)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Entity_type0 item : participants) {
				try {
					VidyoPortalAdminServiceUtil.leaveConference(portalURL,
							tenant.getAdminName(), tenant.getAdminPassword(),
							roomId2, item.getParticipantID().getEntityID());
				} catch (Exception e) {
					// Continue! Maybe some participant has already left the
					// conference.
				}
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean addLegacy(String url, String name, String extension)
			throws ServiceException {
		TLegacyExample tLegacyExample = new TLegacyExample();
		tLegacyExample.createCriteria().andUrlEqualTo(url)
				.andLegacyNameEqualTo(name);
		List<TLegacy> test = (List<TLegacy>) tLegacyDAO
				.selectByExample(tLegacyExample);
		if (!CommonUtil.isNullOrEmpty(test)) {
			throw new DuplicateLegacyNameException("");
		}
		TExtVidyoTenantExample tExtVidyoTenantExample = new TExtVidyoTenantExample();
		tExtVidyoTenantExample.createCriteria().andUrlEqualTo(url);
		List<TExtVidyoTenant> tenants = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(tExtVidyoTenantExample);
		if (CommonUtil.isNullOrEmpty(tenants)) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenant tenant = tenants.get(0);
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			throw new OperationRejectedException("");
		}

		TLegacy legacy = new TLegacy();
		Date now = new Date();
		legacy.setUrl(url);
		legacy.setLegacyName(name);
		legacy.setLegacyExtension(extension);
		legacy.setRoomId("");
		legacy.setCreateTime(now);
		legacy.setUpdateTime(now);
		tLegacyDAO.insertSelective(legacy);

		String adminName = tenant.getAdminName();
		String adminPassword = tenant.getAdminPassword();
		String portalurl = portal.getPortalurl();
		boolean b = VidyoPortalAdminServiceUtil.addLegacy(portalurl, adminName,
				adminPassword, name, extension);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean updateLegacy(long id, String name, String extension)
			throws ServiceException {
		TLegacy legacy = tLegacyDAO.selectByPrimaryKey(id);
		if (legacy == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenantExample tExtVidyoTenantExample = new TExtVidyoTenantExample();
		tExtVidyoTenantExample.createCriteria().andUrlEqualTo(legacy.getUrl());
		List<TExtVidyoTenant> tenants = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(tExtVidyoTenantExample);
		if (CommonUtil.isNullOrEmpty(tenants)) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenant tenant = tenants.get(0);
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			throw new OperationRejectedException("");
		}
		String adminName = tenant.getAdminName();
		String adminPassword = tenant.getAdminPassword();
		String portalurl = portal.getPortalurl();
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member member = getLegacy(
				portalurl, adminName, adminPassword, name);
		if (member == null) {
			throw new OperationRejectedException("");
		}

		Date now = new Date();
		legacy.setLegacyExtension(extension);
		legacy.setUpdateTime(now);
		tLegacyDAO.updateByPrimaryKeySelective(legacy);

		member.setExtension(extension);
		boolean b = VidyoPortalAdminServiceUtil.updateLegacy(portalurl,
				adminName, adminPassword, member.getMemberID(), member);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<LegacyVO> getLegacies(String url, String roomId)
			throws ServiceException {
		List<LegacyVO> result = new ArrayList<LegacyVO>();
		TLegacyExample example = new TLegacyExample();
		example.createCriteria().andUrlEqualTo(url).andRoomIdEqualTo(roomId);
		List<TLegacy> list = (List<TLegacy>) tLegacyDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(list)) {
			for (TLegacy item : list) {
				LegacyVO vo = new LegacyVO();
				vo.setLegacy(item);
				result.add(vo);
			}
		}
		return result;
	}

	public boolean assignLegacies(String url, String roomId,
			List<Long> legacyIds) throws ServiceException {
		Date now = new Date();
		TLegacyExample example = new TLegacyExample();
		example.createCriteria().andUrlEqualTo(url).andRoomIdEqualTo(roomId);
		TLegacy legacy = new TLegacy();
		legacy.setRoomId("");
		legacy.setUpdateTime(now);
		tLegacyDAO.updateByExampleSelective(legacy, example);
		if (!CommonUtil.isNullOrEmpty(legacyIds)) {
			example = new TLegacyExample();
			example.createCriteria().andIdIn(legacyIds);
			legacy = new TLegacy();
			legacy.setRoomId(roomId);
			legacy.setUpdateTime(now);
			tLegacyDAO.updateByExampleSelective(legacy, example);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean deleteLegacy(long id) throws ServiceException {
		TLegacy legacy = tLegacyDAO.selectByPrimaryKey(id);
		if (legacy == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenantExample tExtVidyoTenantExample = new TExtVidyoTenantExample();
		tExtVidyoTenantExample.createCriteria().andUrlEqualTo(legacy.getUrl());
		List<TExtVidyoTenant> tenants = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(tExtVidyoTenantExample);
		if (CommonUtil.isNullOrEmpty(tenants)) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenant tenant = tenants.get(0);
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			throw new OperationRejectedException("");
		}
		String adminName = tenant.getAdminName();
		String adminPassword = tenant.getAdminPassword();
		String portalurl = portal.getPortalurl();
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member member = getLegacy(
				portalurl, adminName, adminPassword, legacy.getLegacyName());
		if (member == null) {
			throw new OperationRejectedException("");
		}
		tLegacyDAO.deleteByPrimaryKey(id);

		boolean b = VidyoPortalAdminServiceUtil.deleteMember(portalurl,
				adminName, adminPassword, member.getMemberID().getEntityID());
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public List<LegacyVO> searchLegacies(long tenantId, String keyword,
			int pageSize, int pageNo) throws ServiceException {
		List<LegacyVO> result = new ArrayList<LegacyVO>();
		int startIndex = pageNo * pageSize;
		List<TLegacy> list = tLegacyDAO.searchLegacies(tenantId, keyword,
				startIndex, pageSize);
		if (CommonUtil.isNullOrEmpty(list)) {
			return result;
		}
		for (TLegacy item : list) {
			LegacyVO vo = new LegacyVO();
			vo.setLegacy(item);
			result.add(vo);
		}
		return result;
	}

	public int countLegacies(long tenantId, String keyword)
			throws ServiceException {
		return tLegacyDAO.countLegacies(tenantId, keyword);
	}

	public RoomProfileVO getRoomProfile(long tenantId, int roomId)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			throw new OperationRejectedException("");
		}
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.EntityID roomID2 = new com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.EntityID();
		roomID2.setEntityID(roomId);
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.RoomProfile roomProfile = VidyoPortalAdminServiceUtil
				.getRoomProfile(portal.getPortalurl(), tenant.getAdminName(),
						tenant.getAdminPassword(), roomID2);
		RoomProfileVO vo = new RoomProfileVO();
		vo.setRoomProfile(roomProfile);
		return vo;
	}

	public boolean setRoomProfile(long tenantId, int roomId,
			String roomProfileName) throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			throw new OperationRejectedException("");
		}
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.EntityID roomID2 = new com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.EntityID();
		roomID2.setEntityID(roomId);
		boolean b = VidyoPortalAdminServiceUtil.setRoomProfile(portal
				.getPortalurl(), tenant.getAdminName(), tenant
				.getAdminPassword(), roomID2, roomProfileName);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean removeRoomProfile(long tenantId, int roomId)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			throw new OperationRejectedException("");
		}
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.EntityID roomID2 = new com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.EntityID();
		roomID2.setEntityID(roomId);
		boolean b = VidyoPortalAdminServiceUtil.removeRoomProfile(portal
				.getPortalurl(), tenant.getAdminName(), tenant
				.getAdminPassword(), roomID2);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean resetRoomURL(long tenantId, int roomId)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			throw new OperationRejectedException("");
		}
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.EntityID roomID2 = new com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.EntityID();
		roomID2.setEntityID(roomId);
		boolean b = VidyoPortalAdminServiceUtil.removeRoomURL(portal
				.getPortalurl(), tenant.getAdminName(), tenant
				.getAdminPassword(), roomID2);
		boolean b2 = VidyoPortalAdminServiceUtil.createRoomURL(portal
				.getPortalurl(), tenant.getAdminName(), tenant
				.getAdminPassword(), roomID2);
		if (!b || !b2) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public PortalConfigVO storePortal(long id, String portalName,
			String portalUrl, String superName, String superPassword)
			throws ServiceException {
		if (id > 0) {
			TExtVidyoPortalConfigExample example = new TExtVidyoPortalConfigExample();
			example.createCriteria().andPortalNameEqualTo(portalName);
			List<TExtVidyoPortalConfig> test = (List<TExtVidyoPortalConfig>) tExtVidyoPortalConfigDAO
					.selectByExample(example);
			for (TExtVidyoPortalConfig item : test) {
				if (item.getId() != id) {
					throw new DuplicatePortalNameException("");
				}
			}
			example = new TExtVidyoPortalConfigExample();
			example.createCriteria().andPortalUrlEqualTo(portalUrl);
			test = (List<TExtVidyoPortalConfig>) tExtVidyoPortalConfigDAO
					.selectByExample(example);
			for (TExtVidyoPortalConfig item : test) {
				if (item.getId() != id) {
					throw new DuplicatePortalUrlException("");
				}
			}

			TExtVidyoPortalConfig config = new TExtVidyoPortalConfig();
			config.setPortalName(portalName);
			config.setPortalUrl(portalUrl);
			config.setSuperName(superName);
			config.setSuperPassword(superPassword);
			config.setId(id);
			tExtVidyoPortalConfigDAO.updateByPrimaryKeySelective(config);
		} else {
			TExtVidyoPortalConfigExample example = new TExtVidyoPortalConfigExample();
			example.createCriteria().andPortalNameEqualTo(portalName);
			List<TExtVidyoPortalConfig> test = (List<TExtVidyoPortalConfig>) tExtVidyoPortalConfigDAO
					.selectByExample(example);
			if (!CommonUtil.isNullOrEmpty(test)) {
				throw new DuplicatePortalNameException("");
			}
			example = new TExtVidyoPortalConfigExample();
			example.createCriteria().andPortalUrlEqualTo(portalUrl);
			test = (List<TExtVidyoPortalConfig>) tExtVidyoPortalConfigDAO
					.selectByExample(example);
			if (!CommonUtil.isNullOrEmpty(test)) {
				throw new DuplicatePortalUrlException("");
			}

			TExtVidyoPortalConfig config = new TExtVidyoPortalConfig();
			config.setPortalName(portalName);
			config.setPortalUrl(portalUrl);
			config.setSuperName(superName);
			config.setSuperPassword(superPassword);
			id = tExtVidyoPortalConfigDAO.insertSelective(config);
		}
		return getPortal(id);
	}

	public PortalConfigVO getPortal(long id) throws ServiceException {
		TExtVidyoPortalConfig config = tExtVidyoPortalConfigDAO
				.selectByPrimaryKey(id);
		PortalConfigVO vo = new PortalConfigVO();
		vo.settExtVidyoPortalConfig(config);
		return vo;
	}

	@SuppressWarnings("unchecked")
	public boolean deletePortal(long id) throws ServiceException {
		TExtVidyoPortalExample example = new TExtVidyoPortalExample();
		example.createCriteria().andReferenceIdEqualTo(id);
		List<TExtVidyoPortal> portals = (List<TExtVidyoPortal>) tExtVidyoPortalDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(portals)) {
			List<Long> portalIds = new ArrayList<Long>();
			if (!CommonUtil.isNullOrEmpty(portals)) {
				for (TExtVidyoPortal item : portals) {
					portalIds.add(item.getId());
				}
			}
			TExtVidyoTenantExample example2 = new TExtVidyoTenantExample();
			example2.createCriteria().andPortalIdIn(portalIds);
			List<TExtVidyoTenant> tenants = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
					.selectByExample(example2);
			if (!CommonUtil.isNullOrEmpty(tenants)) {
				throw new HasTenantsLeftException("");
			}
			tExtVidyoPortalDAO.deleteByExample(example);
		}
		tExtVidyoPortalConfigDAO.deleteByPrimaryKey(id);
		return true;
	}

	public List<PortalConfigVO> searchPortals(String keyword, int pageSize,
			int pageNo) throws ServiceException {
		List<PortalConfigVO> result = new ArrayList<PortalConfigVO>();
		int startIndex = pageNo * pageSize;
		List<TExtVidyoPortalConfig> list = tExtVidyoPortalConfigDAO
				.searchTExtVidyoPortalConfigs(keyword, startIndex, pageSize);
		if (CommonUtil.isNullOrEmpty(list)) {
			return result;
		}
		for (TExtVidyoPortalConfig item : list) {
			PortalConfigVO vo = new PortalConfigVO();
			vo.settExtVidyoPortalConfig(item);
			result.add(vo);
		}
		return result;
	}

	public int countPortals(String keyword) throws ServiceException {
		return tExtVidyoPortalConfigDAO.countTExtVidyoPortalConfigs(keyword);
	}

	public List<TenantAndPortalConfigVO> searchTenants(String keyword,
			long portalId, int pageSize, int pageNo) throws ServiceException {
		List<TenantAndPortalConfigVO> result = new ArrayList<TenantAndPortalConfigVO>();
		int startIndex = pageNo * pageSize;
		List<TExtVidyoTenant> list = tExtVidyoTenantDAO.searchTenants(keyword,
				portalId, startIndex, pageSize);
		if (CommonUtil.isNullOrEmpty(list)) {
			return result;
		}
		// TODO Improve me.
		for (TExtVidyoTenant item : list) {
			TenantAndPortalConfigVO vo = new TenantAndPortalConfigVO();
			TExtVidyoPortalConfig portalConfig = tExtVidyoPortalConfigDAO
					.getPortalConfigByPortalID(item.getPortalId());
			vo.setPortalConfig(portalConfig);
			vo.setTenant(item);
			result.add(vo);
		}
		return result;
	}

	public int countTenants(String keyword, long portalConfigID)
			throws ServiceException {
		return tExtVidyoTenantDAO.countTenants(keyword, portalConfigID);
	}

	public Map<String, List<ComponentDataVO>> getServiceComponentsData(
			long portalId) throws ServiceException {
		Map<String, List<ComponentDataVO>> result = new HashMap<String, List<ComponentDataVO>>();
		TExtVidyoPortalConfig portalConfig = tExtVidyoPortalConfigDAO
				.selectByPrimaryKey(portalId);
		if (portalConfig == null) {
			return result;
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.ComponentData[] arr = VidyoPortalSuperServiceUtil
				.getServiceComponentsData(portalConfig.getPortalUrl(),
						portalConfig.getSuperName(), portalConfig
								.getSuperPassword());
		if (CommonUtil.isNullOrEmpty(arr)) {
			return result;
		}
		for (com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.ComponentData item : arr) {
			ComponentDataVO vo = new ComponentDataVO();
			vo.setComponentData(item);
			String key = item.getComponentType().getValue();
			if (result.containsKey(key)) {
				List<ComponentDataVO> list = result.get(key);
				list.add(vo);
				result.put(key, list);
			} else {
				List<ComponentDataVO> list = new ArrayList<ComponentDataVO>();
				list.add(vo);
				result.put(key, list);
			}
		}
		return result;
	}

	public Map<String, String> getLicenseData(long portalId, long tenantId)
			throws ServiceException {
		long start = System.currentTimeMillis();
		int callWebServiceTimes = 0;

		Map<String, String> result = new HashMap<String, String>();
		TExtVidyoPortalConfig portalConfig = tExtVidyoPortalConfigDAO
				.selectByPrimaryKey(portalId);
		if (portalConfig == null) {
			return result;
		}

		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.LicenseFeatureData[] initLicenseFeatureDatas = VidyoPortalSuperServiceUtil
				.getLicenseData(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword(), 0);
		callWebServiceTimes++;
		if (CommonUtil.isNullOrEmpty(initLicenseFeatureDatas)) {
			return result;
		}
		for (com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.LicenseFeatureData initLicenseFeatureData : initLicenseFeatureDatas) {
			if ("Installs".equalsIgnoreCase(initLicenseFeatureData.getName())) {
				result.put("Installs", initLicenseFeatureData.getMaxValue());
			} else if ("Seats".equalsIgnoreCase(initLicenseFeatureData
					.getName())) {
				result.put("Seats", initLicenseFeatureData.getMaxValue());
			} else if ("Ports".equalsIgnoreCase(initLicenseFeatureData
					.getName())) {
				result.put("Ports", initLicenseFeatureData.getMaxValue());
			} else if ("LimitTypeExecutiveSystem"
					.equalsIgnoreCase(initLicenseFeatureData.getName())) {
				result.put("LimitTypeExecutiveSystem", initLicenseFeatureData
						.getMaxValue());
			} else if ("LimitTypePanoramaSystem"
					.equalsIgnoreCase(initLicenseFeatureData.getName())) {
				result.put("LimitTypePanoramaSystem", initLicenseFeatureData
						.getMaxValue());
			}
		}

		TExtVidyoTenant tenant = null;
		if (tenantId > 0) {
			tenant = tExtVidyoTenantDAO.selectByPrimaryKey(tenantId);
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType[] tenants = VidyoPortalSuperServiceUtil
				.getListOfTenants(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword());
		callWebServiceTimes++;
		if (!CommonUtil.isNullOrEmpty(tenants)) {
			for (com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType item : tenants) {
				if (tenant == null
						|| !tenant.getName().equalsIgnoreCase(
								item.getTenantName())) {
					com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.LicenseFeatureData[] tenantLicenseFeatureDatas = VidyoPortalSuperServiceUtil
							.getLicenseData(portalConfig.getPortalUrl(),
									portalConfig.getSuperName(), portalConfig
											.getSuperPassword(), item
											.getTenantId().getEntityID());
					callWebServiceTimes++;
					Map<String, String> tenantLicenseFeatureDataMap = new HashMap<String, String>();
					for (com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.LicenseFeatureData tenantLicenseFeatureData : tenantLicenseFeatureDatas) {
						if ("Installs"
								.equalsIgnoreCase(tenantLicenseFeatureData
										.getName())) {
							tenantLicenseFeatureDataMap.put("Installs",
									tenantLicenseFeatureData.getMaxValue());
						} else if ("Seats"
								.equalsIgnoreCase(tenantLicenseFeatureData
										.getName())) {
							tenantLicenseFeatureDataMap.put("Seats",
									tenantLicenseFeatureData.getMaxValue());
						}
					}
					int installs = Integer.parseInt(result.get("Installs"))
							- Integer.parseInt(tenantLicenseFeatureDataMap
									.get("Installs"));
					int seats = Integer.parseInt(result.get("Seats"))
							- Integer.parseInt(tenantLicenseFeatureDataMap
									.get("Seats"));
					result.put("Installs", String.valueOf(installs));
					result.put("Seats", String.valueOf(seats));
				}
			}
		}
		logger
				.debug("VidyoServiceImpl.getLicenseData() call web service times: "
						+ callWebServiceTimes
						+ ", costs: "
						+ (System.currentTimeMillis() - start) + " ms.");
		return result;
	}

	public List<LocationTagVO> getLocationTags(long portalId)
			throws ServiceException {
		List<LocationTagVO> result = new ArrayList<LocationTagVO>();
		TExtVidyoPortalConfig portalConfig = tExtVidyoPortalConfigDAO
				.selectByPrimaryKey(portalId);
		if (portalConfig == null) {
			return result;
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.LocationTag[] locationTags = VidyoPortalSuperServiceUtil
				.getLocationTags(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword());
		if (!CommonUtil.isNullOrEmpty(locationTags)) {
			for (com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.LocationTag item : locationTags) {
				LocationTagVO vo = new LocationTagVO();
				vo.setLocationTag(item);
				result.add(vo);
			}
		}
		return result;
	}

	public List<Tenant4SuperVO> getListOfTenants(long portalId)
			throws ServiceException {
		List<Tenant4SuperVO> result = new ArrayList<Tenant4SuperVO>();
		TExtVidyoPortalConfig portalConfig = tExtVidyoPortalConfigDAO
				.selectByPrimaryKey(portalId);
		if (portalConfig == null) {
			return result;
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType[] tenants = VidyoPortalSuperServiceUtil
				.getListOfTenants(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword());
		if (!CommonUtil.isNullOrEmpty(tenants)) {
			for (com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType item : tenants) {
				Tenant4SuperVO vo = new Tenant4SuperVO();
				vo.setTenant(item);
				result.add(vo);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public boolean createTenant(long portalId, String tenantName,
			String tenantURL, String tenantAdminName,
			String tenantAdminPassword, String extensionPrefix,
			String dialinNumber, String vidyoReplayUrl, String description,
			int numOfInstalls, int numOfSeats, int numOfLines,
			int numOfExecutives, int numOfPanoramas, boolean enableGuestLogin,
			List<Integer> allowedTenantList, int vidyoManager,
			List<Integer> vidyoProxyList,
			List<Integer> allowedVidyoGatewayList,
			List<Integer> allowedVidyoReplayRecorderList,
			List<Integer> allowedVidyoReplayList,
			List<Integer> allowedLocationTagList, boolean vidyoMobileAllowed,
			boolean ipcAllowOutbound, boolean ipcAllowInbound, String portalURL,String acl)
			throws ServiceException {
		TExtVidyoPortalConfig portalConfig = tExtVidyoPortalConfigDAO
				.selectByPrimaryKey(portalId);
		if (portalConfig == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenantExample example = new TExtVidyoTenantExample();
		example.createCriteria().andNameEqualTo(tenantName);
		List<TExtVidyoTenant> test = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			throw new DuplicateTenantNameException("");
		}
		example = new TExtVidyoTenantExample();
		example.createCriteria().andUrlEqualTo(tenantURL);
		test = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			throw new DuplicateTenantURLException("");
		}
		example = new TExtVidyoTenantExample();
		example.createCriteria().andExtensionPrefixEqualTo(extensionPrefix);
		test = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			throw new DuplicateExtensionPrefixException("");
		}
		TExtVidyoPortalExample example2 = new TExtVidyoPortalExample();
		example2.createCriteria().andPortalurlEqualTo(portalURL);
		List<TExtVidyoPortal> test2 = tExtVidyoPortalDAO
				.selectByExample(example2);
		if (!CommonUtil.isNullOrEmpty(test2)) {
			throw new DuplicatePortalUrlException("");
		}
		Date now = new Date();
		// Add to t_portal_info
		TPortalInfo portalInfo = new TPortalInfo();
		portalInfo.setPortalUrl(tenantURL);
		portalInfo.setAdminName(tenantAdminName);
		portalInfo.setAdminPassword(tenantAdminPassword);
		portalInfo.setDisplayName(tenantAdminName);
		portalInfo.setCreateTime(now);
		portalInfo.setUpdateTime(now);
		portalInfo.setAcl(acl);
		tPortalInfoDAO.insertSelective(portalInfo);
		// Add to t_ext_vidyo_portal
		TExtVidyoPortal portal = new TExtVidyoPortal();
		portal.setPortalname(portalConfig.getSuperName());
		portal.setPortalpassword(portalConfig.getSuperPassword());
		portal.setStatus("1");
		portal.setReferenceId(portalId);
		portal.setPortalurl(portalURL);
		long portalId2 = tExtVidyoPortalDAO.insertSelective(portal);
		// Add to t_ext_vidyo_tenant
		String adminName = "admin";
		String adminPassword = "password";
		TExtVidyoTenant tenant = new TExtVidyoTenant();
		tenant.setAdminName(adminName);
		tenant.setAdminPassword(adminPassword);
		tenant.setExtensionPrefix(extensionPrefix);
		tenant.setName(tenantName);
		tenant.setUrl(tenantURL);
		tenant.setPortalId(portalId2);
		tExtVidyoTenantDAO.insertSelective(tenant);
		// Create tenant
		boolean b = VidyoPortalSuperServiceUtil.createTenant(portalConfig
				.getPortalUrl(), portalConfig.getSuperName(), portalConfig
				.getSuperPassword(), tenantName, portalURL, extensionPrefix,
				dialinNumber, vidyoReplayUrl, description, numOfInstalls,
				numOfSeats, numOfLines, numOfExecutives, numOfPanoramas,
				enableGuestLogin, allowedTenantList, vidyoManager,
				vidyoProxyList, allowedVidyoGatewayList,
				allowedVidyoReplayRecorderList, allowedVidyoReplayList,
				allowedLocationTagList, vidyoMobileAllowed, ipcAllowOutbound,
				ipcAllowInbound);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		/**
		 * <pre>
		 * // Change password
		 * String newPassword = CommonUtil.genRandom(6);
		 * boolean b2 = VidyoPortalUserServiceUtil.updatePassword(realPortalUrl,
		 * 		adminName, adminPassword, newPassword);
		 * if (!b2) {
		 * 	throw new CallVidyoWebServiceException(&quot;&quot;);
		 * }
		 * tenant = new TExtVidyoTenant();
		 * tenant.setId(tenantId);
		 * tenant.setAdminPassword(newPassword);
		 * tExtVidyoTenantDAO.updateByPrimaryKeySelective(tenant);
		 * </pre>
		 */
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean updateTenant(long tenantId, long portalId,
			String tenantName, String tenantURL, String tenantAdminName,
			String tenantAdminPassword, String extensionPrefix,
			String dialinNumber, String vidyoReplayUrl, String description,
			int numOfInstalls, int numOfSeats, int numOfLines,
			int numOfExecutives, int numOfPanoramas, boolean enableGuestLogin,
			List<Integer> allowedTenantList, int vidyoManager,
			List<Integer> vidyoProxyList,
			List<Integer> allowedVidyoGatewayList,
			List<Integer> allowedVidyoReplayRecorderList,
			List<Integer> allowedVidyoReplayList,
			List<Integer> allowedLocationTagList, boolean vidyoMobileAllowed,
			boolean ipcAllowOutbound, boolean ipcAllowInbound, String portalURL,String acl)
			throws ServiceException {
		TExtVidyoPortalConfig portalConfig = tExtVidyoPortalConfigDAO
				.selectByPrimaryKey(portalId);
		if (portalConfig == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenantExample example = new TExtVidyoTenantExample();
		example.createCriteria().andNameEqualTo(tenantName);
		List<TExtVidyoTenant> test = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			if (test.size() > 1) {
				throw new DuplicateTenantNameException("");
			} else if (test.get(0).getId() != tenantId) {
				throw new DuplicateTenantNameException("");
			}
		}
		example = new TExtVidyoTenantExample();
		example.createCriteria().andUrlEqualTo(tenantURL);
		test = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			if (test.size() > 1) {
				throw new DuplicateTenantURLException("");
			} else if (test.get(0).getId() != tenantId) {
				throw new DuplicateTenantURLException("");
			}
		}
		example = new TExtVidyoTenantExample();
		example.createCriteria().andExtensionPrefixEqualTo(extensionPrefix);
		test = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			if (test.size() > 1) {
				throw new DuplicateExtensionPrefixException("");
			} else if (test.get(0).getId() != tenantId) {
				throw new DuplicateExtensionPrefixException("");
			}
		}
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoPortalExample example2 = new TExtVidyoPortalExample();
		example2.createCriteria().andPortalurlEqualTo(portalURL);
		List<TExtVidyoPortal> test2 = tExtVidyoPortalDAO
				.selectByExample(example2);
		if (!CommonUtil.isNullOrEmpty(test2)) {
			if (test2.size() > 1) {
				throw new DuplicatePortalUrlException("");
			}
			if (test2.get(0).getId() != portal.getId()) {
				throw new DuplicatePortalUrlException("");
			}
		}
		TPortalInfo portalInfo = null;
		TPortalInfoExample example3 = new TPortalInfoExample();
		example3.createCriteria().andPortalUrlEqualTo(tenant.getUrl());
		List<TPortalInfo> list = (List<TPortalInfo>) tPortalInfoDAO
				.selectByExample(example3);
		if (CommonUtil.isNullOrEmpty(list)) {
			throw new OperationRejectedException("");
		}
		portalInfo = list.get(0);

		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType[] tenants = VidyoPortalSuperServiceUtil
				.getListOfTenants(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword());
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType matchedTenant = getMatchedTenant(
				tenants, tenant);
		if (matchedTenant == null) {
			throw new OperationRejectedException("");
		}

		Date now = new Date();
		// Update t_portal_info
		portalInfo.setPortalUrl(tenantURL);
		portalInfo.setAdminName(tenantAdminName);
		portalInfo.setAdminPassword(tenantAdminPassword);
		portalInfo.setDisplayName(tenantAdminName);
		portalInfo.setUpdateTime(now);
		portalInfo.setAcl(acl);
		tPortalInfoDAO.updateByPrimaryKeySelective(portalInfo);
		// Update t_ext_vidyo_tenant
		tenant.setExtensionPrefix(extensionPrefix);
		tenant.setName(tenantName);
		tenant.setUrl(tenantURL);
		tExtVidyoTenantDAO.updateByPrimaryKeySelective(tenant);

		boolean b = VidyoPortalSuperServiceUtil.updateTenant(matchedTenant
				.getTenantId().getEntityID(), portalConfig.getPortalUrl(),
				portalConfig.getSuperName(), portalConfig.getSuperPassword(),
				tenantName, portal.getPortalurl(), extensionPrefix,
				dialinNumber, vidyoReplayUrl, description, numOfInstalls,
				numOfSeats, numOfLines, numOfExecutives, numOfPanoramas,
				enableGuestLogin, allowedTenantList, vidyoManager,
				vidyoProxyList, allowedVidyoGatewayList,
				allowedVidyoReplayRecorderList, allowedVidyoReplayList,
				allowedLocationTagList, vidyoMobileAllowed, ipcAllowOutbound,
				ipcAllowInbound);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<String> check4StoreTenant(long tenantId, String tenantName,
			String tenantURL, String extensionPrefix, String portalURL)
			throws ServiceException {
		List<String> result = new ArrayList<String>();

		TExtVidyoTenantExample example = new TExtVidyoTenantExample();
		example.createCriteria().andNameEqualTo(tenantName);
		List<TExtVidyoTenant> test = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			if (tenantId > 0) {
				if (test.size() > 1) {
					result.add("err_duplicate_tenant_name");
				} else if (test.get(0).getId() != tenantId) {
					result.add("err_duplicate_tenant_name");
				}
			} else {
				result.add("err_duplicate_tenant_name");
			}
		}
		example = new TExtVidyoTenantExample();
		example.createCriteria().andUrlEqualTo(tenantURL);
		test = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			if (tenantId > 0) {
				if (test.size() > 1) {
					result.add("err_duplicate_tenant_url");
				} else if (test.get(0).getId() != tenantId) {
					result.add("err_duplicate_tenant_url");
				}
			} else {
				result.add("err_duplicate_tenant_url");
			}
		}
		example = new TExtVidyoTenantExample();
		example.createCriteria().andExtensionPrefixEqualTo(extensionPrefix);
		test = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(test)) {
			if (tenantId > 0) {
				if (test.size() > 1) {
					result.add("err_duplicate_extension_prefix");
				} else if (test.get(0).getId() != tenantId) {
					result.add("err_duplicate_extension_prefix");
				}
			} else {
				result.add("err_duplicate_extension_prefix");
			}
		}
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant != null) {
			TExtVidyoPortal portal = tExtVidyoPortalDAO
					.selectByPrimaryKey(tenant.getPortalId());
			if (portal != null) {
				TExtVidyoPortalExample example2 = new TExtVidyoPortalExample();
				example2.createCriteria().andPortalurlEqualTo(portalURL);
				List<TExtVidyoPortal> test2 = tExtVidyoPortalDAO
						.selectByExample(example2);
				if (!CommonUtil.isNullOrEmpty(test2)) {
					if (tenantId > 0) {
						if (test2.size() > 1) {
							result.add("err_duplicate_portal_url");
						} else if (test2.get(0).getId() != portal.getId()) {
							result.add("err_duplicate_portal_url");
						}
					} else {
						result.add("err_duplicate_portal_url");
					}
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public Tenant4SuperExtVO getTenant(long tenantId) throws ServiceException {
		Tenant4SuperExtVO result = new Tenant4SuperExtVO();
		TExtVidyoPortalConfig portalConfig = getPortalConfigByTenantId(tenantId);
		if (portalConfig == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}

		TPortalInfo portalInfo = null;
		TPortalInfoExample example = new TPortalInfoExample();
		example.createCriteria().andPortalUrlEqualTo(tenant.getUrl());
		List<TPortalInfo> list = (List<TPortalInfo>) tPortalInfoDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(list)) {
			portalInfo = list.get(0);
		}
		result.setPortalInfo(portalInfo);

		TExtVidyoPortal vidyoPortal = tExtVidyoPortalDAO
				.selectByPrimaryKey(tenant.getPortalId());
		result.setVidyoPortal(vidyoPortal);

		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType[] tenants = VidyoPortalSuperServiceUtil
				.getListOfTenants(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword());
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType matchedTenant = getMatchedTenant(
				tenants, tenant);
		if (matchedTenant == null) {
			throw new OperationRejectedException("");
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.TenantDataExtType tenantDetail = VidyoPortalSuperServiceUtil
				.getTenantDetails(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword(),
						matchedTenant.getTenantId().getEntityID());
		result.setTenant(tenantDetail);
		return result;
	}

	@SuppressWarnings("unchecked")
	public boolean deleteTenant(long tenantId) throws ServiceException {
		TExtVidyoPortalConfig portalConfig = getPortalConfigByTenantId(tenantId);
		if (portalConfig == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		TExtVidyoMemberExample example = new TExtVidyoMemberExample();
		example.createCriteria().andTenantIdEqualTo(tenant.getId());
		List<TExtVidyoMember> members = (List<TExtVidyoMember>) tExtVidyoMemberDAO
				.selectByExample(example);
		if (!CommonUtil.isNullOrEmpty(members)) {
			throw new HasMembersLeftException("");
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType[] tenants = VidyoPortalSuperServiceUtil
				.getListOfTenants(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword());
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType matchedTenant = getMatchedTenant(
				tenants, tenant);
		if (matchedTenant == null) {
			throw new OperationRejectedException("");
		}
		tExtVidyoTenantDAO.deleteByPrimaryKey(tenantId);
		tExtVidyoPortalDAO.deleteByPrimaryKey(tenant.getPortalId());
		TPortalInfoExample example2 = new TPortalInfoExample();
		example2.createCriteria().andPortalUrlEqualTo(tenant.getUrl());
		tPortalInfoDAO.deleteByExample(example2);

		boolean b = VidyoPortalSuperServiceUtil.deleteTenant(portalConfig
				.getPortalUrl(), portalConfig.getSuperName(), portalConfig
				.getSuperPassword(), matchedTenant.getTenantId().getEntityID());
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public List<GroupVO> getGroupsByTenantId(long tenantId)
			throws ServiceException {
		List<GroupVO> result = new ArrayList<GroupVO>();
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Group[] groups = VidyoPortalAdminServiceUtil
				.getGroups(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword());
		if (!CommonUtil.isNullOrEmpty(groups)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Group group : groups) {
				GroupVO vo = new GroupVO();
				vo.setGroup(group);
				result.add(vo);
			}
		}
		return result;
	}

	public List<ComponentDataVO> getVidyoProxyListByTenantId(long tenantId)
			throws ServiceException {
		List<ComponentDataVO> result = new ArrayList<ComponentDataVO>();
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			return result;
		}
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			return result;
		}
		TExtVidyoPortalConfig portalConfig = tExtVidyoPortalConfigDAO
				.selectByPrimaryKey(portal.getReferenceId());
		if (portalConfig == null) {
			return result;
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType[] tenants = VidyoPortalSuperServiceUtil
				.getListOfTenants(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword());
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType tenant2 = getMatchedTenant(
				tenants, tenant);
		if (tenant2 == null) {
			return result;
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.TenantDataExtType tenantDetails = VidyoPortalSuperServiceUtil
				.getTenantDetails(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword(),
						tenant2.getTenantId().getEntityID());
		if (tenantDetails == null) {
			return result;
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.ComponentData[] components = VidyoPortalSuperServiceUtil
				.getServiceComponentsData(portalConfig.getPortalUrl(),
						portalConfig.getSuperName(), portalConfig
								.getSuperPassword());
		if (CommonUtil.isNullOrEmpty(components)) {
			return result;
		}
		for (int vidyoProxy : tenantDetails.getVidyoProxyList()) {
			for (com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.ComponentData component : components) {
				if (component.getIdentifier().equalsIgnoreCase(
						String.valueOf(vidyoProxy))) {
					ComponentDataVO vo = new ComponentDataVO();
					vo.setComponentData(component);
					result.add(vo);
					break;
				}
			}
		}
		return result;
	}

	public List<LocationTagVO> getLocationTagsByTenantId(long tenantId)
			throws ServiceException {
		List<LocationTagVO> result = new ArrayList<LocationTagVO>();
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			return result;
		}
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			return result;
		}
		TExtVidyoPortalConfig portalConfig = tExtVidyoPortalConfigDAO
				.selectByPrimaryKey(portal.getReferenceId());
		if (portalConfig == null) {
			return result;
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType[] tenants = VidyoPortalSuperServiceUtil
				.getListOfTenants(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword());
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType tenant2 = getMatchedTenant(
				tenants, tenant);
		if (tenant2 == null) {
			return result;
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.TenantDataExtType tenantDetails = VidyoPortalSuperServiceUtil
				.getTenantDetails(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword(),
						tenant2.getTenantId().getEntityID());
		if (tenantDetails == null) {
			return result;
		}
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.LocationTag[] locationTags = VidyoPortalSuperServiceUtil
				.getLocationTags(portalConfig.getPortalUrl(), portalConfig
						.getSuperName(), portalConfig.getSuperPassword());
		if (CommonUtil.isNullOrEmpty(locationTags)) {
			return result;
		}
		for (int locationTagID : tenantDetails.getAllowedLocationTagList()) {
			for (com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.LocationTag locationTag : locationTags) {
				if (locationTagID == locationTag.getLocationTagID()) {
					LocationTagVO vo = new LocationTagVO();
					vo.setLocationTag(locationTag);
					result.add(vo);
					break;
				}
			}
		}
		return result;
	}

	public boolean createRoom(long tenantId, String description,
			String extension, String groupName, String name, String ownerName,
			String roomType, String roomPIN, String moderatorPIN)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room[] rooms = VidyoPortalAdminServiceUtil
				.getRooms(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword());
		if (!CommonUtil.isNullOrEmpty(rooms)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room item : rooms) {
				if (name.equalsIgnoreCase(item.getName())) {
					throw new DuplicateRoomNameException("");
				}
				if (extension.equalsIgnoreCase(item.getExtension())) {
					throw new DuplicateRoomExtensionException("");
				}
			}
		}
		boolean b = VidyoPortalAdminServiceUtil.addRoom(portalURL, tenant
				.getAdminName(), tenant.getAdminPassword(), description,
				extension, groupName, name, ownerName, roomType, roomPIN,
				moderatorPIN);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean updateRoom(long tenantId, int roomID, String description,
			String extension, String groupName, String name, String ownerName,
			String roomType) throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room room = null;
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room[] rooms = VidyoPortalAdminServiceUtil
				.getRooms(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword());
		if (!CommonUtil.isNullOrEmpty(rooms)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room item : rooms) {
				if (name.equalsIgnoreCase(item.getName())
						&& roomID != item.getRoomID().getEntityID()) {
					throw new DuplicateRoomNameException("");
				}
				if (extension.equalsIgnoreCase(item.getExtension())
						&& roomID != item.getRoomID().getEntityID()) {
					throw new DuplicateRoomExtensionException("");
				}
				if (roomID == item.getRoomID().getEntityID()) {
					room = item;
				}
			}
		}
		if (room == null) {
			throw new OperationRejectedException("");
		}
		boolean b = VidyoPortalAdminServiceUtil.updateRoom(portalURL, tenant
				.getAdminName(), tenant.getAdminPassword(), description,
				extension, groupName, name, ownerName, roomType, room);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public RoomVO getRoom(long tenantId, int roomID) throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room room = VidyoPortalAdminServiceUtil
				.getRoom(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword(), roomID);
		RoomVO result = new RoomVO();
		result.setRoom(room);
		return result;
	}

	public boolean deleteRoom(long tenantId, int roomID)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		String portalURL = getRealPortalURL(tenant.getPortalId());
		boolean b = VidyoPortalAdminServiceUtil.deleteRoom(portalURL, tenant
				.getAdminName(), tenant.getAdminPassword(), roomID);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean removeRoomModeratorPin(long tenantId, long roomId)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		int roomId2 = new Long(roomId).intValue();
		String portalURL = getRealPortalURL(tenant.getPortalId());
		boolean b = VidyoPortalAdminServiceUtil.removeModeratorPIN(portalURL,
				tenant.getAdminName(), tenant.getAdminPassword(), roomId2);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean createRoomModeratorPin(long tenantId, long roomId,
			String pinCode) throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		int roomId2 = new Long(roomId).intValue();
		String portalURL = getRealPortalURL(tenant.getPortalId());
		boolean b = VidyoPortalAdminServiceUtil.createModeratorPIN(portalURL,
				tenant.getAdminName(), tenant.getAdminPassword(), roomId2,
				pinCode);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean createGroup(long tenantId, String name, String description,
			String roomMaxUsers, String userMaxBandWidthIn,
			String userMaxBandWidthOut, boolean allowRecording)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Group[] groups = VidyoPortalAdminServiceUtil
				.getGroups(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword());
		if (!CommonUtil.isNullOrEmpty(groups)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Group item : groups) {
				if (item.getName().equalsIgnoreCase(name)) {
					throw new DuplicateGroupNameException("");
				}
			}
		}
		boolean b = VidyoPortalAdminServiceUtil.addGroup(portalURL, tenant
				.getAdminName(), tenant.getAdminPassword(), name, description,
				roomMaxUsers, userMaxBandWidthIn, userMaxBandWidthOut,
				allowRecording);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean updateGroup(long tenantId, int groupId, String name,
			String description, String roomMaxUsers, String userMaxBandWidthIn,
			String userMaxBandWidthOut, boolean allowRecording)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Group[] groups = VidyoPortalAdminServiceUtil
				.getGroups(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword());
		if (!CommonUtil.isNullOrEmpty(groups)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Group item : groups) {
				if (item.getName().equalsIgnoreCase(name)
						&& item.getGroupID().getEntityID() != groupId) {
					throw new DuplicateGroupNameException("");
				}
			}
		}
		boolean b = VidyoPortalAdminServiceUtil.updateGroup(portalURL, tenant
				.getAdminName(), tenant.getAdminPassword(), groupId, name,
				description, roomMaxUsers, userMaxBandWidthIn,
				userMaxBandWidthOut, allowRecording);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public boolean deleteGroup(long tenantId, int groupId)
			throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		String portalURL = getRealPortalURL(tenant.getPortalId());
		boolean b = VidyoPortalAdminServiceUtil.deleteGroup(portalURL, tenant
				.getAdminName(), tenant.getAdminPassword(), groupId);
		if (!b) {
			throw new CallVidyoWebServiceException("");
		}
		return true;
	}

	public GroupVO getGroup(long tenantId, int groupId) throws ServiceException {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			throw new OperationRejectedException("");
		}
		String portalURL = getRealPortalURL(tenant.getPortalId());
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Group group = VidyoPortalAdminServiceUtil
				.getGroup(portalURL, tenant.getAdminName(), tenant
						.getAdminPassword(), groupId);
		GroupVO vo = new GroupVO();
		vo.setGroup(group);
		return vo;
	}

	@SuppressWarnings("unchecked")
	public List<TenantAndPortalConfigVO> getTenantsByPortalConfigId(
			long portalConfigId) throws ServiceException {
		List<TenantAndPortalConfigVO> result = new ArrayList<TenantAndPortalConfigVO>();

		TExtVidyoPortalConfig portalConfig = tExtVidyoPortalConfigDAO
				.selectByPrimaryKey(portalConfigId);
		if (portalConfig == null) {
			return result;
		}
		TExtVidyoPortalExample example = new TExtVidyoPortalExample();
		example.createCriteria().andReferenceIdEqualTo(portalConfigId);
		List<TExtVidyoPortal> portals = (List<TExtVidyoPortal>) tExtVidyoPortalDAO
				.selectByExample(example);
		if (CommonUtil.isNullOrEmpty(portals)) {
			return result;
		}
		List<Long> portalIds = new ArrayList<Long>();
		for (TExtVidyoPortal item : portals) {
			portalIds.add(item.getId());
		}
		TExtVidyoTenantExample example2 = new TExtVidyoTenantExample();
		example2.createCriteria().andPortalIdIn(portalIds);
		List<TExtVidyoTenant> tenants = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(example2);
		if (CommonUtil.isNullOrEmpty(tenants)) {
			return result;
		}
		for (TExtVidyoTenant item : tenants) {
			if (!CommonUtil.isNullOrEmpty(item.getDescription())) {
				TenantAndPortalConfigVO vo = new TenantAndPortalConfigVO();
				vo.setPortalConfig(portalConfig);
				vo.setTenant(item);
				result.add(vo);
			}
		}
		return result;
	}

	public List<CdrVO> searchCdrs(long portalId, long tenantId, long memberId,
			Date startTime, Date endTime, int pageSize, int pageNo)
			throws ServiceException {
		int startIndex = pageNo * pageSize;
		if (portalId > 0) {
			if (tenantId > 0) {
				if (memberId > 0) {
					// Only this member.
					List<CdrVO> cdrs = tCdrDAO.searchCdrsByMemberId(portalId,
							tenantId, memberId, startTime, endTime, startIndex,
							pageSize);
					return feedName4CdrsAsMember(cdrs);
				} else {
					// Members belongs to this tenant.
					List<CdrVO> cdrs = tCdrDAO.searchCdrsByTenantId(portalId,
							tenantId, startTime, endTime, startIndex, pageSize);
					return feedName4CdrsAsMember(cdrs);
				}
			} else {
				// Tenants belongs to this portal.
				List<CdrVO> cdrs = tCdrDAO.searchCdrsByPortalId(portalId,
						startTime, endTime, startIndex, pageSize);
				return feedName4CdrsAsTenant(cdrs);
			}
		} else {
			// All tenants.
			List<CdrVO> cdrs = tCdrDAO.searchCdrsAll(startTime, endTime,
					startIndex, pageSize);
			return feedName4CdrsAsTenant(cdrs);
		}
	}

	public int countCdrs(long portalId, long tenantId, long memberId,
			Date startTime, Date endTime) throws ServiceException {
		if (portalId > 0) {
			if (tenantId > 0) {
				if (memberId > 0) {
					return tCdrDAO.countCdrsByMemberId(portalId, tenantId,
							memberId, startTime, endTime);
				} else {
					return tCdrDAO.countCdrsByTenantId(portalId, tenantId,
							startTime, endTime);
				}
			} else {
				return tCdrDAO
						.countCdrsByPortalId(portalId, startTime, endTime);
			}
		} else {
			return tCdrDAO.countCdrsAll(startTime, endTime);
		}
	}

	private String getRealPortalURL(long portalId) throws ServiceException {
		TExtVidyoPortal portal = tExtVidyoPortalDAO
				.selectByPrimaryKey(portalId);
		if (portal == null) {
			return null;
		}
		return portal.getPortalurl();
	}

	private void uploadLogoFile(DiskFileItem item, long id, String suffix)
			throws ServiceException {
		String logoDir = UploadUtil.getLogoDir(id);
		String logoFilePath = UploadUtil.getLogoFilePath(id, suffix,
				IMAGE_TYPE.ORIGINAL);
		try {
			File dir = new File(logoDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(logoFilePath);
			item.write(file);

			checkLogoFile(file);
		} catch (LogoSizeExceededException e) {
			File file = new File(logoFilePath);
			file.delete();
			throw e;
		} catch (FileTypeInvalidException e) {
			File file = new File(logoFilePath);
			file.delete();
			throw e;
		} catch (Throwable t) {
			logger.error("Upload logo fail, id : " + id
					+ ". DB will roll back!", t);
			throw new ServiceException("");
		}
	}

	/**
	 * 校验长宽，并暗中包含判断文件是否为图片的逻辑。
	 * 
	 * @param file
	 * @throws ServiceException
	 */
	private void checkLogoFile(File file) throws ServiceException {
		InputStream is = null;
		BufferedImage src = null;
		try {
			is = new FileInputStream(file);
			src = ImageIO.read(is);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			if (width > Constants.LogoMaxSize.WIDTH
					|| height > Constants.LogoMaxSize.HEIGHT) {
				throw new LogoSizeExceededException("");
			}
		} catch (IOException e) {
			throw new FileTypeInvalidException("");
		}
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	private com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member getLegacy(
			String portalURL, String username, String password, String name)
			throws ServiceException {
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member member = null;
		com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member[] members = VidyoPortalAdminServiceUtil
				.getMembers(portalURL, username, password, name);
		if (!CommonUtil.isNullOrEmpty(members)) {
			for (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Member item : members) {
				if (com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.RoleName_type0.Legacy
						.getValue().equalsIgnoreCase(
								item.getRoleName().getValue())
						&& name.equalsIgnoreCase(item.getName())) {
					member = item;
					break;
				}
			}
		}
		return member;
	}

	private com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType getMatchedTenant(
			com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType[] tenants,
			TExtVidyoTenant tenant) {
		com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType result = null;
		if (!CommonUtil.isNullOrEmpty(tenants)) {
			for (com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType item : tenants) {
				if (tenant.getName().equalsIgnoreCase(item.getTenantName())) {
					result = item;
					break;
				}
			}
		}
		return result;
	}

	private TExtVidyoPortalConfig getPortalConfigByTenantId(long tenantId) {
		TExtVidyoTenant tenant = tExtVidyoTenantDAO
				.selectByPrimaryKey(tenantId);
		if (tenant == null) {
			return null;
		}
		TExtVidyoPortal portal = tExtVidyoPortalDAO.selectByPrimaryKey(tenant
				.getPortalId());
		if (portal == null) {
			return null;
		}
		TExtVidyoPortalConfig portalConfig = tExtVidyoPortalConfigDAO
				.selectByPrimaryKey(portal.getReferenceId());
		return portalConfig;
	}

	@SuppressWarnings("unchecked")
	private List<CdrVO> feedName4CdrsAsMember(List<CdrVO> cdrs) {
		List<CdrVO> result = new ArrayList<CdrVO>();
		if (!CommonUtil.isNullOrEmpty(cdrs)) {
			Set<Long> memberIds = new HashSet<Long>();
			for (CdrVO cdr : cdrs) {
				memberIds.add(cdr.getMemberId());
			}
			TExtVidyoMemberExample example = new TExtVidyoMemberExample();
			example.createCriteria().andIdIn(new ArrayList(memberIds));
			List<TExtVidyoMember> members = (List<TExtVidyoMember>) tExtVidyoMemberDAO
					.selectByExample(example);
			Map<Long, TExtVidyoMember> memberMap = new HashMap<Long, TExtVidyoMember>();
			if (!CommonUtil.isNullOrEmpty(members)) {
				for (TExtVidyoMember member : members) {
					memberMap.put(new Long(member.getId()), member);
				}
			}
			for (CdrVO cdr : cdrs) {
				long memberId = cdr.getMemberId();
				TExtVidyoMember member = memberMap.get(memberId);
				if (member != null) {
					cdr.setName(member.getDisplayname());
				}
				result.add(cdr);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<CdrVO> feedName4CdrsAsTenant(List<CdrVO> cdrs) {
		List<CdrVO> result = new ArrayList<CdrVO>();
		if (!CommonUtil.isNullOrEmpty(cdrs)) {
			Set<Long> tenantIds = new HashSet<Long>();
			for (CdrVO cdr : cdrs) {
				tenantIds.add(cdr.getTenantId());
			}
			TExtVidyoTenantExample example = new TExtVidyoTenantExample();
			example.createCriteria().andIdIn(new ArrayList(tenantIds));
			List<TExtVidyoTenant> tenants = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
					.selectByExample(example);
			Map<Long, TExtVidyoTenant> tenantMap = new HashMap<Long, TExtVidyoTenant>();
			if (!CommonUtil.isNullOrEmpty(tenants)) {
				for (TExtVidyoTenant tenant : tenants) {
					tenantMap.put(tenant.getId(), tenant);
				}
			}
			for (CdrVO cdr : cdrs) {
				long tenantId = cdr.getTenantId();
				TExtVidyoTenant tenant = tenantMap.get(tenantId);
				if (tenant != null) {
					cdr.setName(tenant.getName());
				}
				result.add(cdr);
			}
		}
		return result;
	}

}
