package com.liferay.testmap.Util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import java.io.InputStream;

public class UtilExcel {

    public Serializable readFileExcel(InputStream inputStream, String fileName) throws IOException {
        
        Workbook workbook;

        if (fileName.contains(".xls")) {
            workbook = new XSSFWorkbook(inputStream);
        }else {
            workbook = new HSSFWorkbook(inputStream);
        }

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        Sheet sheetSummary = workbook.getSheetAt(5);

        Iterator<Row> rowIterator = sheetSummary.iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                switch (cell.getCellType()) {
                    case BOOLEAN:
                        return cell.getBooleanCellValue();
                    case NUMERIC:
                         return cell.getNumericCellValue();
                    case STRING:
                        return cell.getStringCellValue();
                    case FORMULA:
                        getFormulaResulte(evaluator, cell);

                }
            }
        }
            

        return null;
    }
    
    public Serializable getFormulaResulte(FormulaEvaluator evaluator, Cell cell){

        CellValue cellValue = evaluator.evaluate(cell);

        switch (cellValue.getCellType()) {
            case BOOLEAN:
                return cellValue.getBooleanValue();
            case NUMERIC:
                return cellValue.getNumberValue();
            case STRING:
                return cellValue.getStringValue();
        }

        return false;
    }
}
