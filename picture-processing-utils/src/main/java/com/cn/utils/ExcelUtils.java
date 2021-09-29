package com.cn.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {


    /**
     * 读取excel文件内容,文件格式xlsx
     *
     * @param file       文件
     * @param sheetIndex sheet编号
     * @return 文件读取结果
     * @throws IOException 文件IO解析报错
     */
    public static List<String[]> readXLSXExcelFile(MultipartFile file, int sheetIndex) throws IOException {
        InputStream inputStream = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        int index = 0;
        List<String[]> resultList = new ArrayList<>();
        for (Sheet sheet : workbook) {
            if (index != sheetIndex) {
                continue;
            }
            index++;
            for (Row row : sheet) {
                int cells = row.getPhysicalNumberOfCells();
                String[] result = new String[cells];
                int i = 0;
                for (Cell cell : row) {
                    result[i] = getCellStringVal(cell);
                }
                resultList.add(result);
            }
        }
        return resultList;
    }

    private static String getCellStringVal(Cell cell) {
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case NUMERIC:
                return new DecimalFormat("#.##").format(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            default:
                return StringUtils.EMPTY;
        }
    }

    public static void writeExcelFile(List<String> titles, OutputStream outputStream, List<String[]> data) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            rowTitle.createCell(i).setCellValue(titles.get(i));
        }
        int rowIndex = 1;
        for (String[] strings : data) {
            XSSFRow rowData = sheet.createRow(rowIndex);
            rowIndex++;
            int cellIndex = 0;
            for (String string : strings) {
                XSSFCell cell = rowData.createCell(cellIndex);
                cell.setCellValue(string);
                cellIndex++;
            }
        }
        workbook.write(outputStream);
    }

}
