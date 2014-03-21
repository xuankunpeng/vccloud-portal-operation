package com.vccloud.portal.db.dao;

import com.vccloud.portal.db.model.TExtVidyoPortal;
import com.vccloud.portal.db.model.TExtVidyoPortalExample;
import java.util.List;

public interface TExtVidyoPortalDAO {
    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_ext_vidyo_portal
     *
     * @ibatorgenerated Mon Dec 02 20:09:05 CST 2013
     */
    int countByExample(TExtVidyoPortalExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_ext_vidyo_portal
     *
     * @ibatorgenerated Mon Dec 02 20:09:05 CST 2013
     */
    int deleteByExample(TExtVidyoPortalExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_ext_vidyo_portal
     *
     * @ibatorgenerated Mon Dec 02 20:09:05 CST 2013
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_ext_vidyo_portal
     *
     * @ibatorgenerated Mon Dec 02 20:09:05 CST 2013
     */
    Long insert(TExtVidyoPortal record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_ext_vidyo_portal
     *
     * @ibatorgenerated Mon Dec 02 20:09:05 CST 2013
     */
    Long insertSelective(TExtVidyoPortal record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_ext_vidyo_portal
     *
     * @ibatorgenerated Mon Dec 02 20:09:05 CST 2013
     */
    List selectByExample(TExtVidyoPortalExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_ext_vidyo_portal
     *
     * @ibatorgenerated Mon Dec 02 20:09:05 CST 2013
     */
    TExtVidyoPortal selectByPrimaryKey(Long id);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_ext_vidyo_portal
     *
     * @ibatorgenerated Mon Dec 02 20:09:05 CST 2013
     */
    int updateByExampleSelective(TExtVidyoPortal record, TExtVidyoPortalExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_ext_vidyo_portal
     *
     * @ibatorgenerated Mon Dec 02 20:09:05 CST 2013
     */
    int updateByExample(TExtVidyoPortal record, TExtVidyoPortalExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_ext_vidyo_portal
     *
     * @ibatorgenerated Mon Dec 02 20:09:05 CST 2013
     */
    int updateByPrimaryKeySelective(TExtVidyoPortal record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table t_ext_vidyo_portal
     *
     * @ibatorgenerated Mon Dec 02 20:09:05 CST 2013
     */
    int updateByPrimaryKey(TExtVidyoPortal record);
}