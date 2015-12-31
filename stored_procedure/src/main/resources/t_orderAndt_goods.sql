--触发器(trigger)：监视某种情况，并触发某种操作。
--触发器创建语法四要素：1.监视地点(table) 2.监视事件(insert/update/delete)
--                  3.触发时间(after/before) 4.触发事件(insert/update/delete)

--语法：
--create trigger triggerName
--after/before insert/update/delete on 表名
--for each row   #这句话在mysql是固定的
--begin
--sql语句;
--end;

--触发器新建两个表
--商品表
CREATE TABLE t_goods (
  id int(11) NOT NULL AUTO_INCREMENT,
  goods_name varchar(255) DEFAULT NULL,
  num int(11) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--订单表
create table t_orders(
  id int(11) NOT NULL AUTO_INCREMENT,
  goods_id int(11) DEFAULT NULL,
  much int(11) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

insert into t_goods(goods_name,num) values('商品1',12),('商品2',10),('商品3',10),('商品4',10),('商品5',10);

--创建触发器插入数据（当我们执行插入订单操作的时候，数据库会自动更新商品数据）
create trigger afterInsertOrders after insert on t_orders for each row
begin
     update t_goods set num=num-3 where id=1;
end;

insert into t_orders(goods_id,much) values(1,3);--会发现商品1的数量变为9了，说明在我们插入一条订单的时候，触发器自动帮我们做了更新操作。

--但现在会有一个问题，因为我们触发器里面num和id都是写死的，所以不管我们买哪个商品，最终更新的都是商品1的数量。
--比如：我们往订单表再插入一条记录：insert into t_orders(goods_id,much) values(2,3),执行完后会发现商品1的数量变6了，
--而商品2的数量没变，这样显然不是我们想要的结果。我们需要改改我们之前创建的触发器。
--我们如何在触发器引用行的值，也就是说我们要得到我们新插入的订单记录中的goods_id或much的值。
--对于insert而言，新插入的行用new来表示，行中的每一列的值用new.列名来表示。

create trigger afterInsertOrders_new after insert on t_orders for each row
begin
   update t_goods set num=num-new.much where id=new.goods_id;--(注意关键字new)
end

--第二个触发器创建完毕，我们先把第一个触发器删掉
drop trigger afterInsertOrders;
--再来测试一下，插入一条订单记录:
insert into t_orders(goods_id,much) values(2,3);
--执行完发现商品2的数量变为7了，现在就对了。

--现在还存在两种情况：
--第一种情况：
--1.当用户撤销一个订单的时候，我们这边直接删除一个订单，我们是不是需要把对应的商品数量再加回去呢？
--2.当用户修改一个订单的数量时，我们触发器修改怎么写?
--我们先分析一下第一种情况：
--监视地点：t_orders表
--监视事件：delete
--触发时间：after
--触发事件：update
--对于delete而言：原本有一行,后来被删除，想引用被删除的这一行，用old来表示，old.列名可以引用被删除的行的值。
--那我们的触发器就该这样写：

create trigger afterDeleteUpdate after delete on t_orders for each row
begin
   update t_goods set num = num + old.much where id = old.goods_id;--(注意关键字old)
end;

delete from t_orders where id = 2;--会发现商品2的数量又变为10了。
--第二种情况
--监视地点：t_orders表
--监视事件：update
--触发时间：after
--触发事件：update
--对于update而言：被修改的行，修改前的数据，用old来表示，old.列名引用被修改之前行中的值；
--修改的后的数据，用new来表示，new.列名引用被修改之后行中的值。
--那我们的触发器就该这样写：
create trigger afterUpdate after update on t_orders for each row
begin
    update t_goods set num = num+old.much-new.much where id = old.goods_id/new.goods_id;
end;
--先把旧的数量恢复再减去新的数量就是修改后的数量了。
--测试下：先把商品表和订单表的数据都清掉，易于测试。
--假设我们往商品表插入三个商品，数量都是10，
--买3个商品1：
  insert into t_orders(goods_id,much) values(1,3);
--我们再修改插入的订单记录:
  update t_orders set much = 5 where id = 1;
--我们变为买5个商品1，这时候再查询商品表就会发现商品1的数量只剩5了，说明我们的触发器发挥作用了。

