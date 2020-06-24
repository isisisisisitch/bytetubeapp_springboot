package com.bytetube.service.impl;

import com.bytetube.dao.ProductDao;
import com.bytetube.dao.ProductImgDao;
import com.bytetube.dto.ProductExecution;
import com.bytetube.entity.Product;
import com.bytetube.entity.ProductImg;
import com.bytetube.enums.ProductStateEnum;
import com.bytetube.service.ProductService;
import com.bytetube.util.FileUtil;
import com.bytetube.util.ImageUtil;
import com.bytetube.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		// 页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		// 基于同样的查询条件返回该查询条件下的商品总数
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}



	@Override
	@Transactional
	// 1.若缩略图参数有值，则处理缩略图，
	// 若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
	// 2.若商品详情图列表参数有值，对商品详情图片列表进行同样的操作
	// 3.将tb_product_img下面的该商品原先的商品详情图记录全部清除
	// 4.更新tb_product_img以及tb_product的信息
	public ProductExecution modifyProduct(Product product, CommonsMultipartFile thumbnail,
                                          List<CommonsMultipartFile> productImgs) throws RuntimeException {
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 给商品设置上默认属性
			product.setLastEditTime(new Date());
			// 若商品缩略图不为空且原有缩略图不为空则删除原有缩略图并添加
			if (thumbnail != null) {
				// 先获取一遍原有信息，因为原来的信息里有原图片地址
				Product tempProduct = productDao.queryProductById(product.getProductId());
				if (tempProduct.getImgAddr() != null) {
					FileUtil.deleteFile(tempProduct.getImgAddr());
				}
				addThumbnail(product, thumbnail);
			}
			// 如果有新存入的商品详情图，则将原先的删除，并添加新的图片
			if (productImgs != null && productImgs.size() > 0) {
				deleteProductImgs(product.getProductId());
				addProductImgs(product, productImgs);
			}
			try {
				// 更新商品信息
				int effectedNum = productDao.updateProduct(product);
				if (effectedNum <= 0) {
					throw new RuntimeException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				throw new RuntimeException("更新商品信息失败:" + e.toString());
			}
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductById(productId);

	}

	@Override
	@Transactional
	// 1.处理缩略图，获取缩略图相对路径并赋值给product
	// 2.往tb_product写入商品信息，获取productId
	// 3.结合productId批量处理商品详情图
	// 4.将商品详情图列表批量插入tb_product_img中
	public ProductExecution addProduct(Product product, CommonsMultipartFile thumbnail,
                                       List<CommonsMultipartFile> productImgs) throws RuntimeException {
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 给商品设置上默认属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			// 默认为上架的状态
			product.setEnableStatus(1);
			// 若商品缩略图不为空则添加
			if (thumbnail != null) {
				addThumbnail(product, thumbnail);
			}
			try {
				// 创建商品信息
				int effectedNum = productDao.insertProduct(product);
				if (effectedNum <= 0) {
					throw new RuntimeException("创建商品失败");
				}
			} catch (Exception e) {
				throw new RuntimeException("创建商品失败:" + e.toString());
			}
			// 若商品详情图不为空则添加
			if (productImgs != null && productImgs.size() > 0) {
				addProductImgs(product, productImgs);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			// 传参为空则返回空值错误信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	private void addProductImgs(Product product, List<CommonsMultipartFile> productImgs) {
		// 获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
		List<String> imgAddrList = ImageUtil.generateNormalImgs(productImgs, dest);
		if (imgAddrList != null && imgAddrList.size() > 0) {
			List<ProductImg> productImgList = new ArrayList<ProductImg>();
			// 遍历图片一次去处理，并添加进productImg实体类里
			for (String imgAddr : imgAddrList) {
				ProductImg productImg = new ProductImg();
				productImg.setImgAddr(imgAddr);
				productImg.setProductId(product.getProductId());
				productImg.setCreateTime(new Date());
				productImgList.add(productImg);
			}
			// 如果确实是有图片需要添加的，就执行批量添加操作
			if (productImgList.size() > 0) {

				try {
					int effectedNum = productImgDao.batchInsertProductImg(productImgList);
					if (effectedNum <= 0) {
						throw new RuntimeException("创建商品详情图片失败");
					}
				} catch (Exception e) {
					throw new RuntimeException("创建商品详情图片失败:" + e.toString());
				}
			}
		}
	}

	/**
	 * 删除某个商品下的所有详情图
	 *
	 * @param productId
	 */
	private void deleteProductImgs(long productId) {
		// 根据productId获取原来的图片
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		// 干掉原来的图片
		for (ProductImg productImg : productImgList) {
			FileUtil.deleteFile(productImg.getImgAddr());
		}
		// 删除数据库里原有图片的信息
		productImgDao.deleteProductImgByProductId(productId);
	}

	private void addThumbnail(Product product, CommonsMultipartFile thumbnail) {
		String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}
}