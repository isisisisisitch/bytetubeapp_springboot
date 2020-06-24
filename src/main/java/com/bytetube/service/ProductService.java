package com.bytetube.service;

import com.bytetube.dto.ProductExecution;
import com.bytetube.entity.Product;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

public interface ProductService {

	/**
	 * 添加商品信息以及图片处理
	 *
	 * @param product
	 * @param thumbnail
	 * @param productImgs
	 * @return
	 *
	 */
	ProductExecution addProduct(Product product, CommonsMultipartFile thumbnail, List<CommonsMultipartFile> productImgs)
			throws RuntimeException;


	/**
	 * 通过商品Id查询唯一的商品信息
	 *
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);

	/**
	 * 修改商品信息以及图片处理
	 *
	 * @param product
	 * @param thumbnail
	 * @param productImgs
	 * @return
	 *
	 */
	ProductExecution modifyProduct(Product product, CommonsMultipartFile thumbnail,
                                   List<CommonsMultipartFile> productImgs) throws RuntimeException;

	/**
	 * 查询商品列表并分页，可输入的条件有： 商品名（模糊），商品状态，店铺Id,商品类别
	 *
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
}
