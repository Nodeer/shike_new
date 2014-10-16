package com.yshow.shike.utils;

import java.util.List;

import com.yshow.shike.entity.Goods;

public class Gift {

	
	private List<Goods> data;

	public List<Goods> getData() {
		return data;
	}

	public void setData(List<Goods> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Gift [data=" + data + "]";
	}
	
	
}
