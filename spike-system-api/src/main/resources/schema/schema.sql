
-- 创建用户表
create table `user`
(
    `id`          bigint            unsigned not null auto_increment comment '用户id',
    `username`    varchar(100)      not null comment '用户名',
    `password`    varchar(100)      not null comment '密码',
    `email`       varchar(100)      not null comment '邮箱',
    `create_time` timestamp         not null default current_timestamp comment '创建时间',
    `update_time` timestamp         not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (`id`),
    unique key (`username`)
)charset = utf8mb4
 engine = InnoDB comment '用户基础信息表';

-- 创建用户钱包表
create table `wallet`
(
    `id`           bigint           unsigned not null auto_increment comment '钱包ID',
    `username`     varchar(100)     not null comment '用户名',
    `pay_password` varchar(100)     not null comment '支付密码',
    `money`        decimal(10, 2)   not null default 0 comment '钱包余额',
    `version`      int              unsigned not null default 0 comment '版本',
    primary key (`id`),
    foreign key (`username`) references user(`username`)
)charset = utf8mb4
 engine = InnoDB comment '用户钱包表';

-- 创建商铺表
create table `shop`
(
    `id`        bigint              unsigned not null auto_increment comment '商铺ID',
    `owner`     varchar(100)        not null comment '卖家用户名',
    `shop_name` varchar(100)        not null comment '商铺名',
    `money`     decimal(10, 2)      not null default 0 comment '商铺余额',
    `version`   int                 unsigned not null default 0 comment '版本',
    primary key (`id`),
    foreign key (owner) references user(`username`)
)charset = utf8mb4
 engine = InnoDB comment '商铺表';

-- 创建商品表
create table `good`
(
    `id`               bigint                   unsigned not null auto_increment comment '商品ID',
    `name`             varchar(100)             default null comment '商品标题',
    `amount`           int                      default 0 comment '商品数量',
    `origin_price`     decimal(10, 2)           not null comment '商品原价格',
    `spike_price`      decimal(10, 2)           default null comment '商品秒杀价格',
    `spike_start_time` timestamp                default null default '1970-02-01 00:00:01' comment '秒杀开始时间',
    `spike_end_time`   timestamp                default null default '1970-02-01 00:00:01' comment '秒杀结束时间',
    `create_time`      timestamp                not null default current_timestamp comment '创建时间',
    `update_time`      timestamp                not null default current_timestamp on update current_timestamp comment '最近修改时间',
    `shop_id`          bigint                   unsigned not null comment '商铺',
    `image_url`        varchar(1000)            default null comment '商品图片',
    `info`             varchar(10000)           default null comment '商品描述',
    primary key (`id`),
    foreign key (`shop_id`) references shop(`id`),
    key `idx_start_time` (`spike_start_time`),
    key `idx_end_time` (`spike_end_time`),
    key `idx_create_time` (`create_time`)
) charset = utf8mb4
  engine = InnoDB comment '商品表';

-- 创建订单表
create table `indent`
(
    `id`             bigint             unsigned not null comment '订单号',
    `order_time`     timestamp          not null comment '下单时间',
    `order_username` varchar(100)       not null comment '下单用户用户名',
    `order_status`   tinyint            not null default 0 comment '订单状态 -1取消 0准备 1成功',
    `good_id`        bigint             unsigned not null comment '秒杀商品ID',
    `amount`         int                unsigned not null comment '秒杀商品数量',
    `pay_money`      decimal(10, 2)     default null comment '支付金额',
    `pay_time`       timestamp          default null comment '支付时间',
    `pay_wallet_id`  bigint             unsigned default null comment '支付用钱包ID',
    `create_time`    timestamp          not null default current_timestamp comment '创建时间',
    `update_time`    timestamp          not null default current_timestamp on update current_timestamp comment '最近修改时间',
    `info`           varchar(100)       default null comment '备注',
    primary key (`id`),
    foreign key (`good_id`) references good (`id`),
    foreign key (`order_username`) references user(`username`),
    foreign key (`pay_wallet_id`) references wallet(`id`)
) charset = utf8mb4
  engine = InnoDB comment '订单表';
