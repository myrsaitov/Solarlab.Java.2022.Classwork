package ru.solarlab.study.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.solarlab.study.entity.Task;

/**
 * ProducerService.
 *
 * @author stepochkin
 */
@Slf4j
@AllArgsConstructor
@Service
public class ProducerService {

    private final KafkaTemplate<String, Task> kafkaTemplate;
    @Value(value = "${kafka.topic}")
    private String topic;

    public void sendMessage(Task task) {
        ListenableFuture<SendResult<String, Task>> future = kafkaTemplate.send(topic, task);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Task>>() {

            @Override
            public void onSuccess(SendResult<String, Task> result) {
                log.info("Sent Task=[" + task.getId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send Task=[" + task.getId() + "] due to : " + ex.getMessage());
            }
        });
    }

}