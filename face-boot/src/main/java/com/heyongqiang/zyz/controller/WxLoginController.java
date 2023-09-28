package com.heyongqiang.zyz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heyongqiang.zyz.common.vo.ErrorCode;
import com.heyongqiang.zyz.mapper.LoginImgMapper;
import com.heyongqiang.zyz.mapper.UserMapper;
import com.heyongqiang.zyz.params.LoginImgParams;
import com.heyongqiang.zyz.po.LoginImg;
import com.heyongqiang.zyz.po.User;
import com.heyongqiang.zyz.service.WxLoginService;
import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.LoginParams;
import com.obs.services.exception.ObsException;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.obs.services.ObsClient;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Api("登录接口")
@RestController
public class WxLoginController {

    // obs原区域地址
    private static String endPoint = "https://obs.cn-north-1.myhuaweicloud.com";

    // access key
    private static String ak = "RVCB8XY3JPFNRHCTP85P";

    // secret key
    private static String sk = "TWzFsBq0KZkkp4yivbimvNaRg8681ZgBrQeq9uHF";

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-DD-mm");


    @Resource
    private WxLoginService wxLoginService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private LoginImgMapper loginImgMapper;

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat insertFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ApiOperation(value = "用户登录接口", notes = "用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = false, dataType = "String")
    })
    @PostMapping("/login")
    public Result login(@RequestBody LoginParams loginParams){
        return wxLoginService.login(loginParams);
    }

    /**
     * 用户登录之后拍摄上班图片
     * @RequestHeader(value = "faceName") String faceName
     * @return
     */
    @RequestMapping("/postImg")
    @ApiImplicitParam(name = "img and faceName", value = "布控人脸参数")
    public Result uploadLoginImg(@RequestParam("file") MultipartFile file , @RequestParam(value = "userName") String userName){
        /**
         * 先查看用户是否已经有拍摄过登录照片  若是已经存在那么就直接放回
         *
         *  判断该用户是否存在
         *     拿到id之后 将日期 + 状态 + userId 作为用户登录图片的名字
         */
        // 查询用户是否已经有登录的图片
        String loginTime = format.format(new Date());
        LambdaQueryWrapper<LoginImg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LoginImg::getUser_name,userName).le(LoginImg::getLogin_time,loginTime);
        queryWrapper.last("limit 1");
        LoginImg loginImg = loginImgMapper.selectOne(queryWrapper);
        if(loginImg != null){
            // 已经插入过图片了
            return Result.fail(ErrorCode.INVALID_REQUEST.getCode() , ErrorCode.INVALID_REQUEST.getMsg());
        }
        loginImg = new LoginImg();
        User user = userMapper.searchUserInfoByName(userName);
        if(user == null){
            return Result.fail(ErrorCode.USER_NOEXIT.getCode() , ErrorCode.USER_NOEXIT.getMsg());
        }
        String data = format.format(new Date());
        String fullName = data + "-login-" + user.getId().toString() + ".png";
        // ObsClient是线程安全的，可在并发场景下使用
        ObsClient obsClient = null;
        try
        {
            // 创建ObsClient实例
            obsClient = new ObsClient(ak, sk, endPoint);
            // 调用接口进行操作，例如上传对象
            obsClient.putObject("huawei-obs-3651", "weixing_login_img/" + fullName , new ByteArrayInputStream(file.getBytes()));  // localfile为待上传的本地文件路径，需要指定到具体的文件名
//            System.out.println(response);
            // 插入图片
            loginImg.setLogin_img("https://huawei-obs-3651.obs.cn-north-1.myhuaweicloud.com/weixing_login_img/"+  fullName );
            loginImg.setLogin_time(new Date());
            loginImg.setUser_id(user.getId());
            loginImg.setUser_name(userName);
            loginImgMapper.insert(loginImg);
            return Result.success();
        }
        catch (ObsException | IOException e)
        {
            return Result.fail(ErrorCode.IMG_NOREADY.getCode() , ErrorCode.IMG_NOREADY.getMsg());
        }finally{
            // 关闭ObsClient实例，如果是全局ObsClient实例，可以不在每个方法调用完成后关闭
            // ObsClient在调用ObsClient.close方法关闭后不能再次使用
            if(obsClient != null){
                try
                {
                     obsClient.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
