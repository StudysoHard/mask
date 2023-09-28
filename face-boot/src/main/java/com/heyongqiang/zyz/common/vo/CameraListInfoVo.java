package com.heyongqiang.zyz.common.vo;


import lombok.Data;

@Data
public class CameraListInfoVo {

    private Integer id;

    private String val;

    private String label;

    public CameraListInfoVo() {
    }

    public CameraListInfoVo(Integer id, String val, String label) {
        this.id = id;
        this.val = val;
        this.label = label;
    }
}
