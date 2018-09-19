package com.fangxiaofeng.o2o.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.fangxiaofeng.o2o.dao.ShopDao;
import com.fangxiaofeng.o2o.dto.ShopExecution;
import com.fangxiaofeng.o2o.entity.Shop;
import com.fangxiaofeng.o2o.enums.ShopStateEnum;
import com.fangxiaofeng.o2o.exceptions.ShopOperationException;
import com.fangxiaofeng.o2o.service.ShopService;
import com.fangxiaofeng.o2o.util.ImageUtil;
import com.fangxiaofeng.o2o.util.PageCalculator;
import com.fangxiaofeng.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService{
	@Autowired
	private ShopDao shopDao;
	
	@Transactional
	public ShopExecution addShop(Shop shop,InputStream shopImgInputStream,String fileName) {
		//空值判断
		if(shop == null){
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		
		try{
			//给店铺信息赋初始值
			shop.setEnableStatus(0);//审核中
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if(effectedNum <= 0){
				throw new ShopOperationException("店铺创建失败");
				/*这里使用RuntimeException而不用exception是为了让事务回滚*/
			}else{
				if(shopImgInputStream!= null){
					//存储图片
					try{
						addShopImg(shop, shopImgInputStream, fileName);
					}catch(Exception e){
						throw new ShopOperationException("addShopImg error:"+ e.getMessage());
					}
					//更新店铺的图片地址
					effectedNum = shopDao.updateShop(shop);
					if(effectedNum <= 0){
						throw new ShopOperationException("更新图片地址失败");
					}
				}
			}
		}catch (Exception e) {
			throw new ShopOperationException("addShop error:"+e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}
	
	private void addShopImg(Shop shop,InputStream ShopImgInputStream,String fileName){
		//获取shop图片的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(ShopImgInputStream, fileName,dest);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName)
			throws ShopOperationException {
		if(shop == null||shop.getShopId()==null){
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else{
			try{	
				//1.判断是否需要处理图片
				System.out.println("走到处理图片");
				if(shopImgInputStream!=null&&fileName !=null&&!"".equals(fileName)){
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if(tempShop.getShopImg()!=null){
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					//这里不用tempShop是因为要存新的图片路径，然后传shop更新数据库
					addShopImg(shop, shopImgInputStream, fileName);
				}
				//2.更新店铺信息
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if(effectedNum<=0){
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				}else {
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS,shop);
				}
			}catch(Exception e){
				throw new ShopOperationException("modifyShop error:"+e.getMessage());
			}	
		}

	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if(shopList!=null){
			se.setShopList(shopList);
			se.setCount(count);
		}else{
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
		
	}



}
