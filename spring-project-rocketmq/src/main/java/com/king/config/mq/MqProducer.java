package com.king.config.mq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.king.common.util.Func;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 描述哈哈
 *
 * @author jinfeng.wu
 * @date 2020/11/20 17:55
 */
@Service
@Slf4j
public class MqProducer {

    @Resource
    private MqConfiguration mqConfiguration;

    public void sendByBizName(Object msg, String bizName) {
        log.info("业务:【{}】 mq通知:【{}】", bizName, JSON.toJSONString(msg));
        Message message = buildMessage(JSON.toJSONString(msg), bizName);
        this.send(message, bizName);
    }

    public void sendByBizName(Object msg, String bizName, String key) {
        log.info("业务:【{}】 mq通知:【{}】", bizName, JSON.toJSONString(msg));
        Message message = buildMessage(JSON.toJSONString(msg), bizName, key);
        this.send(message, bizName);
    }

    public void sendByBizName(Object msg, String bizName, String key, LocalDateTime time) {
        log.info("业务:【{}】 mq通知:【{}】", bizName, JSON.toJSONString(msg));
        Message message = buildMessage(JSON.toJSONString(msg), bizName, key, JSON.toJSONString(msg), 0L, null, time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.send(message, bizName);
    }

    private void send(Message message, String bizName){
        mqConfiguration.buildProducerBean().sendAsync(message, new SendCallback() {
            @Override
            public void onSuccess(final SendResult sendResult) {
                // 消息发送成功。
                log.info("send 业务:【{}】 mq通知 success. topic=" + sendResult.getTopic() + ", msgId=" + sendResult.getMessageId(), bizName);
            }

            @Override
            public void onException(OnExceptionContext context) {
                // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理。
                log.error("send 业务:【{}】 mq通知 failed. topic=" + context.getTopic() + ", msgId=" + context.getMessageId(), bizName);
            }
        });
    }


    private Message buildMessage(String body, String bizName) {
        return buildMessage(body, bizName, null);
    }

    private Message buildMessage(String body, String bizName, String key) {
        return buildMessage(body, bizName, key, null);
    }

    private Message buildMessage(String body, String bizName, String key, LocalDateTime time) {
        log.info("业务:【{}】 mq通知:【{}】", bizName, body);
        MqEntity.GroupBean groupBean = mqConfiguration.mqEntity().findTopicBean(bizName);
        if(time != null){
            return buildMessage(groupBean.getTopic(), groupBean.getTag(), key, body, 0L, null, time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        return buildMessage(groupBean.getTopic(), groupBean.getTag(), key, body, 0L, null, null);
    }

    private Message buildMessage(String topic, String tag, String key, String body, long delayTime, TimeUnit delayTimeUnit, String time) {
        Message message = new Message();
        message.setTopic(topic);
        message.setTag(tag);
        message.setBody(body.getBytes());
        if (Func.isNotBlank(key)) {
            message.setKey(key);
        }
        if (delayTimeUnit != null && delayTime > 0) {
            message.setStartDeliverTime(System.currentTimeMillis() + delayTimeUnit.toMillis(delayTime));
        }
        if(Func.isNotBlank(time)){
            try {
                message.setStartDeliverTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

}
