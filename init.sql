create table if not exists constant_variable
(
    id        int auto_increment
        primary key,
    name      varchar(255)         not null,
    type      int                  not null comment '0: 本表类别',
    is_delete tinyint(1) default 0 not null comment '0: 未删除 1: 已删除'
)
    charset = utf8;

create table if not exists department_kind
(
    id                int auto_increment
        primary key,
    kind_name         varchar(64)          not null,
    classification_id int                  not null comment '参照常量表',
    is_delete         tinyint(1) default 0 not null
)
    charset = utf8;

create table if not exists department
(
    id        int auto_increment
        primary key,
    name      varchar(64)          not null,
    kind_id   int                  not null,
    code      varchar(24)          not null,
    is_delete tinyint(1) default 0 not null,
    constraint department_department_kind_id_fk
        foreign key (kind_id) references department_kind (id)
)
    charset = utf8;

create table if not exists diagnose
(
    id          int auto_increment
        primary key,
    item_id     int        not null comment '病历或者模板的id',
    disease_id  int        not null,
    create_time datetime   not null,
    is_major    tinyint(1) not null comment '是否为确诊',
    is_template tinyint(1) not null
)
    comment '诊断结果' charset = utf8;

create index diagnose_disease_second_id_fk
    on diagnose (disease_id);

create index diagnose_medical_record_id_fk
    on diagnose (item_id);

create table if not exists disease_first
(
    id        int auto_increment
        primary key,
    name      varchar(64)          not null,
    is_delete tinyint(1) default 0 not null
)
    charset = utf8;

create table if not exists disease_second
(
    id               int auto_increment
        primary key,
    icd_id           varchar(64)          not null,
    disease_coding   varchar(64)          not null,
    name             varchar(64)          not null,
    disease_first_id int                  not null,
    is_delete        tinyint(1) default 0 not null
)
    charset = utf8;

create index disease_second_disease_first_id_fk
    on disease_second (disease_first_id);

create table if not exists drug
(
    id              int auto_increment
        primary key,
    code            varchar(64)              not null comment '编码',
    name            varchar(64)              not null,
    standard        varchar(64)              not null comment '规格',
    package_company varchar(64)              not null comment '包装单位',
    factory         varchar(64)              null,
    formulation     int                      not null comment '药剂剂型',
    drug_type       int                      not null comment '中草药
西药
中成药
之类的
',
    price           decimal(10, 2)           not null,
    spell           varchar(64)              not null,
    stock_amount    int        default 10000 not null,
    fee_type_id     int                      not null,
    is_delete       tinyint(1) default 0     not null
)
    charset = utf8;

create table if not exists frequently_used
(
    user_id int not null,
    item_id int not null,
    type    int not null
)
    comment '常用项目表' charset = utf8;

create table if not exists non_drug
(
    id                   int auto_increment
        primary key,
    code                 varchar(24)            not null,
    name                 varchar(24)            not null,
    standard             varchar(24) default '' null,
    price                decimal(10, 2)         not null,
    fee_type_id          int                    not null comment '非药物费用类别',
    executive_department int                    null,
    is_delete            tinyint(1)  default 0  not null
)
    comment 'standard-规格' charset = utf8;

create table if not exists inspection_application
(
    id          int auto_increment
        primary key,
    item_id     int                  not null comment '病历或者模板',
    non_drug_id int                  not null,
    create_time datetime             not null,
    quantity    int        default 1 not null,
    is_canceled tinyint(1) default 0 not null,
    is_emerged  tinyint(1) default 0 not null comment '是否为加急项目',
    is_done     tinyint(1) default 0 not null,
    is_template int        default 0 not null,
    fee_type_id int                  not null,
    is_check    tinyint(1) default 0 not null,
    constraint inspection_application_non_drug_id_fk
        foreign key (non_drug_id) references non_drug (id)
)
    comment 'is_canceled-是否取消项目' charset = utf8;

create index inspection_application_medical_record_id_fk
    on inspection_application (item_id);

