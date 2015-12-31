package com.wa.edu.service.Impl;

import com.wa.edu.domain.User;
import com.wa.edu.service.UserService;
import com.wa.edu.utils.DBUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 15-11-6.
 **/
@Configuration
@Repository(value = "userServices")
public class UserServicesImpl implements UserService{


    @Override
    public int insertUser(User user) {
        int resultCode = 0;
        Connection conn=null;
        try {
            conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("insert into t_user(username,userpass) values(?,md5(?))");
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getUserpass());
            resultCode = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DBUtil.closeConnection(conn);
        }
        return resultCode;
    }


    @Override
    public List<User> selectAllUsers() {
        ArrayList<User> allUsers = new ArrayList<User>();
        ResultSet rs = null;
        Connection conn=null;
        try {
            conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from t_user");
            rs = pstmt.executeQuery();
            User user = null;
            while(rs.next()){
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("userpass"));
                allUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBUtil.closeConnection(conn);
        }
        return allUsers;
    }





    public static void main(String[] args) {
        UserService userDAO = new UserServicesImpl();
        for(User u:userDAO.selectAllUsers()){
            System.out.println(u);
        }
    }
}
