package com.heyongqiang.zyz.service.impl;

import com.heyongqiang.zyz.common.dto.TopCameraCountsDto;
import com.heyongqiang.zyz.common.dto.TopXunJainCountsDto;
import com.heyongqiang.zyz.common.vo.*;
import com.heyongqiang.zyz.mapper.ImgMapper;
import com.heyongqiang.zyz.mapper.UserMapper;
import com.heyongqiang.zyz.po.MaskImage;
import com.heyongqiang.zyz.po.User;
import com.heyongqiang.zyz.service.UchartsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class UchartsServiceImpl implements UchartsService {

    @Resource
    private ImgMapper imgMapper;

    @Resource
    private UserMapper userMapper;

    private final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("MM-dd");


    /**
     * 获取该用户的所有图表数据
     * @param userName
     * @return
     */
    @Override
    public Result allCharts(String userName) {
        UChartsVo uChartsVo = new UChartsVo();
        // 通过用户名查询id
        User user = userMapper.searchUserInfoByName(userName);
        if(user == null){
            return Result.fail(ErrorCode.USER_NOEXIT.getCode() , ErrorCode.USER_NOEXIT.getMsg());
        }
        // 获取全部管理的摄像头的捕捉次数
        List<TopCameraCountsDto> cameraCatch = imgMapper.peopleCatchCounts(user.getId());
        CharOprtColumnVo charOprtColumnVo = new CharOprtColumnVo();
        if(cameraCatch != null){
            charOprtColumnVo.setCategories(cameraCatch.stream().map(e -> e.getId().toString()).toArray(String[]::new));
            charOprtColumnVo.setSeries(
                    cameraCatch.stream().map(TopCameraCountsDto::getCounts).mapToInt(Integer::valueOf).toArray()
                    ,"摄像头排行榜");
        }
        /**
         * 这里查询的应该是
         *    按照最近巡检时间分布的人数
         *  返回的信息默认按照了时间排序
         *   --
         */
        List<TopXunJainCountsDto> xunJianList = imgMapper.xunJianCatchCounts(user.getId());
        CharOprtMountVo charOprtMountVo = new CharOprtMountVo();
        ChartNameValueVo[] xunjianList = new ChartNameValueVo[5];
        HashMap<String,Integer> map = new HashMap<>();
        for (int i = 0; i < xunJianList.size(); i++) {
            ChartNameValueVo temp = new ChartNameValueVo();
            TopXunJainCountsDto e = xunJianList.get(i);
            String data = DATEFORMAT.format(e.getCatchTime());
            if(!map.containsKey(data)) {
                map.put(data , 1);
                temp.setData(data+"--1");
            } else {
                map.put(data,map.get(data) + 1);
                temp.setData(data +"--" + map.get(data));
            }
            temp.setName(e.getXunjianId().toString());
            temp.setValue(e.getCounts());
            xunjianList[i] = temp;
        }
        charOprtMountVo.setSeries(xunjianList);
        /**
         * 花瓣图
         *      用户识别人数排行榜
         */
        List<TopCameraCountsDto> userCounts =  imgMapper.userTopCount();
        CharOprtRoseVo charOprtRoseVo = new CharOprtRoseVo();
        ChartNameValueVo[] userList = new ChartNameValueVo[userCounts.size()];
        for (int i = 0; i < userCounts.size(); i++) {
            ChartNameValueVo userTemp = new ChartNameValueVo();
            TopCameraCountsDto e = userCounts.get(i);
            userTemp.setName(e.getId().toString());
            userTemp.setValue(e.getCounts());
            userList[i] = userTemp;
        }
        charOprtRoseVo.setSeries(userList);

        CharOprtWordVo charOprtWordVo = new CharOprtWordVo();

//        charOprtWordVo.setSeries();

        uChartsVo.setUserTop(charOprtRoseVo);
        uChartsVo.setCameraCatch(charOprtColumnVo);
        uChartsVo.setXunJianCatch(charOprtMountVo);
        // 获取最近巡检分布的识别人数
        return Result.success(uChartsVo);
    }
}
