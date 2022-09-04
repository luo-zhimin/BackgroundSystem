/*
 Navicat Premium Data Transfer

 Source Server         : thisMac
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : backgroundSystem

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 27/08/2022 16:01:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `openId` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '用户唯一标识',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `province` varchar(255) DEFAULT NULL COMMENT '省市区',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `is_default` char(1) NOT NULL DEFAULT '0' COMMENT '是否是默认地址 默认不是',
  `is_del` char(1) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for caizhi
-- ----------------------------
DROP TABLE IF EXISTS `caizhi`;
CREATE TABLE `caizhi` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '材质',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for check_rule
-- ----------------------------
DROP TABLE IF EXISTS `check_rule`;
CREATE TABLE `check_rule` (
  `ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `RULE_NAME` varchar(255) DEFAULT NULL,
  `RULE_SCORE` varchar(255) DEFAULT NULL,
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `REMARK` varchar(400) DEFAULT NULL,
  `TYPE` varchar(255) DEFAULT NULL,
  `PARENT_ID` varchar(200) DEFAULT NULL,
  `RULE_LEVEL` int DEFAULT NULL,
  `FILE_NAME` varchar(255) DEFAULT NULL,
  `SORT` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for check_template
-- ----------------------------
DROP TABLE IF EXISTS `check_template`;
CREATE TABLE `check_template` (
  `ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `TEMPLATE_NAME` varchar(200) DEFAULT NULL,
  `LOCATION_TYPE` varchar(255) DEFAULT NULL,
  `CHECK_RULE_IDS` varchar(255) DEFAULT NULL,
  `ADD_USER` varchar(255) DEFAULT NULL,
  `ADD_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `ux_type` (`LOCATION_TYPE`) USING BTREE
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `config_key` varchar(255) NOT NULL,
  `config_value` varchar(255) NOT NULL,
  `remark` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_key` (`config_key`) USING BTREE
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `coupon_id` varchar(255) DEFAULT NULL COMMENT '优惠券id',
  `is_used` char(1) DEFAULT '0' COMMENT '是否使用',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '是否失效',
  `price` decimal(10,2) DEFAULT NULL COMMENT '消费券金额',
  `use_limit` int DEFAULT NULL COMMENT '消费限制',
  `open_id` varchar(255) DEFAULT NULL COMMENT '使用者',
  `picture_id` bigint DEFAULT NULL COMMENT '图片id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for orderd
-- ----------------------------
DROP TABLE IF EXISTS `orderd`;
CREATE TABLE `orderd` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信支付订单号',
  `kd_no` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '快递单号',
  `is_pay` char(1) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否支付',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `port_price` decimal(10,2) DEFAULT NULL COMMENT '运费',
  `total` decimal(10,2) DEFAULT NULL COMMENT '商品总价（不包含优惠券）',
  `coupon_id` bigint DEFAULT NULL COMMENT '优惠券id',
  `status` char(1) DEFAULT NULL COMMENT '订单状态：待付款，待发货，售后订单，交易关闭',
  `address_id` bigint DEFAULT NULL COMMENT '收货地址id',
  `num` int DEFAULT NULL COMMENT '购买数量',
  `picture_id` mediumtext COMMENT '下单图片id，逗号分割',
  `caizhi_id` bigint DEFAULT NULL COMMENT '材质id',
  `size_id` bigint DEFAULT NULL COMMENT '尺寸id',
  `create_user` varchar(50) NOT NULL COMMENT '创建人',
  `is_del` char(1) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for order_element
-- ----------------------------
DROP TABLE IF EXISTS `order_element`;
CREATE TABLE `order_element` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单号',
  `number` int NOT NULL DEFAULT '0' COMMENT '商品数量',
  `picture_id` mediumtext CHARACTER SET utf8mb3 COLLATE utf8_general_ci COMMENT '商品图片',
  `is_del` char(1) DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- ----------------------------
-- Table structure for picture
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件名字',
  `url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `father` varchar(255) DEFAULT NULL COMMENT '上级目录',
  `is_del` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for picture_accessory
-- ----------------------------
DROP TABLE IF EXISTS `picture_accessory`;
CREATE TABLE `picture_accessory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `picture_id` bigint DEFAULT NULL COMMENT '图片id',
  `like` bigint DEFAULT NULL COMMENT '点赞数',
  `num` bigint DEFAULT NULL COMMENT '下单数',
  `pv` bigint DEFAULT NULL COMMENT '访问量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for size
-- ----------------------------
DROP TABLE IF EXISTS `size`;
CREATE TABLE `size` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '名字',
  `pic` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '尺寸详情页的大图',
  `price` decimal(10,2) NOT NULL COMMENT '原价',
  `u_price` decimal(10,2) DEFAULT NULL COMMENT '优惠后价格',
  `material_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '可以使用的材质id集合',
  `size` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '尺寸大小 第一个width 第二个height',
  `faces` varchar(5) DEFAULT NULL COMMENT '单面or 双面',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for user_admin
-- ----------------------------
DROP TABLE IF EXISTS `user_admin`;
CREATE TABLE `user_admin` (
  `id` varchar(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `is_delete` int NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `mobile` varchar(11) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_admin_user_name_IDX` (`user_name`) USING BTREE,
  KEY `user_admin_password_IDX` (`password`) USING BTREE
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for wechat_user
-- ----------------------------
DROP TABLE IF EXISTS `wechat_user`;
CREATE TABLE `wechat_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `open_id` varchar(100) NOT NULL COMMENT 'open Id',
  `union_id` varchar(100) DEFAULT NULL,
  `user_info` varchar(300) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户信息',
  `is_del` int NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_code_del` (`open_id`,`is_del`) USING BTREE
) ENGINE=InnoDB ;

SET FOREIGN_KEY_CHECKS = 1;
