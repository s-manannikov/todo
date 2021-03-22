create table if not exists users (
    id serial primary key,
    name text,
    email text unique,
    password text
);

create table if not exists categories (
    id serial primary key,
    name text
);

create table if not exists items (
    id serial primary key,
    description text,
    created timestamp,
    done integer,
    userId integer references users(id),
    categoryId integer references categories(id)
);

insert into categories(id, name) values (1, 'category 1');
insert into categories(id, name) values (2, 'category 2');
insert into categories(id, name) values (3, 'category 3');
insert into categories(id, name) values (4, 'category 4');
insert into categories(id, name) values (5, 'category 5');