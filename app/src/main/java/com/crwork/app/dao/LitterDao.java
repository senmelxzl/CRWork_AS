package com.crwork.app.dao;


import com.crwork.app.dbutil.CRWorkJDBC;
import com.crwork.app.domain.LitterDomain;
import com.crwork.app.util.DateUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LitterDao {
    private final static String TAG = "LitterDao";

    private Connection mConnection = null;
    private CRWorkJDBC mCRWorkJDBC = null;
    LitterTypeDao mLitterTypeDao = null;

    public LitterDao() {
        super();
        // TODO Auto-generated constructor stub
        mCRWorkJDBC = new CRWorkJDBC();
        mConnection = mCRWorkJDBC.getCRWorkConn();
    }

    /**
     * insert litter data
     *
     * @param mLitterModel
     * @return
     */
    public boolean insertLitterData(LitterDomain mLitterModel) {

        try {
            PreparedStatement psql;
            psql = mConnection.prepareStatement("insert into " + CRWorkJDBC.LITTER_TABLE
                    + " (userId,littertypeID,weight,tPrice,litterdate)" + "values(?,?,?,?,?)");
            psql.setString(1, mLitterModel.getUserId());
            psql.setInt(2, mLitterModel.getLittertypeID());
            psql.setDouble(3, mLitterModel.getWeight());
            psql.setDouble(4, mLitterModel.gettPrice());
            psql.setDate(5, mLitterModel.getLitterdate());
            psql.executeUpdate();
            psql.close();
            System.out.println("insertLitterData() insert data success!" + "\n");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("insertLitterData() function completed!" + "\n");
        }
        return false;
    }

    /**
     * query litter data
     *
     * @param userId
     * @return
     */
    public ArrayList<LitterDomain> queryLitterDataByUserID(String userId) {
        ArrayList<LitterDomain> lmList = new ArrayList<LitterDomain>();
        try {
            Statement sql = mConnection.createStatement();
            ResultSet rs = sql.executeQuery(
                    "SELECT * FROM " + CRWorkJDBC.LITTER_TABLE + (userId.equals("0") ? "" : " where userId=" + userId));
            LitterDomain mLitterModel = null;
            while (rs.next()) {
                mLitterModel = new LitterDomain();
                mLitterModel.setID(rs.getInt(1));
                mLitterModel.setUserId(rs.getString(2));
                mLitterModel.setLittertypeID(rs.getInt(3));
                mLitterModel.setWeight(rs.getDouble(4));
                mLitterModel.settPrice(rs.getDouble(5));
                mLitterModel.setLitterdate(rs.getDate(6));
                lmList.add(mLitterModel);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lmList;
    }

    /**
     * analysis txt content
     *
     * @param file
     * @return
     */
    public ArrayList<LitterDomain> readDatafromFile(File file) {
        // TODO Auto-generated method stub
        ArrayList<LitterDomain> list = new ArrayList<LitterDomain>();
        BufferedReader reader = null;
        String temp = null;
        mLitterTypeDao = new LitterTypeDao();
        int line = 1;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((temp = reader.readLine()) != null) {
                System.out.println(TAG + line + ":" + temp);
                LitterDomain mLitterModel = new LitterDomain();
                String[] list_temp = temp.split(" ");
                mLitterModel.setUserId(list_temp[0]);
                mLitterModel.setLittertypeID(Integer.parseInt(list_temp[1]));
                mLitterModel.setWeight(Double.parseDouble(list_temp[2]));
                mLitterModel.settPrice(mLitterTypeDao.getTPriceByLitterTypeId(Integer.parseInt(list_temp[1]))
                        * Double.parseDouble(list_temp[2]));
                mLitterModel.setLitterdate(DateUtil.getCurrentDate());
                list.add(mLitterModel);
                line++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * analysis txt content
     *
     * @param dataFile
     * @return
     */
    public ArrayList<LitterDomain> readDatafromFilePath(File dataFile) {
        // TODO Auto-generated method stub
        ArrayList<LitterDomain> list = new ArrayList<LitterDomain>();
        BufferedReader reader = null;
        String temp = null;
        int line = 1;
        try {
            reader = new BufferedReader(new FileReader(dataFile));
            while ((temp = reader.readLine()) != null) {
                System.out.println(TAG + line + ":" + temp);
                LitterDomain mLitterModel = new LitterDomain();
                String[] list_temp = temp.split(" ");
                mLitterModel.setUserId(list_temp[0]);
                mLitterModel.setLittertypeID(Integer.parseInt(list_temp[1]));
                mLitterModel.setWeight(Double.parseDouble(list_temp[2]));
                mLitterTypeDao = new LitterTypeDao();
                mLitterModel.settPrice(mLitterTypeDao.getTPriceByLitterTypeId(Integer.parseInt(list_temp[1]))
                        * Double.parseDouble(list_temp[2]));
                mLitterModel.setLitterdate(DateUtil.getCurrentDate());
                list.add(mLitterModel);
                line++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * for upload litter data from local file
     *
     * @param mLitterModelList
     * @return
     */
    public boolean uploadLitterlistData(ArrayList<LitterDomain> mLitterModelList) {
        for (int i = 0; i < mLitterModelList.size(); i++) {
            if (insertLitterData(mLitterModelList.get(i))) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * get litter by date and usernameF
     *
     * @param UserName
     * @param mStartDate
     * @param mEndDate
     * @return
     */
    public ArrayList<LitterDomain> getLitterListByDate(String UserName, Date mStartDate, Date mEndDate) {
        UserDao mUserDao = new UserDao();
        int UserId = mUserDao.getUserIdByUserName(UserName);
        ArrayList<LitterDomain> lmList = new ArrayList<LitterDomain>();
        try {
            Statement sql = mConnection.createStatement();
            ResultSet rs = sql.executeQuery("SELECT * FROM " + CRWorkJDBC.LITTER_TABLE
                    + (mStartDate == null || mEndDate == null ? ";"
                    : " where litterdate >= '" + mStartDate + "' and litterdate <= '" + mEndDate
                    + "' and userId = '" + UserId + "';"));
            LitterDomain mLitterModel = null;
            while (rs.next()) {
                mLitterModel = new LitterDomain();
                mLitterModel.setID(rs.getInt(1));
                mLitterModel.setUserId(rs.getString(2));
                mLitterModel.setLittertypeID(rs.getInt(3));
                mLitterModel.setWeight(rs.getDouble(4));
                mLitterModel.settPrice(rs.getDouble(5));
                mLitterModel.setLitterdate(rs.getDate(6));
                lmList.add(mLitterModel);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lmList;
    }

    /**
     * get litter data union
     *
     * @return
     */
    public ArrayList<String[]> exportLitterData() {
        ArrayList<String[]> mLitterList = new ArrayList<String[]>();
        try {
            Statement sql = mConnection.createStatement();
            ResultSet rs = sql.executeQuery(
                    "SELECT cu.userId,cu.userName,cc.city_name_zh,cl.weight,clt.typeName,cl.littertypeID,cl.tPrice,cl.litterdate FROM crwork_user cu INNER JOIN crwork_litter cl On cu.userId = cl.userId INNER JOIN crwork_litter_type clt On cl.littertypeID = clt.littertypeID INNER JOIN crwork_citys cc On cc.id = cu.regionID ;");
            String[] mLitter = null;
            while (rs.next()) {
                mLitter = new String[8];
                mLitter[0] = rs.getString(1);
                mLitter[1] = rs.getString(2);
                mLitter[2] = rs.getString(3);
                mLitter[3] = String.valueOf(rs.getDouble(4));
                mLitter[4] = rs.getString(5);
                mLitter[5] = String.valueOf(rs.getInt(6));
                mLitter[6] = String.valueOf(rs.getDouble(7));
                mLitter[7] = String.valueOf(rs.getDate(8));
                mLitterList.add(mLitter);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mLitterList;
    }

    /**
     * get litter data union
     *
     * @param UserName
     * @param mStartDate
     * @param mEndDate
     * @return
     */
    public ArrayList<String[]> exportLitterData(String UserName, String regionName, Date mStartDate, Date mEndDate) {
        ArrayList<String[]> mLitterList = new ArrayList<String[]>();
        try {
            Statement sql = mConnection.createStatement();
            ResultSet rs = sql.executeQuery(
                    "SELECT cu.userId,cu.userName,cc.city_name_zh,cl.weight,clt.typeName,cl.littertypeID,cl.tPrice,cl.litterdate FROM crwork_user cu INNER JOIN crwork_litter cl On cu.userId = cl.userId INNER JOIN crwork_litter_type clt On cl.littertypeID = clt.littertypeID INNER JOIN crwork_citys cc On cc.id = cu.regionID"
                            + (mStartDate == null || mEndDate == null ? ";" : " where cl.litterdate >= '" + mStartDate + "' and cl.litterdate <= '" + mEndDate
                            + (UserName == null || UserName.equals("") ? "" : "' and cu.userName = '" + UserName)
                            + (regionName == null || regionName.equals("") ? "" : "' and cc.city_name_zh = '" + regionName) + "';"));
            String[] mLitter = null;
            while (rs.next()) {
                mLitter = new String[8];
                mLitter[0] = rs.getString(1);
                mLitter[1] = rs.getString(2);
                mLitter[2] = rs.getString(3);
                mLitter[3] = String.valueOf(rs.getDouble(4));
                mLitter[4] = rs.getString(5);
                mLitter[5] = String.valueOf(rs.getInt(6));
                mLitter[6] = String.valueOf(rs.getDouble(7));
                mLitter[7] = String.valueOf(rs.getDate(8));
                mLitterList.add(mLitter);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(TAG + e + "\n");
        }
        return mLitterList;
    }

    public void CloseConnection() {
        try {
            mConnection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
