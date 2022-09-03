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
    id   bigint auto_increment
        primary key,
    name varchar(20) null comment '材质'
);

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

INSERT INTO config (config_key, config_value, remark) VALUES ('wechat_appid', '', '小程序appid');
INSERT INTO config (config_key, config_value, remark) VALUES ('wechat_secret', '', '小程序密钥');
INSERT INTO config (config_key, config_value, remark) VALUES ('merchantId', '', '商户Id');
INSERT INTO config (config_key, config_value, remark) VALUES ('serialNumber', '', '序列号');
INSERT INTO config (config_key, config_value, remark) VALUES ('apiV3Key', '', 'key');
INSERT INTO config (config_key, config_value, remark) VALUES ('algorithm', '', '算法');


create table if not exists coupon
(
    id         bigint auto_increment
        primary key,
    coupon_id  varchar(255)     null comment '优惠券id',
    is_used    char default '0' null comment '是否使用',
    status     char default '0' not null comment '是否失效',
    price      decimal(10, 2)   null comment '消费券金额',
    use_limit  int              null comment '消费限制',
    open_id    varchar(255)     null comment '使用者',
    picture_id bigint           null comment '图片id'
);

create table if not exists `order`
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
    address_id  bigint                             null comment '收货地址id',
    caizhi_id   bigint                             null comment '材质id',
    size_id     bigint                             null comment '尺寸id',
    zip_path    varchar(100)                       null comment '下载zip链接',
    is_download char     default '0'               not null comment '是否下载',
    create_user varchar(50)                        not null comment '创建人',
    is_del      char     default '0'               not null comment '是否删除',
    create_time datetime                           null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    update_user varchar(30)                        null comment '修改人'
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
    title       varchar(255)   null comment '标题',
    name        varchar(255)   not null comment '名字',
    pic         varchar(255)   null comment '尺寸详情页的大图',
    price       decimal(10, 2) not null comment '原价',
    u_price     decimal(10, 2) null comment '优惠后价格',
    material_id varchar(255)   not null comment '可以使用的材质id集合',
    size        varchar(255)   null comment '尺寸大小 第一个width 第二个height',
    faces       varchar(5)     null comment '单面or 双面'
);

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

create index user_admin_password_IDX
    on user_admin (password);

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

