package com.kt.smp.stt.comm.directory.controller;

import com.kt.smp.base.annotation.SmpServicePage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${smp.service.uri.prefix}")
@RequiredArgsConstructor
public class DirectoryGroupWebController {

    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;

    @SmpServicePage(name = "디렉토리 그룹", path = "/directory/group", html = "/html/directory/group")
    public String goDirectoryGroupList(Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "pages/index";
    }

    @RequestMapping("/html/directory/group")
    public String getDirectoryGroupListHtml(Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "stt/comm/directoryGroupList";
    }

    @SmpServicePage(name = "디렉토리 그룹 상세", path = "/directory/group/{groupId}", html = "/html/directory/group/detail", parent = "/directory/group")
    public String goDirectoryGroupDetail(Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "pages/index";
    }

    @RequestMapping("/html/directory/group/detail")
    public String getDirectoryGroupDetailHtml(Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "stt/comm/directoryGroupDetail";
    }
}
