package com.heyongqiang.zyz.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CharOprtRoseVo implements Serializable {
    // 山峰图数据
    private InnerClass[] series;


    public void setSeries(ChartNameValueVo[] series) {
        InnerClass[] innerClass = new InnerClass[1];
        InnerClass innerClass1 = new InnerClass();
        innerClass1.setData(series);
        innerClass[0] = innerClass1;
        this.series = innerClass;
    }

    class InnerClass{
        private ChartNameValueVo[] data;

        public ChartNameValueVo[] getData() {
            return data;
        }

        public void setData(ChartNameValueVo[] data) {
            this.data = data;
        }
    }

}
