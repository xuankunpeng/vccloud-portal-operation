package com.vccloud.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vccloud.portal.db.model.TExtVidyoPortal;
import com.vccloud.portal.db.model.TExtVidyoPortalExample;
import com.vccloud.portal.db.model.TExtVidyoTenant;
import com.vccloud.portal.db.model.TExtVidyoTenantExample;
import com.vccloud.portal.db.model.TLegacy;
import com.vccloud.portal.db.model.TLegacyExample;
import com.vccloud.portal.service.ScheduleService;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceUtil;

public class ScheduleServiceImpl extends ServiceDefault implements
		ScheduleService {

	private Logger logger = Logger.getLogger(ScheduleServiceImpl.class);

	@SuppressWarnings("unchecked")
	public void legacing() {
		TExtVidyoTenantExample tExtVidyoTenantExample = new TExtVidyoTenantExample();
		List<TExtVidyoTenant> tenants = (List<TExtVidyoTenant>) tExtVidyoTenantDAO
				.selectByExample(tExtVidyoTenantExample);
		if (CommonUtil.isNullOrEmpty(tenants)) {
			return;
		}
		// Filter tenants.
		TLegacyExample tLegacyExample = new TLegacyExample();
		tLegacyExample.createCriteria().andRoomIdIsNotNull()
				.andRoomIdNotEqualTo("");
		List<TLegacy> legacies = (List<TLegacy>) tLegacyDAO
				.selectByExample(tLegacyExample);
		if (CommonUtil.isNullOrEmpty(legacies)) {
			return;
		}
		List<TExtVidyoTenant> tenants2 = new ArrayList<TExtVidyoTenant>();
		for (TExtVidyoTenant tenant : tenants) {
			for (TLegacy legacy : legacies) {
				if (legacy.getUrl() != null
						&& legacy.getUrl().equalsIgnoreCase(tenant.getUrl())) {
					tenants2.add(tenant);
					break;
				}
			}
		}
		tenants = tenants2;

		List<Long> portalIds = new ArrayList<Long>();
		for (TExtVidyoTenant item : tenants) {
			portalIds.add(item.getPortalId());
		}
		TExtVidyoPortalExample tExtVidyoPortalExample = new TExtVidyoPortalExample();
		tExtVidyoPortalExample.createCriteria().andIdIn(portalIds);
		List<TExtVidyoPortal> portals = (List<TExtVidyoPortal>) tExtVidyoPortalDAO
				.selectByExample(tExtVidyoPortalExample);
		if (CommonUtil.isNullOrEmpty(portals)) {
			return;
		}
		Map<Long, TExtVidyoPortal> portalMaps = new HashMap<Long, TExtVidyoPortal>();
		for (TExtVidyoPortal item : portals) {
			portalMaps.put(item.getId(), item);
		}

		List<T> list = new ArrayList<T>();
		for (TExtVidyoTenant item : tenants) {
			T t = new T();
			t.setAdminName(item.getAdminName());
			t.setAdminPassword(item.getAdminPassword());
			t.setWebURL(item.getUrl());
			String portalURL = "";
			if (portalMaps.get(item.getPortalId()) != null) {
				portalURL = portalMaps.get(item.getPortalId()).getPortalurl();
			}
			t.setPortalURL(portalURL);
			list.add(t);
		}

		for (T t : list) {
			try {
				legacing(t);
			} catch (Throwable e) {
				logger
						.error("==================> ScheduleServiceImpl.legacing() caught exception, [ adminName: "
								+ t.getAdminName()
								+ ", adminPassword: "
								+ t.getAdminPassword()
								+ ", portalURL: "
								+ t.getPortalURL()
								+ ", webURL: "
								+ t.getWebURL() + " ]");
				logger.error("", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void legacing(T t) throws Throwable {
		com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceStub.Entity_type0[] rooms = VidyoPortalUserServiceUtil
				.searchPublicRooms(t.getPortalURL(), t.getAdminName(), t
						.getAdminPassword());
		if (CommonUtil.isNullOrEmpty(rooms)) {
			return;
		}
		for (com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceStub.Entity_type0 room : rooms) {
			com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceStub.Entity_type0[] participants = null;
			try {
				participants = VidyoPortalUserServiceUtil.getParticipants(t
						.getPortalURL(), t.getAdminName(),
						t.getAdminPassword(), room.getEntityID());
			} catch (Throwable e) {
				continue;
			}
			if (CommonUtil.isNullOrEmpty(participants)) {
				continue;
			}
			List<String> legacyExtensionsInConference = new ArrayList<String>();
			for (com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceStub.Entity_type0 participant : participants) {
				if (com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceStub.EntityType_type0.Legacy
						.getValue().equalsIgnoreCase(
								participant.getEntityType().getValue())) {
					legacyExtensionsInConference
							.add(participant.getExtension());
				}
			}
			if (legacyExtensionsInConference.size() == participants.length) {
				// Legacy踢出会议室
				for (com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceStub.Entity_type0 participant : participants) {
					com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceStub.EntityID participantID = new com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceStub.EntityID();
					participantID.setEntityID(String.valueOf(participant
							.getParticipantID()));
					try {
						VidyoPortalUserServiceUtil.leaveConference(t
								.getPortalURL(), t.getAdminName(), t
								.getAdminPassword(), room.getEntityID(),
								participantID);
					} catch (Throwable e) {
						continue;
					}
				}
			} else {
				// Legacy拉进会议室
				TLegacyExample tLegacyExample = new TLegacyExample();
				tLegacyExample.createCriteria().andRoomIdEqualTo(
						room.getEntityID().getEntityID()).andUrlEqualTo(
						t.getWebURL());
				List<TLegacy> legaciesInDB = (List<TLegacy>) tLegacyDAO
						.selectByExample(tLegacyExample);
				if (CommonUtil.isNullOrEmpty(legaciesInDB)) {
					continue;
				}
				for (TLegacy legacy : legaciesInDB) {
					if (!legacyExtensionsInConference.contains(legacy
							.getLegacyExtension())) {
						com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceStub.Entity_type0[] arr = null;
						try {
							arr = VidyoPortalUserServiceUtil.searchLegacies(t
									.getPortalURL(), t.getAdminName(), t
									.getAdminPassword(), legacy
									.getLegacyExtension());
						} catch (Throwable e) {
							continue;
						}
						if (!CommonUtil.isNullOrEmpty(arr)) {
							com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceStub.Entity_type0 targetLegacy = arr[0];
							for (com.vccloud.portal.webservice.VidyoPortalUserService.VidyoPortalUserServiceStub.Entity_type0 item : arr) {
								if (legacy.getLegacyExtension()
										.equalsIgnoreCase(item.getExtension())) {
									targetLegacy = item;
									break;
								}
							}
							try {
								VidyoPortalUserServiceUtil.inviteToConference(t
										.getPortalURL(), t.getAdminName(), t
										.getAdminPassword(),
										room.getEntityID(), targetLegacy
												.getEntityID(), targetLegacy
												.getExtension());
							} catch (Throwable e) {
								continue;
							}
						}
					}
				}
			}
		}
	}

	class T {
		private String webURL;
		private String portalURL;
		private String adminName;
		private String adminPassword;

		public String getWebURL() {
			return webURL;
		}

		public void setWebURL(String webURL) {
			this.webURL = webURL;
		}

		public String getPortalURL() {
			return portalURL;
		}

		public void setPortalURL(String portalURL) {
			this.portalURL = portalURL;
		}

		public String getAdminName() {
			return adminName;
		}

		public void setAdminName(String adminName) {
			this.adminName = adminName;
		}

		public String getAdminPassword() {
			return adminPassword;
		}

		public void setAdminPassword(String adminPassword) {
			this.adminPassword = adminPassword;
		}
	}

}
