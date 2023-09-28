package com.heyongqiang.zyz.common.vo;

import lombok.Data;

import java.io.Serializable;

public class CharOprtColumnVo implements Serializable {


    // 纵坐标
    private String[] categories;

    // 映射数据
    private Series[] series = new Series[1];


    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public Series[] getSeries() {
        return series;
    }

    public void setSeries(int[] data , String name) {
        Series series = new Series();
        series.setData(data);
        series.setName(name);
        this.series[0] = series;
    }


    public class Series{
        // 柱状图的描述名称
        private String name;

        // 具体的数量
        private int[] data;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int[] getData() {
            return data;
        }

        public void setData(int[] data) {
            this.data = data;
        }
    }

}
