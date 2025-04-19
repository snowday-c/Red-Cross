/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 19/04/2025 22:07:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for advice
-- ----------------------------
DROP TABLE IF EXISTS `advice`;
CREATE TABLE `advice`  (
  `advice_id` int NOT NULL AUTO_INCREMENT COMMENT '用户反馈id',
  `advice_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户反馈内容',
  `advice_sender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '反馈的用户',
  `advice_time` datetime NULL DEFAULT NULL COMMENT '反馈时间',
  `advice_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否处理：未处理，已处理',
  `advice_handler` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理人',
  PRIMARY KEY (`advice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of advice
-- ----------------------------
INSERT INTO `advice` VALUES (3, '发现一个问题', 'zwj', '2025-04-17 22:06:33', '已处理', '张三');
INSERT INTO `advice` VALUES (4, '发现第二个个问题', 'zwj', '2025-04-17 22:09:13', '已处理', 'clt');
INSERT INTO `advice` VALUES (5, '额外人生巅峰', '匿名用户', '2025-04-18 16:05:19', '未处理', NULL);
INSERT INTO `advice` VALUES (6, '二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰二次额外人生巅峰', '匿名用户', '2025-04-18 16:05:47', '未处理', NULL);
INSERT INTO `advice` VALUES (7, '是非得失服', '张三', '2025-04-18 16:06:53', '未处理', NULL);

-- ----------------------------
-- Table structure for certificate
-- ----------------------------
DROP TABLE IF EXISTS `certificate`;
CREATE TABLE `certificate`  (
  `certificate_id` int NOT NULL AUTO_INCREMENT COMMENT '证书ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `certificate_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '证书标题',
  `certificate_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '证书内容',
  `certificate_time` datetime NULL DEFAULT NULL COMMENT '证书审批时间',
  `certificate_type` int NOT NULL COMMENT '证书状态：0审核中，1已发放，-1未通过审核',
  `approver` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批人',
  PRIMARY KEY (`certificate_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of certificate
-- ----------------------------
INSERT INTO `certificate` VALUES (4, 3, '红十字水上安全救生员资格证书', '本证书确认zwj已完成红十字救生员培训并通过考核，具备在紧急情况下实施水上救援的资格和能力', '2023-03-19 10:48:56', 1, 'clt');
INSERT INTO `certificate` VALUES (7, 4, '红十字急救与救生技能认证证书', '兹证明张三同志已完成红十字救生员全部培训课程，经考核合格，具备红十字救生员资格，特发此证', '2023-03-17 22:29:32', 1, 'clt');
INSERT INTO `certificate` VALUES (10, 3, '', '', '2024-03-19 22:47:46', 0, 'clt');
INSERT INTO `certificate` VALUES (11, 4, '红十字救生员培训合格证书', '张三学员已完成红十字水上安全救生员培训计划，掌握相关救生技能，准予颁发此资格证书。', '2024-03-24 17:57:16', 1, 'clt');
INSERT INTO `certificate` VALUES (12, 4, '红十字急救与救生技能认证证书', '本证书确认张三已完成红十字救生员培训并通过考核，具备在紧急情况下实施水上救援的资格和能力', '2025-03-30 23:31:00', 1, 'clt');

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `exam_id` int NOT NULL AUTO_INCREMENT COMMENT '试卷id',
  `user_id` int NOT NULL COMMENT '用户id',
  `question_ids` json NOT NULL COMMENT '试卷题目id',
  `answers` json NULL COMMENT '答案列表',
  `score` int NULL DEFAULT NULL COMMENT '得分',
  `exam_time` datetime NULL DEFAULT NULL COMMENT '考试时间',
  PRIMARY KEY (`exam_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES (11, 4, '[49, 20, 46, 65, 76, 93]', '[\"A\", \"B\", \"B\", \"T\", \"F\", \"水中\"]', 100, '2025-03-01 10:52:45');
INSERT INTO `exam` VALUES (12, 4, '[51, 33, 52, 62, 64, 96]', '[\"D\", \"A\", \"B\", \"T\", \"T\", \"呼吸道通畅\"]', 60, '2025-03-02 10:52:53');
INSERT INTO `exam` VALUES (13, 4, '[15, 28, 34, 60, 63, 104]', '[\"B\", \"B\", \"B\", \"T\", \"T\", \"1\"]', 120, '2025-03-03 10:52:56');
INSERT INTO `exam` VALUES (14, 4, '[28, 23, 19, 74, 67, 94]', '[\"D\", \"B\", \"C\", \"F\", \"T\", \"降温\"]', 40, '2025-03-04 10:53:00');
INSERT INTO `exam` VALUES (15, 4, '[41, 39, 24, 83, 81, 87]', NULL, 0, '2025-03-05 10:53:05');
INSERT INTO `exam` VALUES (16, 4, '[27, 16, 40, 65, 75, 95]', '[\"B\", \"C\", \"A\", \"T\", \"T\", \"伤害\"]', 40, '2025-03-06 10:53:09');
INSERT INTO `exam` VALUES (17, 1, '[27, 20, 31, 79, 73, 100]', '[\"A\", \"B\", \"B\", \"T\", \"T\", \"呼吸\"]', 40, '2025-03-07 10:53:13');
INSERT INTO `exam` VALUES (18, 1, '[40, 27, 21, 70, 62, 87]', '[\"B\", \"B\", \"B\", \"F\", \"T\", \"救援\"]', 80, '2025-03-08 10:53:16');
INSERT INTO `exam` VALUES (19, 1, '[34, 47, 27, 55, 70, 86]', '[\"B\", \"B\", \"B\", \"T\", \"F\", \"或者\"]', 100, '2025-03-09 10:53:21');
INSERT INTO `exam` VALUES (20, 3, '[54, 44, 49, 55, 77, 98]', '[\"B\", \"B\", \"B\", \"T\", \"T\", \"消毒\"]', 100, '2025-03-10 10:53:24');
INSERT INTO `exam` VALUES (21, 4, '[46, 27, 21, 71, 66, 104]', '[\"B\", \"B\", \"B\", \"F\", \"F\", \"3\"]', 80, '2025-03-12 12:26:40');
INSERT INTO `exam` VALUES (22, 4, '[28, 30, 51, 80, 62, 104]', '[\"B\", \"B\", \"B\", \"F\", \"T\", \"3\"]', 80, '2025-03-17 16:27:51');
INSERT INTO `exam` VALUES (23, 4, '[30, 20, 47, 61, 66, 87]', NULL, 0, '2025-03-17 16:34:44');
INSERT INTO `exam` VALUES (24, 4, '[21, 48, 51, 56, 71, 89]', '[\"B\", \"B\", \"B\", \"F\", \"F\", \"呼叫\"]', 60, '2025-03-17 22:06:38');
INSERT INTO `exam` VALUES (25, 4, '[21, 28, 38, 84, 69, 89]', '[\"B\", \"B\", \"B\", \"F\", \"T\", \"呼叫\"]', 80, '2025-03-17 22:07:13');
INSERT INTO `exam` VALUES (26, 4, '[36, 38, 41, 64, 82, 96]', '[\"B\", \"B\", \"B\", \"F\", \"F\", \"\"]', 80, '2025-03-17 22:12:44');
INSERT INTO `exam` VALUES (27, 4, '[42, 38, 44, 78, 76, 102]', '[\"B\", \"B\", \"B\", \"F\", \"F\", \"冷静\"]', 120, '2025-03-17 22:18:32');
INSERT INTO `exam` VALUES (28, 4, '[48, 43, 46, 74, 59, 93]', '[\"B\", \"B\", \"B\", \"F\", \"F\", \"水中\"]', 100, '2025-03-20 12:58:55');
INSERT INTO `exam` VALUES (29, 4, '[20, 25, 45, 67, 59, 98]', '[\"B\", \"D\", \"D\", \"T\", \"F\", \"3\"]', 100, '2025-03-20 22:16:43');
INSERT INTO `exam` VALUES (30, 4, '[49, 48, 34, 80, 64, 96]', NULL, 0, '2025-03-28 20:25:29');
INSERT INTO `exam` VALUES (31, 4, '[50, 27, 20, 58, 82, 86]', '[\"B\", \"B\", \"B\", \"T\", \"F\", \"564\"]', 100, '2025-03-28 20:25:58');
INSERT INTO `exam` VALUES (32, 4, '[52, 17, 34, 83, 65, 93]', NULL, 0, '2025-03-31 13:51:56');
INSERT INTO `exam` VALUES (33, 1, '[53, 18, 31, 23, 29, 60, 82, 77, 90, 101]', NULL, 0, '2025-04-04 20:35:51');
INSERT INTO `exam` VALUES (34, 1, '[22, 39, 35, 19, 41, 34, 51, 42, 28, 53, 68, 82, 55, 61, 75, 85, 99, 92, 89, 98]', '[\"B\", \"B\", \"B\", \"B\", \"C\", \"B\", \"B\", \"B\", \"B\", \"D\", \"F\", \"F\", \"T\", \"T\", \"T\", \"456\", \"456\", \"456456\", \"456456\", \"45645456\"]', 66, '2025-04-04 21:16:21');

-- ----------------------------
-- Table structure for examtype
-- ----------------------------
DROP TABLE IF EXISTS `examtype`;
CREATE TABLE `examtype`  (
  `examtype_id` int NOT NULL AUTO_INCREMENT COMMENT '题型id',
  `choice` int NULL DEFAULT NULL COMMENT '选择题数量',
  `truefalse` int NULL DEFAULT NULL COMMENT '判断题数量',
  `blank` int NULL DEFAULT NULL COMMENT '填空题数量',
  `score` int NULL DEFAULT NULL COMMENT '每道题分数',
  `time` int NULL DEFAULT NULL COMMENT '考试时长（分钟）',
  `current` int NULL DEFAULT NULL COMMENT '0：非当前 1：当前',
  PRIMARY KEY (`examtype_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of examtype
-- ----------------------------
INSERT INTO `examtype` VALUES (1, 3, 2, 1, 20, 30, 0);
INSERT INTO `examtype` VALUES (2, 5, 3, 2, 12, 60, 0);
INSERT INTO `examtype` VALUES (3, 10, 5, 5, 6, 80, 0);
INSERT INTO `examtype` VALUES (4, 15, 10, 5, 4, 100, 0);
INSERT INTO `examtype` VALUES (7, 4, 2, 2, 15, 40, 1);
INSERT INTO `examtype` VALUES (8, 1, 1, 1, 40, 15, 0);

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `message_id` int NOT NULL AUTO_INCREMENT COMMENT '通知信息id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知内容',
  `sender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送人',
  `receiver` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接收人',
  `time` datetime NOT NULL COMMENT '发送时间',
  `message_type` int NOT NULL COMMENT '通知类型：0公共 1个人',
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (1, '个人通知1', '123123', 'clt', '张三', '2025-02-04 17:37:55', 1);
INSERT INTO `message` VALUES (2, '个人通知2', '1234', 'clt', '张三', '2025-02-06 14:16:54', 1);
INSERT INTO `message` VALUES (3, '通知001', '123123111', 'clt', '', '2025-02-06 17:37:15', 0);
INSERT INTO `message` VALUES (4, '通知2', '1234', 'clt', '', '2025-02-14 14:17:00', 0);
INSERT INTO `message` VALUES (5, '通知3', '12345', 'clt', '', '2025-02-16 14:17:04', 0);
INSERT INTO `message` VALUES (9, '私人消息1', '私人消息1', 'clt', '张三', '2025-02-19 11:20:10', 1);
INSERT INTO `message` VALUES (10, '私人消息2', '私人消息2', 'clt', '张三', '2025-02-19 11:21:52', 1);
INSERT INTO `message` VALUES (12, '私人消息4', '私人消息4', 'clt', '李四', '2025-02-19 11:28:23', 1);
INSERT INTO `message` VALUES (15, '公共通知001', '公共通知001', 'clt', '', '2025-02-26 15:22:41', 0);
INSERT INTO `message` VALUES (16, '公共通知002', '我是公共通知002', 'clt', '', '2025-03-28 16:46:18', 0);
INSERT INTO `message` VALUES (17, '个人消息001', '个人消息001', 'clt', '张三', '2025-02-26 15:24:43', 1);
INSERT INTO `message` VALUES (20, '公共123123', '公共123123', 'zwj', '', '2025-02-26 17:49:00', 0);
INSERT INTO `message` VALUES (21, '你是一个好人', '你是一个好人', 'zwj', '张三', '2025-02-26 17:54:36', 1);
INSERT INTO `message` VALUES (22, '速来上班', '再不来上班以后就别来了', 'clt', '李四', '2025-03-17 14:29:07', 1);
INSERT INTO `message` VALUES (23, '证书审核通过', '您的荣誉证书已发放', 'clt', '张三', '2025-03-17 14:31:44', 1);
INSERT INTO `message` VALUES (24, '证书申请结果', '您的证书已通过审核！', '系统', 'zwj', '2025-03-19 10:48:56', 1);
INSERT INTO `message` VALUES (25, '证书申请结果', '您申请的证书未通过审核！', '系统', 'zwj', '2025-03-19 14:14:14', 1);
INSERT INTO `message` VALUES (26, '证书申请结果', '您申请的证书已通过审核！', '系统', 'zwj', '2025-03-19 14:15:31', 1);
INSERT INTO `message` VALUES (27, '证书申请结果', '您申请的证书未通过审核！', '系统', '张三', '2025-03-19 14:35:19', 1);
INSERT INTO `message` VALUES (28, '证书申请结果', '您申请的证书未通过审核！', '系统', '张三', '2025-03-19 14:36:13', 1);
INSERT INTO `message` VALUES (29, '证书申请结果', '您申请的证书未通过审核！如有疑问请联系管理员。', '系统', 'zwj', '2025-03-19 14:45:40', 1);
INSERT INTO `message` VALUES (30, '证书申请结果', '您申请的证书未通过审核！如有疑问请联系管理员。', '系统', 'zwj', '2025-03-19 15:14:36', 1);
INSERT INTO `message` VALUES (31, '证书申请结果', '您申请的证书未通过审核！如有疑问请联系管理员。', '系统', 'zwj', '2025-03-19 15:43:57', 1);
INSERT INTO `message` VALUES (32, '证书申请结果', '您申请的证书已通过审核！', '系统', '张三', '2025-03-19 15:45:23', 1);
INSERT INTO `message` VALUES (33, '证书申请结果', '您申请的证书未通过审核！如有疑问请联系管理员。', '系统', 'zwj', '2025-03-19 22:45:11', 1);
INSERT INTO `message` VALUES (34, '证书申请结果', '您申请的证书未通过审核！如有疑问请联系管理员。', '系统', '张三', '2025-03-19 22:46:51', 1);
INSERT INTO `message` VALUES (35, '证书申请结果', '您申请的证书未通过审核！如有疑问请联系管理员。', '系统', 'zwj', '2025-03-19 22:47:46', 1);
INSERT INTO `message` VALUES (37, '证书申请结果', '您申请的证书已通过审核！', '系统', '张三', '2025-03-19 23:03:33', 1);
INSERT INTO `message` VALUES (38, '证书申请结果', '您申请的证书已通过审核！', '系统', '张三', '2025-03-24 17:57:16', 1);
INSERT INTO `message` VALUES (39, '阿斯达神殿', '啊实打实大师大撒大声地', '张三', '', '2025-03-29 19:27:54', 0);
INSERT INTO `message` VALUES (40, '阿打发发生', '	\r\n递四方速递发大水发大水发打撒', 'clt', '', '2025-03-29 19:28:16', 0);
INSERT INTO `message` VALUES (41, '证书申请结果', '您申请的证书已通过审核！', '系统', '张三', '2025-03-30 23:31:00', 1);
INSERT INTO `message` VALUES (42, '尊敬的用户', '我们要跑路了，拜拜', 'clt', '', '2025-04-14 18:51:20', 0);

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `question_id` int NOT NULL AUTO_INCREMENT COMMENT '题目ID',
  `question_type` int NOT NULL COMMENT '题目类型：1选择题2判断题3填空题',
  `question` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '题目',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '答案',
  PRIMARY KEY (`question_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 107 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (15, 1, '在进行心肺复苏时，按压深度应为多少厘米？A. 2-3 B. 4-5 C. 6-7 D. 8-9', 'B');
INSERT INTO `question` VALUES (16, 1, '救生员在进行救援时，首先应该做什么？A. 直接跳入水中 B. 评估现场安全 C. 呼叫帮助 D. 准备救生设备', 'B');
INSERT INTO `question` VALUES (17, 1, '以下哪种情况应立即停止心肺复苏？A. 患者开始呼吸 B. 救护车到达 C. 救生员疲劳 D. 以上都是', 'D');
INSERT INTO `question` VALUES (18, 1, '救生员在救援溺水者时，应优先考虑哪种救援方式？A. 直接下水救援 B. 使用救生设备 C. 呼叫他人帮助 D. 等待专业人员', 'B');
INSERT INTO `question` VALUES (19, 1, '在进行人工呼吸时，每次吹气的时间应为多少秒？A. 1秒 B. 2秒 C. 3秒 D. 4秒', 'A');
INSERT INTO `question` VALUES (20, 1, '救生员在发现溺水者时，应首先做什么？A. 立即跳入水中 B. 呼叫紧急服务 C. 评估溺水者情况 D. 准备救生设备', 'B');
INSERT INTO `question` VALUES (21, 1, '以下哪种情况不需要立即进行心肺复苏？A. 无呼吸 B. 无脉搏 C. 有轻微呼吸 D. 无意识', 'C');
INSERT INTO `question` VALUES (22, 1, '救生员在救援过程中应如何保持自身安全？A. 忽略自身安全 B. 使用救生设备 C. 直接跳入水中 D. 等待他人救援', 'B');
INSERT INTO `question` VALUES (23, 1, '在进行心肺复苏时，按压频率应为每分钟多少次？A. 60-80 B. 80-100 C. 100-120 D. 120-140', 'C');
INSERT INTO `question` VALUES (24, 1, '救生员在救援过程中应如何与溺水者沟通？A. 大声喊叫 B. 保持冷静，使用简单指令 C. 忽略溺水者 D. 等待专业人员', 'B');
INSERT INTO `question` VALUES (25, 1, '以下哪种设备是救生员必备的？A. 救生圈 B. 充电器 C. 氦气瓶 D. 以上都是', 'A');
INSERT INTO `question` VALUES (26, 1, '救生员在救援过程中应如何评估溺水者的情况？A. 忽略评估 B. 快速评估呼吸和意识 C. 等待专业人员 D. 直接进行心肺复苏', 'B');
INSERT INTO `question` VALUES (27, 1, '在进行心肺复苏时，按压与人工呼吸的比例是多少？A. 15:2 B. 30:2 C. 5:1 D. 10:1', 'B');
INSERT INTO `question` VALUES (28, 1, '救生员在救援过程中应如何保持溺水者的体温？A. 忽略体温 B. 使用保温毯 C. 直接进行心肺复苏 D. 等待专业人员', 'B');
INSERT INTO `question` VALUES (29, 1, '以下哪种情况应立即进行心肺复苏？A. 有呼吸 B. 无脉搏 C. 有意识 D. 有轻微呼吸', 'B');
INSERT INTO `question` VALUES (30, 1, '救生员在救援过程中应如何与团队协作？A. 忽略团队 B. 保持沟通，分工明确 C. 独自行动 D. 等待专业人员', 'B');
INSERT INTO `question` VALUES (31, 1, '在进行心肺复苏时，按压位置应在哪里？A. 胸部左侧 B. 胸部右侧 C. 胸骨下半部 D. 腹部', 'C');
INSERT INTO `question` VALUES (32, 1, '救生员在救援过程中应如何处理溺水者的呕吐物？A. 忽略呕吐物 B. 清理呕吐物，保持气道通畅 C. 直接进行心肺复苏 D. 等待专业人员', 'B');
INSERT INTO `question` VALUES (33, 1, '以下哪种情况不需要立即进行人工呼吸？A. 无呼吸 B. 有脉搏 C. 无意识 D. 有轻微呼吸', 'D');
INSERT INTO `question` VALUES (34, 1, '救生员在救援过程中应如何保持自身冷静？A. 忽略自身情绪 B. 深呼吸，保持冷静 C. 直接跳入水中 D. 等待他人救援', 'B');
INSERT INTO `question` VALUES (35, 1, '在进行心肺复苏时，按压深度应为多少厘米？A. 2-3 B. 4-5 C. 6-7 D. 8-9', 'B');
INSERT INTO `question` VALUES (36, 1, '救生员在进行救援时，首先应该做什么？A. 直接跳入水中 B. 评估现场安全 C. 呼叫帮助 D. 准备救生设备', 'B');
INSERT INTO `question` VALUES (37, 1, '以下哪种情况应立即停止心肺复苏？A. 患者开始呼吸 B. 救护车到达 C. 救生员疲劳 D. 以上都是', 'D');
INSERT INTO `question` VALUES (38, 1, '救生员在救援溺水者时，应优先考虑哪种救援方式？A. 直接下水救援 B. 使用救生设备 C. 呼叫他人帮助 D. 等待专业人员', 'B');
INSERT INTO `question` VALUES (39, 1, '在进行人工呼吸时，每次吹气的时间应为多少秒？A. 1秒 B. 2秒 C. 3秒 D. 4秒', 'A');
INSERT INTO `question` VALUES (40, 1, '救生员在发现溺水者时，应首先做什么？A. 立即跳入水中 B. 呼叫紧急服务 C. 评估溺水者情况 D. 准备救生设备', 'B');
INSERT INTO `question` VALUES (41, 1, '以下哪种情况不需要立即进行心肺复苏？A. 无呼吸 B. 无脉搏 C. 有轻微呼吸 D. 无意识', 'C');
INSERT INTO `question` VALUES (42, 1, '救生员在救援过程中应如何保持自身安全？A. 忽略自身安全 B. 使用救生设备 C. 直接跳入水中 D. 等待他人救援', 'B');
INSERT INTO `question` VALUES (43, 1, '在进行心肺复苏时，按压频率应为每分钟多少次？A. 60-80 B. 80-100 C. 100-120 D. 120-140', 'C');
INSERT INTO `question` VALUES (44, 1, '救生员在救援过程中应如何与溺水者沟通？A. 大声喊叫 B. 保持冷静，使用简单指令 C. 忽略溺水者 D. 等待专业人员', 'B');
INSERT INTO `question` VALUES (45, 1, '以下哪种设备是救生员必备的？A. 救生圈 B. 呼吸器 C. 氧气瓶 D. 以上都是', 'D');
INSERT INTO `question` VALUES (46, 1, '救生员在救援过程中应如何评估溺水者的情况？A. 忽略评估 B. 快速评估呼吸和意识 C. 等待专业人员 D. 直接进行心肺复苏', 'B');
INSERT INTO `question` VALUES (47, 1, '在进行心肺复苏时，按压与人工呼吸的比例是多少？A. 15:2 B. 30:2 C. 5:1 D. 10:1', 'B');
INSERT INTO `question` VALUES (48, 1, '救生员在救援过程中应如何保持溺水者的体温？A. 忽略体温 B. 使用保温毯 C. 直接进行心肺复苏 D. 等待专业人员', 'B');
INSERT INTO `question` VALUES (49, 1, '以下哪种情况应立即进行心肺复苏？A. 有呼吸 B. 无脉搏 C. 有意识 D. 有轻微呼吸', 'B');
INSERT INTO `question` VALUES (50, 1, '救生员在救援过程中应如何与团队协作？A. 忽略团队 B. 保持沟通，分工明确 C. 独自行动 D. 等待专业人员', 'B');
INSERT INTO `question` VALUES (51, 1, '在进行心肺复苏时，按压位置应在哪里？A. 胸部左侧 B. 胸部右侧 C. 胸骨下半部 D. 腹部', 'C');
INSERT INTO `question` VALUES (52, 1, '救生员在救援过程中应如何处理溺水者的呕吐物？A. 忽略呕吐物 B. 清理呕吐物，保持气道通畅 C. 直接进行心肺复苏 D. 等待专业人员', 'B');
INSERT INTO `question` VALUES (53, 1, '以下哪种情况不需要立即进行人工呼吸？A. 无呼吸 B. 有脉搏 C. 无意识 D. 有轻微呼吸', 'D');
INSERT INTO `question` VALUES (54, 1, '救生员在救援过程中应如何保持自身冷静？A. 忽略自身情绪 B. 深呼吸，保持冷静 C. 直接跳入水中 D. 等待他人救援', 'B');
INSERT INTO `question` VALUES (55, 2, '救生员在救援过程中应优先考虑自身安全。', 'T');
INSERT INTO `question` VALUES (56, 2, '心肺复苏时，按压深度应为5-6厘米。', 'F');
INSERT INTO `question` VALUES (57, 2, '救生员在发现溺水者时应立即跳入水中救援。', 'F');
INSERT INTO `question` VALUES (58, 2, '人工呼吸时，每次吹气时间应为1秒。', 'T');
INSERT INTO `question` VALUES (59, 2, '救生员在救援过程中应忽略溺水者的呕吐物。', 'F');
INSERT INTO `question` VALUES (60, 2, '心肺复苏时，按压频率应为每分钟100-120次。', 'T');
INSERT INTO `question` VALUES (61, 2, '救生员在救援过程中应保持冷静，使用简单指令与溺水者沟通。', 'T');
INSERT INTO `question` VALUES (62, 2, '救生员在救援过程中应优先使用救生设备。', 'T');
INSERT INTO `question` VALUES (63, 2, '心肺复苏时，按压与人工呼吸的比例应为30:2。', 'T');
INSERT INTO `question` VALUES (64, 2, '救生员在救援过程中应忽略团队协作。', 'F');
INSERT INTO `question` VALUES (65, 2, '救生员在救援过程中应保持溺水者的体温。', 'T');
INSERT INTO `question` VALUES (66, 2, '救生员在救援过程中应忽略自身情绪。', 'F');
INSERT INTO `question` VALUES (67, 2, '救生员在救援过程中应优先考虑使用救生圈。', 'T');
INSERT INTO `question` VALUES (68, 2, '救生员在救援过程中应忽略现场安全评估。', 'F');
INSERT INTO `question` VALUES (69, 2, '救生员在救援过程中应优先考虑呼叫紧急服务。', 'T');
INSERT INTO `question` VALUES (70, 2, '救生员在救援过程中应忽略溺水者的呼吸情况。', 'F');
INSERT INTO `question` VALUES (71, 2, '救生员在救援过程中应优先考虑直接下水救援。', 'F');
INSERT INTO `question` VALUES (72, 2, '救生员在救援过程中应忽略溺水者的意识状态。', 'F');
INSERT INTO `question` VALUES (73, 2, '救生员在救援过程中应优先考虑使用呼吸器。', 'F');
INSERT INTO `question` VALUES (74, 2, '救生员在救援过程中应忽略溺水者的脉搏情况。', 'F');
INSERT INTO `question` VALUES (75, 2, '救生员在救援过程中应优先考虑使用氧气瓶。', 'F');
INSERT INTO `question` VALUES (76, 2, '救生员在救援过程中应忽略溺水者的体温。', 'F');
INSERT INTO `question` VALUES (77, 2, '救生员在救援过程中应优先考虑使用保温毯。', 'T');
INSERT INTO `question` VALUES (78, 2, '救生员在救援过程中应忽略溺水者的呕吐物。', 'F');
INSERT INTO `question` VALUES (79, 2, '救生员在救援过程中应优先考虑使用救生设备。', 'T');
INSERT INTO `question` VALUES (80, 2, '救生员在救援过程中应忽略溺水者的呼吸情况。', 'F');
INSERT INTO `question` VALUES (81, 2, '救生员在救援过程中应优先考虑直接下水救援。', 'F');
INSERT INTO `question` VALUES (82, 2, '救生员在救援过程中应忽略溺水者的意识状态。', 'F');
INSERT INTO `question` VALUES (83, 2, '救生员在救援过程中应优先考虑使用呼吸器。', 'F');
INSERT INTO `question` VALUES (84, 2, '救生员在救援过程中应忽略溺水者的脉搏情况。', 'F');
INSERT INTO `question` VALUES (85, 2, '救生员在进行救援时，首先应该直接跳入水中', 'F');
INSERT INTO `question` VALUES (86, 3, '当有人溺水时，救生员首先应该___，确保自己安全。', '评估环境');
INSERT INTO `question` VALUES (87, 3, '在紧急情况下，呼叫___是第一步。', '紧急救援');
INSERT INTO `question` VALUES (88, 3, '救生员在进行人工呼吸时，要保证___畅通。', '气道');
INSERT INTO `question` VALUES (89, 3, '如果救生员发现一个人在水中遇险，应该用___的方式引起注意。', '吹哨');
INSERT INTO `question` VALUES (90, 3, '在进行心肺复苏时，按压的位置应该在___下段。', '胸骨');
INSERT INTO `question` VALUES (91, 3, '救生员在进行CPR时，每次胸部按压的深度应达到___厘米。', '5');
INSERT INTO `question` VALUES (92, 3, '溺水者如果在水中清醒并能自行呼吸，救生员应鼓励其___。', '抓住浮具');
INSERT INTO `question` VALUES (93, 3, '救生员在培训中学习的一个重要技能是___救援技巧。', '水中');
INSERT INTO `question` VALUES (94, 3, '对于中暑患者，救生员应该将其移至阴凉处并___。', '补充水分');
INSERT INTO `question` VALUES (95, 3, '在水上急救过程中，救生员应避免___溺水者，以免加重危险。', '拖拽');
INSERT INTO `question` VALUES (96, 3, '儿童溺水时，救生员应优先确保儿童的___，再进行其他救援。', '呼吸道通畅');
INSERT INTO `question` VALUES (97, 3, '当救生员进行水中救援时，应使用___等辅助工具。', '救生圈');
INSERT INTO `question` VALUES (98, 3, '对于被毒虫咬伤的伤员，救生员应清洗伤口并___伤口。', '包扎');
INSERT INTO `question` VALUES (99, 3, '如果伤员呼吸困难，救生员应采取___体位。', '坐位');
INSERT INTO `question` VALUES (100, 3, '在进行现场急救时，救生员应先检查伤员的___状态。', '意识');
INSERT INTO `question` VALUES (101, 3, '在进行水上急救时，救生员的首要目标是___生命安全。', '确保');
INSERT INTO `question` VALUES (102, 3, '救生员在进行急救时，应该时刻保持___的心态。', '冷静');
INSERT INTO `question` VALUES (103, 3, '如果救生员无法单独救援溺水者，应该尽快___。', '呼叫支援');
INSERT INTO `question` VALUES (104, 3, '在施行人工呼吸时，救生员的每次吹气时间应保持___秒。', '1');
INSERT INTO `question` VALUES (105, 3, '救生员的主要职责是确保水域中的___。', '安全');

-- ----------------------------
-- Table structure for retrain
-- ----------------------------
DROP TABLE IF EXISTS `retrain`;
CREATE TABLE `retrain`  (
  `retrain_id` int NOT NULL AUTO_INCREMENT COMMENT '复训id',
  `train_id` int NOT NULL COMMENT '培训id',
  `user_id` int NOT NULL COMMENT '用户',
  `train_place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '培训地点',
  `train_time` datetime NOT NULL COMMENT '当前培训时间',
  `train_next` datetime NOT NULL COMMENT '下一次最远复训时间',
  `participate_type` int NOT NULL COMMENT '是否参与活动：0未参加 1已参加',
  PRIMARY KEY (`retrain_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of retrain
-- ----------------------------
INSERT INTO `retrain` VALUES (1, 4, 4, '北京1', '2023-10-01 00:00:00', '2024-10-01 00:00:00', 1);
INSERT INTO `retrain` VALUES (2, 5, 14, '上海', '2023-10-01 00:00:00', '2024-10-01 00:00:00', 1);
INSERT INTO `retrain` VALUES (7, 12, 4, '郑州', '2024-03-20 00:00:00', '2025-03-20 00:00:00', 1);
INSERT INTO `retrain` VALUES (9, 15, 1, '北京', '2025-03-25 14:00:00', '2026-03-25 14:00:00', 1);
INSERT INTO `retrain` VALUES (10, 15, 4, '北京', '2025-03-25 14:00:00', '2026-03-25 14:00:00', 1);
INSERT INTO `retrain` VALUES (11, 16, 4, '洛杉矶', '2025-05-25 14:00:00', '2026-05-25 14:00:00', 0);

-- ----------------------------
-- Table structure for train
-- ----------------------------
DROP TABLE IF EXISTS `train`;
CREATE TABLE `train`  (
  `train_id` int NOT NULL AUTO_INCREMENT COMMENT '培训记录ID',
  `train_time` datetime NOT NULL COMMENT '培训时间',
  `train_place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '培训地点',
  `train_type` int NOT NULL COMMENT '培训状态：-1已结束，0报名中，1进行中',
  `train_people` int NOT NULL COMMENT '可参加培训人数',
  `current_people` int NULL DEFAULT NULL COMMENT '当前报名人数',
  `user_ids` json NULL COMMENT '参加培训用户id',
  `participate_ids` json NULL COMMENT '创建活动签到用户id',
  PRIMARY KEY (`train_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of train
-- ----------------------------
INSERT INTO `train` VALUES (4, '2023-10-01 00:00:00', '北京1', -1, 80, 6, '[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30]', '[1, 2, 3, 5, 6, 4]');
INSERT INTO `train` VALUES (5, '2023-10-01 00:00:00', '上海', -1, 70, 3, '[11, 12, 13]', '[11, 12, 13]');
INSERT INTO `train` VALUES (6, '2023-10-01 00:00:00', '广州', -1, 60, 1, '[24]', '[24]');
INSERT INTO `train` VALUES (12, '2025-03-20 00:00:00', '郑州', -1, 75, 1, '[4]', '[4]');
INSERT INTO `train` VALUES (15, '2025-03-25 14:00:00', '北京', -1, 40, 2, '[1, 4]', '[1, 4]');
INSERT INTO `train` VALUES (16, '2025-05-25 14:00:00', '洛杉矶', 0, 40, 1, '[4]', '[]');
INSERT INTO `train` VALUES (17, '2025-05-25 14:00:00', '伦敦', 0, 40, 0, '[]', '[]');
INSERT INTO `train` VALUES (18, '2025-09-03 14:00:00', '天堂', 0, 20, 0, '[]', '[]');
INSERT INTO `train` VALUES (19, '2025-09-03 14:00:00', '地府', 0, 20, 0, '[]', '[]');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `picture_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像图片',
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱地址',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录账号',
  `user_type` int NOT NULL DEFAULT 0 COMMENT '用户身份：0用户1管理员2超级管理员',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '加密',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `account`(`account` ASC) USING BTREE,
  UNIQUE INDEX `user_name`(`user_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('https://120.27.161.155/files/avatar/avatar2.jpg', 1, '2572886630@qq.com', '123456', 'clt', '123456', 2, NULL);
INSERT INTO `user` VALUES ('/assets/avatar/avatar1.jpg', 3, '1842501791@qq.com', '123456', 'zwj', '1234567', 1, NULL);
INSERT INTO `user` VALUES ('https://120.27.161.155/files/avatar/avatar4.jpg', 4, '8888@qq.com', '123456', '张三', '123456777', 1, NULL);
INSERT INTO `user` VALUES ('/assets/avatar/avatar3.jpg', 5, '99998@qq.com', '123456', '李四', '1234567777', 0, NULL);
INSERT INTO `user` VALUES (NULL, 9, '12345678@123com', '123456', '王五', '15846795', 0, NULL);
INSERT INTO `user` VALUES (NULL, 16, 'chenlintao002@2925.com', '123456', 'CLT002', 'clt123456002', 0, NULL);
INSERT INTO `user` VALUES (NULL, 17, 'chenlintao156174@2925.com', '123456', 'clt001', '156174', 0, NULL);
INSERT INTO `user` VALUES (NULL, 18, 'chenlintao147258@2925.com', '123456', 'clt147258', '147258', 0, NULL);

-- ----------------------------
-- Procedure structure for RemoveUserFromTrain
-- ----------------------------
DROP PROCEDURE IF EXISTS `RemoveUserFromTrain`;
delimiter ;;
CREATE PROCEDURE `RemoveUserFromTrain`(IN trainId INT, IN userId INT)
BEGIN
    DECLARE newUserIds JSON;
    DECLARE i INT DEFAULT 0;
    DECLARE arrayLength INT;
    DECLARE currentUserId INT;

    -- 获取 user_ids 数组长度
    SELECT JSON_LENGTH(user_ids) INTO arrayLength
    FROM train
    WHERE train_id = trainId;

    -- 初始化新的 user_ids 数组
    SET newUserIds = JSON_ARRAY();

    -- 遍历 user_ids 数组
    WHILE i < arrayLength DO
        -- 获取当前 user_id
        SELECT JSON_EXTRACT(user_ids, CONCAT('$[', i, ']')) INTO currentUserId
        FROM train
        WHERE train_id = trainId;

        -- 如果当前 user_id 不等于要移除的 user_id，则添加到新数组中
        IF currentUserId != userId THEN
            SET newUserIds = JSON_ARRAY_APPEND(newUserIds, '$', currentUserId);
        END IF;

        -- 递增索引
        SET i = i + 1;
    END WHILE;

    -- 更新 user_ids 和 current_people
    UPDATE train
    SET
        user_ids = newUserIds,
        current_people = current_people - 1
    WHERE train_id = trainId;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
