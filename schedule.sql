CREATE Table planner (
                         id Bigint AUTO_INCREMENT PRIMARY KEY not null,
                         task VARCHAR(200) not null,
                         user_id bigint not null,
                         `password` varchar(100) not null,
                         created_at DATE not null,
                         updated_at DATE not null,
                         FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

create table user
(
    user_id    bigint auto_increment primary key,
    writer     varchar(100) not null,
    email      varchar(100) not null,
    created_at date         not null,
    updated_at date         not null
);

