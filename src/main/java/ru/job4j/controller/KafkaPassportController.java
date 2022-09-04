package ru.job4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.domain.MessageDTO;

@RestController
public class KafkaPassportController {

    @Autowired
    private KafkaTemplate<Integer, MessageDTO> template;

    @PostMapping
    public void sendNotification(MessageDTO messageDTO) {
        ListenableFuture<SendResult<Integer, MessageDTO>> future
                = template.send("passport_service", messageDTO.getId(), messageDTO);
        future.addCallback(System.out :: println, System.err :: println);
        template.flush();
    }
}
