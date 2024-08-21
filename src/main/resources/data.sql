insert into user_details(id,birth_date,name) values(10001, current_date(), 'Krupa');
insert into user_details(id,birth_date,name) values(10002, current_date(), 'Deepika');
insert into user_details(id,birth_date,name) values(10003, current_date(), 'Anu');

insert into post (id, description, user_id) 
values (20001, 'I am learning Spring Boot', 10001);
insert into post (id, description, user_id) 
values (20002, 'I will learn Kubernetes', 10001);
insert into post (id, description, user_id) 
values (20003, 'I am learning Spring Boot', 10002);
insert into post (id, description, user_id) 
values (20004, 'What?!', 10003);