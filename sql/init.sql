CREATE TABLE `t_bu_leave` (
  `id` bigint NOT NULL COMMENT '请假单唯一标识',
  `applicant_id` bigint NOT NULL COMMENT '申请人ID',
  `applicant_name` varchar(100) DEFAULT NULL COMMENT '申请人姓名',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime NOT NULL COMMENT '结束日期',
  `leave_type` varchar(50) NOT NULL COMMENT '请假类型',
  `leave_type_name` varchar(50) DEFAULT NULL COMMENT '请假类型名称',
  `days` varchar(10) NOT NULL COMMENT '请假天数',
  `reason` varchar(500) NOT NULL COMMENT '请假原因',
  `status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '状态：pending, approved, rejected, cancelled',
  `approver_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approver_name` varchar(100) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_comment` varchar(500) DEFAULT NULL COMMENT '审批意见',
  `attachment_url` varchar(500) DEFAULT NULL COMMENT '附件图片url',
  `department` varchar(100) DEFAULT NULL COMMENT '部门',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假申请表';

CREATE TABLE `t_bu_approval` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `leave_id` bigint NOT NULL COMMENT '请假申请ID',
  `type` varchar(50) DEFAULT 'leave' COMMENT '审批类型(leave-请假)',
  `status` varchar(20) DEFAULT 'pending' COMMENT '审批状态(pending-待审批,approved-已通过,rejected-已拒绝)',
  `current_step` int DEFAULT '1' COMMENT '当前审批步骤',
  `total_steps` int DEFAULT NULL COMMENT '总审批步骤数',
  `current_approver_id` bigint DEFAULT NULL COMMENT '当前审批人ID',
  `current_approver_name` varchar(100) DEFAULT NULL COMMENT '当前审批人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_leave_id` (`leave_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批流程主表';

CREATE TABLE `t_bu_approval_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `step_order` int DEFAULT NULL COMMENT '审批步骤顺序',
  `step_name` varchar(100) DEFAULT NULL COMMENT '步骤名称',
  `description` varchar(255) DEFAULT NULL COMMENT '步骤描述',
  `approval_type` varchar(20) DEFAULT NULL COMMENT '审批类型(sequential-顺序审批,parallel-并行审批)',
  `approver_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approver_name` varchar(100) DEFAULT NULL COMMENT '审批人姓名',
  `department` varchar(100) DEFAULT NULL COMMENT '部门',
  `position` varchar(100) DEFAULT NULL COMMENT '职位',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批配置表';

CREATE TABLE `t_bu_approval_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `leave_id` bigint NOT NULL COMMENT '请假申请ID',
  `step_order` int DEFAULT NULL COMMENT '审批步骤顺序',
  `approver_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approver_name` varchar(100) DEFAULT NULL COMMENT '审批人姓名',
  `result` varchar(20) DEFAULT NULL COMMENT '审批结果(approved-通过,rejected-拒绝)',
  `comment` varchar(500) DEFAULT NULL COMMENT '审批意见',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_leave_id` (`leave_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批流转记录表';

CREATE TABLE `t_bu_repair_diagnosis` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `order_id` varchar(64) NOT NULL COMMENT '工单编号',
  `vehicle` varchar(100) NOT NULL COMMENT '车辆型号',
  `symptom` varchar(500) NOT NULL COMMENT '故障现象',
  `dtc` varchar(50) DEFAULT NULL COMMENT '故障码',
  `diagnosis` varchar(500) DEFAULT NULL COMMENT '诊断结论',
  `solution` varchar(1000) DEFAULT NULL COMMENT '维修方案',
  `parts` varchar(500) DEFAULT NULL COMMENT '更换配件',
  `hours` decimal(10,2) DEFAULT NULL COMMENT '维修工时(小时)',
  `confidence` decimal(4,2) DEFAULT NULL COMMENT '诊断置信度',
  `result_verification` varchar(50) DEFAULT NULL COMMENT '验证结果',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_vehicle` (`vehicle`),
  KEY `idx_dtc` (`dtc`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修诊断知识库表';

CREATE TABLE `t_bu_user` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `openid` varchar(100) DEFAULT NULL COMMENT '用户的OpenID',
  `mp_openid` varchar(100) DEFAULT NULL COMMENT '用户的mpOpenID',
  `user_first_login_time` datetime DEFAULT NULL COMMENT '首次登陆时间',
  `user_last_login_time` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `name` varchar(100) DEFAULT NULL COMMENT '用户名字',
  `department` varchar(100) DEFAULT NULL COMMENT '所在部门',
  `position` varchar(100) DEFAULT NULL COMMENT '职位',
  `permissions` text COMMENT '用户权限',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像URL',
  `audit_flag` int DEFAULT '0' COMMENT '审批人标识',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';