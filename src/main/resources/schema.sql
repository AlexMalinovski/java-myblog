create table if not exists posts(
  id bigserial primary key,
  title varchar(256) not null,
  body text not null,
  image blob);

create table if not exists tags(
  name varchar(30) primary key);

create table if not exists posts_tags(
  post_id bigint not null references posts(id) on delete cascade,
  tag_name varchar(30) not null references tags(name) on delete cascade,
  constraint posts_tags_pk primary key (post_id, tag_name));

create table if not exists posts_reactions(
  post_id bigint not null references posts(id) on delete cascade,
  user_id bigint not null,
  type varchar(15) not null,
  constraint posts_reactions_pk primary key (post_id, user_id));

create table if not exists post_comments(
  id bigserial primary key,
  post_id bigint not null references posts(id) on delete cascade,
  body text not null);