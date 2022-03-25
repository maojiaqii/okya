package com.top.okya.util.common;

/**
 * 类名： PageUtil <br>
 * 路径： com.nc.util.page <br>
 * 描述： 分页工具类 <br>
 * 编写： maojiaqi <br>
 * 日期： 2018年10月25日 上午11:35:49
 *
 */
public class PageBean {

	private int firstResult; // 分页的起始索引
	private int maxResult; // 结束索引
	private int pageSize; // 每页的记录条数
	private int currentPage; // 当前页
	private int totalPage; // 总页数
	private int totalCount; // 总的记录数

	public PageBean() {
		super();
	}

	/**
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页的大小
	 * @param totalCount
	 *            总记录数
	 */
	public void pageInit(int currentPage, int pageSize, int totalCount) {
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.totalPage = totalCount % pageSize != 0 ? totalCount / pageSize + 1 : totalCount / pageSize;
		this.currentPage = currentPage >= totalPage ? totalPage : currentPage;
		this.currentPage = this.currentPage <= 1 ? 1 : this.currentPage;
		this.firstResult = (currentPage - 1) * pageSize;
		this.maxResult = pageSize * currentPage;
		if (this.maxResult >= totalCount) {
			this.maxResult = totalCount;
		}
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public String toString() {
		return "PageUtil [firstResult=" + firstResult + ", maxResult=" + maxResult + ", pageSize=" + pageSize
				+ ", currentPage=" + currentPage + ", totalPage=" + totalPage + ", totalCount=" + totalCount + "]";
	}

}
