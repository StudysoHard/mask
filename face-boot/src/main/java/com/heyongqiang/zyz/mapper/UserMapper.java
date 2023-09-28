package com.heyongqiang.zyz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heyongqiang.zyz.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User searchUserInfoByName(@Param("userName") String userName);

    int updateLoginStatus(@Param("id") Long id,@Param("loginTime") Date loginTime);

    String selectUserStatusByUserName(@Param("userName") String userName);

    int verifyUserInfo(@Param("userName") String userName , @Param("password") String userPassword);

    int updateLogoutStatus(@Param("userName")String userName,@Param("date") Date date);

}

