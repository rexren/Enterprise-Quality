package com.hikvision.hepaestus.common.support;

/**
 * 文件成功上传后的结果，包括基本返回信息和三张表的导入数据量
 * @author langyicong
 *
 */
public class ImportResult extends BaseResult{
	private int numOfInspections;
	private int numOfCopyRight;
	private int numOf3C;

	/**
	 * 构造函数
	 */
	public ImportResult() {
		super();
	}

	/**
	 * 构造函数
	 * @param code 结果码
	 * @param msg 结果信息
	 */
	public ImportResult(String code, String msg) {
		super(code, msg);
		this.numOfInspections=0;
		this.numOfCopyRight=0;
		this.numOf3C=0;
	}

	/**
	 * 构造函数
     * @param code 结果码
     * @param msg 结果信息
	 * @param numOfInspections 型检列表的导入数量
	 * @param numOfCopyRight 双证列表的导入数量
	 * @param numOf3C CCC列表的导入数量
	 */
	public ImportResult(String code, String msg, int numOfInspections, int numOfCopyRight, int numOf3C) {
		super(code, msg);
		this.numOfInspections=numOfInspections;
		this.numOfCopyRight=numOfCopyRight;
		this.numOf3C=numOf3C;
	}

	/**
	 * 获取型检列表的导入数量
	 * @return 型检列表的导入数量
	 */
	public int getNumOfInspections() {
		return numOfInspections;
	}

	/**
	 * 设置型检列表的导入数量
	 * @param numOfInspections 型检列表的导入数量
	 */
	public void setNumOfInspections(int numOfInspections) {
		this.numOfInspections = numOfInspections;
	}

	/**
	 * 获取双证列表的导入数量
	 * @return 双证列表的导入数量
	 */
	public int getNumOfCopyRight() {
		return numOfCopyRight;
	}

	/**
	 * 设置双证列表的导入数量
	 * @param numOfCopyRight 双证列表的导入数量
	 */
	public void setNumOfCopyRight(int numOfCopyRight) {
		this.numOfCopyRight = numOfCopyRight;
	}

	/**
	 * 获取CCC列表的导入数量
	 * @return CCC列表的导入数量
	 */
	public int getNumOf3C() {
		return numOf3C;
	}
	
	/**
     * 设置CCC列表的导入数量
	 * @param numOf3C CCC列表的导入数量
     */
	public void setNumOf3C(int numOf3C) {
		this.numOf3C = numOf3C;
	}

}
