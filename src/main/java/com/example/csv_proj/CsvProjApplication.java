package com.example.csv_proj;

import com.example.csv_proj.svc.CsvService;
import com.example.csv_proj.svc.CsvTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CsvProjApplication implements CommandLineRunner {

    @Autowired
    private CsvService csvService;
    @Autowired
    private CsvTokenService csvTokenService;

    public static void main(String[] args) {
        SpringApplication.run(CsvProjApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        csvService.doWork();
        csvTokenService.doWork();
    }

}
