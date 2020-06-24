package com.bytetube.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();											//targetAddr存储路径
	public static String generateThumbnail(CommonsMultipartFile thumbnail, String targetAddr) {
//		String realFileName = FileUtil.getRandomFileName();//生成随机名字 因为用户传来de名字有可能会重复
//		String extension = getFileExtension(thumbnail);//获取扩展名
//		makeDirPath(targetAddr);
//		String relativeAddr = targetAddr + realFileName + extension;
//		File dest = new File(FileUtil.getImgBasePath() + relativeAddr);
//		try {
//			Thumbnails.of(thumbnail.getInputStream()).size(200, 200).
//					watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f).
//					outputQuality(0.8f).toFile(dest);
//		} catch (IOException e) {
//			throw new RuntimeException("创建缩略图失败：" + e.toString());
//		}
//		return relativeAddr;
		String realFileName = FileUtil.getRandomFileName();
		String extension = getFileExtension(thumbnail);
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(FileUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getInputStream()).size(200, 200).outputQuality(0.5f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

	public static String generateNormalImg(CommonsMultipartFile thumbnail, String targetAddr) {
		String realFileName = FileUtil.getRandomFileName();
		String extension = getFileExtension(thumbnail);
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(FileUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getInputStream()).size(337, 640).outputQuality(0.5f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

	public static List<String> generateNormalImgs(List<CommonsMultipartFile> imgs, String targetAddr) {
		int count = 0;
		List<String> relativeAddrList = new ArrayList<String>();
		if (imgs != null && imgs.size() > 0) {
			makeDirPath(targetAddr);
			for (CommonsMultipartFile img : imgs) {
				String realFileName = FileUtil.getRandomFileName();
				String extension = getFileExtension(img);
				String relativeAddr = targetAddr + realFileName + count + extension;
				File dest = new File(FileUtil.getImgBasePath() + relativeAddr);
				count++;
				try {
					Thumbnails.of(img.getInputStream()).size(600, 300).outputQuality(0.5f).toFile(dest);
				} catch (IOException e) {
					throw new RuntimeException("创建图片失败：" + e.toString());
				}
				relativeAddrList.add(relativeAddr);
			}
		}
		return relativeAddrList;
	}
	//创建目标路径所涉及到的目录，即/Users/dalluo/image/xxx.jpg
			//那么Users dalluo image这3个文件夹都是自动创建的
			private static void makeDirPath(String targetAddr) {
				String realFileParentPath = FileUtil.getImgBasePath() + targetAddr;
				File dirPath = new File(realFileParentPath);
				if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	private static String getFileExtension(CommonsMultipartFile cFile) {
		String originalFileName = cFile.getOriginalFilename();
		return originalFileName.substring(originalFileName.lastIndexOf("."));
	}
}
