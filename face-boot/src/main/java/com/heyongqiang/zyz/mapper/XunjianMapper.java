package com.heyongqiang.zyz.mapper;

import com.heyongqiang.zyz.po.Xunjian;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @Entity com.heyongqiang.zyz.po.Xunjian
 */
public interface XunjianMapper extends BaseMapper<Xunjian> {

    int updateFinalTimeById(@Param("xunJianId") Long xunJianId,@Param("mask") Integer mask ,@Param("noMask") Integer noMask ,@Param("date") Date date);

    Long searchPreErrorLogout(@Param("id") Long id);

    Date searchRecentBeginTime();

}




