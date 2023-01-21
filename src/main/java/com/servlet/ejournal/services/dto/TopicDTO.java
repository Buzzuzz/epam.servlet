package com.servlet.ejournal.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicDTO {
    private final long topicId;
    private String topicName;
    private String topicDescription;
}
