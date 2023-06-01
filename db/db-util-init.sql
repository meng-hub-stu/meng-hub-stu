-- 用户登录表
CREATE TABLE `hub_user` (
  `id` bigint(64) NOT NULL COMMENT ''id'',
  `tenant_id` varchar(12) DEFAULT null COMMENT '租户id',
  `birthday` datetime DEFAULT NULL COMMENT ''生日'',
  `gender` varchar(1) DEFAULT NULL COMMENT ''性别'',
  `user_name` varchar(32) NOT NULL COMMENT ''用户名'',
  `password` varchar(256) NOT NULL COMMENT ''密码'',
  `remark` varchar(32) DEFAULT NULL COMMENT ''描述'',
  `mobile` varchar(11) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL COMMENT ''姓名'',
  `create_user` bigint(64) DEFAULT NULL COMMENT ''创建人'',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `update_user` bigint(64) DEFAULT NULL COMMENT ''修改人'',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ''修改时间'',
	`status` TINYINT(1) DEFAULT 1 COMMENT ''状态'',
  `is_deleted` TINYINT(1) DEFAULT 0 COMMENT ''是否已删除'',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_username` (`user_name`),
  KEY `idx_name` (`name`) COMMENT ''姓名''
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT=''用户表'';

-- 存储日志
create table hub_log(
	`id` bigint(64) NOT NULL COMMENT ''id'',
	`tenant_id` varchar(12) DEFAULT null COMMENT '租户id',
	`method` varchar(64) DEFAULT null COMMENT ''方法'',
	`title` varchar(64) DEFAULT null COMMENT ''标体'',
	`content` varchar(512) DEFAULT null COMMENT ''内容'', 	
	`create_user` bigint(64) DEFAULT NULL COMMENT ''创建人'',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `update_user` bigint(64) DEFAULT NULL COMMENT ''修改人'',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ''修改时间'',
	`status` TINYINT(1) DEFAULT 1 COMMENT ''状态'',
  `is_deleted` TINYINT(1) DEFAULT 0 COMMENT ''是否已删除'',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT=''日志表'';

-- 部门隔离表
create table hub_dept(
	`id` bigint(64) NOT NULL COMMENT 'id',
	`tenant_id` varchar(12) DEFAULT null COMMENT '租户id',
	`dept_name` varchar(64) DEFAULT null COMMENT '标体',
	`dept_id` BIGINT(64) DEFAULT null COMMENT '部门id',
	`dept_alias` varchar(512) DEFAULT null COMMENT '部门别名',
	`create_user` bigint(64) DEFAULT NULL COMMENT '创建人',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(64) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	`status` TINYINT(1) DEFAULT 1 COMMENT '状态',
  `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否已删除',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='部门隔离表';