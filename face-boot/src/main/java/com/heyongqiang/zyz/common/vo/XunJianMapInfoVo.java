package com.heyongqiang.zyz.common.vo;


import com.heyongqiang.zyz.po.MaskImage;
import lombok.Data;

import java.util.List;

@Data
public class XunJianMapInfoVo {

    private List<String> images;

    private List<MaskImage> infos;

}
