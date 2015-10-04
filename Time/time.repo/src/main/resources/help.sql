alter table phrase add dateByTen bigint not null;
alter table phrase add dateByTen3 int not null;
alter table phrase add dateByTen6 int not null;
alter table phrase add dateByTen9 int not null;

update phrase set dateByTen = date / 10;
update phrase set dateByTen3 = date / 10000;
update phrase set dateByTen6 = date / 10000000;
update phrase set dateByTen9 = date / 10000000000;