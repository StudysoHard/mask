package com.heyongqiang.zyz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heyongqiang.zyz.common.dto.*;
import com.heyongqiang.zyz.po.MaskImage;
import com.heyongqiang.zyz.params.FaceMaskImagePathParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ImgMapper extends BaseMapper<MaskImage> {
    /**
     * 插入初始化的img数据
     * @param maskImage
     */
    void insertMaskImg(@Param("maskImage")MaskImage maskImage);


    /**
     * 通过用户名和初始时间查询巡检的图片
     * @param beginData
     * @param userName
     * @return
     */
    List<String> selectImgByDate(@Param("beginData")String beginData,@Param("userName") String userName);

    String selectPreImgPathById(@Param("id") Long id);

    int updateImgPathById(@Param("path") FaceMaskImagePathParams path);

    List<TopCameraCountsDto> peopleCatchCounts(@Param("id") Long id);

    XunJianPeopleDto searchPreLogOutPeopleNumber(@Param("beginTime") Date beginTime,@Param("id") Long id);

    List<TopXunJainCountsDto> xunJianCatchCounts(@Param("id") Long id);

    List<TopCameraCountsDto> userTopCount();

    XunJianGroupByDto selectCountsByXunJianId(long xunJianId);

    List<ImgPathQueryDto> selectImgByUserId(@Param("id") Long id);
}
