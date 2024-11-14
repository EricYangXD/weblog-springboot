package com.quanxiaoha.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_user")
public class UserDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(condition = SqlCondition.LIKE)
    private String username;

    private String password;

    private Date createTime;

    private Date updateTime;

    @TableField(exist = false)
    private Boolean isDeleted;

    private String email;

    private Integer age;

    private String address;
}
