package com.bytetube.service;

import com.bytetube.dto.ShopExecution;
import com.bytetube.entity.Shop;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface ShopService {


	/**
	 * 创建商铺
	 *
	 * @param shop
	 *
	 * @return ShopExecution shopExecution
	 * @throws Exception
	 */
	ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg);

	/**
	 * 根据shopCondition分页返回相应店铺列表
	 *
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

	/**
	 * 查询该用户下面的店铺信息
	 * 
	 * @param
	 *
	 * @return List<Shop>
	 * @throws Exception
	 */
	ShopExecution getByEmployeeId(long employeeId) throws RuntimeException;

	/**
	 * 查询指定店铺信息
	 * 
	 * @param
	 *            shopId
	 * @return Shop shop
	 */
	Shop getByShopId(long shopId);



	/**
	 * 更新店铺信息（从店家角度）
	 * 

	 * @return
	 * @throws RuntimeException
	 */
	ShopExecution modifyShop(Shop shop, CommonsMultipartFile shopImg) throws RuntimeException;

}
