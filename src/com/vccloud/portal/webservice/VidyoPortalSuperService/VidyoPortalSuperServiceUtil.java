package com.vccloud.portal.webservice.VidyoPortalSuperService;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties.Authenticator;
import org.apache.log4j.Logger;

import com.vccloud.portal.exception.CallVidyoWebServiceException;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.ComponentData;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.CreateTenantRequest;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.CreateTenantResponse;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.DeleteTenantRequest;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.DeleteTenantResponse;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.EntityID;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.GetLicenseDataRequest;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.GetLicenseDataResponse;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.GetLocationTagsRequest;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.GetLocationTagsResponse;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.GetServiceComponentsDataRequest;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.GetServiceComponentsDataResponse;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.GetTenantDetailsRequest;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.GetTenantDetailsResponse;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.IntHolder;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.LicenseFeatureData;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.ListTenantsRequest;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.ListTenantsResponse;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.LocationTag;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.NonNegativeInt;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.SingleTenantDataType;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.String20;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.TenantDataExtType;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.TenantDataType;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.TenantExtensionPrefixPattern;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.TenantNamePattern;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.TenantUrlPattern;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.UpdateTenantRequest;
import com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.UpdateTenantResponse;

public class VidyoPortalSuperServiceUtil {

	private static Logger logger = Logger
			.getLogger(VidyoPortalSuperServiceUtil.class);

	private static VidyoPortalSuperServiceStub createStub(String portalURL,
			String username, String password) throws AxisFault {
		try {
			VidyoPortalSuperServiceStub stub = null;
			if (CommonUtil.isNullOrEmpty(portalURL)) {
				stub = new VidyoPortalSuperServiceStub();
			} else {
				if (portalURL.endsWith("/")) {
					portalURL = portalURL.substring(0, portalURL.length() - 1);
				}
				if (portalURL.toLowerCase().startsWith("http://")) {
					stub = new VidyoPortalSuperServiceStub(portalURL
							+ "/services/VidyoPortalSuperService/");
				} else {
					stub = new VidyoPortalSuperServiceStub("http://"
							+ portalURL + "/services/VidyoPortalSuperService/");
				}
			}
			List<String> authSchemes = new ArrayList<String>();
			authSchemes.add(Authenticator.BASIC);
			Authenticator authenticator = new Authenticator();
			authenticator.setAuthSchemes(authSchemes);
			authenticator.setUsername(username);
			authenticator.setPassword(password);
			authenticator.setPreemptiveAuthentication(true);
			stub._getServiceClient().getOptions().setProperty(
					HTTPConstants.AUTHENTICATE, authenticator);
			return stub;
		} catch (AxisFault e) {
			logger.error("", e);
			throw e;
		}
	}

	private static void clearStub(VidyoPortalSuperServiceStub stub)
			throws AxisFault {
		if (stub != null) {
			try {
				stub._getServiceClient().cleanupTransport();
				stub._getServiceClient().cleanup();
				stub.cleanup();
				stub = null;
			} catch (AxisFault e) {
				logger.error("", e);
				throw e;
			}
		}
	}

