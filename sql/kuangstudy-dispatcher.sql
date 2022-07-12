/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 60011
Source Host           : localhost:3306
Source Database       : kuangstudy-dispatcher

Target Server Type    : MYSQL
Target Server Version : 60011
File Encoding         : 65001

Date: 2021-03-31 20:25:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ksd_dispather_order
-- ----------------------------
DROP TABLE IF EXISTS `ksd_dispather_order`;
CREATE TABLE `ksd_dispather_order` (
  `dispatch_id` varchar(100) DEFAULT NULL,
  `order_id` varchar(100) NOT NULL DEFAULT '',
  `status` varchar(255) DEFAULT NULL,
  `order_content` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
