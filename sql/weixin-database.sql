/*
 Navicat Premium Data Transfer

 Source Server         : hz docker
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 114.116.116.54:3306
 Source Schema         : weixin-database

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 20/02/2023 08:46:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for weixing_action
-- ----------------------------
DROP TABLE IF EXISTS `weixing_action`;
CREATE TABLE `weixing_action`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `action_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行为',
  `action_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行为名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixing_action
-- ----------------------------
INSERT INTO `weixing_action` VALUES (1, 'INSERT', '添加');
INSERT INTO `weixing_action` VALUES (2, 'DELETE', '删除');
INSERT INTO `weixing_action` VALUES (3, 'UPDATE', '修改');
INSERT INTO `weixing_action` VALUES (4, 'SELECT', '查询');
INSERT INTO `weixing_action` VALUES (5, 'APPROVAL', '审批');
INSERT INTO `weixing_action` VALUES (6, 'EXPORT', '导出');
INSERT INTO `weixing_action` VALUES (7, 'BACKUP', '备份');

-- ----------------------------
-- Table structure for weixing_camera
-- ----------------------------
DROP TABLE IF EXISTS `weixing_camera`;
CREATE TABLE `weixing_camera`  (
  `id` bigint(20) NOT NULL COMMENT '摄像头主键id',
  `longitude` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经度',
  `latitude` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `srcUser` bigint(20) NULL DEFAULT NULL COMMENT '创建id',
  `createTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `isDelete` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除  0正常 1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixing_camera
-- ----------------------------

-- ----------------------------
-- Table structure for weixing_maskimg
-- ----------------------------
DROP TABLE IF EXISTS `weixing_maskimg`;
CREATE TABLE `weixing_maskimg`  (
  `id` bigint(255) NOT NULL COMMENT 'id',
  `mask_number` int(20) NULL DEFAULT NULL COMMENT '佩戴口罩人数',
  `nomask_number` int(20) NULL DEFAULT NULL COMMENT '未佩戴口罩人数',
  `img_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '巡检用户',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '巡检用户id',
  `is_delete` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除字段  0 正常  1 删除',
  `catch_time` datetime(0) NULL DEFAULT NULL COMMENT '拍摄时间',
  `longitude` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拍摄地经度',
  `latitude` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拍摄地纬度',
  `is_camera` tinyint(1) NOT NULL COMMENT '是否是摄像头拍摄的  0是  1不是',
  `camera_id` bigint(30) NULL DEFAULT NULL COMMENT '拍摄的摄像头id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixing_maskimg
-- ----------------------------
INSERT INTO `weixing_maskimg` VALUES (1, 1, 2, '55', '1', 1, 1, '2023-02-09 08:47:53', '1', '1', 0, 1);
INSERT INTO `weixing_maskimg` VALUES (2, 3, 2, '55', '1', 1, 1, '2023-02-09 08:47:53', '1', '1', 0, 2);
INSERT INTO `weixing_maskimg` VALUES (3, 1, 5, '55', '1', 1, 1, '2020-02-09 08:47:53', '1', '1', 0, 2);
INSERT INTO `weixing_maskimg` VALUES (4, 0, 5, '55', '1', 1, 1, '2023-02-09 08:47:53', '1', '1', 0, 2);
INSERT INTO `weixing_maskimg` VALUES (5, 8, 2, '55', '1', 1, 1, '2023-02-09 08:47:53', '1', '1', 0, 1);
INSERT INTO `weixing_maskimg` VALUES (6, 2, 1, '55', '1', 1, 1, '2023-02-09 08:47:53', '1', '1', 0, 3);

-- ----------------------------
-- Table structure for weixing_model
-- ----------------------------
DROP TABLE IF EXISTS `weixing_model`;
CREATE TABLE `weixing_model`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `model_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模式码',
  `model_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模式名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixing_model
-- ----------------------------
INSERT INTO `weixing_model` VALUES (1, 'CAMERA', '摄像头管理');
INSERT INTO `weixing_model` VALUES (2, 'XUNJIAN', '区域巡检管理');
INSERT INTO `weixing_model` VALUES (3, 'USER', '用户管理');
INSERT INTO `weixing_model` VALUES (4, 'CHAT', '聊天管理');

-- ----------------------------
-- Table structure for weixing_permissions
-- ----------------------------
DROP TABLE IF EXISTS `weixing_permissions`;
CREATE TABLE `weixing_permissions`  (
  `id` bigint(30) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `permissions_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名称',
  `model_id` bigint(20) NOT NULL COMMENT '模块id',
  `action_id` bigint(20) NOT NULL COMMENT '行为id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixing_permissions
-- ----------------------------
INSERT INTO `weixing_permissions` VALUES (1, 'CAMERA::INSERT', 1, 1);
INSERT INTO `weixing_permissions` VALUES (2, 'XUNJIAN::INSERT', 2, 1);
INSERT INTO `weixing_permissions` VALUES (3, 'USER::INSERT', 3, 1);
INSERT INTO `weixing_permissions` VALUES (4, 'CHAT::INSERT', 4, 1);
INSERT INTO `weixing_permissions` VALUES (5, 'CAMERA::DELETE', 1, 2);
INSERT INTO `weixing_permissions` VALUES (6, 'XUNJIAN::DELETE', 2, 2);
INSERT INTO `weixing_permissions` VALUES (7, 'USER::DELETE', 3, 2);
INSERT INTO `weixing_permissions` VALUES (8, 'CHAT::DELETE', 4, 2);
INSERT INTO `weixing_permissions` VALUES (9, 'CAMERA::UPDATE', 1, 3);
INSERT INTO `weixing_permissions` VALUES (10, 'XUNJIAN::UPDATE', 2, 3);
INSERT INTO `weixing_permissions` VALUES (11, 'USER::UPDATE', 3, 3);
INSERT INTO `weixing_permissions` VALUES (12, 'CHAT::UPDATE', 4, 3);
INSERT INTO `weixing_permissions` VALUES (13, 'CAMERA::SELECT', 1, 4);
INSERT INTO `weixing_permissions` VALUES (14, 'XUNJIAN::SELECT', 2, 4);
INSERT INTO `weixing_permissions` VALUES (15, 'USER::SELECT', 3, 4);
INSERT INTO `weixing_permissions` VALUES (16, 'CHAT::SELECT', 4, 4);
INSERT INTO `weixing_permissions` VALUES (17, 'CAMERA::APPROVAL', 1, 5);
INSERT INTO `weixing_permissions` VALUES (18, 'XUNJIAN::APPROVAL', 2, 5);
INSERT INTO `weixing_permissions` VALUES (19, 'USER::APPROVAL', 3, 5);
INSERT INTO `weixing_permissions` VALUES (20, 'CHAT::APPROVAL', 4, 5);
INSERT INTO `weixing_permissions` VALUES (21, 'CAMERA::EXPORT', 1, 6);
INSERT INTO `weixing_permissions` VALUES (22, 'XUNJIAN::EXPORT', 2, 6);
INSERT INTO `weixing_permissions` VALUES (23, 'USER::EXPORT', 3, 6);
INSERT INTO `weixing_permissions` VALUES (24, 'CHAT::EXPORT', 4, 6);
INSERT INTO `weixing_permissions` VALUES (25, 'CAMERA::BACKUP', 1, 7);
INSERT INTO `weixing_permissions` VALUES (26, 'XUNJIAN::BACKUP', 2, 7);
INSERT INTO `weixing_permissions` VALUES (27, 'USER::BACKUP', 3, 7);
INSERT INTO `weixing_permissions` VALUES (28, 'CHAT::BACKUP', 4, 7);

-- ----------------------------
-- Table structure for weixing_request_permissions
-- ----------------------------
DROP TABLE IF EXISTS `weixing_request_permissions`;
CREATE TABLE `weixing_request_permissions`  (
  `id` bigint(30) NOT NULL COMMENT '主键id',
  `request_time` datetime(0) NULL DEFAULT NULL COMMENT '申请时间',
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `request_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容说明',
  `floor` tinyint(1) NULL DEFAULT NULL COMMENT '升级 0 降级 1 ',
  `permissions_id` bigint(30) NULL DEFAULT NULL COMMENT '权限id',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '申请状态  0 未读 1已读  2已处理',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixing_request_permissions
-- ----------------------------

-- ----------------------------
-- Table structure for weixing_role
-- ----------------------------
DROP TABLE IF EXISTS `weixing_role`;
CREATE TABLE `weixing_role`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `permission_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限集合',
  `cicle_radius` int(20) NULL DEFAULT NULL COMMENT '巡检半径 ',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixing_role
-- ----------------------------
INSERT INTO `weixing_role` VALUES (1, 'ROOT', '0', 0);
INSERT INTO `weixing_role` VALUES (2, 'NOMAL_USER', '1-10', 5);
INSERT INTO `weixing_role` VALUES (3, 'VIP_USER', '1-15', 10);
INSERT INTO `weixing_role` VALUES (4, 'SVIP_USER', '1-20', 20);
INSERT INTO `weixing_role` VALUES (5, 'REGIONAL_ADMINISTRATOR', '1-25', 30);

-- ----------------------------
-- Table structure for weixing_user
-- ----------------------------
DROP TABLE IF EXISTS `weixing_user`;
CREATE TABLE `weixing_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '最近登录时间',
  `is_delete` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除  0删除 / 1未删除',
  `md5key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加密字符',
  `role_id` bigint(20) NOT NULL COMMENT '权限id',
  `telephone_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录状态  ‘L’ 登录 , \'U\' 未登录',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixing_user
-- ----------------------------
INSERT INTO `weixing_user` VALUES (1, '1', '961c30ac73ee3e1768959e575febaf64', '2023-02-02 16:29:04', 1, '1', 0, '1', '0');

-- ----------------------------
-- Table structure for weixing_user_camera
-- ----------------------------
DROP TABLE IF EXISTS `weixing_user_camera`;
CREATE TABLE `weixing_user_camera`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `cameraId` bigint(20) NOT NULL COMMENT '摄像头id',
  `userId` bigint(20) NOT NULL COMMENT '用户id',
  `created` tinyint(1) NOT NULL COMMENT '是否是创建者   0 是  1否',
  `addTime` datetime(0) NOT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixing_user_camera
-- ----------------------------

-- ----------------------------
-- Table structure for weixing_user_permissions
-- ----------------------------
DROP TABLE IF EXISTS `weixing_user_permissions`;
CREATE TABLE `weixing_user_permissions`  (
  `id` bigint(30) NOT NULL COMMENT '主键id',
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `permissions_id` bigint(30) NOT NULL COMMENT '权限id',
  `isDelete` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除字段  0正常 1删除',
  `cameraList` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户摄像头列表id  使用 , 隔开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixing_user_permissions
-- ----------------------------

-- ----------------------------
-- Table structure for weixing_xunjian
-- ----------------------------
DROP TABLE IF EXISTS `weixing_xunjian`;
CREATE TABLE `weixing_xunjian`  (
  `id` bigint(30) NOT NULL COMMENT 'id',
  `latitude` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `longitude` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经度',
  `user_id` bigint(30) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `final_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `nomask_number` int(11) NULL DEFAULT NULL COMMENT '未佩戴人数',
  `mask_number` int(11) NULL DEFAULT NULL COMMENT '佩戴人数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixing_xunjian
-- ----------------------------
INSERT INTO `weixing_xunjian` VALUES (1, '1', '1', 1, '2023-02-16 16:59:57', '2023-02-08 09:29:18', 1, 1);
INSERT INTO `weixing_xunjian` VALUES (2, '1', '1', 1, '2023-02-16 16:59:57', '2023-02-16 17:00:18', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
