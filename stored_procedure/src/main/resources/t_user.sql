
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------

-- Table structure for t_user

-- ----------------------------

DROP TABLE IF EXISTS t_user;

CREATE TABLE t_user (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(20) DEFAULT NULL,
  userpass varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------

-- Records of t_user

-- ----------------------------

INSERT INTO t_user VALUES ('1', 'casper', '514612121');
INSERT INTO t_user VALUES ('2', 'test', '12341212');
INSERT INTO t_user VALUES ('3', 'test1', '1111121');
INSERT INTO t_user VALUES ('4', 'haha ', '11112121');
INSERT INTO t_user VALUES ('5', 'heheh', '948fab37aeaa563cfa7f009d836ead8d');
INSERT INTO t_user VALUES ('6', 'haha', '4e4d6c332b6fe62a63afe56171fd3725');
INSERT INTO t_user VALUES ('7', 'maxingping', '2b9f6e07e9fbea86b486d207a9eee40e');
INSERT INTO t_user VALUES ('8', 'hello', 'hello212121');
INSERT INTO t_user VALUES ('9', 'zhangqing', 'zhangqing212');

CREATE  PROCEDURE addUser(IN p_name varchar(20),IN p_pass varchar(20))
BEGIN
	insert into t_user(username,userpass) values(p_name,p_pass);
END

CREATE  PROCEDURE selectAllUser()
BEGIN
	SELECT id,username,userpass from t_user;
END

CREATE  PROCEDURE selectUserById(IN id int)
BEGIN
	  SELECT * from t_user t where t.id =id;
END

--MD5加密插入数据并返回插入的最大id
CREATE  PROCEDURE (IN p_name varchar(20),IN p_pass varchar(20),out parout int)
BEGIN
     INSERT INTO t_user(username,userpass) VALUES(p_name,MD5(p_pass));
     SELECT MAX(id) INTO parout FROM t_user;
END;

--进行查询所有数据并进行插入数据（缺少插入数据统计的数量）（2015-11-6）
CREATE PROCEDURE getUserAndInsert()
BEGIN
    DECLARE c INT;
    DECLARE t_name,t_pass varchar(100);
    DECLARE b INT  DEFAULT 0;
    DECLARE cur_1 CURSOR FOR SELECT username ,userpass  FROM t_user;
    SELECT count(*) INTO c FROM t_user;
    OPEN cur_1;
    REPEAT
        FETCH  cur_1 INTO t_name,t_pass;
        INSERT INTO t_user(username,userpass) VALUES(t_name, t_pass);
        SET b=b+1;
    UNTIL b>=c
    END REPEAT;
    CLOSE cur_1;
END;

--进行查询所有数据并进行插入数据并统计和最大Id（2015-11-7 14:23）
CREATE PROCEDURE getUserAndInsertAndCount(OUT statistics INT,OUT maxId INT)
BEGIN
    DECLARE c INT;
    DECLARE t_name,t_pass varchar(100);
    DECLARE b INT  DEFAULT 0;
    DECLARE cur_1 CURSOR FOR SELECT username ,userpass  FROM t_user;
    SELECT count(*) INTO c FROM t_user;
    OPEN cur_1;
    SET statistics=1;
    REPEAT
        FETCH  cur_1 INTO t_name,t_pass;
        INSERT INTO t_user(username,userpass) VALUES(t_name, t_pass);
        SET b=b+1;
        SET statistics=statistics+1;
    UNTIL b>=c
    END REPEAT;
    SELECT MAX(id) INTO maxId FROM t_user;
    SELECT statistics;
    CLOSE cur_1;
END;


--此存储过程是查询所有记录根据id是不是奇数来更新数据（t_id mod 2会返回1或者0 直接得出真假） （2015-11-7 17:07）
drop PROCEDURE getUserAndUpdateAndCount;
CREATE PROCEDURE getUserAndUpdateAndCount(OUT statistics INT,OUT maxId INT)
BEGIN
    DECLARE t_id,c INT;
    DECLARE t_name,t_pass varchar(100);
    DECLARE b INT  DEFAULT 0;
    DECLARE cur_1 CURSOR FOR SELECT id,username ,userpass FROM t_user ;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET t_id = NULL;--是为了解决no data - zero rows fetched,selected,or processed而引入的
    SELECT count(*) INTO c FROM t_user;
    OPEN cur_1;
    SET statistics=0;
    REPEAT
        FETCH  cur_1 INTO t_id,t_name,t_pass;
        IF (t_id mod 2)  THEN
            UPDATE t_user SET username:=concat(t_name,'qq'), userpass:=substring(t_pass,1,6)  WHERE id=t_id;
            SET statistics=statistics+1;
        ELSE
            SET b=b+1;
        END IF ;
    UNTIL b>=c
    END REPEAT;
    SELECT MAX(id) INTO maxId FROM t_user;
    SELECT statistics;
    CLOSE cur_1;
END;





