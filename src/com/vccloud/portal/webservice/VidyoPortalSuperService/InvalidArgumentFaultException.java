
/**
 * InvalidArgumentFaultException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.vccloud.portal.webservice.VidyoPortalSuperService;

public class InvalidArgumentFaultException extends java.lang.Exception{

    private static final long serialVersionUID = 1380280526186L;
    
    private com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.InvalidArgumentFault faultMessage;

    
        public InvalidArgumentFaultException() {
            super("InvalidArgumentFaultException");
        }

        public InvalidArgumentFaultException(java.lang.String s) {
           super(s);
        }

        public InvalidArgumentFaultException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public InvalidArgumentFaultException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.InvalidArgumentFault msg){
       faultMessage = msg;
    }
    
    public com.vccloud.portal.webservice.VidyoPortalSuperService.VidyoPortalSuperServiceStub.InvalidArgumentFault getFaultMessage(){
       return faultMessage;
    }
}
    