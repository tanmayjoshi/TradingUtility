package com.jpmorgan.org.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmorgan.org.commons.Constants;
import com.jpmorgan.org.commons.Constants.TradeType;
import com.jpmorgan.org.commons.TradeUtils;
import com.jpmorgan.org.exception.ReadingTradeDataException;
import com.jpmorgan.org.exception.TradingException;
import com.jpmorgan.org.modal.EntityData;
import com.jpmorgan.org.modal.TradeDataVO;

public class GenerateReport {
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	public void generateReport() throws TradingException,IOException, ReadingTradeDataException {
		
		try {
				List<TradeDataVO> tradeDataList =  objectMapper.readValue(readData("TradeData.json"), new TypeReference<List<TradeDataVO>>(){});
				Map<String, Map<Integer, EntityData>> settelementData =  objectMapper.readValue(readData("SettelementData.json"), new TypeReference<Map<String, Map<Integer, EntityData>>>(){});
				formatReportData(tradeDataList,settelementData);
				displaySettlements(tradeDataList);
				displayEntitiesByRank(tradeDataList);
			} catch (TradingException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param tradeDataList
	 * 
	 * Alter the records and set USD Trade Amount & Settelement Date
	 */
	public void formatReportData(List<TradeDataVO> tradeDataList, Map<String, Map<Integer, EntityData>> settelementData ) {
		
		for(TradeDataVO data : tradeDataList) {
			
			data.setUsdTradeAmt(data.getPricePerUnit()*data.getUnits()*data.getAgreedFx()); 
			alterSettlementDate(data);
			data.setTradeSetteled(isTradeExecuted(data, settelementData));
			System.out.println(data);
		}
	}
	
	public boolean isTradeExecuted(TradeDataVO tradeDataVO, Map<String, Map<Integer, EntityData>> settelementData) {
		
		Map<Integer, EntityData>  entityDataList = objectMapper.convertValue( settelementData.get(tradeDataVO.getSettlementDate()),  new TypeReference<Map<Integer, EntityData>>(){});
		if(entityDataList != null) {
			EntityData entityData = entityDataList.get(tradeDataVO.getEntityId());
			if((TradeType.BUY.getValue().equalsIgnoreCase(tradeDataVO.getByeSellFlag()) && 
					tradeDataVO.getPricePerUnit() >= entityData.getBuyingPrice()) || 
				(TradeType.SELL.getValue().equalsIgnoreCase(tradeDataVO.getByeSellFlag()) && 
					tradeDataVO.getPricePerUnit() >= entityData.getSellingPrice())) {
				tradeDataVO.setTradeSetteled(true);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param tradeDataVO
	 * 
	 * Calculate Sttelement depending on the region and working days
	 * 
	 * SUN 0,MON 1,TUE 2, WED 3, THU 4, FRI 5, SAT 6
	 */
	@SuppressWarnings("deprecation")
	public void alterSettlementDate(TradeDataVO tradeDataVO) {
		final List<Integer> arabWeekDays = Arrays.asList(5,6);
		final List<Integer> englishWeekDays = Arrays.asList(0,6);
		

		try {
			Date date = TradeUtils.stringToDateFormatter(tradeDataVO.getSettlementDate(), Constants.DATE_FORMAT_DD_MM_YYY);
			final int dayOfSettlementDate = date.getDay();
			
			if((Constants.SAUDI_RIYAL_CURRENCY.equalsIgnoreCase(tradeDataVO.getCurrency()) ||
					Constants.UAE_CURRENCY.equalsIgnoreCase(tradeDataVO.getCurrency())) &&
					arabWeekDays.contains(dayOfSettlementDate)) {
				date = dayOfSettlementDate == 5 ?TradeUtils.addDays(date, 2): TradeUtils.addDays(date, 1);
			}else if((!Constants.SAUDI_RIYAL_CURRENCY.equalsIgnoreCase(tradeDataVO.getCurrency()) ||
					!Constants.UAE_CURRENCY.equalsIgnoreCase(tradeDataVO.getCurrency())) && 
					englishWeekDays.contains(dayOfSettlementDate)) {
				date = dayOfSettlementDate == 6 ?TradeUtils.addDays(date, 2): TradeUtils.addDays(date, 1);
			}
			tradeDataVO.setSettlementDateTime(date);
			tradeDataVO.setSettlementDate(TradeUtils.dateToStringFormatter(date, Constants.DATE_FORMAT_DD_MM_YYY));
			

		} catch (TradingException exception) {
			throw new TradingException(Constants.ErrorCodes.TRADE001.getValue(), exception );
		} 
		
	}
	
	/**
	 * 
	 * @param tradeDataList
	 * @param butSellFlag
	 * @return
	 * 
	 * Generate settlement report per day
	 * @throws IOException 
	 */
	public Map<String, Double> settlementPerday(List<TradeDataVO> tradeDataList, String butSellFlag) throws IOException {
		Map<String, Double> settlementPerDay = tradeDataList.stream()
				.filter(s -> s.isTradeSetteled() && s.getByeSellFlag().equalsIgnoreCase(butSellFlag) ).collect(Collectors.groupingBy(
						TradeDataVO::getSettlementDate, Collectors.summingDouble(TradeDataVO::getUsdTradeAmt)));
		String file = Constants.TradeType.BUY.getValue().equalsIgnoreCase(butSellFlag)?"D:/IncomingSettelements.txt":"D:/OutGoingSettelements.txt";
		 
        System.out.println("Writing to file: " + file);
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
             settlementPerDay.forEach((k,v)->{
				try {
					writer.write(MessageFormat.format(Constants.SETTLEMENT, k,v));
					writer.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
        }
		return settlementPerDay;
	}
	
	/**
	 * 
	 * @return
	 * 
	 * Read JSON file to read data
	 * @throws ReadingTradeDataException 
	 */
	public  byte[] readData(String fileName) throws ReadingTradeDataException{
		
        ClassLoader classLoader = new GenerateReport().getClass().getClassLoader();
        byte[] jsonData = null;
        if(classLoader.getResource(fileName) != null) {
	        File file = new File(classLoader.getResource(fileName).getFile());
			try {
				jsonData = Files.readAllBytes(file.toPath());
			} catch (IOException exception) {
				throw new ReadingTradeDataException(Constants.ErrorCodes.TRADE002.getValue(), exception );
			}
        }else {
        	throw new ReadingTradeDataException(Constants.ErrorCodes.TRADE002.getValue(),null );
        }
		return jsonData;
	}
	
	/**
	 * 
	 * @param tradeDataList
	 * 
	 * Display Daily Settlements
	 * @throws IOException 
	 */
	public void displaySettlements(List<TradeDataVO> tradeDataList) throws IOException {
		System.out.println("****************Incoming(Buy) Settelment****************");
		settlementPerday(tradeDataList,  TradeType.BUY.getValue()).forEach((k,v)->System.out.println(MessageFormat.format(Constants.SETTLEMENT, k,v)));
		System.out.println("");
		System.out.println("****************Outgoing(Sell) Settelment****************");
		settlementPerday(tradeDataList, TradeType.SELL.getValue()).forEach((k,v)->System.out.println(MessageFormat.format(Constants.SETTLEMENT, k,v)));
	}
	
	/**
	 * 
	 * @param tradeDataList
	 * 
	 * Display Entities By Rank
	 * @throws IOException 
	 */
	public void displayEntitiesByRank(List<TradeDataVO> tradeDataList) throws IOException {
		System.out.println("**************** Entities Ranked on Incoming(Buy) settlement****************");
		tradeDataList.sort(Comparator.comparingDouble(TradeDataVO::getUsdTradeAmt).reversed());
		displayRank(tradeDataList, TradeType.BUY.getValue());
		
		System.out.println("**************** Entities Ranked on Outgoing(Sale) settlement****************");
		displayRank(tradeDataList, TradeType.SELL.getValue());
	}
	
	public void displayRank(List<TradeDataVO> tradeDataList, String buySellFlag) throws IOException {
		
		 SortedMap<Date, List<TradeDataVO>> sortedMap =  new TreeMap<Date, List<TradeDataVO>>(); 
		 
		 tradeDataList.forEach(data -> {
			 if(data.isTradeSetteled() && buySellFlag.equalsIgnoreCase(data.getByeSellFlag())) {
				 List<TradeDataVO> tradeDataVOs = null;
				 if(sortedMap.get(data.getSettlementDateTime()) == null) {
					 tradeDataVOs = new ArrayList<>();		 
				 }else {
					 tradeDataVOs = sortedMap.get(data.getSettlementDateTime());
				 }
				 tradeDataVOs.add(data);
				 sortedMap.put(data.getSettlementDateTime(), tradeDataVOs);
			 }
		 });
		 String file = Constants.TradeType.BUY.getValue().equalsIgnoreCase(buySellFlag)?"D:/IncomingRankSettelements.txt":"D:/OutGoingRankSettelements.txt";
		 try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
		 sortedMap.forEach((key, value) -> {
			 AtomicInteger atomicInteger = new AtomicInteger(0);
			 value.forEach(g -> {
				 System.out.println(MessageFormat.format(Constants.ENTITY_USD_AMT,g.getSettlementDate(),atomicInteger.get(), g.getEntity(), g.getUsdTradeAmt()));
				 try {
					writer.write(MessageFormat.format("\n"+Constants.ENTITY_USD_AMT,g.getSettlementDate(),atomicInteger.incrementAndGet(), g.getEntity(), g.getUsdTradeAmt()));
				 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 });
		 });
		 }
		 
	}
	
}
