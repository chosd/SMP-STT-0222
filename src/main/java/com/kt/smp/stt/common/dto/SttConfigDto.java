package com.kt.smp.stt.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SttConfigDto {

	private Integer Id;
	
    private String codeKey;

    private String codeValue;
    
    private String description;

    @Override
    public String toString() {
        return "BaseDto{" +
                "Id='" + Id + '\'' +
                ", codeKey='" + codeKey + '\'' +
                ", codeValue='" + codeValue + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
