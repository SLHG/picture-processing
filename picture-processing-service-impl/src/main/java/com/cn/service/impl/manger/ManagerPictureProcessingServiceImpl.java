package com.cn.service.impl.manger;

import com.cn.beans.common.Constant;
import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerPictureProcessingInfo;
import com.cn.dao.manager.ManagerPictureProcessingDao;
import com.cn.service.config.ProjectConfig;
import com.cn.service.manger.ManagerPictureProcessingService;
import com.cn.utils.ExcelUtils;
import com.cn.utils.FileUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ManagerPictureProcessingServiceImpl implements ManagerPictureProcessingService {

    final
    ManagerPictureProcessingDao managerPictureProcessingDao;

    private static final List<String> TITLES = new ArrayList<String>() {{
        add("图片编码");
        add("用户昵称");
        add("图片");
        add("模板图片");
        add("创建时间");
    }};


    public ManagerPictureProcessingServiceImpl(ManagerPictureProcessingDao managerPictureProcessingDao) {
        this.managerPictureProcessingDao = managerPictureProcessingDao;
    }

    @Override
    public PageInfo<ManagerPictureProcessingInfo> getList(int start, int page, String id) {
        PageHelper.startPage(start, page);
        return new PageInfo<>(managerPictureProcessingDao.getList(id));
    }

    @Override
    public void download(MultipartFile file, HttpServletResponse response) throws IOException {
        List<String[]> list = ExcelUtils.readXLSXExcelFile(file, 0);
        Set<String> idSet = new HashSet<>();
        if (!list.isEmpty()) {
            //去除表头
            list.remove(0);
        }
        if (list.isEmpty()) {
            response.getWriter().write(new ResultBean(ResultBean.FAIL_CODE, "数据为空").toJSONString());
            return;
        }
        for (String[] strings : list) {
            if (!StringUtils.isBlank(strings[0])) {
                idSet.add(strings[0]);
            }
        }
        if (idSet.isEmpty()) {
            response.getWriter().write(new ResultBean(ResultBean.FAIL_CODE, "数据为空").toJSONString());
            return;
        }
        List<ManagerPictureProcessingInfo> result = managerPictureProcessingDao.getListByIds(idSet);
        List<String[]> data = new ArrayList<>();
        String baseDir = ProjectConfig.PROJECT_CONFIG.get(Constant.PHOTO_UPLOAD_BASE_DIR.getValue(String.class));
        for (ManagerPictureProcessingInfo info : result) {
            String[] strings = new String[5];
            strings[0] = info.getId();
            strings[1] = info.getNickName();
            String picturePath = info.getPicturePath();
            strings[2] = StringUtils.isBlank(picturePath) ? "" : baseDir + picturePath;
            String pictureTemplatePath = info.getPictureTemplatePath();
            strings[3] = StringUtils.isBlank(pictureTemplatePath) ? "" : baseDir + pictureTemplatePath;
            strings[4] = info.getCreateTime();
            data.add(strings);
        }
        response.reset();
        response.addHeader("Content-Disposition", "attachment; filename=data.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        writeExcelFile(TITLES, response.getOutputStream(), data);
    }

    @Override
    public PageInfo<ManagerPictureProcessingInfo> getListLikeId(int start, int page, String id) {
        PageHelper.startPage(start, page);
        return new PageInfo<>(managerPictureProcessingDao.getListLikeId(id));
    }

    @Override
    public ResultBean delete(String id) {
        ManagerPictureProcessingInfo info = managerPictureProcessingDao.selectById(id);
        if (info == null) {
            return new ResultBean(ResultBean.FAIL_CODE, "数据不存在");
        }
        String baseDir = ProjectConfig.PROJECT_CONFIG.get(Constant.PHOTO_UPLOAD_BASE_DIR.getValue(String.class));
        if (!StringUtils.isBlank(info.getPicturePath())) {
            boolean delFile = FileUtils.delFile(baseDir, info.getPicturePath());
            if (!delFile) {
                return new ResultBean(ResultBean.FAIL_CODE, "删除失败");
            }
        }
        if (!StringUtils.isBlank(info.getPictureTemplatePath())) {
            boolean delFile = FileUtils.delFile(baseDir, info.getPictureTemplatePath());
            if (!delFile) {
                return new ResultBean(ResultBean.FAIL_CODE, "删除失败");
            }
        }
        int i = managerPictureProcessingDao.delete(id);
        if (i > 0) {
            return new ResultBean();
        }
        return new ResultBean(ResultBean.FAIL_CODE, ResultBean.FAIL_MSG);
    }

    public static void writeExcelFile(List<String> titles, OutputStream outputStream, List<String[]> dataList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFDrawing drawingPatriarch = sheet.createDrawingPatriarch();
        XSSFRow rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            rowTitle.createCell(i).setCellValue(titles.get(i));
        }
        int rowIndex = 1;
        for (String[] data : dataList) {
            XSSFRow rowData = sheet.createRow(rowIndex);
            for (int i = 0; i < data.length; i++) {
                String s = data[i];
                if (i == 2 || i == 3) {
                    if (!StringUtils.isBlank(s)) {
                        //创建文件
                        File file = new File(s);
                        //获取扩展名
                        String extension = FilenameUtils.getExtension(file.getName());
                        //读取图片内容
                        BufferedImage bufferedImage = ImageIO.read(file);
                        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                        ImageIO.write(bufferedImage, extension, byteArray);
                        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) i, rowIndex, (short) (i + 1), rowIndex + 1);
                        drawingPatriarch.createPicture(anchor, workbook.addPicture(byteArray.toByteArray(), ExcelUtils.png_jpg_image_type.get(extension)));
                        continue;
                    }
                }
                XSSFCell cell = rowData.createCell(i);
                cell.setCellValue(s);
            }
            rowIndex++;
        }
        workbook.write(outputStream);
    }

}