	public static TenantDataExtType getTenantDetails(String portalURL,
			String portalName, String portalPassword, int tenantID)
			throws CallVidyoWebServiceException {
		try {
			VidyoPortalSuperServiceStub stub = createStub(portalURL,
					portalName, portalPassword);

			GetTenantDetailsRequest request = new GetTenantDetailsRequest();
			EntityID entityID = new EntityID();
			entityID.setEntityID(tenantID);
			request.setTenantId(entityID);
			GetTenantDetailsResponse response = stub.getTenantDetails(request);

			clearStub(stub);
			return response.getTenantDetail();
		} catch (AxisFault e) {
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (RemoteException e) {
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (GeneralFaultException e) {
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (InvalidTenantFaultException e) {
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		}
	}

	public static boolean updateTenantName(String portalURL, String portalName,
			String portalPassword, int tenantID, String tenantName)
			throws CallVidyoWebServiceException {
		TenantDataExtType tenant = getTenantDetails(portalURL, portalName,
				portalPassword, tenantID);
		if (tenant == null) {
			return false;
		}

		try {
			VidyoPortalSuperServiceStub stub = createStub(portalURL,
					portalName, portalPassword);

			UpdateTenantRequest request = new UpdateTenantRequest();
			TenantNamePattern pattern = new TenantNamePattern();
			pattern.setTenantNamePattern(tenantName);
			tenant.setTenantName(pattern);
			request.setTenantData(tenant);
			UpdateTenantResponse response = stub.updateTenant(request);

			clearStub(stub);
			return "OK".equalsIgnoreCase(response.getOK().getValue());
		} catch (AxisFault e) {
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (RemoteException e) {
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (NotLicensedFaultException e) {
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (InvalidArgumentFaultException e) {
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (GeneralFaultException e) {
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (ExistingTenantFaultException e) {
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (InvalidTenantFaultException e) {
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		}
	}

	public static SingleTenantDataType[] getListOfTenants(String portalURL,
			String portalName, String portalPassword)
			throws CallVidyoWebServiceException {
		try {
			VidyoPortalSuperServiceStub stub = createStub(portalURL,
					portalName, portalPassword);

			ListTenantsRequest request = new ListTenantsRequest();
			IntHolder limit = new IntHolder();
			limit.setIntHolder(Integer.MAX_VALUE);
			request.setLimit(limit);
			ListTenantsResponse response = stub.getListOfTenants(request);

			clearStub(stub);
			return response.getTenant();
		} catch (AxisFault e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getListOfTenants() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (RemoteException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getListOfTenants() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (GeneralFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getListOfTenants() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		}
	}

	public static ComponentData[] getServiceComponentsData(String portalURL,
			String portalName, String portalPassword)
			throws CallVidyoWebServiceException {
		try {
			VidyoPortalSuperServiceStub stub = createStub(portalURL,
					portalName, portalPassword);

			GetServiceComponentsDataRequest request = new GetServiceComponentsDataRequest();
			GetServiceComponentsDataResponse response = stub
					.getServiceComponentsData(request);

			clearStub(stub);
			return response.getComponent();
		} catch (AxisFault e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getServiceComponentsData() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (RemoteException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getServiceComponentsData() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (InvalidArgumentFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getServiceComponentsData() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (GeneralFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getServiceComponentsData() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		}
	}

	public static LicenseFeatureData[] getLicenseData(String portalURL,
			String portalName, String portalPassword, int tenantId)
			throws CallVidyoWebServiceException {
		try {
			VidyoPortalSuperServiceStub stub = createStub(portalURL,
					portalName, portalPassword);

			GetLicenseDataRequest request = new GetLicenseDataRequest();
			if (tenantId > 0) {
				EntityID entityID = new EntityID();
				entityID.setEntityID(tenantId);
				request.setTenantId(entityID);
			}
			GetLicenseDataResponse response = stub.getLicenseData(request);

			clearStub(stub);
			return response.getLicenseFeature();
		} catch (AxisFault e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getLicenseData() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: "
							+ portalPassword
							+ ", tenantId: " + tenantId + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (RemoteException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getLicenseData() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: "
							+ portalPassword
							+ ", tenantId: " + tenantId + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (NotLicensedFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getLicenseData() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: "
							+ portalPassword
							+ ", tenantId: " + tenantId + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (GeneralFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getLicenseData() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: "
							+ portalPassword
							+ ", tenantId: " + tenantId + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (NotAuthorizedFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getLicenseData() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: "
							+ portalPassword
							+ ", tenantId: " + tenantId + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (InvalidTenantFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getLicenseData() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: "
							+ portalPassword
							+ ", tenantId: " + tenantId + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		}
	}

	public static LocationTag[] getLocationTags(String portalURL,
			String portalName, String portalPassword)
			throws CallVidyoWebServiceException {
		try {
			VidyoPortalSuperServiceStub stub = createStub(portalURL,
					portalName, portalPassword);

			GetLocationTagsRequest request = new GetLocationTagsRequest();
			GetLocationTagsResponse response = stub.getLocationTags(request);

			clearStub(stub);
			return response.getLocationTagsList();
		} catch (AxisFault e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getLocationTags() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (RemoteException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getLocationTags() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (GeneralFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.getLocationTags() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		}
	}

	public static boolean createTenant(String portalURL, String portalName,
			String portalPassword, String tenantName, String tenantURL,
			String extensionPrefix, String dialinNumber, String vidyoReplayUrl,
			String description, int numOfInstalls, int numOfSeats,
			int numOfLines, int numOfExecutives, int numOfPanoramas,
			boolean enableGuestLogin, List<Integer> allowedTenantList,
			int vidyoManager, List<Integer> vidyoProxyList,
			List<Integer> allowedVidyoGatewayList,
			List<Integer> allowedVidyoReplayRecorderList,
			List<Integer> allowedVidyoReplayList,
			List<Integer> allowedLocationTagList, boolean vidyoMobileAllowed,
			boolean ipcAllowOutbound, boolean ipcAllowInbound)
			throws CallVidyoWebServiceException {
		try {
			VidyoPortalSuperServiceStub stub = createStub(portalURL,
					portalName, portalPassword);

			CreateTenantRequest request = new CreateTenantRequest();
			TenantDataType tenantDataType = new TenantDataType();
			if (!CommonUtil.isNullOrEmpty(tenantName)) {
				TenantNamePattern tenantName2 = new TenantNamePattern();
				tenantName2.setTenantNamePattern(tenantName);
				tenantDataType.setTenantName(tenantName2);
			}
			if (!CommonUtil.isNullOrEmpty(tenantURL)) {
				TenantUrlPattern tenantURL2 = new TenantUrlPattern();
				tenantURL2.setTenantUrlPattern(tenantURL);
				tenantDataType.setTenantUrl(tenantURL2);
			}
			if (!CommonUtil.isNullOrEmpty(extensionPrefix)) {
				TenantExtensionPrefixPattern extensionPrefix2 = new TenantExtensionPrefixPattern();
				extensionPrefix2
						.setTenantExtensionPrefixPattern(extensionPrefix);
				tenantDataType.setExtensionPrefix(extensionPrefix2);
			}
			if (!CommonUtil.isNullOrEmpty(dialinNumber)) {
				String20 dialinNumber2 = new String20();
				dialinNumber2.setString20(dialinNumber);
				tenantDataType.setDialinNumber(dialinNumber2);
			}
			if (!CommonUtil.isNullOrEmpty(vidyoReplayUrl)) {
				TenantUrlPattern vidyoReplayUrl2 = new TenantUrlPattern();
				vidyoReplayUrl2.setTenantUrlPattern(vidyoReplayUrl);
				tenantDataType.setVidyoReplayUrl(vidyoReplayUrl2);
			}
			if (!CommonUtil.isNullOrEmpty(description)) {
				tenantDataType.setDescription(description);
			}
			NonNegativeInt numOfInstalls2 = new NonNegativeInt();
			numOfInstalls2.setNonNegativeInt(numOfInstalls);
			tenantDataType.setNumOfInstalls(numOfInstalls2);
			NonNegativeInt numOfSeats2 = new NonNegativeInt();
			numOfSeats2.setNonNegativeInt(numOfSeats);
			tenantDataType.setNumOfSeats(numOfSeats2);
			NonNegativeInt numOfLines2 = new NonNegativeInt();
			numOfLines2.setNonNegativeInt(numOfLines);
			tenantDataType.setNumOfLines(numOfLines2);
			NonNegativeInt numOfExecutives2 = new NonNegativeInt();
			numOfExecutives2.setNonNegativeInt(numOfExecutives);
			tenantDataType.setNumOfExecutives(numOfExecutives2);
			NonNegativeInt numOfPanoramas2 = new NonNegativeInt();
			numOfPanoramas2.setNonNegativeInt(numOfPanoramas);
			tenantDataType.setNumOfPanoramas(numOfPanoramas2);
			tenantDataType.setEnableGuestLogin(enableGuestLogin);
			int[] allowedTenantList2 = CommonUtil
					.convertFromListToArray(allowedTenantList);
			tenantDataType.setAllowedTenantList(allowedTenantList2);
			tenantDataType.setVidyoManager(vidyoManager);
			int[] vidyoProxyList2 = CommonUtil
					.convertFromListToArray(vidyoProxyList);
			tenantDataType.setVidyoProxyList(vidyoProxyList2);
			int[] allowedVidyoGatewayList2 = CommonUtil
					.convertFromListToArray(allowedVidyoGatewayList);
			tenantDataType.setAllowedVidyoGatewayList(allowedVidyoGatewayList2);
			int[] allowedVidyoReplayRecorderList2 = CommonUtil
					.convertFromListToArray(allowedVidyoReplayRecorderList);
			tenantDataType
					.setAllowedVidyoReplayRecorderList(allowedVidyoReplayRecorderList2);
			int[] allowedVidyoReplayList2 = CommonUtil
					.convertFromListToArray(allowedVidyoReplayList);
			tenantDataType.setAllowedVidyoRepalyList(allowedVidyoReplayList2);
			int[] allowedLocationTagList2 = CommonUtil
					.convertFromListToArray(allowedLocationTagList);
			tenantDataType.setAllowedLocationTagList(allowedLocationTagList2);
			tenantDataType.setVidyoMobileAllowed(vidyoMobileAllowed);
			tenantDataType.setIpcAllowOutbound(ipcAllowOutbound);
			tenantDataType.setIpcAllowInbound(ipcAllowInbound);
			request.setTenantData(tenantDataType);
			CreateTenantResponse response = stub.createTenant(request);

			clearStub(stub);
			return "OK".equalsIgnoreCase(response.getOK().getValue());
		} catch (AxisFault e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.createTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (RemoteException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.createTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (NotLicensedFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.createTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (InvalidArgumentFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.createTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (GeneralFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.createTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (ExistingTenantFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.createTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		}
	}

	public static boolean updateTenant(int tenantID, String portalURL,
			String portalName, String portalPassword, String tenantName,
			String tenantURL, String extensionPrefix, String dialinNumber,
			String vidyoReplayUrl, String description, int numOfInstalls,
			int numOfSeats, int numOfLines, int numOfExecutives,
			int numOfPanoramas, boolean enableGuestLogin,
			List<Integer> allowedTenantList, int vidyoManager,
			List<Integer> vidyoProxyList,
			List<Integer> allowedVidyoGatewayList,
			List<Integer> allowedVidyoReplayRecorderList,
			List<Integer> allowedVidyoReplayList,
			List<Integer> allowedLocationTagList, boolean vidyoMobileAllowed,
			boolean ipcAllowOutbound, boolean ipcAllowInbound)
			throws CallVidyoWebServiceException {
		try {
			VidyoPortalSuperServiceStub stub = createStub(portalURL,
					portalName, portalPassword);

			UpdateTenantRequest request = new UpdateTenantRequest();
			TenantDataExtType tenantDataExtType = new TenantDataExtType();
			EntityID entityID = new EntityID();
			entityID.setEntityID(tenantID);
			tenantDataExtType.setTenantID(entityID);
			if (!CommonUtil.isNullOrEmpty(tenantName)) {
				TenantNamePattern tenantName2 = new TenantNamePattern();
				tenantName2.setTenantNamePattern(tenantName);
				tenantDataExtType.setTenantName(tenantName2);
			}
			if (!CommonUtil.isNullOrEmpty(tenantURL)) {
				TenantUrlPattern tenantURL2 = new TenantUrlPattern();
				tenantURL2.setTenantUrlPattern(tenantURL);
				tenantDataExtType.setTenantUrl(tenantURL2);
			}
			if (!CommonUtil.isNullOrEmpty(extensionPrefix)) {
				TenantExtensionPrefixPattern extensionPrefix2 = new TenantExtensionPrefixPattern();
				extensionPrefix2
						.setTenantExtensionPrefixPattern(extensionPrefix);
				tenantDataExtType.setExtensionPrefix(extensionPrefix2);
			}
			if (!CommonUtil.isNullOrEmpty(dialinNumber)) {
				String20 dialinNumber2 = new String20();
				dialinNumber2.setString20(dialinNumber);
				tenantDataExtType.setDialinNumber(dialinNumber2);
			}
			if (!CommonUtil.isNullOrEmpty(vidyoReplayUrl)) {
				TenantUrlPattern vidyoReplayUrl2 = new TenantUrlPattern();
				vidyoReplayUrl2.setTenantUrlPattern(vidyoReplayUrl);
				tenantDataExtType.setVidyoReplayUrl(vidyoReplayUrl2);
			}
			if (!CommonUtil.isNullOrEmpty(description)) {
				tenantDataExtType.setDescription(description);
			}
			NonNegativeInt numOfInstalls2 = new NonNegativeInt();
			numOfInstalls2.setNonNegativeInt(numOfInstalls);
			tenantDataExtType.setNumOfInstalls(numOfInstalls2);
			NonNegativeInt numOfSeats2 = new NonNegativeInt();
			numOfSeats2.setNonNegativeInt(numOfSeats);
			tenantDataExtType.setNumOfSeats(numOfSeats2);
			NonNegativeInt numOfLines2 = new NonNegativeInt();
			numOfLines2.setNonNegativeInt(numOfLines);
			tenantDataExtType.setNumOfLines(numOfLines2);
			NonNegativeInt numOfExecutives2 = new NonNegativeInt();
			numOfExecutives2.setNonNegativeInt(numOfExecutives);
			tenantDataExtType.setNumOfExecutives(numOfExecutives2);
			NonNegativeInt numOfPanoramas2 = new NonNegativeInt();
			numOfPanoramas2.setNonNegativeInt(numOfPanoramas);
			tenantDataExtType.setNumOfPanoramas(numOfPanoramas2);
			tenantDataExtType.setEnableGuestLogin(enableGuestLogin);
			int[] allowedTenantList2 = CommonUtil
					.convertFromListToArray(allowedTenantList);
			tenantDataExtType.setAllowedTenantList(allowedTenantList2);
			tenantDataExtType.setVidyoManager(vidyoManager);
			int[] vidyoProxyList2 = CommonUtil
					.convertFromListToArray(vidyoProxyList);
			tenantDataExtType.setVidyoProxyList(vidyoProxyList2);
			int[] allowedVidyoGatewayList2 = CommonUtil
					.convertFromListToArray(allowedVidyoGatewayList);
			tenantDataExtType
					.setAllowedVidyoGatewayList(allowedVidyoGatewayList2);
			int[] allowedVidyoReplayRecorderList2 = CommonUtil
					.convertFromListToArray(allowedVidyoReplayRecorderList);
			tenantDataExtType
					.setAllowedVidyoReplayRecorderList(allowedVidyoReplayRecorderList2);
			int[] allowedVidyoReplayList2 = CommonUtil
					.convertFromListToArray(allowedVidyoReplayList);
			tenantDataExtType
					.setAllowedVidyoRepalyList(allowedVidyoReplayList2);
			int[] allowedLocationTagList2 = CommonUtil
					.convertFromListToArray(allowedLocationTagList);
			tenantDataExtType
					.setAllowedLocationTagList(allowedLocationTagList2);
			tenantDataExtType.setVidyoMobileAllowed(vidyoMobileAllowed);
			tenantDataExtType.setIpcAllowOutbound(ipcAllowOutbound);
			tenantDataExtType.setIpcAllowInbound(ipcAllowInbound);
			request.setTenantData(tenantDataExtType);
			UpdateTenantResponse response = stub.updateTenant(request);

			clearStub(stub);
			return "OK".equalsIgnoreCase(response.getOK().getValue());
		} catch (AxisFault e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.updateTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (RemoteException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.updateTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (NotLicensedFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.updateTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (InvalidArgumentFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.updateTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (GeneralFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.updateTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (ExistingTenantFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.updateTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (InvalidTenantFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.updateTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: " + portalPassword + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		}
	}

	public static boolean deleteTenant(String portalURL, String portalName,
			String portalPassword, int tenantID)
			throws CallVidyoWebServiceException {
		try {
			VidyoPortalSuperServiceStub stub = createStub(portalURL,
					portalName, portalPassword);

			DeleteTenantRequest request = new DeleteTenantRequest();
			EntityID entityID = new EntityID();
			entityID.setEntityID(tenantID);
			request.setTenantId(entityID);
			DeleteTenantResponse response = stub.deleteTenant(request);

			clearStub(stub);
			return "OK".equalsIgnoreCase(response.getOK().getValue());
		} catch (AxisFault e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.deleteTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: "
							+ portalPassword
							+ ", tenantID: " + tenantID + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (RemoteException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.deleteTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: "
							+ portalPassword
							+ ", tenantID: " + tenantID + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (GeneralFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.deleteTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: "
							+ portalPassword
							+ ", tenantID: " + tenantID + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		} catch (InvalidTenantFaultException e) {
			logger
					.error("==================> VidyoPortalSuperServiceUtil.deleteTenant() caught exception, [ portalURL: "
							+ portalURL
							+ ", portalName: "
							+ portalName
							+ ", portalPassword: "
							+ portalPassword
							+ ", tenantID: " + tenantID + " ]");
			logger.error("", e);
			throw new CallVidyoWebServiceException("");
		}
	}

}
