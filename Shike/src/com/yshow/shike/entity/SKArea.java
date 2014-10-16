package com.yshow.shike.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.yshow.shike.utils.SkArea_Item;

public class SKArea implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4868516832578204424L;
	
	private String id;
	private String aid;
	private String name;
	private ArrayList<SkArea_Item> skarea_item;
	public ArrayList<SkArea_Item> getSkarea_item() {
		return skarea_item;
	}

	public void setSkarea_item(ArrayList<SkArea_Item> skarea_item) {
		this.skarea_item = skarea_item;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SKArea() {
		super();
	}

	@Override
	public String toString() {
		return "SKArea [id=" + id + ", aid=" + aid + ", name=" + name
				+ ", skarea_item=" + skarea_item + "]";
	}	
}
