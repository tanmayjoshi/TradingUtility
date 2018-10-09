package com.jpmorgan.org.runner;

import java.io.IOException;

import com.jpmorgan.org.exception.ReadingTradeDataException;
import com.jpmorgan.org.exception.TradingException;
import com.jpmorgan.org.service.GenerateReport;

public class ReportRunner {

	public static void main(String[] args) {
		GenerateReport generateReport = new GenerateReport();
		try {
			generateReport.generateReport();
		} catch (TradingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadingTradeDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

}
