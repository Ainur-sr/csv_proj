package com.example.csv_proj.dto;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class TokenEntity {

    @CsvBindByPosition(position = 2)
    private String mobNumber;

    @CsvBindByPosition(position = 4)
    private String projectId;



}
