package ru.solarlab.study.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.solarlab.study.entity.Task;

/**
 * ConsumerService.
 *
 * @author stepochkin
 */
@Slf4j
@Service
public class ConsumerService {

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.groupId}")
    public void listenGroupFoo(Task task) {
        log.info("Received Task {}", task);
    }
}