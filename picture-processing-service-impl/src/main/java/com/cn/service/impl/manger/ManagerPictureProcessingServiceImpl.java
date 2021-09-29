package com.cn.service.impl.manger;

import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerPictureProcessingInfo;
import com.cn.dao.manager.ManagerPictureProcessingDao;
import com.cn.service.manger.ManagerPictureProcessingService;
import com.cn.utils.ExcelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        for (ManagerPictureProcessingInfo info : result) {
            String[] strings = new String[5];
            strings[0] = info.getId();
            strings[1] = info.getNickName();
            strings[2] = info.getPicturePath();
            strings[3] = info.getPictureTemplatePath();
            strings[4] = info.getCreateTime();
            data.add(strings);
        }
        response.reset();
        response.addHeader("Content-Disposition", "attachment; filename=data.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        ExcelUtils.writeExcelFile(TITLES, response.getOutputStream(), data);
    }

}
