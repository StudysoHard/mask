package com.heyongqiang.zyz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heyongqiang.zyz.common.dto.XunJianPeopleDto;
import com.heyongqiang.zyz.common.vo.*;
import com.heyongqiang.zyz.mapper.ImgMapper;
import com.heyongqiang.zyz.mapper.UserMapper;
import com.heyongqiang.zyz.params.XunJianBeginParams;
import com.heyongqiang.zyz.params.XunJianFinalParams;
import com.heyongqiang.zyz.po.MaskImage;
import com.heyongqiang.zyz.po.User;
import com.heyongqiang.zyz.po.Xunjian;
import com.heyongqiang.zyz.service.XunjianService;
import com.heyongqiang.zyz.mapper.XunjianMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class XunjianServiceImpl extends ServiceImpl<XunjianMapper, Xunjian>
    implements XunjianService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private XunjianMapper xunjianMapper;

    @Resource
    private ImgMapper imgMapper;


    /**
     * 开始巡检的时候调用该方法开启巡检
     * @param xunJianBeginParams
     * @return
     */
    @Override
    public Result beginXunJian(XunJianBeginParams xunJianBeginParams) {
        // 查询用户信息
        User user = userMapper.searchUserInfoByName(xunJianBeginParams.getUserName());
        if(user == null){
            return Result.fail(ErrorCode.USER_NOEXIT.getCode() , ErrorCode.USER_NOEXIT.getMsg());
        }
        Xunjian xunjian = new Xunjian();
        xunjian.setCreateTime(new Date());
        xunjian.setUserId(user.getId());
        xunjian.setLatitude(xunJianBeginParams.getLatitude());
        xunjian.setLongitude(xunJianBeginParams.getLongitude());
        this.save(xunjian);
        // 返沪巡检id  结束巡检的时候传递
        return Result.success(xunjian.getId().toString());
    }

    /**
     * 正常结束巡检
     * @param xunJianId
     * @return
     */
    @Override
    public Result endXunJianNormal(XunJianFinalParams xunJianId) {
        // todo 开启巡检成功  关闭巡检失败
//        XunJianGroupByDto xunJianGroupByDto =  imgMapper.selectCountsByXunJianId(Long.parseLong(xunJianId));
        // 更新上次巡检的识别结果
        int update = xunjianMapper.updateFinalTimeById(Long.parseLong(xunJianId.getXunJianId()), xunJianId.getMask() , xunJianId.getNoMask() , new Date());
        if(update != 1){
            // 更新数据不为1
            return Result.fail(ErrorCode.SQL_UPDATE.getCode() , ErrorCode.SQL_UPDATE.getMsg());
        }
        return Result.success();
    }

    /**
     * 查询数据库中 该用户是否有未结束的巡检
     *  如果存在那么将查询上次巡检的识别结果返回
     * @param userName
     * @return
     */
    @Override
    public Result loginFindXunJianError(String userName) {
        User user = userMapper.searchUserInfoByName(userName);
        if(user == null){
            return Result.fail(ErrorCode.USER_NOEXIT.getCode() , ErrorCode.USER_NOEXIT.getMsg());
        }
        Long id = xunjianMapper.searchPreErrorLogout(user.getId());
        if(id == null){
            // 没有未完成的巡检
            return Result.fail();
        }
        // 获得上次巡检的开始时间
        Date beginTime = xunjianMapper.searchRecentBeginTime();
        // 通过开始时间以及用户id 查询拍摄结果集中的信息
        XunJianPeopleDto xunJianPeopleDto = imgMapper.searchPreLogOutPeopleNumber(beginTime , user.getId());
        ErrorExitXunJianInfoVo errorExitXunJianInfoVo = new ErrorExitXunJianInfoVo();
        BeanUtils.copyProperties(xunJianPeopleDto , errorExitXunJianInfoVo);
        // 设置巡检的id
        errorExitXunJianInfoVo.setXunJianId(id.toString());
        return Result.success(errorExitXunJianInfoVo);
    }

    /**
     * 获取该巡检的移动轨迹经纬度信息
     * @param id
     * @return
     */
    @Override
    public Result getXunJianMove(String id) {
        // 获取本次巡检的位置信息
        List<XunPositationVo> source = new ArrayList<>();
        LambdaQueryWrapper<MaskImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaskImage::getXunjian_id , Long.parseLong(id));
        queryWrapper.orderByAsc(MaskImage::getCatchTime);
        List<MaskImage> maskImages = imgMapper.selectList(queryWrapper);
        maskImages.forEach(
                e -> {
                    XunPositationVo xunPositationVo = new XunPositationVo();
                    xunPositationVo.setLatitude(e.getLatitude());
                    xunPositationVo.setLongitude(e.getLongitude());
                    xunPositationVo.setImgPath(e.getImgPath());
                    source.add(xunPositationVo);
                }
        );
        return Result.success(source);
    }

    @Override
    public Result getXunJianCounts(String id) {
        LambdaQueryWrapper<MaskImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaskImage::getXunjian_id , Long.parseLong(id));
        return Result.success(imgMapper.selectCount(queryWrapper));
    }


    /**
     * 根据id 查看巡检图片的
     * @param id
     * @return
     */
    @Override
    public Result getXunJianInfos(String id) {
        LambdaQueryWrapper<MaskImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaskImage::getXunjian_id , id);
        List<MaskImage> maskImages = imgMapper.selectList(queryWrapper);
        XunJianMapInfoVo xunJianMapInfoVo = new XunJianMapInfoVo();
        xunJianMapInfoVo.setImages(maskImages.stream().map( e -> {
            return e.getImgPath();
        }).collect(Collectors.toList()));
        maskImages = maskImages.stream().map(e -> {
            if(e.getLongitude().length() < 10 || e.getLatitude().length() < 10) return e;
            e.setLongitude(e.getLongitude().substring(0,10));
            e.setLatitude(e.getLatitude().substring(0,10));
            return e;
        }).collect(Collectors.toList());
        xunJianMapInfoVo.setInfos(maskImages);
        return Result.success(xunJianMapInfoVo);
    }


}




