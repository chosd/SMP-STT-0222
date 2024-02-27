package com.kt.smp.stt.comm.directory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectoryGroupSearchCondition {

    private String textType;
    private String gubunType;
    private String text;
    private String name;
    private Integer id;
    private Integer start;
    private Integer size;

    public void determineConditionByType() {

    	if ("ALL".equals(textType) || textType.isEmpty()) {
            try {
                name = text;
                id = Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                id = null;
            }
            return;
    	}
    	
        if ("ID".equals(textType)) {
            try {
                name = null;
                id = Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                id = null;
            }
            return;
        }
        
        name = text;
    }
}
