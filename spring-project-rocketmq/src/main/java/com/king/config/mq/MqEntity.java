package com.king.config.mq;

import lombok.Data;

import java.util.List;

/**
 * MqEntity
 *
 * @author jinfeng.wu
 * @date 2020/10/24 15:47
 */
@Data
public class MqEntity {
    /**
     * RocketMQ regionId
     */
    private String regionId;
    /**
     * RocketMQ accessKey
     */
    private String accessKey;
    /**
     * RocketMQ secretKey
     */
    private String secretKey;
    /**
     * RocketMQ nameSrvAddr
     */
    private String nameSrvAddr;
    /**
     * RocketMQ group列表
     */
    private List<GroupBean> groupList;

    @Data
    public static class GroupBean {
        /**
         * RocketMQ groupId 要处理的业务名称
         */
        private String bizName;
        /**
         * RocketMQ instanceId
         */
        private String instanceId;
        /**
         * RocketMQ topic
         */
        private String topic;
        /**
         * RocketMQ api调用需要的区域id
         */
        private String onsRegionId;
        /**
         * RocketMQ groupId
         */
        private String groupId;
        /**
         * RocketMQ tag
         */
        private String tag;
        /**
         * RocketMQ ConsumeThreadNums 消费端线程数
         */
        private String consumeThreadNums;
    }

    public GroupBean findTopicBean(String bizName) {
        for (GroupBean groupBean : groupList) {
            if (bizName.equals(groupBean.bizName)) {
                return groupBean;
            }
        }
        return null;
    }
}
