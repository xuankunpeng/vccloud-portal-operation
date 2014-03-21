package com.vccloud.portal.service;

import java.util.List;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.vccloud.portal.db.model.TExtVidyoMember;
import com.vccloud.portal.db.model.TExtVidyoOperationAccount;
import com.vccloud.portal.db.model.TExtVidyoTenant;
import com.vccloud.portal.exception.ServiceException;
import com.vccloud.portal.vo.MemberVO;
import com.vccloud.portal.vo.TenantAndPortalInfoVO;

public interface UserService {

	public TExtVidyoOperationAccount signInByOperator(String name,
			String password) throws ServiceException;

	public TenantAndPortalInfoVO signInByTenant(String adminName,
			String adminPassword, String portalUrl) throws ServiceException;

	public TExtVidyoMember signInByMember(String name, String password,
			String portalUrl) throws ServiceException;

	public List<MemberVO> searchMembers(long tenantId, String name,
			int pageSize, int pageNo) throws ServiceException;

	public int countMembers(long tenantId, String name) throws ServiceException;

	public boolean updateMemberPasswordByTenant(long tenantId, long memberId,
			String newPassword) throws ServiceException;

	public boolean updateMemberDisplayNameByTenant(long tenantId,
			long memberId, String displayName) throws ServiceException;

	public boolean updateMemberPassword(long memberId, String password,
			String newPassword) throws ServiceException;

	public boolean updateMemberDisplayName(long memberId, String displayName)
			throws ServiceException;

	public boolean updateTenantPassword(long tenantId, String password,
			String newPassword) throws ServiceException;

	public boolean updateTenantDisplayName(long tenantId, String displayName)
			throws ServiceException;

	public TExtVidyoMember getMember(long memberId) throws ServiceException;

	public TExtVidyoTenant getTenant(long tenantId) throws ServiceException;

	public boolean createMember(long tenantId, String name, String password,
			String displayName, String email, String extension,
			String groupName, String proxyName, String locationTag,
			String language, String description, String roleName)
			throws ServiceException;

	public boolean updateMember(int memberId, String displayName, String email,
			String extension, String groupName, String proxyName,
			String locationTag, String language, String description,
			String roleName) throws ServiceException;

	public boolean deleteMember(int memberId) throws ServiceException;

	public boolean updateMemberPasswordByOperator(int memberId,
			String newPassword) throws ServiceException;

	public void importMember(long operatorId, DiskFileItem item,long tenantId,String extensionPrefix)throws ServiceException;

}
