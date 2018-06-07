package com.crwork.app.dao;

import com.crwork.app.dbutil.CRWorkJDBC;
import com.crwork.app.domain.LitterTypeDomain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LitterTypeDao {

    private final static String TAG = "LitterTypeDao";

    private Connection mConnection = null;
    private CRWorkJDBC mCRWorkJDBC = null;

    public LitterTypeDao() {
        super();
        // TODO Auto-generated constructor stub
        mCRWorkJDBC = new CRWorkJDBC();
        mConnection = mCRWorkJDBC.getCRWorkConn();
        if (mConnection != null) {
            System.out.print(TAG + " is not null database!");
        } else {
            System.out.print(TAG + " is null database!");
        }
    }

    public ArrayList<LitterTypeDomain> getLitterTypes() {
        ArrayList<LitterTypeDomain> lmList = new ArrayList<LitterTypeDomain>();
        try {
            Statement sql = mConnection.createStatement();
            ResultSet rs = sql.executeQuery(
                    "SELECT * FROM " + CRWorkJDBC.LITTER_TYPE_TABLE);
            LitterTypeDomain mLitterTypeDomain = null;
            while (rs.next()) {
                mLitterTypeDomain = new LitterTypeDomain();
                mLitterTypeDomain.setID(rs.getInt(1));
                mLitterTypeDomain.setLittertypeID(rs.getInt(2));
                mLitterTypeDomain.setTypeName(rs.getString(3));
                mLitterTypeDomain.setTypemark(rs.getInt(4));
                mLitterTypeDomain.setPrice(rs.getDouble(5));
                lmList.add(mLitterTypeDomain);
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

    public boolean AddLitterType(LitterTypeDomain mLitterTypeDomain) {
        try {
            String sql = "insert into " + CRWorkJDBC.LITTER_TYPE_TABLE + " (littertypeID,typeName,typemark,price)"
                    + "values(?,?,?,?)";
            PreparedStatement pst = mConnection.prepareStatement(sql);
            pst.setInt(1, mLitterTypeDomain.getLittertypeID());
            pst.setString(2, mLitterTypeDomain.getTypeName());
            pst.setInt(3, mLitterTypeDomain.getTypemark());
            pst.setDouble(4, mLitterTypeDomain.getPrice());
            pst.executeUpdate();
            pst.close();
            mConnection.close();
            System.out.println(TAG + " AddLitterType()  success!" + "\n");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(TAG + e + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG + e + "\n");
        } finally {
            try {
                mConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(TAG + " AddLitterType() completed!" + "\n");
        }
        return false;
    }

    /**
     * update litter type data
     *
     * @param mLitterTypeDomain
     * @return
     */
    public Boolean ModifyLitterType(LitterTypeDomain mLitterTypeDomain) {
        PreparedStatement psql = null;
        try {
            psql = mConnection.prepareStatement("update " + CRWorkJDBC.LITTER_TYPE_TABLE
                    + " set typeName=?,typemark=?,price=? ,littertypeID=? where ID=?");
            psql.setString(1, mLitterTypeDomain.getTypeName());
            psql.setInt(2, mLitterTypeDomain.getTypemark());
            psql.setDouble(3, mLitterTypeDomain.getPrice());
            psql.setInt(4, mLitterTypeDomain.getLittertypeID());
            psql.setInt(5, mLitterTypeDomain.getID());
            psql.executeUpdate();
            psql.close();
            System.out.println(TAG + "updateLitterTypeData() success!" + "\n");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mConnection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(TAG + "updateLitterTypeData() completed!" + "\n");
        }
        return false;
    }
}
