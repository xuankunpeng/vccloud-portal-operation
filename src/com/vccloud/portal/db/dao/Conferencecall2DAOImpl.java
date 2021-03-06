package com.vccloud.portal.db.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.vccloud.portal.db.model.Conferencecall2;
import com.vccloud.portal.db.model.Conferencecall2Example;
import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.vo.ConferenceCall2VO;

public class Conferencecall2DAOImpl extends SqlMapClientDaoSupport implements Conferencecall2DAO {

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public Conferencecall2DAOImpl() {
        super();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public int countByExample(Conferencecall2Example example) {
        Integer count = (Integer)  getSqlMapClientTemplate().queryForObject("ConferenceCall2.ibatorgenerated_countByExample", example);
        return count.intValue();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public int deleteByExample(Conferencecall2Example example) {
        int rows = getSqlMapClientTemplate().delete("ConferenceCall2.ibatorgenerated_deleteByExample", example);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public int deleteByPrimaryKey(Integer callid) {
        Conferencecall2 key = new Conferencecall2();
        key.setCallid(callid);
        int rows = getSqlMapClientTemplate().delete("ConferenceCall2.ibatorgenerated_deleteByPrimaryKey", key);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public void insert(Conferencecall2 record) {
        getSqlMapClientTemplate().insert("ConferenceCall2.ibatorgenerated_insert", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public void insertSelective(Conferencecall2 record) {
        getSqlMapClientTemplate().insert("ConferenceCall2.ibatorgenerated_insertSelective", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public List selectByExample(Conferencecall2Example example) {
        List list = getSqlMapClientTemplate().queryForList("ConferenceCall2.ibatorgenerated_selectByExample", example);
        return list;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public Conferencecall2 selectByPrimaryKey(Integer callid) {
        Conferencecall2 key = new Conferencecall2();
        key.setCallid(callid);
        Conferencecall2 record = (Conferencecall2) getSqlMapClientTemplate().queryForObject("ConferenceCall2.ibatorgenerated_selectByPrimaryKey", key);
        return record;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public int updateByExampleSelective(Conferencecall2 record, Conferencecall2Example example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("ConferenceCall2.ibatorgenerated_updateByExampleSelective", parms);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public int updateByExample(Conferencecall2 record, Conferencecall2Example example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("ConferenceCall2.ibatorgenerated_updateByExample", parms);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public int updateByPrimaryKeySelective(Conferencecall2 record) {
        int rows = getSqlMapClientTemplate().update("ConferenceCall2.ibatorgenerated_updateByPrimaryKeySelective", record);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    public int updateByPrimaryKey(Conferencecall2 record) {
        int rows = getSqlMapClientTemplate().update("ConferenceCall2.ibatorgenerated_updateByPrimaryKey", record);
        return rows;
    }

    /**
     * This class was generated by Apache iBATIS ibator.
     * This class corresponds to the database table ConferenceCall2
     *
     * @ibatorgenerated Thu Sep 05 15:50:58 CST 2013
     */
    private static class UpdateByExampleParms extends Conferencecall2Example {
        private Object record;

        public UpdateByExampleParms(Object record, Conferencecall2Example example) {
            super(example);
            this.record = record;
        }

        public Object getRecord() {
            return record;
        }
    }

	public List<Conferencecall2> searchConferenceCall2s(String tenantName,
			String callerName, Date startTime, Date endTime,
			String orderByClause, int startIndex, int pageSize) {
		Map params = new HashMap();
		params.put("callState", "COMPLETED");
		if (!CommonUtil.isNullOrEmpty(tenantName)) {
			params.put("tenantName", tenantName);
		}
		if (!CommonUtil.isNullOrEmpty(callerName)) {
			params.put("callerName", callerName);
		}
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		if (!CommonUtil.isNullOrEmpty(orderByClause)) {
			params.put("orderByClause", orderByClause);
		}
		params.put("startIndex", startIndex);
		params.put("pageSize", pageSize);
		List<Conferencecall2> resultList = (List<Conferencecall2>) getSqlMapClientTemplate()
				.queryForList("ConferenceCall2.searchConferenceCall2s", params);
		return resultList;
	}

	public int countConferenceCall2s(String tenantName, String callerName,
			Date startTime, Date endTime) {
		Map params = new HashMap();
		params.put("callState", "COMPLETED");
		if (!CommonUtil.isNullOrEmpty(tenantName)) {
			params.put("tenantName", tenantName);
		}
		if (!CommonUtil.isNullOrEmpty(callerName)) {
			params.put("callerName", callerName);
		}
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject(
				"ConferenceCall2.countConferenceCall2s", params);
		return count.intValue();
	}
}