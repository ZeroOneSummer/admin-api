package com.dms.api.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 * 
 */
public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	//总记录数
	private int totalCount;
	//每页记录数
	private int pageSize;
	//总页数
	private int totalPage;
	//当前页数
	private int currPage;
	//列表数据
	private List<?> list;
	//自定义数据
	private Object userData;
	
	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param pageSize    每页记录数
	 * @param currPage    当前页数
	 */
	public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}
	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param query    获取每页记录数、当前页数、userdata
	 */
	public PageUtils(List<?> list, int totalCount,Query query) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = query.getLimit();
		this.currPage = query.getPage();
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
		this.userData = query;
	}

	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
	
}
