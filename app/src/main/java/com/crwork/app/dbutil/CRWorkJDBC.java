package com.crwork.app.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DB class
 *
 * @author Amao
 */
public class CRWorkJDBC {
    private final static String TAG = "CRWorkJDBC";
    // Load DB driver com.mysql.jdbc.Driver
    private static String dbdriver = "com.mysql.jdbc.Driver";
    // DB name
    private static String database = "crwork";
    // DB login user name
    private static String username = "root";
    // DB login password
    private static String userpassword = "xzl198819";
    // Load DB address
    private static String dburl = "jdbc:mysql://66.98.126.237:3306/" + database
            + "?useUnicode=true&amp;characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";

    private static Connection conn = null;
    private static Statement stat = null;
    /**
     * DB table name
     */
    public final static String USER_TABLE = "crwork_user";
    public final static String LITTER_TABLE = "crwork_litter";
    public final static String LITTER_TYPE_TABLE = "crwork_litter_type";
    public final static String CITY_TABLE = "crwork_citys";

    /**
     * DB table create SQL
     */
    public final static String USER_TABLE_SQL = "create table " + USER_TABLE
            + "(ID int auto_increment primary key not null,userId varchar(20) not null,userName varchar(20),regionID int not null,userType int not null,registeredDate date not null,psw varchar(20) not null)";

    public final static String LITTER_TABLE_SQL = "create table " + LITTER_TABLE
            + "(ID int auto_increment primary key not null,userId int not null,littertypeID int not null,weight double(16,2) not null,tPrice double(16,2) not null,litterdate date not null)";

    public final static String LITTER_TYPE_TABLE_SQL = "create table " + LITTER_TYPE_TABLE
            + "(ID int auto_increment primary key not null,littertypeID int not null,typeName varchar(20) not null,typemark int not null,price double(16,2) not null)";

    public final static String CITY_TABLE_SQL = "create table " + CITY_TABLE
            + "(id int auto_increment primary key not null,parent_id int default null,city_name_zh varchar(20),city_name_en varchar(20),city_level int(11) not null,city_code char(12) not null,city_status_cr int(11) not null)";

    public CRWorkJDBC() {
        try {
            Class.forName(dbdriver).newInstance();
            conn = DriverManager.getConnection(dburl, username, userpassword);
            stat = conn.createStatement();
            validateTableNameExist(USER_TABLE);
            validateTableNameExist(LITTER_TABLE);
            validateTableNameExist(LITTER_TYPE_TABLE);
            validateTableNameExist(CITY_TABLE);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            System.out.println(TAG + e1);
            e1.printStackTrace();
        } catch (SQLException e) {
            System.out.println(TAG + e);

        }
    }

    /**
     * get DB Connection
     *
     * @return Connection
     */
    public Connection getCRWorkConn() {
        return conn;
    }

    /**
     * check table exist
     *
     * @param tableName
     * @return
     */
    public static boolean validateTableNameExist(String tableName) {
        boolean vtne = false;
        ResultSet rs;
        try {
            rs = conn.getMetaData().getTables(null, null, tableName, null);
            if (rs.next()) {
                vtne = true;
            } else {
                vtne = createtable(tableName);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println(TAG + e);
            e.printStackTrace();
        }
        return vtne;
    }

    /**
     * create table
     */
    private static boolean createtable(String tableName) {
        // TODO Auto-generated method stub
        String SQL_TABLE = "";
        if (tableName.equals(USER_TABLE)) {
            SQL_TABLE = USER_TABLE_SQL;
        } else if (tableName.equals(LITTER_TABLE)) {
            SQL_TABLE = LITTER_TABLE_SQL;
        } else if (tableName.equals(LITTER_TYPE_TABLE)) {
            SQL_TABLE = LITTER_TYPE_TABLE_SQL;
        } else if (tableName.equals(CITY_TABLE)) {
            SQL_TABLE = CITY_TABLE_SQL;
        }
        if (!SQL_TABLE.equals("")) {
            System.out.print(tableName + " table is not exist! \n");
            try {
                System.out.print(tableName + " table start to create! \n");
                stat.executeUpdate(SQL_TABLE);
                System.out.print(tableName + " table is Created! \n");
                return true;
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }
}
