package com.yeb.server.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 消息
 * @author Suntingxing
 * @date 2021/10/27 0:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChatMsg {
    private String from; //发起人
    private String to; //接收人
    private String content; //内容
    private LocalDateTime date; //时间
    private String fromNickName; //发起人昵称
}
