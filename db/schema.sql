create table if not exists users (
    id serial primary key,
    name text,
    email text unique,
    password text
);

create table if not exists items (
    id serial primary key,
    description text,
    created timestamp,
    done integer,
    userId integer references users(id)
);