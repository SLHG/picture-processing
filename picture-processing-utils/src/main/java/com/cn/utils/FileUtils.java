package com.cn.utils;

import cn.hutool.core.util.IdUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;


/**
 * 文件上传工具类
 */
public class FileUtils {
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     */
    public static String upload(String baseDir, MultipartFile file) throws IOException {
        if (file == null) {
            return "";
        }
        try {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir          相对应用的基目录
     * @param multipartFile    上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws IOException 比如读写文件出错时
     */
    public static String upload(String baseDir, MultipartFile multipartFile, String[] allowedExtension)
            throws IOException {
        String extension = getExtension(multipartFile);
        assertAllowed(multipartFile, allowedExtension, extension);
        String fileName = extractFilename(extension);
        File file = getAbsoluteFile(baseDir, fileName);
        if (file == null) {
            return "";
        }
        multipartFile.transferTo(file);
        return getPathFileName(fileName);
    }

    /**
     * 文件上传
     *
     * @param baseDir    相对应用的基目录
     * @param base64File base64图片文件
     * @param extension  上传文件类型
     * @return 返回上传成功的文件名
     * @throws IOException 比如读写文件出错时
     */
    public static String upload(String baseDir, String base64File, String extension)
            throws IOException {
        //创建文件名
        String fileName = extractFilename(extension);
        byte[] decodeFile = Base64.getDecoder().decode(base64File);
        File file = getAbsoluteFile(baseDir, fileName);
        if (file == null) {
            return "";
        }
        Files.write(Paths.get(baseDir + File.separator + fileName), decodeFile, StandardOpenOption.CREATE);
        return getPathFileName(fileName);
    }

    /**
     * 文件删除
     *
     * @param baseDir      文件所在基础目录
     * @param filePathName 文件地址/文件名
     * @return 是否删除成功
     */
    public static boolean delFile(String baseDir, String filePathName) {
        File file = new File(baseDir + filePathName);
        if (file.exists()) {
            return file.delete();
        } else {
            return true;
        }
    }

    /**
     * 编码文件名
     */
    public static String extractFilename(String extension) {
        return DateUtils.datePath() + "/" + IdUtil.fastSimpleUUID() + "." + extension;
    }

    private static File getAbsoluteFile(String uploadDir, String fileName) {
        File file = new File(uploadDir + File.separator + fileName);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    return null;
                }
            }
        }
        return file.isAbsolute() ? file : file.getAbsoluteFile();
    }

    private static String getPathFileName(String fileName) {
        return "/" + fileName;
    }

    /**
     * 文件大小校验
     *
     * @param file      上传的文件
     * @param extension 文件格式
     */
    public static void assertAllowed(MultipartFile file, String[] allowedExtension, String extension) {
        long size = file.getSize();
        if (size > DEFAULT_MAX_SIZE) {
            throw new FileSizeLimitExceededException("超出文件大小限制" + size);
        }
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            throw new InvalidExtensionException("文件扩展字段错误" + extension);
        }
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension        上传文件类型
     * @param allowedExtension 允许上传文件类型
     * @return true/false
     */
    public static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension) && file.getContentType() != null) {
            return MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }
}
