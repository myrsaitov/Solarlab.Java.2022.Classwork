package ru.solarlab.study.rabbitmq;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final Queue queue;

    public void send(Task task) {
        rabbitTemplate.convertAndSend(this.queue.getName(), task);
    }
}