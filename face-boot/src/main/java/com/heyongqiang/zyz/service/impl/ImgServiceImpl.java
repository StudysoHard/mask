package com.heyongqiang.zyz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.heyongqiang.zyz.common.dto.ImgPathQueryDto;
import com.heyongqiang.zyz.common.vo.ImgPathQueryVo;
import com.heyongqiang.zyz.mapper.CapturedFaceMapper;
import com.heyongqiang.zyz.mapper.UserMapper;
import com.heyongqiang.zyz.po.CapturedFace;
import com.heyongqiang.zyz.po.MaskImage;
import com.heyongqiang.zyz.common.vo.ErrorCode;
import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.common.vo.outter.PostPythonImageVo;
import com.heyongqiang.zyz.mapper.ImgMapper;
import com.heyongqiang.zyz.params.Base64ImageParams;
import com.heyongqiang.zyz.params.FaceMaskImagePathParams;
import com.heyongqiang.zyz.params.FaseMaskInfoParams;
import com.heyongqiang.zyz.po.User;
import com.heyongqiang.zyz.service.ImgService;
import com.heyongqiang.zyz.utils.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImgServiceImpl implements ImgService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CapturedFaceMapper capturedFaceMapper;

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    // 调用的 python url地址
    private String url = "http://114.116.17.107:5002/post_base";
//    private String url = "http://localhost:5002/post_base";


    @Resource
    private ImgMapper imgMapper;

    private IdWorker worker = new IdWorker(1,1,1);


    /**
     * 1. 获取base64图片 插入一条img数据   将图片发送给python服务器
     * 2. 收到人员信息的结果将数据存储到数据库
     * 3. 将信息脱敏封装为vo返回给小程序  【小程序存储到本地storage中】
     * @param base64ImageParams
     * @return
     */
    @Override
    public Result disposeXunjianImg(Base64ImageParams base64ImageParams) {
        Date date = new Date();
        // 查询用户信息
        User user = userMapper.searchUserInfoByName(base64ImageParams.getName());
        if(user == null) return Result.fail(ErrorCode.USER_NOEXIT.getCode() , ErrorCode.USER_NOEXIT.getMsg());
        MaskImage maskImage = new MaskImage();
        BeanUtils.copyProperties(base64ImageParams , maskImage);
        maskImage.setCatchTime(date);
        maskImage.setUserId(user.getId());
        maskImage.setXunjian_id(Long.parseLong(base64ImageParams.getXunJianId()));
        maskImage.setUserName(base64ImageParams.getName());
        maskImage.setIsDelete(0);
        // 初始化一条图片字段
        imgMapper.insert(maskImage);
        // 将信息转发给python
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        PostPythonImageVo params = new PostPythonImageVo();
        params.setImg(base64ImageParams.getImg());
        params.setName(base64ImageParams.getName());
        params.setXunJian_id(base64ImageParams.getXunJianId());
        // id生成器
        params.setIndex(String.valueOf(worker.nextId()));
        JSONObject jsonObj = (JSONObject) JSONObject.toJSON(params);

        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);
        // 转发到python端 传回来的数据
        String result = restTemplate.postForObject(url, formEntity, String.class);
        // 分割字符
        Integer mask = Integer.valueOf(result.split(",")[0].split(":")[1]);
        Integer nomask = Integer.valueOf(result.split(",")[1].split(":")[1]);
        maskImage.setMask_number(mask);
        maskImage.setNomask_number(nomask);
        maskImage.setImgPath("https://huawei-obs-3651.obs.cn-north-1.myhuaweicloud.com/python_resolve_img/"+ params.getName() +"/"+ params.getXunJian_id() +"/" + params.getIndex() + "-resolve.png");
        imgMapper.updateById(maskImage);
        CapturedFace capturedFace = new CapturedFace();
        capturedFace.setCatchTime(date.getTime());
        capturedFace.setImgPath(maskImage.getImgPath());
        capturedFace.setMask(mask);
        capturedFace.setNomask(nomask);
        capturedFaceMapper.insert(capturedFace);
        FaseMaskInfoParams faseMaskInfoParams = new FaseMaskInfoParams();
        faseMaskInfoParams.setMask_human(mask);
        faseMaskInfoParams.setNomask_human(nomask);
        faseMaskInfoParams.setHuman(mask+ nomask);
        // 回填之前回调得到的id参数
        faseMaskInfoParams.setId(maskImage.getId().toString());
        return Result.success(faseMaskInfoParams);
    }

    /**
     * 根据时间查询该用户的
     * @param beginData
     * @param userName
     * @return
     */
    @Override
    public Result getXunJianImg(String beginData , String userName) {
        // 查询所有图片
        List<String> imgPathList = imgMapper.selectImgByDate(beginData , userName);
        return Result.success(imgPathList);
    }

    /**
     * 获取上一次的判别的图片
     * @param id
     * @return
     */
    @Override
    public Result getPreImage(String id) {
        // todo 这里获取的都是同一张图片
        // 查询表中主键为 id的字段
        String imgPath = imgMapper.selectPreImgPathById(Long.valueOf(id));
        if(imgPath == null){
            // 还未识别完成
            return Result.fail(ErrorCode.IMG_NOREADY.getCode(),ErrorCode.IMG_NOREADY.getMsg());
        }
        return Result.success(imgPath);
    }

    /**
     * 用户历史识别图片的列表
     * @param userName
     * @return
     */
    @Override
    public Result getImgList(String userName) {
        // 判断参数
        if(userName == null) return Result.fail(ErrorCode.MAIN_PARAMS_NULL.getCode() , ErrorCode.MAIN_PARAMS_NULL.getMsg());
        User user = userMapper.searchUserInfoByName(userName);
        if(user == null) return Result.fail(ErrorCode.USER_NOEXIT.getCode(), ErrorCode.USER_NOEXIT.getMsg());

        // 通过用户id 查询历史图片记录
        List<ImgPathQueryDto> list =  imgMapper.selectImgByUserId(user.getId());
        // 过滤不是http的网址
        List<ImgPathQueryVo> source = list.stream().filter(
                e -> e.getImgPath().matches("[a-zA-z]+://[^\\s]*")
        ).map(
                e -> new ImgPathQueryVo(e.getImgPath() , format.format(e.getTimes()))
        ).collect(Collectors.toList());
        return Result.success(source);

    }


}
