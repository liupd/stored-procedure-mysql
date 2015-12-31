package com.wa.edu.service.Impl;

import com.wa.edu.domain.User;
import com.wa.edu.utils.DBUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by dell on 15-11-6.
 **/
public class UserServicesImplByProcedure {

    public ArrayList<User> selectAllUser(){
        ArrayList<User> allUsers= new ArrayList<User>();
        ResultSet rs = null;
        Connection conn=null;
        try {
            conn = DBUtil.getConnection();
            CallableStatement callstmt = conn.prepareCall("{call selectAllUser()}");//注意调用的语法
            rs= callstmt.executeQuery();
            User user =null;
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


    public User selectUserById(int id){
        User user = null;
        ResultSet rs = null;
        Connection conn=null;
        try {
            conn = DBUtil.getConnection();
            CallableStatement callstmt = conn.prepareCall("{call selectUserById(?)}");
            callstmt.setInt(1, id);				//设置参数

            rs= callstmt.executeQuery();
            while(rs.next()){
                user = new User(rs.getInt("id"), rs.getString("username"),rs.getString("userpass"));
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
        return user;
    }

    public int adUser(User user){
        int resultCode = 0;
        Connection conn=null;
        try {
            conn = DBUtil.getConnection();
            CallableStatement callstmt = conn.prepareCall("{call addUser(?,?)}");
            callstmt.setString(1, user.getUsername());
            callstmt.setString(2, user.getUserpass());
            resultCode = callstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DBUtil.closeConnection(conn);
        }
        return resultCode;
    }


    public int getMaxUserId(User user){
        int resultCode = 0;
        Connection conn=null;
        try {
            conn = DBUtil.getConnection();
            CallableStatement callstmt = conn.prepareCall("{call getMaxUser(?,?,?)}");
            callstmt.setString(1, user.getUsername());
            callstmt.setString(2, user.getUserpass());
            callstmt.registerOutParameter(3, java.sql.Types.INTEGER);//注册存储过程的out型参数类型；使用之前必须注册；
            callstmt.execute();
            resultCode= Integer.valueOf(callstmt.getString(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DBUtil.closeConnection(conn);
        }
        return resultCode;
    }


    public int  getUserAndInsert(){
        int resultCode = 0;
        Connection conn=null;
        try {
            conn = DBUtil.getConnection();
            CallableStatement callstmt = conn.prepareCall("{call getUserAndInsert()}");
            resultCode = callstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DBUtil.closeConnection(conn);
        }
        return resultCode;
    }

    public Map getUserAndInsertAndCount(){
        Map<String,Object> map=new HashMap<String, Object>();
        Connection conn=null;
        try {
            conn = DBUtil.getConnection();
            CallableStatement callstmt = conn.prepareCall("{call getUserAndInsertAndCount(?,?)}");
            callstmt.registerOutParameter(1, java.sql.Types.INTEGER);//注册存储过程的out型参数类型；使用之前必须注册；
            callstmt.registerOutParameter(2, java.sql.Types.INTEGER);//注册存储过程的out型参数类型；使用之前必须注册；
            callstmt.execute();
            map.put("insertNum",Integer.valueOf(callstmt.getString(1)));
            map.put("maxId",Integer.valueOf(callstmt.getString(2)));
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DBUtil.closeConnection(conn);
        }
        return map;
    }

    public Map getUserAndUpdateAndCount(){
        Map<String,Object> map=new HashMap<String, Object>();
        Connection conn=null;
        try {
            conn = DBUtil.getConnection();
            CallableStatement callstmt = conn.prepareCall("{call getUserAndUpdateAndCount(?,?)}");
            callstmt.registerOutParameter(1, java.sql.Types.INTEGER);//注册存储过程的out型参数类型；使用之前必须注册；
            callstmt.registerOutParameter(2, java.sql.Types.INTEGER);//注册存储过程的out型参数类型；使用之前必须注册；
            callstmt.execute();
            map.put("updateNum",Integer.valueOf(callstmt.getString(1)));
            map.put("maxId",Integer.valueOf(callstmt.getString(2)));
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DBUtil.closeConnection(conn);
        }
        return map;
    }


    public static void main(String[] args) {
        UserServicesImplByProcedure byProcedure = new UserServicesImplByProcedure();
        Map<String,Object> map=byProcedure.getUserAndUpdateAndCount();
        for(String key : map.keySet()){
            System.out.println("key= "+ key + " and value= " + map.get(key));
        }
    }
}
