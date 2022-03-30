package com.yeb.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yeb.server.pojo.Employee;
import com.yeb.server.pojo.MailConstants;
import com.yeb.server.pojo.MailLog;
import com.yeb.server.service.IEmployeeService;
import com.yeb.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件发送定时任务
 * @author Suntingxing
 * @date 2021/10/26 0:10
 */
@Component
public class MailTask {

    @Autowired
    private IMailLogService mailLogService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 邮件发送定时任务
     * 10s执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask(){
        // 查询重试时间小于当前时间
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>()
                .eq("status", 0).lt("tryTime", LocalDateTime.now()));
        for (MailLog mailLog : list) {
            // 如果重试次数超过三次，更新状态为投递失败，并且不再重试
            if(mailLog.getCount() >= 3){
                mailLogService.update(new UpdateWrapper<MailLog>()
                        .set("status",2)
                        .eq("msgId",mailLog.getMsgId()));
            }
            // 如果没有超过重试次数 就更新次数
            mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("count",mailLog.getCount() + 1)
                    .set("updateTime",LocalDateTime.now())
                    .set("tryTime",LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT))
                    .eq("msgId",mailLog.getMsgId()));
            Employee emp = employeeService.getEmployee(mailLog.getEid()).get(0);
            //重新发送消息
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY_NAME
                    ,emp,new CorrelationData(mailLog.getMsgId()));
        }
    }
}
