package com.vccloud.portal.db.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.vccloud.portal.db.model.TCdr;
import com.vccloud.portal.db.model.TCdrExample;
import com.vccloud.portal.vo.CdrVO;

public class TCdrDAOImpl extends SqlMapClientDaoSupport implements TCdrDAO {

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public TCdrDAOImpl() {
        super();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public int countByExample(TCdrExample example) {
        Integer count = (Integer)  getSqlMapClientTemplate().queryForObject("t_cdr.ibatorgenerated_countByExample", example);
        return count.intValue();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public int deleteByExample(TCdrExample example) {
        int rows = getSqlMapClientTemplate().delete("t_cdr.ibatorgenerated_deleteByExample", example);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public int deleteByPrimaryKey(Long id) {
        TCdr key = new TCdr();
        key.setId(id);
        int rows = getSqlMapClientTemplate().delete("t_cdr.ibatorgenerated_deleteByPrimaryKey", key);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public Long insert(TCdr record) {
        Object newKey = getSqlMapClientTemplate().insert("t_cdr.ibatorgenerated_insert", record);
        return (Long) newKey;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public Long insertSelective(TCdr record) {
        Object newKey = getSqlMapClientTemplate().insert("t_cdr.ibatorgenerated_insertSelective", record);
        return (Long) newKey;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public List selectByExample(TCdrExample example) {
        List list = getSqlMapClientTemplate().queryForList("t_cdr.ibatorgenerated_selectByExample", example);
        return list;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public TCdr selectByPrimaryKey(Long id) {
        TCdr key = new TCdr();
        key.setId(id);
        TCdr record = (TCdr) getSqlMapClientTemplate().queryForObject("t_cdr.ibatorgenerated_selectByPrimaryKey", key);
        return record;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public int updateByExampleSelective(TCdr record, TCdrExample example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("t_cdr.ibatorgenerated_updateByExampleSelective", parms);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public int updateByExample(TCdr record, TCdrExample example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("t_cdr.ibatorgenerated_updateByExample", parms);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public int updateByPrimaryKeySelective(TCdr record) {
        int rows = getSqlMapClientTemplate().update("t_cdr.ibatorgenerated_updateByPrimaryKeySelective", record);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    public int updateByPrimaryKey(TCdr record) {
        int rows = getSqlMapClientTemplate().update("t_cdr.ibatorgenerated_updateByPrimaryKey", record);
        return rows;
    }

    /**
     * This class was generated by Apache iBATIS ibator.
     * This class corresponds to the database table t_cdr
     *
     * @ibatorgenerated Thu Feb 27 16:42:21 CST 2014
     */
    private static class UpdateByExampleParms extends TCdrExample {
        private Object record;

        public UpdateByExampleParms(Object record, TCdrExample example) {
            super(example);
            this.record = record;
        }

        public Object getRecord() {
            return record;
        }
	}

	public List<CdrVO> searchCdrsByMemberId(long portalId, long tenantId,
			long memberId, Date startTime, Date endTime, int startIndex,
			int pageSize) {
		Map params = new HashMap();
		params.put("portalId", portalId);
		params.put("tenantId", tenantId);
		params.put("memberId", memberId);
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		params.put("startIndex", startIndex);
		params.put("pageSize", pageSize);
		List<CdrVO> resultList = (List<CdrVO>) getSqlMapClientTemplate()
				.queryForList("t_cdr.searchCdrsByMemberId", params);
		return resultList;
	}

	public List<CdrVO> searchCdrsByTenantId(long portalId, long tenantId,
			Date startTime, Date endTime, int startIndex, int pageSize) {
		Map params = new HashMap();
		params.put("portalId", portalId);
		params.put("tenantId", tenantId);
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		params.put("startIndex", startIndex);
		params.put("pageSize", pageSize);
		List<CdrVO> resultList = (List<CdrVO>) getSqlMapClientTemplate()
				.queryForList("t_cdr.searchCdrsByTenantId", params);
		return resultList;
	}

	public List<CdrVO> searchCdrsByPortalId(long portalId, Date startTime,
			Date endTime, int startIndex, int pageSize) {
		Map params = new HashMap();
		params.put("portalId", portalId);
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		params.put("startIndex", startIndex);
		params.put("pageSize", pageSize);
		List<CdrVO> resultList = (List<CdrVO>) getSqlMapClientTemplate()
				.queryForList("t_cdr.searchCdrsByPortalId", params);
		return resultList;
	}

	public List<CdrVO> searchCdrsAll(Date startTime, Date endTime,
			int startIndex, int pageSize) {
		Map params = new HashMap();
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		params.put("startIndex", startIndex);
		params.put("pageSize", pageSize);
		List<CdrVO> resultList = (List<CdrVO>) getSqlMapClientTemplate()
				.queryForList("t_cdr.searchCdrsAll", params);
		return resultList;
	}

	public int countCdrsByMemberId(long portalId, long tenantId, long memberId,
			Date startTime, Date endTime) {
		Map params = new HashMap();
		params.put("portalId", portalId);
		params.put("tenantId", tenantId);
		params.put("memberId", memberId);
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject(
				"t_cdr.countCdrsByMemberId", params);
		return count.intValue();
	}

	public int countCdrsByTenantId(long portalId, long tenantId,
			Date startTime, Date endTime) {
		Map params = new HashMap();
		params.put("portalId", portalId);
		params.put("tenantId", tenantId);
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject(
				"t_cdr.countCdrsByTenantId", params);
		return count.intValue();
	}

	public int countCdrsByPortalId(long portalId, Date startTime, Date endTime) {
		Map params = new HashMap();
		params.put("portalId", portalId);
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject(
				"t_cdr.countCdrsByPortalId", params);
		return count.intValue();
	}

	public int countCdrsAll(Date startTime, Date endTime) {
		Map params = new HashMap();
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject(
				"t_cdr.countCdrsAll", params);
		return count.intValue();
	}
}