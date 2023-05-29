package com.by.event.service;

import com.by.event.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExcelServiceTest {

    @Autowired
    ExcelService excelService;

//    @Test
//    void createReport() {
//        excelService = new ExcelService();
//        List<Event> events = new ArrayList<>();
//        events.add(new Event(1, "title", "desc", new Date(), new Date(), "1", true));
//        events.add(new Event(2, "title2", "desc2", new Date(), new Date(), "2", false));
//        events.add(new Event(3, "title3", "desc3", new Date(), new Date(), "3", false));
//
//        byte[] bytes = excelService.createReport(events);
//
//        try (OutputStream outputStream = new FileOutputStream("src/main/resources/report.xlsx")) {
//            outputStream.write(bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


}