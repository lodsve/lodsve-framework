-- ----------------------------
--  Table structure for `t_flow_node`
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_node`;
CREATE TABLE `t_flow_node` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `flow_id` bigint(20) DEFAULT NULL,
  `method` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `roles` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `next` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `node_version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `t_workflow`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow`;
CREATE TABLE `t_workflow` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `domain` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `handler` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
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
  `handle_time` bigint(20) DEFAULT NULL,
  `node_id` bigint(20) DEFAULT NULL,
  `process_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `receive_time` bigint(20) DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  `result` int(11) DEFAULT NULL,
  `task_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `task_user_id` bigint(20) DEFAULT NULL,
  `task_user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;