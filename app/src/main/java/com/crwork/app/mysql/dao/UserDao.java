package com.crwork.app.mysql.dao;

import com.crwork.app.mysql.CRWorkJDBC;
import com.crwork.app.domain.UserDomain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author xiezhenlin
 */
public class UserDao {

    private final static String TAG = "UserDao";

    private Connection mConnection = null;
    private CRWorkJDBC mCRWorkJDBC = null;

    public UserDao() {
        // TODO Auto-generated constructor stub
        mCRWorkJDBC = new CRWorkJDBC();
        mConnection = mCRWorkJDBC.getCRWorkConn();
        if (mConnection != null) {
            System.out.print(TAG + " is not null database!");
        } else {
            System.out.print(TAG + " is null database!");
        }
    }

    /**
     * get user list
     *
     * @return lmList
     */
    public ArrayList<String[]> getUserList() {
        ArrayList<String[]> lmList = new ArrayList<String[]>();
        try {
            Statement sql = mConnection.createStatement();
            ResultSet rs = sql.executeQuery(
                    "SELECT cu.ID,cu.userId,cu.userName,cu.regionID,cu.userType,cu.registeredDate,cu.psw,cc.id,cc.parent_id,cc.city_name_zh,cc.city_level,cc.city_code,cc.city_status_cr FROM crwork_user cu INNER JOIN crwork_citys cc On cu.regionID = cc.id");
            String[] mUserDomain = null;
            while (rs.next()) {
                mUserDomain = new String[13];
                mUserDomain[0] = String.valueOf(rs.getInt(1));
                mUserDomain[1] = rs.getString(2);
                mUserDomain[2] = rs.getString(3);
                mUserDomain[3] = String.valueOf(rs.getInt(4));
                mUserDomain[4] = String.valueOf(rs.getInt(5));
                mUserDomain[5] = String.valueOf(rs.getDate(6));
                mUserDomain[6] = rs.getString(7);
                mUserDomain[7] = String.valueOf(rs.getInt(8));
                mUserDomain[8] = String.valueOf(rs.getInt(9));
                mUserDomain[9] = rs.getString(10);
                mUserDomain[10] = String.valueOf(rs.getInt(11));
                mUserDomain[11] = rs.getString(12);
                mUserDomain[12] = String.valueOf(rs.getInt(13));
                lmList.add(mUserDomain);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                mConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lmList;
    }

    /**
     * add user data
     *
     * @param mUserDomain
     * @return
     */
    public boolean AddUser(UserDomain mUserDomain) {
        try {
            String sql = "insert into " + CRWorkJDBC.USER_TABLE + " (userId,userName,regionID,userType,registeredDate,psw)"
                    + "values(?,?,?,?,?,?)";
            PreparedStatement pst = mConnection.prepareStatement(sql);
            pst.setString(1, mUserDomain.getUserId());
            pst.setString(2, mUserDomain.getUserName());
            pst.setInt(3, mUserDomain.getRegionID());
            pst.setInt(4, mUserDomain.getUserType());
            pst.setDate(5, mUserDomain.getRegisteredDate());
            pst.setString(6, mUserDomain.getPsw());
            pst.executeUpdate();
            pst.close();
            mConnection.close();
            System.out.println(TAG + " AddUser()  success!" + "\n");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(TAG + " AddUser() failed!" + "\n");
            System.out.println(TAG + e + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG + " AddUser() failed!" + "\n");
            System.out.println(TAG + e + "\n");
        } finally {
            try {
                mConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(TAG + " AddUser() completed!" + "\n");
        }
        return false;
    }

    /**
     * update user data
     *
     * @param mUserDomain
     * @return
     */
    public Boolean ModifyUser(UserDomain mUserDomain) {
        PreparedStatement psql = null;
        try {
            psql = mConnection.prepareStatement("update " + CRWorkJDBC.LITTER_TYPE_TABLE
                    + " set typeName=?,typemark=?,price=? ,littertypeID=? where ID=?");
            psql.setString(1, mUserDomain.getUserId());
            psql.setString(2, mUserDomain.getUserName());
            psql.setInt(3, mUserDomain.getRegionID());
            psql.setInt(4, mUserDomain.getUserType());
            psql.setDate(5, mUserDomain.getRegisteredDate());
            psql.setString(6, mUserDomain.getPsw());
            psql.executeUpdate();
            psql.close();
            System.out.println(TAG + "ModifyUser() success!" + "\n");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(TAG + "ModifyUser() failed!" + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG + "ModifyUser() failed!" + "\n");
        } finally {
            try {
                mConnection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(TAG + "ModifyUser() completed!" + "\n");
        }
        return false;
    }

    /**
     * get userId by Name
     *
     * @param userName
     * @return
     */
    public int getUserIdByUserName(String userName) {
        // TODO Auto-generated method stub
        int UserId = 0;
        mConnection = mCRWorkJDBC.getCRWorkConn();
        try {
            String sql = "select userId from " + CRWorkJDBC.USER_TABLE + " where userName=?";
            PreparedStatement psmt = mConnection.prepareStatement(sql);
            psmt.setString(1, userName);
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                UserId = rs.getInt(1);
            }
            System.out.println("getUserIdByUserName()  success!" + UserId + "\n");
            mConnection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("getUserIdByUserName() completed!" + UserId + "\n");
        return UserId;
    }
}
