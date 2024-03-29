package com.mdx.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.assertj.core.util.Lists;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

/**
 * @author Mengdl
 * @date 2023/04/25
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@TableName(value = "hub_user")
@ApiModel(value = "User登录对象", description = "User登录对象")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 测试账号名单
     */
    public static List<String> OPERATOR_TEST_LIST;
    /**
     * 支持多人登录账号名单
     */
    public static List<String> MULTIPLE_SIMULTANEOUS_ONLINE_USERS;

    static {
        OPERATOR_TEST_LIST = Lists.newArrayList(
                "cs1", "cs2", "cs3", "cs4", "cs5", "cccezsr2", "ccceshizsr", "shuangyi",
                "ceshiss1", "ceshiss", "ccceshi", "ceshiyyn", "liuyw", "ccpm",
                "fujiawei", "CQyunyingceshi", "LXTBceshi", "LXZBXBKceshi",
                "CHECHEZBKFfy", "liss", "zhaoshirong", "baojia"
        );
        MULTIPLE_SIMULTANEOUS_ONLINE_USERS = Lists.newArrayList("LX2011XKJzx");
        MULTIPLE_SIMULTANEOUS_ONLINE_USERS.addAll(OPERATOR_TEST_LIST);

    }
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "姓名")
    private String name;

    @TableField(exist = false)
    private Set<Role> roles = new HashSet<Role>(0);

}
