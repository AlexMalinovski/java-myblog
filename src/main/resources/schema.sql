create table if not exists posts(
  id bigserial primary key,
  title varchar(256) not null);