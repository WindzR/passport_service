package ru.job4j.domain;

import lombok.Data;

@Data
public class MessageDTO {

    private int id;

    private String message;

    public static MessageDTO of(int id, String message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(id);
        messageDTO.setMessage(message);
        return messageDTO;
    }
}
