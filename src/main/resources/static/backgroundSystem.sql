create table if not exists address
(
    id          bigint auto_increment
        primary key,
    openId      varchar(30)      not null comment '用户唯一标识',
    name        varchar(255)     null comment '姓名',
    phone       varchar(11)      null comment '手机号',
    province    varchar(255)     null comment '省市区',
    address     varchar(255)     null comment '详细地址',
    is_default  char default '0' not null comment '是否是默认地址 默认不是',
    is_del      char default '0' not null,
    create_time datetime         not null
);

create table if not exists caizhi
(
    id     bigint auto_increment
        primary key,
    name   varchar(20)          null comment '材质',
    price  decimal(10, 2)       not null comment '价格',
    is_del tinyint(1) default 0 not null comment '是否删除'
)
    charset = utf8mb4;

create table if not exists check_rule
(
    ID          bigint auto_increment comment 'id'
        primary key,
    RULE_NAME   varchar(255)                        null,
    RULE_SCORE  varchar(255)                        null,
    UPDATE_TIME timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    REMARK      varchar(400)                        null,
    TYPE        varchar(255)                        null,
    PARENT_ID   varchar(200)                        null,
    RULE_LEVEL  int                                 null,
    FILE_NAME   varchar(255)                        null,
    SORT        varchar(100)                        null
);

create table if not exists check_template
(
    ID             bigint auto_increment comment 'id'
        primary key,
    TEMPLATE_NAME  varchar(200)                        null,
    LOCATION_TYPE  varchar(255)                        null,
    CHECK_RULE_IDS varchar(255)                        null,
    ADD_USER       varchar(255)                        null,
    ADD_TIME       timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint ux_type
        unique (LOCATION_TYPE)
);

create table if not exists config
(
    id           int auto_increment
        primary key,
    config_key   varchar(255) not null,
    config_value varchar(255) not null,
    remark       varchar(255) not null,
    constraint ux_key
        unique (config_key)
);

create table if not exists coupon
(
    id           bigint auto_increment
        primary key,
    coupon_id    varchar(255)     not null comment '优惠券id',
    is_used      char default '0' not null comment '是否使用',
    status       char default '0' not null comment '是否失效',
    price        decimal(10, 2)   null comment '消费券金额',
    use_limit    int              null comment '消费限制',
    open_id      varchar(255)     null comment '使用者',
    picture_id   bigint           null comment '图片id',
    release_time datetime         null comment '发布时间',
    update_time  timestamp        null on update CURRENT_TIMESTAMP comment '修改时间',
    constraint ux_coupon
        unique (coupon_id) comment '兑换码'
);

create table if not exists order_element
(
    id          bigint auto_increment
        primary key,
    order_id    bigint           null comment '订单号',
    number      int  default 0   not null comment '商品数量',
    picture_id  mediumtext       not null comment '商品图片',
    is_del      char default '0' not null comment '是否删除',
    create_time datetime         not null comment '创建时间'
);

create table if not exists orderd
(
    id          bigint auto_increment
        primary key,
    order_no    varchar(255)                       null comment '微信支付订单号',
    kd_no       varchar(255)                       null comment '快递单号',
    is_pay      char     default '0'               not null comment '是否支付',
    remark      varchar(255)                       null comment '备注',
    port_price  decimal(10, 2)                     null comment '运费',
    total       decimal(10, 2)                     null comment '商品总价（不包含优惠券）',
    coupon_id   bigint                             null comment '优惠券id',
    status      char     default '0'               not null comment '订单状态：待付款，待发货，售后订单，交易关闭',
    address_id  varchar(50)                        null comment '收货地址id',
    caizhi_id   varchar(100)                       null comment '材质id',
    size_id     bigint                             not null comment '尺寸id',
    zip_path    varchar(255)                       null comment '下载zip链接',
    is_download char     default '0'               not null comment '是否下载',
    create_user varchar(50)                        not null comment '创建人',
    is_del      char     default '0'               not null comment '是否删除',
    create_time datetime                           not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    update_user varchar(30)                        null comment '修改人'
)
    charset = utf8mb4;

create table if not exists picture
(
    id          bigint auto_increment
        primary key,
    name        varchar(255)     null comment '文件名字',
    url         varchar(255)     null comment '图片地址',
    father      varchar(255)     null comment '上级目录',
    is_del      char default '0' not null comment '是否删除',
    create_time datetime         not null comment '创建时间'
);

create table if not exists picture_accessory
(
    id         bigint auto_increment
        primary key,
    picture_id bigint null comment '图片id',
    `like`     bigint null comment '点赞数',
    num        bigint null comment '下单数',
    pv         bigint null comment '访问量'
);

