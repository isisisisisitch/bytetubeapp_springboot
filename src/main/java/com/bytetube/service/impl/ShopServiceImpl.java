package com.bytetube.service.impl;


import com.bytetube.dao.ShopDao;
import com.bytetube.dto.ShopExecution;
import com.bytetube.entity.Shop;
import com.bytetube.enums.ShopStateEnum;
import com.bytetube.exceptions.ShopOperationException;
import com.bytetube.service.ShopService;
import com.bytetube.util.FileUtil;
import com.bytetube.util.ImageUtil;
import com.bytetube.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		//将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		//依据查询条件，调用dao层返回相关的店铺列表
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		//依据相同的查询条件，返回店铺总数
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		} else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}



	@Override
	@Transactional
	/**
	 * 使用注解控制事务方法的优点： 1.开发团队达成一致约定，明确标注事务方法的编程风格
	 * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外部
	 * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
	 */
	public ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg){
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		}
		try {
			//初始化店铺信息
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());

			//添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				throw new ShopOperationException("店铺创建失败");
			} else {
				try {
					if (shopImg != null) {
						//存储图片
						addShopImg(shop, shopImg);
						effectedNum = shopDao.updateShop(shop);
						if (effectedNum <= 0) {
							throw new ShopOperationException("创建图片地址失败");
						}
					}
				} catch (Exception e) {
					throw new ShopOperationException("addShopImg error: "
							+ e.getMessage());
				}

			}
		} catch (Exception e) {
			throw new ShopOperationException("insertShop error: " + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}



	@Override
	public ShopExecution getByEmployeeId(long employeeId)
			throws ShopOperationException {
		return null;
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}



	@Override
	@Transactional
	public ShopExecution modifyShop(Shop shop, CommonsMultipartFile shopImg) throws RuntimeException{
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOPID);
		} else {
			try {
				// 1.判断是否需要处理图片
				if (shopImg != null) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop.getShopImg() != null) {
						FileUtil.deleteFile(tempShop.getShopImg());
					}
					addShopImg(shop, shopImg);
				}
				// 2.更新店铺信息
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {// 创建成功
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				throw new RuntimeException("modifyShop error: "
						+ e.getMessage());
			}
		}


	}

	private void addShopImg(Shop shop, CommonsMultipartFile shopImg) {
		String dest = FileUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImg, dest);
		shop.setShopImg(shopImgAddr);
	}

}
