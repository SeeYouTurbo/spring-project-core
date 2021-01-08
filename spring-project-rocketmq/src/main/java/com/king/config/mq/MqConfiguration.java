package com.king.config.mq;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * mq Config
 *
 * @author jinfeng.wu
 * @date 2020/11/19 17:41
 */
@Configuration
public class MqConfiguration {

    @Resource
    private MqMsgConsumer mqMsgConsumer;

    private Properties getMqProperties(MqEntity mqEntity) {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, mqEntity.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, mqEntity.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, mqEntity.getNameSrvAddr());
        return properties;
    }

    @Bean("mqEntity")
    @ConfigurationProperties(prefix = "rocketmq.user")
    public MqEntity mqEntity() {
        return new MqEntity();
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean buildEventConsumer() {
        MqEntity mqEntity = mqEntity();
        if (mqEntity == null || mqEntity.getAccessKey() == null) {
            return null;
        }
        ConsumerBean consumerBean = new ConsumerBean();
        // 配置文件
        Properties properties = getMqProperties(mqEntity);
        MqEntity.GroupBean groupBean = mqEntity.findTopicBean("PUSH_XINGE");
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupBean.getGroupId());
        // 消费配置参数
        consumerBean.setProperties(properties);
        deploySubscriptionTable(consumerBean, groupBean, mqMsgConsumer);
        return consumerBean;
    }


    private void deploySubscriptionTable(ConsumerBean consumerBean, MqEntity.GroupBean groupBean, MessageListener messageListener) {
        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<Subscription, MessageListener>(1);
        Subscription subscription = new Subscription();
        subscription.setTopic(groupBean.getTopic());
        subscription.setExpression(groupBean.getTag());
        subscriptionTable.put(subscription, messageListener);
        //订阅多个topic如上面设置
        consumerBean.setSubscriptionTable(subscriptionTable);
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown", name = "producerBean")
    public ProducerBean buildProducerBean() {
        MqEntity mqEntity = mqEntity();
        if (mqEntity == null || mqEntity.getAccessKey() == null) {
            return null;
        }
        ProducerBean producerBean = new ProducerBean();
        //配置文件
        Properties properties = getMqProperties(mqEntity);
        producerBean.setProperties(properties);
        return producerBean;
    }

}
