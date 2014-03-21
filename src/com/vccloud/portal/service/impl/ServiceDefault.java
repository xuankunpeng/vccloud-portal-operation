package com.vccloud.portal.service.impl;

import com.vccloud.portal.db.dao.Clientinstallations2DAO;
import com.vccloud.portal.db.dao.Conferencecall2DAO;
import com.vccloud.portal.db.dao.TCdrDAO;
import com.vccloud.portal.db.dao.TExtVidyoMemberDAO;
import com.vccloud.portal.db.dao.TExtVidyoOperationAccountDAO;
import com.vccloud.portal.db.dao.TExtVidyoPortalConfigDAO;
import com.vccloud.portal.db.dao.TExtVidyoPortalDAO;
import com.vccloud.portal.db.dao.TExtVidyoTenantDAO;
import com.vccloud.portal.db.dao.TLegacyDAO;
import com.vccloud.portal.db.dao.TPortalInfoDAO;

public class ServiceDefault {

	protected TExtVidyoMemberDAO tExtVidyoMemberDAO;
	protected TExtVidyoTenantDAO tExtVidyoTenantDAO;
	protected Clientinstallations2DAO clientinstallations2DAO;
	protected Conferencecall2DAO conferencecall2DAO;
	protected TPortalInfoDAO tPortalInfoDAO;
	protected TExtVidyoPortalDAO tExtVidyoPortalDAO;
	protected TLegacyDAO tLegacyDAO;
	protected TExtVidyoOperationAccountDAO tExtVidyoOperationAccountDAO;
	protected TExtVidyoPortalConfigDAO tExtVidyoPortalConfigDAO;
	protected TCdrDAO tCdrDAO;

	public TExtVidyoMemberDAO gettExtVidyoMemberDAO() {
		return tExtVidyoMemberDAO;
	}

	public void settExtVidyoMemberDAO(TExtVidyoMemberDAO tExtVidyoMemberDAO) {
		this.tExtVidyoMemberDAO = tExtVidyoMemberDAO;
	}

	public TExtVidyoTenantDAO gettExtVidyoTenantDAO() {
		return tExtVidyoTenantDAO;
	}

	public void settExtVidyoTenantDAO(TExtVidyoTenantDAO tExtVidyoTenantDAO) {
		this.tExtVidyoTenantDAO = tExtVidyoTenantDAO;
	}

	public Clientinstallations2DAO getClientinstallations2DAO() {
		return clientinstallations2DAO;
	}

	public void setClientinstallations2DAO(
			Clientinstallations2DAO clientinstallations2dao) {
		clientinstallations2DAO = clientinstallations2dao;
	}

	public Conferencecall2DAO getConferencecall2DAO() {
		return conferencecall2DAO;
	}

	public void setConferencecall2DAO(Conferencecall2DAO conferencecall2dao) {
		conferencecall2DAO = conferencecall2dao;
	}

	public TPortalInfoDAO gettPortalInfoDAO() {
		return tPortalInfoDAO;
	}

	public void settPortalInfoDAO(TPortalInfoDAO tPortalInfoDAO) {
		this.tPortalInfoDAO = tPortalInfoDAO;
	}

	public TExtVidyoPortalDAO gettExtVidyoPortalDAO() {
		return tExtVidyoPortalDAO;
	}

	public void settExtVidyoPortalDAO(TExtVidyoPortalDAO tExtVidyoPortalDAO) {
		this.tExtVidyoPortalDAO = tExtVidyoPortalDAO;
	}

	public TLegacyDAO gettLegacyDAO() {
		return tLegacyDAO;
	}

	public void settLegacyDAO(TLegacyDAO tLegacyDAO) {
		this.tLegacyDAO = tLegacyDAO;
	}

	public TExtVidyoOperationAccountDAO gettExtVidyoOperationAccountDAO() {
		return tExtVidyoOperationAccountDAO;
	}

	public void settExtVidyoOperationAccountDAO(
			TExtVidyoOperationAccountDAO tExtVidyoOperationAccountDAO) {
		this.tExtVidyoOperationAccountDAO = tExtVidyoOperationAccountDAO;
	}

	public TExtVidyoPortalConfigDAO gettExtVidyoPortalConfigDAO() {
		return tExtVidyoPortalConfigDAO;
	}

	public void settExtVidyoPortalConfigDAO(
			TExtVidyoPortalConfigDAO tExtVidyoPortalConfigDAO) {
		this.tExtVidyoPortalConfigDAO = tExtVidyoPortalConfigDAO;
	}

	public TCdrDAO gettCdrDAO() {
		return tCdrDAO;
	}

	public void settCdrDAO(TCdrDAO tCdrDAO) {
		this.tCdrDAO = tCdrDAO;
	}

}
