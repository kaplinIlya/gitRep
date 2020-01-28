insert into customer values (1, 'Ivanov', 30, 'Moscow', 1000);
insert into customer values (2, 'Petrov', 40, 'Ryazan', 2000);
insert into customer values (3, 'Sidorov', 30, 'Rostov', 3000);
insert into customer values (4, 'McGregor', 30, 'Dublin', 200000);
insert into customer values (5, 'Tyson', 30, 'Washington', 100000);

DELETE FROM user_authority;
DELETE FROM users;
DELETE FROM authority;

INSERT INTO users (`ID`, `USERNAME`, `PASSWORD`, `DATE_CREATED`)
    VALUES (1,'user','$2a$10$xKGFU4knzQHlDyNdc5pJyOvJXH0h4gE4LmmXIGSGurc3YOl7HfRmS','2015-11-15 22:14:54');
INSERT INTO users (`ID`, `USERNAME`, `PASSWORD`, `DATE_CREATED`)
    VALUES (2,'admin','$2a$10$EnZwl4w.FziSrtUASKYgmeX1ULvljq3Mks2.DvuVS5AG2RhzMG502','2015-10-15 22:14:54');

INSERT INTO authority (`name`, `id`) VALUES ('ROLE_USER', 1);
INSERT INTO authority (`name`, `id`) VALUES ('ROLE_ADMIN', 2);

INSERT INTO user_authority (`authority_id`, `user_id`) VALUES (1, 1);
INSERT INTO user_authority (`authority_id`, `user_id`) VALUES (2, 2);