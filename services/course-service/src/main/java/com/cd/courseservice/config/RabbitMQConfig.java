package com.cd.courseservice.config;

import com.cd.courseservice.dto.ExamSubmittedEvent;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing-key}")
    private String routingKey;
    @Value("${rabbitmq.queue}")
    private String queue;
    @Value("${rabbitmq.exam-submitted.queue}")
    private String examSubmittedQueue;
    @Value("${rabbitmq.exam-submitted.routing-key}")
    private String examSubmittedRoutingKey;

    @Bean
    public TopicExchange courseExchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue examSubmittedQueue (){
        return new Queue(examSubmittedQueue);
    }

    @Bean
    public Binding examSubmittedBinding(){
        return BindingBuilder.bind(examSubmittedQueue()).to(courseExchange()).with(examSubmittedRoutingKey);
    }



    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue queue() {
        return new Queue(queue, true);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
//        converter.setClassMapper(classMapper());
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

//    @Bean
//    public DefaultClassMapper classMapper(){
//        DefaultClassMapper classMapper = new DefaultClassMapper();
//        Map<String,Class<?>> idClassMapping = new HashMap<>();
//        idClassMapping.put("com.cd.examservice.dto.ExamSubmittedEvent", ExamSubmittedEvent.class);
//        classMapper.setIdClassMapping(idClassMapping);
//        return classMapper;
//}


}