create table if not exists patient
(
    id                        int auto_increment
        primary key,
    identity_id               varchar(18)            null comment '身份证号',
    password                  varchar(255)           not null,
    sex                       tinyint(1)             not null comment 'true:women
false:men',
    real_name                 varchar(24)            not null,
    username                  varchar(64)            not null,
    last_login_time           datetime               null,
    create_time               datetime               not null,
    last_password_modify_time datetime               null,
    phone_number              varchar(12) default '' not null,
    constraint patient_identity_id_uindex
        unique (identity_id),
    constraint patient_username_uindex
        unique (username)
)
    charset = utf8;

create table if not exists payment_type
(
    id        int auto_increment
        primary key,
    code      varchar(24)          null,
    name      varchar(64)          not null,
    type      int                  not null comment '大类：0 -- 挂号费
1 -- 处方
2 -- 项目',
    is_delete tinyint(1) default 0 not null
)
    comment '费用科目，e.g:西药费，挂号费，可直接由老师给的表导入' charset = utf8;

create table if not exists prescription
(
    id               int auto_increment
        primary key,
    usage_id         int                     not null comment '用法，参照常量表',
    item_id          int                     not null,
    frequency        varchar(64) default ''  not null comment '频率',
    drug_id          int                     not null,
    amount           int                     not null,
    days             int                     not null,
    use_amount       varchar(64) default '无' not null comment '单次用量',
    note             varchar(255)            null,
    need_skin_test   tinyint(1)  default 0   not null,
    skin_test_result tinyint(1)              null,
    is_template      tinyint(1)  default 0   not null,
    create_time      datetime                not null,
    fee_type_id      int                     not null,
    constraint prescription_drug_id_fk
        foreign key (drug_id) references drug (id)
)
    comment '处方' charset = utf8;

create index prescription_medical_record_id_fk
    on prescription (item_id);

create table if not exists registration_type
(
    id                      int auto_increment
        primary key,
    name                    varchar(64)           not null,
    is_default              tinyint(1)            not null,
    price                   decimal(10, 2)        not null,
    display_sequence_number int        default 50 not null,
    is_delete               tinyint(1) default 0  not null
)
    charset = utf8;

create table if not exists user
(
    id                        int auto_increment
        primary key,
    username                  varchar(64)                                                                              not null,
    real_name                 varchar(64)                                                                              not null,
    password                  varchar(255)                                                                             not null,
    create_time               datetime                                                                                 not null,
    last_login_time           datetime                                                                                 null,
    last_password_modify_time datetime                                                                                 null,
    type_id                   int                                                                                      not null comment '记录医生类别，具体参考常量表',
    department_id             int                                                                                      not null,
    identify_id               varchar(18)                                                                              not null,
    is_delete                 tinyint(1)   default 0                                                                   not null,
    avatar                    varchar(512) default 'http://www.tjmugh.com.cn/pic/003/000/086/00300008606_eea3f3bf.jpg' not null,
    constraint user_username_uindex
        unique (username),
    constraint user_department_id_fk
        foreign key (department_id) references department (id)
)
    comment '储存医生，操作员' charset = utf8;

create table if not exists daily_settle
(
    id                    int auto_increment
        primary key,
    start_date            datetime                    not null,
    end_date              datetime                    not null,
    make_date             datetime                    not null,
    admin_id              int                         not null comment '收费员id',
    make_id               int                         not null comment '制表人id',
    total_fee             decimal(10, 2) default 0.00 not null comment '总金额',
    drug_fee              decimal(10, 2) default 0.00 not null comment '药费总额',
    inspection_fee        decimal(10, 2) default 0.00 not null comment '检查项目费',
    registration_fee      decimal(10, 2) default 0.00 not null comment '挂号费',
    other_fee             decimal(10, 2) default 0.00 not null comment '其他费用',
    normal_invoice_amount int            default 0    not null comment '一般发票数量',
    anew_invoice_amount   int            default 0    not null,
    is_pass               tinyint(1)     default 0    not null,
    check_id              int                         null comment '核对人id',
    constraint daily_settle_user_id_fk
        foreign key (admin_id) references user (id),
    constraint daily_settle_user_id_fk_2
        foreign key (make_id) references user (id)
)
    comment '日结表';

