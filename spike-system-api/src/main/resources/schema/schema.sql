/*
 *  mysql-v: 8.0.16
 */


drop table if exists `user`;
-- 创建用户表
create table `user`
(
    `id`                       bigint           not null auto_increment comment '用户id',
    `username`                 varchar(100)     not null comment '用户名',
    `password`                 varchar(100)    not null comment '密码',
    `email`                    varchar(100)    not null comment '邮箱',
    `create_time`              timestamp        not null default current_timestamp comment '创建时间',
    `update_time`              timestamp        not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (`id`),
    unique key (`username`)
)charset = utf8mb4
 engine = InnoDB comment '用户基础信息表';

drop table if exists `wallet`;
-- 创建用户钱包表
create table `wallet`
(
    `id`                        bigint          not null auto_increment comment '钱包ID',
    `username`                  varchar(100)    not null comment '用户名',
    `payPassword`               varchar(100)    not null comment '支付密码',
    `money`                     decimal(10, 2)  not null default 0  comment '钱包余额',
    `version`                   int             not null default 0  comment '版本',
    primary key (`id`),
    foreign key (`username`) references user(`username`)
)charset = utf8mb4
 engine = InnoDB comment '用户钱包表';

drop table if exists `product`;
-- 创建秒杀商品表
create table `product`
(
    `id`               bigint                  not null auto_increment comment '商品ID',
    `name`             varchar(100)           default null comment '商品标题',
    `amount`           int                     default null comment '剩余库存数量',
    `origin_price`     decimal(10, 2)          not null comment '商品原价格',
    `spike_price`      decimal(10, 2)          default null comment '商品秒杀价格',
    `spike_start_time` timestamp               default null default '1970-02-01 00:00:01' comment '秒杀开始时间',
    `spike_end_time`   timestamp               default null default '1970-02-01 00:00:01' comment '秒杀结束时间',
    `create_time`      timestamp               not null default current_timestamp comment '创建时间',
    `update_time`      timestamp               not null default current_timestamp on update current_timestamp comment '最近修改时间',
    `image_url`        varchar(1000)           default null comment '商品图片',
    `info`             varchar(10000)          default null comment '商品描述',
    primary key (`id`),
    key `idx_start_time` (`spike_start_time`),
    key `idx_end_time` (`spike_end_time`),
    key `idx_create_time` (`create_time`)
) charset = utf8mb4
  engine = InnoDB comment '秒杀商品表';



drop table if exists `order`;
-- 创建秒杀订单表
create table `order`
(
    `id`                varchar(100)                not null comment '订单号',
    `order_time`        timestamp                   not null comment '下单时间',
    `order_username`    varchar(100)                not null comment '下单用户用户名',
    `order_status`      tinyint                     not null default 0 comment '订单状态 -1取消 0准备 1成功',
    `product_id`        bigint                      not null comment '秒杀商品ID',
    `pay_money`         decimal(10, 2)              default null comment '支付金额',
    `pay_time`          timestamp                   not null default current_timestamp comment '支付时间',
    `pay_username`      varchar(100)                default null comment '支付用户用户名',
    `create_time`       timestamp                   not null default current_timestamp comment '创建时间',
    `update_time`       timestamp                   not null default current_timestamp on update current_timestamp comment '最近修改时间',
    primary key (`id`),
    foreign key (`product_id`) references product (`id`),
    foreign key (`order_username`) references user(`username`),
    foreign key (`pay_username`) references user(`username`)
) charset = utf8mb4
  engine = InnoDB comment '秒杀订单表';
