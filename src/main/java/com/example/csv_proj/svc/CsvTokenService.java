package com.example.csv_proj.svc;

import com.example.csv_proj.dto.TokenEntity;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CsvTokenService {

    public void doWork() {
        File dbTokensFile = getFileFromURL("tokens.csv");
        List<TokenEntity> tokenEntityList = null;
        try {
            tokenEntityList = getListObj(dbTokensFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(tokenEntityList.size());

        tokenEntityList = tokenEntityList.stream()
                .filter(item -> item.getMobNumber() != null && !Objects.equals(item.getMobNumber(), ""))
                .distinct()
                .collect(Collectors.toList());

        System.out.println(tokenEntityList.size());
        System.out.println("GREAT");

        try {
            writeCsvFromBean("db-numbers.csv", tokenEntityList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void writeCsvFromBean(String pathName, List<TokenEntity> tokenEntityList) throws Exception {

        if (tokenEntityList != null) {
            try (
                    Writer writer = new FileWriter(pathName);

                    CSVWriter csvWriter = new CSVWriter(writer,
                            CSVWriter.DEFAULT_SEPARATOR,
                            CSVWriter.NO_QUOTE_CHARACTER,
                            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                            CSVWriter.DEFAULT_LINE_END);
            ) {
                for (TokenEntity tokenEntity : tokenEntityList) {
                    String[] headerRecord = {tokenEntity.getMobNumber(), tokenEntity.getProjectId()};
                    csvWriter.writeNext(headerRecord);
                }
            }

        }

        System.out.println("FINISHED WORK");
    }

    public List<TokenEntity> getListObj(File file) throws IOException {
        CsvToBeanBuilder<TokenEntity> csvToBeanBuilder = new CsvToBeanBuilder<TokenEntity>(new FileReader(file));
        List<TokenEntity> tokenEntityList = csvToBeanBuilder.withType(TokenEntity.class).build().parse();
        return tokenEntityList;
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