create table if not exists doctor
(
    id          int auto_increment
        primary key,
    title_id    int                  not null comment '职称,参照常量表',
    can_arrange tinyint(1) default 0 not null comment '是否排班',
    is_delete   tinyint(1) default 0 not null,
    constraint doctor_user_id_fk
        foreign key (id) references user (id)
)
    comment '医生表' charset = utf8;

create table if not exists drug_template
(
    id            int auto_increment
        primary key,
    department_id int                  not null,
    level         int                  not null,
    name          varchar(24)          not null,
    created_by_id int                  not null,
    is_herbal     tinyint(1) default 0 not null,
    constraint drug_template_user_id_fk
        foreign key (created_by_id) references user (id)
)
    charset = utf8;

create table if not exists drug_template_relationship
(
    template_id int not null,
    item_id     int not null comment '对应某次药id',
    constraint `drug_template_relationship_drug __fk`
        foreign key (item_id) references drug (id),
    constraint drug_template_relationship_drug_template_id_fk
        foreign key (template_id) references drug_template (id)
)
    charset = utf8;

create table if not exists inspection_result
(
    id                        int auto_increment
        primary key,
    operator_id               int      not null,
    department_id             int      not null,
    create_time               datetime not null,
    text                      text     not null,
    picture                   text     not null,
    inspection_application_id int      not null,
    constraint inspection_result_department_id_fk
        foreign key (department_id) references department (id),
    constraint inspection_result_inspection_application_id_fk
        foreign key (inspection_application_id) references inspection_application (id),
    constraint inspection_result_user_id_fk
        foreign key (operator_id) references user (id)
)
    charset = utf8;

create table if not exists inspection_template
(
    id            int auto_increment
        primary key,
    department_id int         not null,
    level         int         not null,
    name          varchar(24) not null comment '模板名称',
    created_by_id int         not null,
    constraint Inspection_template_user_id_fk
        foreign key (created_by_id) references user (id),
    constraint inspection_template_department_id_fk
        foreign key (department_id) references department (id)
)
    charset = utf8;

create table if not exists inspection_template_relationship
(
    id          int auto_increment
        primary key,
    template_id int           not null,
    item_id     int           not null,
    item_type   int default 1 not null comment '1-西药
2-中成药
3-中草药
4-非药',
    amount      int default 1 not null,
    constraint inspection_template_relationship_inspection_template_id_fk
        foreign key (template_id) references inspection_template (id)
)
    charset = utf8;

create table if not exists invoice
(
    id              int auto_increment
        primary key,
    price_amount    decimal(10, 2) default 0.00 not null,
    created_date    datetime                    not null,
    anew_amount     int            default 0    not null comment '重打发票数量',
    again_amount    int            default 0    null comment '补打数量',
    daily_settle_id int                         null comment '日结id',
    operator_id     int                         not null,
    constraint invoice_daily_settle_id_fk
        foreign key (daily_settle_id) references daily_settle (id),
    constraint invoice_user_id_fk
        foreign key (operator_id) references user (id)
)
    charset = utf8;

create table if not exists job_schedule
(
    id                        int auto_increment
        primary key,
    doctor_id                 int        not null,
    registration_type_id      int        not null,
    is_valid                  tinyint(1) not null,
    limit_registration_amount int        not null,
    period                    int        not null comment '午别，参照常量表',
    date                      date       not null,
    create_time               datetime   null,
    constraint job_schedule_doctor_id_fk
        foreign key (doctor_id) references doctor (id),
    constraint job_schedule_registration_type_id_fk
        foreign key (registration_type_id) references registration_type (id)
)
    charset = utf8;

