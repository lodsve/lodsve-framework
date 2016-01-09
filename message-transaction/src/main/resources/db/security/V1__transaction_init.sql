-- ----------------------------
--  Table structure for `t_payment`
-- ----------------------------
DROP TABLE IF EXISTS `t_payment`;
CREATE TABLE `t_payment` (
  `id` bigint(20) NOT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `complete_time` timestamp DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `target_id` varchar(3000) COLLATE utf8_bin DEFAULT NULL,
  `order_time` timestamp DEFAULT NULL,
  `pay_account` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `trade_channel` char(10) DEFAULT NULL,
  `trade_result` char(10) DEFAULT NULL,
  `trade_type` char(10) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `product_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `union_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `charge_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_tel_no` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_acl3f8fyvexbda1dhc95uh758` (`union_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `t_refund`
-- ----------------------------
DROP TABLE IF EXISTS `t_refund`;
CREATE TABLE `t_refund` (
  `id` bigint(20) NOT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `apply_time` timestamp DEFAULT NULL,
  `charge_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `complete_time` timestamp DEFAULT NULL,
  `trade_result` char(10) DEFAULT NULL,
  `payment_id` bigint(20) DEFAULT NULL,
  `refund_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
