package com.fangxiaofeng.o2o.service;


import java.io.InputStream;

import com.fangxiaofeng.o2o.dto.ShopExecution;
import com.fangxiaofeng.o2o.entity.Shop;
import com.fangxiaofeng.o2o.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * 增加商店
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop,InputStream shopImgInputStream,String fileName) 
			throws ShopOperationException;
	
	/**
	 * 通过商店Id获取商店信息
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);
	
	/**
	 * 更新店铺信息，包括对图片的处理
	 * @param shop
	 * @param shopImgInputStream
	 * @param filName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop,InputStream shopImgInputStream,String filName) 
			throws ShopOperationException;
	
	/**
	 * 根据shopCondition分页返回相应店铺列表
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	 ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	 
}
