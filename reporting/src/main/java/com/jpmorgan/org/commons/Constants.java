package com.jpmorgan.org.commons;

public class Constants {
	
	public static final String SAUDI_RIYAL_CURRENCY= "SAR";
	public static final String UAE_CURRENCY = "AED";
	public static final String DATE_FORMAT_DD_MM_YYY = "dd MMM yyyy";
	public static final String SETTLEMENT = "Date : {0} Settlement : ${1}";
	public static final String ENTITY_USD_AMT = "Date: {0} Rank:{1} Entity : {2} --> USD amount of a trade : ${3}";
	
	public enum TradeType {
        BUY("B"), SELL("S");
        private String value;

        private TradeType(String value) {
                this.value = value;
        }
        public String getValue() {
        	return value;
        }
	}; 
	
	public enum ErrorCodes {
        TRADE001("Error while altering settelement data.....!"), 
        TRADE002("Error while reading trading data.......!");
        private String value;

        private ErrorCodes(String value) {
                this.value = value;
        }
        public String getValue() {
        	return value;
        }
	}; 

	
}
