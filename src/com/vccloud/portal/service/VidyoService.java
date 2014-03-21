package com.vccloud.portal.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.db.model.TPortalInfo;
import com.vccloud.portal.exception.ServiceException;
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

public interface VidyoService {

	public List<ConferenceCall2VO> searchConferenceCall2s(String tenantName,
			String callerName, Date startTime, Date endTime,
			String orderByClause, int pageSize, int pageNo)
			throws ServiceException;

	public int countConferenceCall2s(String tenantName, String callerName,
			Date startTime, Date endTime) throws ServiceException;

	public void storePortalInfo(String portalUrl, String welcomeWord,
			DiskFileItem item) throws ServiceException;

	public TPortalInfo getPortalInfo(String portalUrl) throws ServiceException;

	public List<RoomVO> searchRooms(long tenantId) throws ServiceException;

	public List<RoomVO> searchPublicRooms(long tenantId)
			throws ServiceException;

	public List<RoomVO> searchPersonalRooms(long memberId)
			throws ServiceException;

	public String getVidyoPortalURL(String userPortalURL)
			throws ServiceException;

	public boolean updateRoomName(long tenantId, long roomId, String name)
			throws ServiceException;

	public boolean removeRoomPin(long tenantId, long roomId)
			throws ServiceException;

	public boolean createRoomPin(long tenantId, long roomId, String pinCode)
			throws ServiceException;

	public boolean clearRoom(long tenantId, long roomId)
			throws ServiceException;

	public boolean addLegacy(String url, String name, String extension)
			throws ServiceException;

	public boolean updateLegacy(long id, String name, String extension)
			throws ServiceException;

	public List<LegacyVO> getLegacies(String url, String roomId)
			throws ServiceException;

	public boolean assignLegacies(String url, String roomId,
			List<Long> legacyIds) throws ServiceException;

	public boolean deleteLegacy(long id) throws ServiceException;

	public List<LegacyVO> searchLegacies(long tenantId, String keyword,
			int pageSize, int pageNo) throws ServiceException;

	public int countLegacies(long tenantId, String keyword)
			throws ServiceException;

	public RoomProfileVO getRoomProfile(long tenantId, int roomId)
			throws ServiceException;

	public boolean setRoomProfile(long tenantId, int roomId,
			String roomProfileName) throws ServiceException;

	public boolean removeRoomProfile(long tenantId, int roomId)
			throws ServiceException;

	public boolean resetRoomURL(long tenantId, int roomId)
			throws ServiceException;

	public PortalConfigVO storePortal(long id, String portalName,
			String portalUrl, String superName, String superPassword)
			throws ServiceException;

	public PortalConfigVO getPortal(long id) throws ServiceException;

	public boolean deletePortal(long id) throws ServiceException;

	public List<PortalConfigVO> searchPortals(String keyword, int pageSize,
			int pageNo) throws ServiceException;

	public int countPortals(String keyword) throws ServiceException;

	public List<TenantAndPortalConfigVO> searchTenants(String keyword,
			long portalId, int pageSize, int pageNo) throws ServiceException;

	public int countTenants(String keyword, long portalConfigID)
			throws ServiceException;

	public Map<String, List<ComponentDataVO>> getServiceComponentsData(
			long portalId) throws ServiceException;

	public Map<String, String> getLicenseData(long portalId, long tenantId)
			throws ServiceException;

	public List<LocationTagVO> getLocationTags(long portalId)
			throws ServiceException;

	public List<Tenant4SuperVO> getListOfTenants(long portalId)
			throws ServiceException;

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
			throws ServiceException;

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
			throws ServiceException;

	public List<String> check4StoreTenant(long tenantId, String tenantName,
			String tenantURL, String extensionPrefix, String portalURL)
			throws ServiceException;

	public Tenant4SuperExtVO getTenant(long tenantId) throws ServiceException;

	public boolean deleteTenant(long tenantId) throws ServiceException;

	public List<GroupVO> getGroupsByTenantId(long tenantId)
			throws ServiceException;

	public List<ComponentDataVO> getVidyoProxyListByTenantId(long tenantId)
			throws ServiceException;

	public List<LocationTagVO> getLocationTagsByTenantId(long tenantId)
			throws ServiceException;

	public boolean createRoom(long tenantId, String description,
			String extension, String groupName, String name, String ownerName,
			String roomType, String roomPIN, String moderatorPIN)
			throws ServiceException;

	public boolean updateRoom(long tenantId, int roomID, String description,
			String extension, String groupName, String name, String ownerName,
			String roomType) throws ServiceException;

	public RoomVO getRoom(long tenantId, int roomID) throws ServiceException;

	public boolean deleteRoom(long tenantId, int roomID)
			throws ServiceException;

	public boolean removeRoomModeratorPin(long tenantId, long roomId)
			throws ServiceException;

	public boolean createRoomModeratorPin(long tenantId, long roomId,
			String pinCode) throws ServiceException;

	public boolean createGroup(long tenantId, String name, String description,
			String roomMaxUsers, String userMaxBandWidthIn,
			String userMaxBandWidthOut, boolean allowRecording)
			throws ServiceException;

	public boolean updateGroup(long tenantId, int groupId, String name,
			String description, String roomMaxUsers, String userMaxBandWidthIn,
			String userMaxBandWidthOut, boolean allowRecording)
			throws ServiceException;

	public boolean deleteGroup(long tenantId, int groupId)
			throws ServiceException;

	public GroupVO getGroup(long tenantId, int groupId) throws ServiceException;

	public List<TenantAndPortalConfigVO> getTenantsByPortalConfigId(
			long portalConfigId) throws ServiceException;

	public List<CdrVO> searchCdrs(long portalId, long tenantId, long memberId,
			Date startTime, Date endTime, int pageSize, int pageNo)
			throws ServiceException;

	public int countCdrs(long portalId, long tenantId, long memberId,
			Date startTime, Date endTime) throws ServiceException;

}
