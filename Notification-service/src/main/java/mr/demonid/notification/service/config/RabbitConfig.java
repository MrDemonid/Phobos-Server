package mr.demonid.notification.service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Настройка RabbitMQ.
 * Обменник: "PhobosNotification"
 * Очередь: "PhobosQueueNotification"
 */
@Configuration
public class RabbitConfig {

    /**
     * Создаем обменник.
     */
    @Bean
    public FanoutExchange springCloudBusExample() {
        return new FanoutExchange("PhobosNotification");
    }

    /**
     * Создаем очередь для сообщений.
     */
    @Bean
    public Queue springCloudBusQueue() {
        return new Queue("PhobosQueueNotification");
    }

    /**
     * Связываем очередь сообщений с обменником.
     */
    @Bean
    public Binding binding(FanoutExchange springCloudBusExample, Queue springCloudBusQueue) {
        return BindingBuilder.bind(springCloudBusQueue)
                .to(springCloudBusExample);
    }


    /**
     * Компонент для отправки сообщений в RabbitMQ.
     * Назначаем ему новый нормальный конвертер JSON, взамен его собственного,
     * довольно хиленького и примитивного.
     */
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(rabbitMessageConverter());
        return template;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(rabbitMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter rabbitMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}

