/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 60011
Source Host           : localhost:3306
Source Database       : kuangstudy-dispatcher-order

Target Server Type    : MYSQL
Target Server Version : 60011
File Encoding         : 65001

Date: 2021-03-31 20:25:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ksd_order
-- ----------------------------
DROP TABLE IF EXISTS `ksd_order`;
CREATE TABLE `ksd_order` (
  `order_id` varchar(32) DEFAULT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `order_content` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ksd_order_message
-- ----------------------------
DROP TABLE IF EXISTS `ksd_order_message`;
CREATE TABLE `ksd_order_message` (
  `order_id` varchar(100) NOT NULL,
  `status` int(1) DEFAULT NULL,
  `order_content` varchar(255) DEFAULT NULL,
  `unique_id` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
