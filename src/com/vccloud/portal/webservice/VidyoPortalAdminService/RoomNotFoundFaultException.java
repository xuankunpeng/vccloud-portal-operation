
/**
 * RoomNotFoundFaultException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.vccloud.portal.webservice.VidyoPortalAdminService;

public class RoomNotFoundFaultException extends java.lang.Exception{

    private static final long serialVersionUID = 1385103217360L;
    
    private com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.RoomNotFoundFault faultMessage;

    
        public RoomNotFoundFaultException() {
            super("RoomNotFoundFaultException");
        }

        public RoomNotFoundFaultException(java.lang.String s) {
           super(s);
        }

        public RoomNotFoundFaultException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public RoomNotFoundFaultException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.RoomNotFoundFault msg){
       faultMessage = msg;
    }
    
    public com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.RoomNotFoundFault getFaultMessage(){
       return faultMessage;
    }
}
    