package com.heyongqiang.zyz.params;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LoginImgParams {

    // 上传的图片
    private MultipartFile localFile;

    // 用户名
    private String userNamee;

}
