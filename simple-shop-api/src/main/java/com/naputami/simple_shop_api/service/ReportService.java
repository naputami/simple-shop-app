package com.naputami.simple_shop_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.naputami.simple_shop_api.repository.OrderRepository;
import com.naputami.simple_shop_api.model.Order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.InputStream;

import java.util.List;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportService {
    private final OrderRepository orderRepository;

    public byte[] getOrderReport(LocalDate startDate, LocalDate endDate) {

        JasperReport jasperReport;
        List<Order> orders;
        String dateRangeText;

        if (startDate == null || endDate == null) {
            orders = orderRepository.findAll();
            dateRangeText = "All Dates";
        } else {
            orders = orderRepository.findByOrderDateBetween(startDate, endDate);
            dateRangeText = startDate + " to " + endDate;
        }


        try {
            // Load and compile the JRXML file
            InputStream reportInputStream = getClass().getResourceAsStream("/order-report.jrxml");
            if (reportInputStream == null) {
                throw new FileNotFoundException("JRXML file not found in classpath");
            }
            jasperReport = JasperCompileManager.compileReport(reportInputStream);
        } catch (FileNotFoundException | JRException e) {
            log.error("Error compiling Jasper report", e);
            throw new RuntimeException("Error compiling Jasper report", e);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orders);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Order Report");
        parameters.put("DateRange", dateRangeText);
        JasperPrint jasperPrint = null;
        byte[] reportContent;

        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        return reportContent;
    }

}
