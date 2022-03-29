package ru.solarlab.study.rabbitmq;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.solarlab.study.entity.Task;

/**
 * QueueSender.
 *
 * @author stepochkin
 */
@Component
@AllArgsConstructor
public class QueueSender {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange direct;

    public void send(Task task) {
        rabbitTemplate.convertAndSend(direct.getName(), task.getStatus().name().toLowerCase(), task);
    }
}