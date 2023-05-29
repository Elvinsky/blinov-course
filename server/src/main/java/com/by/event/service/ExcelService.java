package com.by.event.service;

import com.by.event.model.Event;
import com.by.event.response.EventResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ExcelService {

    public byte[] createReport(List<EventResponse> events) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row headerRow = sheet.createRow(0);

        int i = 0;
        headerRow.createCell(i++).setCellValue("Event id");
        headerRow.createCell(i++).setCellValue("Title");
        headerRow.createCell(i++).setCellValue("Description");
        headerRow.createCell(i++).setCellValue("Start date");
        headerRow.createCell(i++).setCellValue("End date");
        headerRow.createCell(i++).setCellValue("Is favourite");
        headerRow.createCell(i).setCellValue("Owner id");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        i = 0;
        for (EventResponse event : events) {
            Row newRow = sheet.createRow(i + 1);
            int j = 0;
            newRow.createCell(j++).setCellValue(event.getId());
            newRow.createCell(j++).setCellValue(event.getTitle());
            newRow.createCell(j++).setCellValue(event.getDescription());
            newRow.createCell(j++).setCellValue(event.getStartDate());
            newRow.createCell(j++).setCellValue(event.getEndDate());
            newRow.createCell(j++).setCellValue(event.isFavourite() ? "true" : "false");
            newRow.createCell(j).setCellValue(event.getOwnerId());
            i++;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }
}
