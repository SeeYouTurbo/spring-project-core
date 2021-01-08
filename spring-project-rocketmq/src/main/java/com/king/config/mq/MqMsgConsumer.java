package com.king.config.mq;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 推送延时队列消费
 *
 * @author jinfeng.wu
 * @date 2020/10/24 16:06
 */
@Service
@Slf4j
public class MqMsgConsumer implements MessageListener {


    @Override
    public Action consume(Message message, ConsumeContext context) {
        String string = new String(message.getBody());
        log.info("推送延时队列消息===> 消费msgId:【{}】,消息body:【{}】", message.getMsgID(), string);
        try {
            return Action.CommitMessage;
        }catch (Exception e){
            log.error("推送延时队列消息消费 失败，错误信息【{}】", e.getMessage(), e);
            //消费失败
            return Action.ReconsumeLater;
        }
    }

}
