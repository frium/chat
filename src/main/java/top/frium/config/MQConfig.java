package top.frium.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static top.frium.context.RabbitMQConstant.EMAIL_QUEUE;

/**
 *
 * @date 2024-08-02 14:23:41
 * @description
 */
@Configuration
public class MQConfig {
    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }
}
