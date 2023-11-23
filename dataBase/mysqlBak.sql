/*
SQLyog  v12.2.6 (64 bit)
MySQL - 8.0.26 : Database - yzl
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`yzl` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `yzl`;

/*Table structure for table `meituan_wm` */

DROP TABLE IF EXISTS `meituan_wm`;

CREATE TABLE `meituan_wm` (
  `wm_shop_id` varchar(32) NOT NULL COMMENT '外卖门店id',
  `wm_shop_name` varchar(60) DEFAULT NULL COMMENT '门店名称',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`wm_shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `meituan_wm_turnover` */

DROP TABLE IF EXISTS `meituan_wm_turnover`;

CREATE TABLE `meituan_wm_turnover` (
  `wm_shop_id` varchar(32) DEFAULT NULL COMMENT '门店id',
  `wm_time` varchar(60) DEFAULT NULL COMMENT '时间',
  `wm_shop_name` varchar(60) DEFAULT NULL COMMENT '门店名称',
  `wm_turnover` decimal(10,2) DEFAULT NULL COMMENT '一周营业额',
  `wm_order` int DEFAULT NULL COMMENT '一周客单量',
  `wm_cus_pay` decimal(8,2) DEFAULT NULL COMMENT '一周实付客单',
  `wm_cus_income` decimal(8,2) DEFAULT NULL COMMENT '一周实收客单',
  `wm_exposure` int DEFAULT NULL COMMENT '一周曝光量',
  `wm_exposure_topten` int DEFAULT NULL COMMENT '商圈前10曝光量',
  `wm_store_rate` decimal(6,4) DEFAULT NULL COMMENT '入店转化率',
  `wm_store_rate_topten` decimal(6,4) DEFAULT NULL COMMENT '商圈前10入店转化率',
  `wm_order_rate` decimal(6,4) DEFAULT NULL COMMENT '下单转化率',
  `wm_order_rate_topten` decimal(6,4) DEFAULT NULL COMMENT '商圈前10下单转化率',
  `wm_repurchase_rate` decimal(6,4) DEFAULT NULL COMMENT '复购率',
  `wm_repurchase_rate_topten` decimal(6,4) DEFAULT NULL COMMENT '商圈前10复购率',
  `wm_score` decimal(4,2) DEFAULT NULL COMMENT '评分',
  KEY `Index_1` (`wm_time`,`wm_shop_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `test` */

DROP TABLE IF EXISTS `test`;

CREATE TABLE `test` (
  `id` varchar(255) NOT NULL COMMENT 'id',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(30) DEFAULT NULL COMMENT '联系方式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `tokens` */

DROP TABLE IF EXISTS `tokens`;

CREATE TABLE `tokens` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '自建应用agentId',
  `token` text COMMENT '调用凭证的值',
  `des` text COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `wm_time_query` */

DROP TABLE IF EXISTS `wm_time_query`;

CREATE TABLE `wm_time_query` (
  `wm_time` varchar(60) NOT NULL COMMENT '时间段',
  PRIMARY KEY (`wm_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
