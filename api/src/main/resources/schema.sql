use orders;

create table if not exists customers (
                                         id integer primary key auto_increment,
                                         name varchar(50) not null,
                                         surname varchar(50) not null,
                                         age integer not null,
                                         email varchar(50) not null
);

create table if not exists products (
                                        id integer primary key auto_increment,
                                        name varchar(50) not null,
                                        price decimal not null,
                                        category varchar(50) not null
);

create table if not exists orders (
                                      id integer primary key auto_increment,
                                      customer_id integer not null,
                                      product_id integer not null,
                                      quantity integer not null,
                                      order_date varchar(50) not null,
                                      foreign key (customer_id) references customers(id) on delete cascade on update cascade,
                                      foreign key (product_id) references products(id) on delete cascade on update cascade
);

create table if not exists users (
                                     id integer primary key auto_increment,
                                     username varchar(50) not null,
                                     email varchar(50) not null,
                                     password varchar(512) not null,
                                     role varchar(50) not null,
                                     is_active bool default false
);