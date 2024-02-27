package com.kt.smp.stt.verify.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VerifyDataDeleteDto {

    private List<Integer> targetIdList;
    private Integer datasetId;
    private String datasetName;

}
