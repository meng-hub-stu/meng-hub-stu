package com.mdx.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体父类，可以进行分租户管理
 * @author Mengdl
 * @date 2023/04/23
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseEntity implements Serializable {

    @ApiModelProperty("租户ID")
    private String tenantId;

    @JsonSerialize(
        using = ToStringSerializer.class
    )
    private Long createUser;
    @DateTimeFormat(
        pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;
    @JsonSerialize(
        using = ToStringSerializer.class
    )
    private Long updateUser;
    @DateTimeFormat(
        pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
        pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateTime;
    private Integer status;
    @TableLogic
    private Integer isDeleted;

}
