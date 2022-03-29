package ru.solarlab.study.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.solarlab.study.entity.Task;

import java.util.Random;

/**
 * QueueConsumer.
 *
 * @author stepochkin
 */
@Slf4j
@Component
@AllArgsConstructor
public class QueueConsumer {

    private final Random random = new Random();

    @RabbitListener(queues = "queue1")
    public void receive(final Task task) throws InterruptedException {
        log.info("receive 1: {}", task);
        Thread.sleep(100 * random.nextInt(20));
    }

    /*@RabbitListener(queues = "queue1")
    public void receive2(final Task task) throws InterruptedException {
        log.info("receive2: {}", task);
        Thread.sleep(100 * random.nextInt(20));
    }*/
}