package com.kt.smp.stt.common.cipher.db;

import org.springframework.stereotype.Component;

@Component
public class CipherWrapper {

    public String encode(String source) {
        if (source != null && !source.isEmpty()) {
            //TODO 암호화로직
            return "<cipher>" + source + "</cipher>";
        }

        return source;
    }

    public String decode(String source) {
        if (source != null && !source.isEmpty()) {
            //TODO 복호화로직
            return source.replace("<cipher>", "").replace("</cipher>", "");
        }

        return source;
    }

}
