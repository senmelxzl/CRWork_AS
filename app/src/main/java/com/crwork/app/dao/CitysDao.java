package com.crwork.app.dao;

import com.crwork.app.dbutil.CRWorkJDBC;
import com.crwork.app.domain.CitysDomain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CitysDao {
    private final static String TAG = "CitysDao";

    private Connection mConnection = null;
    private CRWorkJDBC mCRWorkJDBC = null;

    public CitysDao() {
        super();
        // TODO Auto-generated constructor stub
        mCRWorkJDBC = new CRWorkJDBC();
        mConnection = mCRWorkJDBC.getCRWorkConn();
        if (mConnection != null) {
            System.out.print("xzl" + "not null database!");
        } else {
            System.out.print("xzl" + "is null database!");
        }
    }

    public ArrayList<CitysDomain> getCitys(int parent_id) {
        ArrayList<CitysDomain> lmList = new ArrayList<CitysDomain>();
        try {
            Statement sql = mConnection.createStatement();
            ResultSet rs = sql.executeQuery(
                    "SELECT * FROM " + CRWorkJDBC.CITY_TABLE + " where parent_id = " + parent_id);
            CitysDomain mCitysDomain = null;
            while (rs.next()) {
                mCitysDomain = new CitysDomain();
                mCitysDomain.setId(rs.getInt(1));
                mCitysDomain.setParent_id(rs.getInt(2));
                mCitysDomain.setCity_name_zh(rs.getString(3));
                mCitysDomain.setCity_name_en(rs.getString(4));
                mCitysDomain.setCity_level(rs.getInt(5));
                mCitysDomain.setCity_code(rs.getString(6));
                mCitysDomain.setCity_status_cr(rs.getInt(7));
                lmList.add(mCitysDomain);
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

    public boolean AddCitys(CitysDomain mCitysDomain) {
        try {
            String sql = "insert into " + CRWorkJDBC.CITY_TABLE + " (parent_id,city_name_zh,city_level,city_code,city_status_cr)"
                    + "values(?,?,?,?,?)";
            PreparedStatement pst = mConnection.prepareStatement(sql);
            pst.setInt(1, mCitysDomain.getParent_id());
            pst.setString(2, mCitysDomain.getCity_name_zh());
            pst.setInt(3, mCitysDomain.getCity_level());
            pst.setString(4, mCitysDomain.getCity_code());
            pst.setInt(5, mCitysDomain.getCity_status_cr());
            pst.executeUpdate();
            pst.close();
            System.out.println("AddCitys()  success!" + "\n");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("AddCitys() completed!" + "\n");
        }
        return false;
    }
}
