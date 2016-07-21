package com.app.data;

import java.util.List;

/**
 * Created by ningd on 2015/12/1.
 */
public class City {
    /**
     * custList : [{"custID":"guian","custName":"贵安新区"}]
     * message : 查询成功
     * orgList : [{"gateway":"","orgID":308,"orgName":"贵安新区"}]
     * status : success
     */

    private String message;
    private String status;
    /**
     * custID : guian
     * custName : 贵安新区
     */

    private List<CustListEntity> custList;
    /**
     * gateway :
     * orgID : 308
     * orgName : 贵安新区
     */

    private List<OrgListEntity> orgList;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCustList(List<CustListEntity> custList) {
        this.custList = custList;
    }

    public void setOrgList(List<OrgListEntity> orgList) {
        this.orgList = orgList;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public List<CustListEntity> getCustList() {
        return custList;
    }

    public List<OrgListEntity> getOrgList() {
        return orgList;
    }

    public static class CustListEntity {
        private String custID;
        private String custName;

        public void setCustID(String custID) {
            this.custID = custID;
        }

        public void setCustName(String custName) {
            this.custName = custName;
        }

        public String getCustID() {
            return custID;
        }

        public String getCustName() {
            return custName;
        }
    }

    public static class OrgListEntity {
        private String gateway;
        private String orgID;
        private String orgName;

        public void setGateway(String gateway) {
            this.gateway = gateway;
        }

        public void setOrgID(String orgID) {
            this.orgID = orgID;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getGateway() {
            return gateway;
        }

        public String getOrgID() {
            return orgID;
        }

        public String getOrgName() {
            return orgName;
        }
    }
}
