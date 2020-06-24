package com.bytetube.service.impl;

import com.bytetube.dao.ShopCategoryDao;
import com.bytetube.entity.ShopCategory;
import com.bytetube.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

	@Autowired
	private ShopCategoryDao shopCategoryDao;




	@Override
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition)
			throws IOException {
		return shopCategoryDao.queryShopCategory(shopCategoryCondition);
	}


}
