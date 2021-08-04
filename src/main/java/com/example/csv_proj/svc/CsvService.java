package com.example.csv_proj.svc;

import com.example.csv_proj.dto.CsvEntity;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ainur Shigapov
 */

@Service
public class CsvService {
    public void doWork() {
        File dbFile = getFileFromURL("db.csv");
        File mktFile = getFileFromURL("mkt.csv");

        List<CsvEntity> dbObjList = null;
        List<CsvEntity> mktObjList = null;
        try {
            dbObjList = getListObj(dbFile);
            mktObjList = getListObj(mktFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<String> dbCollect = new HashSet<>();
        if (dbObjList != null) {
            dbCollect = dbObjList.stream()
                    .map(item -> PhoneNumberUtil.normalizePhoneNumber(item.getMobNumber()))
                    .collect(Collectors.toSet());
        }
        Set<String> mktCollect = new HashSet<>();
        if (mktObjList != null) {
            mktCollect = mktObjList.stream()
                    .map(item -> PhoneNumberUtil.normalizePhoneNumber(item.getMobNumber()))
                    .collect(Collectors.toSet());
        }

        Set<String> notExistNumbersInDb = processComparing(dbCollect, mktCollect);
        Set<String> notExistNumbersInMKT = processComparing(mktCollect, dbCollect);

        //Get current date time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
        String formatedDateTime = now.format(formatter);

        try {
            writeToFile(notExistNumbersInDb, "not-exist-numbers-in-db " + formatedDateTime + ".csv");
            writeToFile(notExistNumbersInMKT, "not-exist-numbers-in-mkt " + formatedDateTime + ".csv");
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }

    private Set<String> processComparing(Set<String> collectOne, Set<String> collectTwo) {
        final Set<String> resultCollect = new HashSet<>();
        for (String collectTwoNumber : collectTwo) {
            if (collectTwoNumber == null || collectTwoNumber.isEmpty()) continue;
            if (!collectOne.contains(collectTwoNumber)) {
                resultCollect.add(collectTwoNumber);
            }
        }
        return resultCollect;
    }

    private void writeToFile(Set<String> resultCollect, String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String OBJECT_LIST_SAMPLE = "./" + fileName;
        try (Writer writer = Files.newBufferedWriter(Paths.get(OBJECT_LIST_SAMPLE))) {
            StatefulBeanToCsv<CsvEntity> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            final List<CsvEntity> collect = resultCollect.stream().map(x -> {
                final CsvEntity csvEntity = new CsvEntity();
                csvEntity.setMobNumber(x);
                return csvEntity;
            }).collect(Collectors.toList());
            beanToCsv.write(collect);
        }
    }

    public List<CsvEntity> getListObj(File file) throws IOException {
        CsvToBeanBuilder<CsvEntity> csvToBeanBuilder = new CsvToBeanBuilder<CsvEntity>(new FileReader(file));
        List<CsvEntity> paymentCSVEntities = csvToBeanBuilder.withType(CsvEntity.class).build().parse();
        return paymentCSVEntities;
    }

    private File getFileFromURL(String fileName) {
        URL url = this.getClass().getClassLoader().getResource(fileName);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        } finally {
            return file;
        }
    }

}
