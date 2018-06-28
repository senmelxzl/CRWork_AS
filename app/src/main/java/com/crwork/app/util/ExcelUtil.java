package com.crwork.app.util;

import android.os.Environment;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelUtil {
    private String rootPath = Environment.getExternalStorageDirectory().getPath();

    public ExcelUtil() {
    }

    public boolean ExportLDToExcel(String ld_region, String ld_start_date_str, String ld_end_date_str, ArrayList<String[]> mLitterModelList) {
        String[] ldTitle = {"编号", "姓名", "区域", "重量(kg)", "类型", "类型标号", "费用-/收入+(元)", "日期"};
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");
        HSSFRow rowtitle = sheet.createRow(0);
        HSSFCell celltitle = rowtitle.createCell(0);
        celltitle.setCellValue(
                ld_region + "的垃圾数据统计表" + "(日期：" + ld_start_date_str + " 至  " + ld_end_date_str + ")");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        HSSFRow rowColume = sheet.createRow(1);
        for (int i = 0; i < ldTitle.length; i++) {
            rowColume.createCell(i).setCellValue(ldTitle[i]);
        }
        Double totalWeight = 0.00;
        Double totalWeight_R = 0.00;
        Double totalWeight_UR = 0.00;
        Double totalCost = 0.00;
        Double totalIncome = 0.00;
        Double totalEarnings = 0.00;
        for (int row = 2; row < mLitterModelList.size() + 2; row++) {
            HSSFRow rows = sheet.createRow(row);
            String cimark = "-";
            if (mLitterModelList.get(row - 2)[5].equals("0")) {
                totalCost = Arith.add(totalCost, Double.parseDouble(mLitterModelList.get(row - 2)[6]));
                totalWeight_UR = Arith.add(totalWeight_UR, Double.parseDouble(mLitterModelList.get(row - 2)[3]));
            } else {
                cimark = "+";
                totalIncome = Arith.add(totalIncome, Double.parseDouble(mLitterModelList.get(row - 2)[6]));
                totalWeight_R = Arith.add(totalWeight_R, Double.parseDouble(mLitterModelList.get(row - 2)[3]));
            }
            totalWeight = Arith.add(totalWeight, Double.parseDouble(mLitterModelList.get(row - 2)[3]));
            for (int col = 0; col < mLitterModelList.get(row - 2).length; col++) {
                // add data
                rows.createCell(col)
                        .setCellValue(((col == 6 ? cimark : "") + mLitterModelList.get(row - 2)[col]));
            }
        }

        System.out.println("总重量:" + totalWeight + "\n");
        System.out.println("废弃物重量:" + totalWeight_UR + "\n");
        System.out.println("可回收重量:" + totalWeight_R + "\n");
        System.out.println("总费用:" + totalCost + "\n");
        System.out.println("总收入:" + totalIncome + "\n");

        HSSFRow rowTotalWeight_UR = sheet.createRow(mLitterModelList.size() + 2);
        HSSFCell cellTotalWeight_UR = rowTotalWeight_UR.createCell(2);
        cellTotalWeight_UR.setCellValue("废弃物重量:");
        HSSFCell cellTotalWeight_UR_val = rowTotalWeight_UR.createCell(3);
        cellTotalWeight_UR_val.setCellValue(totalWeight_UR);
        HSSFCell cellTotalWeight_UR_val_str = rowTotalWeight_UR.createCell(4);
        cellTotalWeight_UR_val_str.setCellValue("kg/公斤");

        HSSFRow rowTotalWeight_R = sheet.createRow(mLitterModelList.size() + 3);
        HSSFCell cellTotalWeight_R = rowTotalWeight_R.createCell(2);
        cellTotalWeight_R.setCellValue("可回收重量:");
        HSSFCell cellTotalWeight_R_val = rowTotalWeight_R.createCell(3);
        cellTotalWeight_R_val.setCellValue(totalWeight_R);
        HSSFCell cellTotalWeight_R_val_str = rowTotalWeight_R.createCell(4);
        cellTotalWeight_R_val_str.setCellValue("kg/公斤");

        HSSFRow rowTotalWeight = sheet.createRow(mLitterModelList.size() + 4);
        HSSFCell cellTotalWeight = rowTotalWeight.createCell(2);
        cellTotalWeight.setCellValue("总重量:");
        HSSFCell cellTotalWeight_val = rowTotalWeight.createCell(3);
        cellTotalWeight_val.setCellValue(totalWeight);
        HSSFCell cellTotalWeight_val_str = rowTotalWeight.createCell(4);
        cellTotalWeight_val_str.setCellValue("kg/公斤");

        HSSFCell cellTotalCost = rowTotalWeight_UR.createCell(5);
        cellTotalCost.setCellValue("总费用:");
        HSSFCell cellTotalCost_val = rowTotalWeight_UR.createCell(6);
        cellTotalCost_val.setCellValue(totalCost);
        HSSFCell cellTotalCost_val_str = rowTotalWeight_UR.createCell(7);
        cellTotalCost_val_str.setCellValue("元");

        HSSFCell cellTotalIncome = rowTotalWeight_R.createCell(5);
        cellTotalIncome.setCellValue("总收入:");
        HSSFCell cellTotalIncome_val = rowTotalWeight_R.createCell(6);
        cellTotalIncome_val.setCellValue(totalIncome);
        HSSFCell cellTotalIncome_val_str = rowTotalWeight_R.createCell(7);
        cellTotalIncome_val_str.setCellValue("元");

        HSSFCell cellTotalEarnings = rowTotalWeight.createCell(5);
        if (totalCost > totalIncome) {
            totalEarnings = Arith.sub(totalCost, totalIncome);
            cellTotalEarnings.setCellValue("费用支出:");
            System.out.println("费用支出:" + Arith.sub(totalCost, totalIncome) + "\n");
        } else {
            totalEarnings = Arith.sub(totalIncome, totalCost);
            cellTotalEarnings.setCellValue("盈利收入:");
            System.out.println("盈利收入:" + Arith.sub(totalIncome, totalCost) + "\n");
        }
        HSSFCell cellTotalEarnings_val = rowTotalWeight.createCell(6);
        cellTotalEarnings_val.setCellValue(totalEarnings);
        HSSFCell cellTotalEarnings_val_str = rowTotalWeight.createCell(7);
        cellTotalEarnings_val_str.setCellValue("元");

        File xlsFile = new File(
                rootPath + File.separator + "LDExport-" + DateUtil.getCurrentDate().toString() + ".xls");
        System.out.println("导出数据名称：" + xlsFile.getName());
        FileOutputStream xlsStream = null;
        try {
            xlsStream = new FileOutputStream(xlsFile);
            workbook.write(xlsStream);
            return true;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return false;
    }
}
