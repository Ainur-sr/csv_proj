package com.example.csv_proj.dto;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ainur Shigapov <ainur.shigapov@novardis.com>
 */

@Data
@Getter
@Setter
public class CsvEntity {

    @CsvBindByPosition(position = 0)
    private String mobNumber;

}
