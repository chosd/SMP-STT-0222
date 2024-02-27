package com.kt.smp.listener;

import com.kt.smp.common.util.FileUtil;
import com.kt.smp.fileutil.constant.ExcelConstants;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class SessionListener implements HttpSessionListener {

    @Value("${server.servlet.session.timeout}")
    private int sessionTime;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(sessionTime);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        Set<String> excelFilePathes = (HashSet<String>) session.getAttribute(ExcelConstants.SESSION_KEY);

        if (ObjectUtils.isEmpty(excelFilePathes)) {
            return;
        }

        for (String excelFilePath : excelFilePathes) {
            File fileToDelete = new File(excelFilePath);

            if (!fileToDelete.exists()) {
                continue;
            }

            FileUtil.deleteFile(fileToDelete);
        }
    }

}
