package com.batab.blog.controller;

import com.batab.blog.dto.MailDto;
import com.batab.blog.service.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService){
        this.emailService = emailService;
    }

    @GetMapping("/mail/send")
    public String main() {
        return "SendMail.html";
    }

    @PostMapping("/mail/send")
    public String sendMail(MailDto mailDto) {
        emailService.sendSimpleMessage(mailDto);
        System.out.println("메일 전송 완료");

        return "AfterMail.html";
    }

}
