package com.vccloud.portal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import jxl.Cell;
import jxl.JXLException;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.vccloud.portal.db.model.TExtVidyoMember;

public class AnalyExcleUtil {
	@SuppressWarnings("unused")
	public static TExtVidyoMember[] analyExcle(File file) {
		FileInputStream is;
		TExtVidyoMember[] member = null;
		
		try {
			is = new FileInputStream(file);
			Workbook rwb = Workbook.getWorkbook(is);
			Sheet sh = rwb.getSheet(0);
			int rowNum = sh.getRows();
			int colNum = sh.getColumns();
			// List ml=new ArrayList();
			member = new TExtVidyoMember[rowNum - 1];
			int p;
			for (p = 1; p < rowNum; p++) {
				member[p - 1] = new TExtVidyoMember();
				Cell[] ce = sh.getRow(p);
				// System.out.println(ce.length);
				member[p - 1].setDescription(ce[0].getContents().toString());
				member[p - 1].setDisplayname(ce[1].getContents().toString());
				member[p - 1].setEmail(ce[2].getContents().toString());
				member[p - 1].setExtension(ce[3].getContents().toString());
				member[p - 1].setGroupname(ce[4].getContents().toString());
				member[p - 1].setLanguage(ce[5].getContents().toString());
				member[p - 1].setLocationtag(ce[6].getContents().toString());
				member[p - 1].setName(ce[7].getContents().toString());
				member[p - 1].setPassword(ce[8].getContents().toString());
				member[p - 1].setProxyname(ce[9].getContents().toString());
				member[p - 1].setRolename(ce[10].getContents().toString());
			}
			rwb.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return member;

	}

	public static String getPath() {
		return System.getProperty("user.home").replace('\\', '/')
				+ "/data/upload";

	}
}
