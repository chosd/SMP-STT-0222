package com.kt.smp.stt.dictation.dto;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.stt.dictation.type.UsageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DictationUpdateDto {

    private Integer id;
    private String dictatedText;
    private UsageType usageType;
    private List<UsageSaveDto> usageList;
    private String updId;
    private String updIp;


    public DictationUpdateDto(DictationDto dictation, UsageType usageType) {
        this.id = dictation.getId();
        this.dictatedText = dictation.getDictatedText();
        this.usageType = usageType;
        this.usageList = new ArrayList<>();
    }

    public void addUsage(UsageSaveDto newUsage) {
        this.usageList.add(newUsage);
    }

    public void audit(HttpServletRequest request) {
        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        updId = header.getUserId();
        updIp = request.getRemoteAddr();

    }

    public boolean hasSameUsageTypeWith(DictationDto dictation) {
        return usageType.equals(dictation.getUsageType());
    }

    public void setUsageType(UsageType usageType) {
        this.usageType = usageType;
    }
}
