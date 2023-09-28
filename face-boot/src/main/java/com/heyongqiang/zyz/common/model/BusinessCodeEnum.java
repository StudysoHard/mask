package com.heyongqiang.zyz.common.model;

import java.util.HashMap;
import java.util.Map;


public enum BusinessCodeEnum {
    /**
     * 码样式：【CCCBBOOXXX】
     * 编码示例说明：
     * CCC 中心编码&业务系统
     * BB   业务类型
     * OO  操作类型
     * XXX具体编码（000：表示成功，999：系统异常，998：数据库异常，NNN：其它，100：参数异常，200：业务异常）
     * 200开头代码系统默认，其余系统使用10－199之间
     * */
    DEFAULT_SUCCESS(200,"default success"),
    DEFAULT_SYS_ERROR(2000000999,"系统错误"),
    CHECK_PARAM_NO_RESULT(2000000100,"检测参数无结果"),
    CHECK_BIZ_NO_RESULT(2000000101,"检查业务无结果"),
    CHECK_ACTION_NO_RESULT(2000000102,"检查执行情况无结果"),
    CHECK_PARAM_NOT_MATCH(1000000001,"检查参数不匹配"),
    CHECK_BIZ_ERROR_VALIDATE_CODE(1000000002,"检查业务验证码错误"),
    CHECK_BIZ_ERROR_MOBILE_USED(1000000003,"检查业务电话号码已被使用"),
    FILTER_DISTINCET(100000004,"非法请求 ， 请重新刷新页面操作!!"),
    //login
    NO_LOGIN(1000000200,"未登录"),
    HTTP_NO_PERMISSION(1000000005,"没有权限!!"),
    HTTP_NO_LOGIN(100000006,"用户未登录!!!");


    private static final Map<Integer, BusinessCodeEnum> codeMap = new HashMap<Integer, BusinessCodeEnum>((int)(BusinessCodeEnum.values().length/0.75)+1);

    static{
        for(BusinessCodeEnum businessCodeEnum: values()){
            codeMap.put(businessCodeEnum.getCode(), businessCodeEnum);
        }
    }

    /**
     * 根据code获取枚举值
     * @param code
     * @return
     */
    public static BusinessCodeEnum valueOfCode(int code){
        return codeMap.get(code);
    }

    private int code;
    private String msg;

    BusinessCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

