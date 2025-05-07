package com.example.common.core.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Excel工具类
 */
public class ExcelUtils {
    /**
     * 导出Excel
     *
     * @param response 响应对象
     * @param fileName 文件名
     * @param sheetName 工作表名
     * @param data 数据列表
     * @param clazz 数据类型
     */
    public static <T> void exportExcel(HttpServletResponse response, String fileName, String sheetName,
                                     List<T> data, Class<T> clazz) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), clazz)
                .autoCloseStream(Boolean.FALSE)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet(sheetName)
                .doWrite(data);
    }

    /**
     * 导入Excel
     *
     * @param file Excel文件
     * @param clazz 数据类型
     * @return 数据列表
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> clazz) throws IOException {
        return EasyExcel.read(file.getInputStream())
                .head(clazz)
                .sheet()
                .doReadSync();
    }
} 