create table if not exists medical_record_template
(
    id                  int auto_increment
        primary key,
    name                varchar(24)              not null comment '模板名称',
    self_description    varchar(255) default '无' not null,
    current_symptom     varchar(255) default '无' not null,
    history_symptom     varchar(255) default '无' not null,
    previous_treatment  text                     null,
    is_western_medicine tinyint(1)   default 0   not null,
    allergy_history     varchar(255) default '无' not null,
    body_examination    varchar(255) default '无' not null,
    created_by_id       int                      not null,
    department_id       int                      not null,
    level_id            int                      not null comment '权限级别，参照常量表',
    is_pregnant         tinyint(1)   default 0   not null,
    constraint medical_record_template_user_id_fk
        foreign key (created_by_id) references user (id)
)
    charset = utf8;

create table if not exists payment
(
    id                  int auto_increment
        primary key,
    quantity            int        default 1   not null,
    unit_price          decimal(10, 2)         not null,
    create_time         datetime               not null,
    operator_id         int                    null comment '缴费员',
    patient_id          int                    not null,
    settlement_type_id  int                    null comment '参考常量表
结费类型：自费/医保等',
    payment_type_id     int        default 201 not null comment '费用类型：西药费/挂号费之类的 1为申请',
    item_id             int                    not null,
    invoice_id          int                    null,
    state               int        default 0   not null comment '0：形成订单，未缴费
1：已缴费
 2：已取药
3：还药未退款
4：已退款',
    project_operator_id int                    null,
    is_frozen           tinyint(1) default 0   not null,
    doctor_id           int                    null comment '开立药方或检查项目的医生或挂号对应的医生',
    constraint payment_invoice_id_fk
        foreign key (invoice_id) references invoice (id),
    constraint payment_patient_id_fk
        foreign key (patient_id) references patient (id),
    constraint payment_payment_type_id_fk
        foreign key (payment_type_id) references payment_type (id),
    constraint payment_user_id_fk
        foreign key (operator_id) references user (id),
    constraint payment_user_id_fk_2
        foreign key (project_operator_id) references user (id)
)
    comment 'item_type:处方，检查项目，挂号' charset = utf8;

create table if not exists registration
(
    id            int auto_increment
        primary key,
    need_book     tinyint(1) default 0 not null comment '是否需要病历本',
    patient_id    int                  not null,
    doctor_id     int                  not null,
    state         int                  not null comment '状态，参考常量表',
    schedule_id   int                  not null,
    registrar_id  int                  not null,
    age           int(3)               not null,
    create_time   datetime             null,
    sequence      int(3)               not null,
    serial_number int                  null,
    constraint registration_doctor_id_fk
        foreign key (doctor_id) references doctor (id),
    constraint registration_patient_id_fk
        foreign key (patient_id) references patient (id)
)
    charset = utf8;

create table if not exists medical_record
(
    id                  int auto_increment
        primary key,
    self_description    varchar(255)             not null,
    is_pregnant         tinyint(1)               not null,
    current_symptom     varchar(255)             not null,
    history_symptom     varchar(255) default '无' not null comment '无',
    previous_treatment  varchar(255) default '无' not null comment '无',
    is_western_medicine tinyint(1)               not null comment '是否为西医诊断',
    allergy_history     varchar(255) default '无' null comment '无',
    body_examination    varchar(255) default '无' null comment '无',
    registration_id     int                      not null,
    check_advice        varchar(255) default '无' not null comment '检察建议
',
    notification        varchar(255) default '无' not null comment '注意事项
',
    constraint medical_record_registration_id_fk
        foreign key (registration_id) references registration (id)
)
    comment '病历' charset = utf8;

create table if not exists schedule_rule
(
    id                    int auto_increment
        primary key,
    period                int              not null,
    registration_quantity int              not null,
    operator_id           int              null,
    doctor_id             int              null,
    registration_type_id  int              not null,
    day                   int(2) default 1 not null comment '周几 1-7',
    constraint schedule_rule_doctor_id_fk
        foreign key (doctor_id) references doctor (id),
    constraint schedule_rule_registration_type_id_fk
        foreign key (registration_type_id) references registration_type (id),
    constraint schedule_rule_user_id_fk
        foreign key (operator_id) references user (id)
)
    comment '排班规则' charset = utf8;

