package com.heyongqiang.zyz.common.vo;

import lombok.Data;

@Data
public class CharOprtWordVo {

    private InnserClass[] series;


    public void setSeries(ChartNameValueVo[] series) {
        InnserClass[] innserClass = new InnserClass[series.length];

        this.series = innserClass;
    }

    class InnserClass{

        // 柱状图的描述名称
        private String name;

        // 具体的数量
        private String data;

        // 词云大小
        private Integer textSize;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Integer getTextSize() {
            return textSize;
        }

        public void setTextSize(Integer textSize) {
            this.textSize = textSize;
        }
    }

}
