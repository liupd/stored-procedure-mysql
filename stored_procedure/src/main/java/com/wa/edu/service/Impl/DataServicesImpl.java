package com.wa.edu.service.Impl;

import com.wa.edu.service.DataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 15-11-5.
 **/
@Configuration
@Repository(value = "dataService")
public class DataServicesImpl implements DataService{

    private static Logger logger = Logger.getLogger(DataServicesImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String,Object>> getAllPlan() {
        String planSql="select * from teaching_plan";
        List<Map<String,Object>> list =jdbcTemplate.queryForList(planSql);
        if(list.size()==0){
            return null;
        }
        return list;
    }


    public int getMaxUserId(){
        String sql = "{call getMaxUser(?,?,?)}";
        Object obj = jdbcTemplate.execute(sql,new CallableStatementCallback(){
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                cs.setString(1,"zhiyingang");
                cs.setString(2,"zhiyingang");
                cs.registerOutParameter(3,Types.INTEGER,"parout");
                cs.execute();
                return new Integer(cs.getInt("parout"));
            }
        });
        int taskIdResult=((Integer)obj).intValue();
        return taskIdResult;
    }


}
