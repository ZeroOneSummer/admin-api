package com.dms.api.common.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

/**
 * @author 40
 * date 2017/11/8
 * time 14:53
 * decription:
 */
public class FileUtil {
    private final static Logger logger = Logger.getLogger(FileUtil.class);

    /**
     * 获取缩略图路径
     * <br>
     * @param originalName
     * @return
     */
    private String getSmallImgName(String originalName) {
        if (StringUtils.isBlank(originalName))
            return null;
        int index = originalName.lastIndexOf(".");
        String smallPath = "";
        if (index != -1) {
            String prefix = originalName.substring(0, index);
            String suffix = originalName.substring(index + 1);
            smallPath = prefix + "_" + "." + suffix;
        }
        return smallPath;
    }

    /**
     * @Title: saveInputStreamToFile
     * @Description: 输入流拷豆到文件中
     * @param @param in
     * @param @param destfilePath
     * @return void
     * @throws
     */
    public static void saveInputStreamToFile(InputStream in, String destfilePath) {
        try {
            synchronized(FileUtil.class){
                byte[] gif = IOUtils.toByteArray(in);
                FileUtils.writeByteArrayToFile(new File(destfilePath), gif);
                IOUtils.closeQuietly(in);
                logger.info("save pic:" + destfilePath + " success, img size: " + gif.length / 1024);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static String saveImg(String path, MultipartHttpServletRequest request) {
        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = null;
        while (itr.hasNext()) {
            try {
                mpf = request.getFile(itr.next());
                if(mpf.isEmpty()){
                    logger.error("upload img is null------上传文件为空");
                    return null;
                }
                //
                int imgSzie = mpf.getInputStream().available();
                // 获取文件名
                String fileName = mpf.getOriginalFilename();
                if (org.apache.commons.lang.StringUtils.isBlank(fileName)) {
                    logger.error("文件名为空");
                    return null;
                }
                // 文件后缀
                String suffix = fileName.substring(fileName.lastIndexOf("."));
                if (mpf.getSize() / 1000 <= 5000 && mpf.getSize() == imgSzie) {
                    // 图片相对路径
                    String imgPath = path;
//                    if (org.apache.commons.lang.StringUtils.isBlank(imgPath)) {
//                        imgPath = "files" + File.separator + getImagePath(type) + suffix; // 获得目录下的相对路径
//                    }
                    // 图片绝对路径
                    Long time = new Date().getTime();
                    String originalName = imgPath + time+"_"+fileName;
                    // 保存图片
                    FileUtil.saveInputStreamToFile(mpf.getInputStream(), originalName);
                    logger.info(imgPath + " 图片上传成功");
                    return time+"_"+fileName;
                } else {
                    if (mpf.getSize() != imgSzie) {
                        logger.error("#######图片上传失败，服务器获取到图片大小(字节): " + mpf.getSize() + ", 上传图片实际大小(字节): " + imgSzie);
                    } else {
                        logger.error("单张照片不得超过5M");
                    }
                    return null;
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
                return null;
            }
        }
        logger.error("没有上传的图片");
        return null;
    }

    public static File getFile(String filePath){
        File file = new File(filePath);
        return file;
    }

    public static boolean deleteFile(String filePath){
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + filePath + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + filePath + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + filePath + "不存在！");
            return false;
        }
    }
}
