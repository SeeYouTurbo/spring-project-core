package com.king.mybatis.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 描述哈哈
 *
 * @author jinfeng.wu
 * @date 2020/12/8 10:25
 */
@Data
public class BaseEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
//    @ApiModelProperty("主键id")
    private Long id;

//    @ApiModelProperty("创建人")
    private String createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

//    @ApiModelProperty("更新人")
    private String updateUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @TableLogic
//    @ApiModelProperty("删除标识 1->删除 0->未删除")
    private Integer deleteFlag;
}