create table if not exists size
(
    id          bigint auto_increment
        primary key,
    title       varchar(255)         null comment '标题',
    name        varchar(255)         not null comment '名字',
    pic         varchar(255)         null comment '尺寸详情页的大图',
    detail_pic  varchar(255)         null comment '详情图',
    price       decimal(10, 2)       not null comment '原价',
    u_price     decimal(10, 2)       null comment '优惠后价格',
    material_id varchar(255)         not null comment '可以使用的材质id集合',
    size        varchar(255)         null comment '尺寸大小 第一个width 第二个height',
    faces       varchar(5)           null comment '单面or 双面',
    is_del      tinyint(1) default 0 not null comment '是否删除'
)
    charset = utf8mb4;

create table if not exists user_admin
(
    id          varchar(11)   not null
        primary key,
    name        varchar(255)  not null,
    user_name   varchar(100)  not null,
    password    varchar(100)  not null,
    is_delete   int default 0 not null,
    create_time datetime      not null,
    update_time datetime      not null,
    mobile      varchar(11)   null,
    email       varchar(100)  null
);

create index user_admin_user_name_IDX
    on user_admin (user_name);

create table if not exists wechat_user
(
    id          bigint auto_increment
        primary key,
    open_id     varchar(100)  not null comment 'open Id',
    union_id    varchar(100)  null,
    user_info   varchar(300)  null comment '用户信息',
    is_del      int default 0 not null comment '是否删除',
    create_time datetime      not null,
    constraint ux_code_del
        unique (open_id, is_del)
);

create or replace definer = root@localhost view createPictureZip as
select `o`.`id`                                      AS `id`,
       `s`.`name`                                    AS `sizeName`,
       `s`.`size`                                    AS `size`,
       `s`.`faces`                                   AS `face`,
       `o`.`order_no`                                AS `wxNo`,
       `o`.`zip_path`                                AS `zip_path`,
       group_concat(`oe`.`picture_id` separator ',') AS `pictureId`,
       sum(`oe`.`number`)                            AS `number`,
       `o`.`create_time`                             AS `create_time`,
       `o`.`total`                                   AS `total`
from ((`backgroundsystem`.`orderd` `o` join `backgroundsystem`.`size` `s`
       on ((`o`.`size_id` = `s`.`id`))) join `backgroundsystem`.`order_element` `oe` on ((`o`.`id` = `oe`.`order_id`)))
where ((`o`.`is_del` = 0) and (`o`.`is_pay` = 1) and (`o`.`zip_path` is null) and (`o`.`order_no` is not null))
group by `o`.`id`;

-- comment on column createpicturezip.sizeName not supported: 名字

-- comment on column createpicturezip.size not supported: 尺寸大小 第一个width 第二个height

-- comment on column createpicturezip.face not supported: 单面or 双面

-- comment on column createpicturezip.wxNo not supported: 微信支付订单号

-- comment on column createpicturezip.zip_path not supported: 下载zip链接

-- comment on column createpicturezip.create_time not supported: 创建时间

-- comment on column createpicturezip.total not supported: 商品总价（不包含优惠券）

create or replace definer = root@localhost view getPriceEveryday as
select date_format(`o`.`create_time`, '%Y-%m-%d') AS `create_time`,
       sum(`o`.`total`)                           AS `total`,
       count(`o`.`id`)                            AS `number`
from `backgroundsystem`.`orderd` `o`
where ((`o`.`is_del` = 0) and (`o`.`is_pay` = 1))
group by date_format(`o`.`create_time`, '%Y-%m-%d')
order by `create_time` desc;

-- 
INSERT INTO `config` (`id`, `config_key`, `config_value`, `remark`) VALUES (1, 'wechat_appid', '', '小程序appid');
INSERT INTO `config` (`id`, `config_key`, `config_value`, `remark`) VALUES (2, 'wechat_secret', '', '小程序密钥');
INSERT INTO `config` (`id`, `config_key`, `config_value`, `remark`) VALUES (3, 'merchantId', '', '商户Id');
INSERT INTO `config` (`id`, `config_key`, `config_value`, `remark`) VALUES (4, 'serialNumber', '', '序列号');
INSERT INTO `config` (`id`, `config_key`, `config_value`, `remark`) VALUES (5, 'apiV3Key', '', 'apiV3Key');
INSERT INTO `config` (`id`, `config_key`, `config_value`, `remark`) VALUES (6, 'algorithm', 'SHA256withRSA', 'algorithm');
INSERT INTO `config` (`id`, `config_key`, `config_value`, `remark`) VALUES (7, 'ENDPOINT', 'https://oss-cn-hangzhou.aliyuncs.com', 'ENDPOINT');
INSERT INTO `config` (`id`, `config_key`, `config_value`, `remark`) VALUES (8, 'RESULT_URL', '', 'RESULT_URL');
INSERT INTO `config` (`id`, `config_key`, `config_value`, `remark`) VALUES (9, 'ACCESS_KEY_ID', '', 'ACCESS_KEY_ID');
INSERT INTO `config` (`id`, `config_key`, `config_value`, `remark`) VALUES (10, 'ACCESS_KEY_SECRET', '', 'ACCESS_KEY_SECRET');
INSERT INTO `config` (`id`, `config_key`, `config_value`, `remark`) VALUES (11, 'send-email', '', '接收邮件的邮箱多个逗号分隔');