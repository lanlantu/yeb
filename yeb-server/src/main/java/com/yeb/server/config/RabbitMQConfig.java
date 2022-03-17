package com.yeb.server.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.yeb.server.pojo.MailConstants;
import com.yeb.server.pojo.MailLog;
import com.yeb.server.service.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * RabbitMQ配置类
 * @author Suntingxing
 * @date 2021/10/25 0:43
 */
@Configuration
public class RabbitMQConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;
    @Autowired
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);

        /**
         * 消息确认回调，确认消息是否到达broker
         * data:消息唯一标识
         * ack:确认结果 就是成功或者失败
         * cause：失败的原因
         */
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            String msgId = data.getId();
            if(ack){
                LOGGER.info("{}========>消息发送成功",msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",1).eq("msgId",msgId));
            }else {
                LOGGER.error("{}========>消息发送失败",msgId);
            }
        });

        /**
         * 消息回调失败。比如router不到queue时回调
         * msg:消息的主题
         * repCode：响应码
         * repText：响应描述
         * exchange：交换机
         * routingkey：路由键
         */
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                LOGGER.error("{}=======>消息发送失败",returnedMessage);
            }
        });
        // 如果上面那个错了用这个
        /*rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("消息从Exchange路由到Queue失败: exchange: {}, route: {}, replyCode: {}, replyText: {}, message: {}", exchange, routingKey, replyCode, replyText, message);
        });*/
        return rabbitTemplate;
    }


    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }
}
