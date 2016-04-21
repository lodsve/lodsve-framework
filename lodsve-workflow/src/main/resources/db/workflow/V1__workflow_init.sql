-- ----------------------------
--  Table structure for `t_flow_node`
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_node`;
CREATE TABLE `t_flow_node` (
  `id` bigint(20) NOT NULL,
  `flow_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `conditional` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `next` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `node_version` int(11) DEFAULT NULL,
  `interceptor_bean` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `interceptor_class` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `url_type` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `t_workflow`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow`;
CREATE TABLE `t_workflow` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `domain` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `xml` varchar(3000) COLLATE utf8_bin DEFAULT NULL,
  `xml_md` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `t_work_task`
-- ----------------------------
DROP TABLE IF EXISTS `t_work_task`;
CREATE TABLE `t_work_task` (
  `id` bigint(20) NOT NULL,
  `flow_id` bigint(20) DEFAULT NULL,
  `flow_title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `node_id` bigint(20) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  `process_title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `node_title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `url_type` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `task_user_id` bigint(20) DEFAULT NULL,
  `task_user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `result` int(11) DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `receive_time` datetime DEFAULT NULL,
  `handle_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `t_form_url`
-- ----------------------------
DROP TABLE IF EXISTS `t_form_url`;
CREATE TABLE `t_form_url` (
  `id` bigint(20) NOT NULL,
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `url_type` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `flow_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `t_process_instance`
-- ----------------------------
DROP TABLE IF EXISTS `t_process_instance`;
CREATE TABLE `t_process_instance` (
  `id` bigint(20) NOT NULL,
  `flow_id` bigint(20) DEFAULT NULL,
  `flow_title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  `process_title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `start_user_id` bigint(20) DEFAULT NULL,
  `start_user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `current_node_id` bigint(20) DEFAULT NULL,
  `current_node_title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;