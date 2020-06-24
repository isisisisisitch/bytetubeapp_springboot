package com.bytetube.service;

import com.bytetube.entity.ShopCategory;

import java.io.IOException;
import java.util.List;

public interface ShopCategoryService {

	/**
	 * 
	 *
	 * @return
	 * @throws IOException
	 */
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategory) throws IOException;



}
