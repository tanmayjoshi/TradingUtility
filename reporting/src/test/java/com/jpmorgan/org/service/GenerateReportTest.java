package com.jpmorgan.org.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmorgan.org.commons.Constants;
import com.jpmorgan.org.commons.TradeUtils;
import com.jpmorgan.org.exception.ReadingTradeDataException;
import com.jpmorgan.org.exception.TradingException;
import com.jpmorgan.org.modal.EntityData;
import com.jpmorgan.org.modal.TradeDataVO;
import static org.mockito.Mockito.*; 

public class GenerateReportTest {
	
	@InjectMocks
	GenerateReport generateReport;
	
	@Mock
	ObjectMapper objectMapper;
	
	@Mock
	TradeUtils tradeUtils;
	
    @Before
    public void setUp() throws Exception {

         MockitoAnnotations.initMocks(this);

    }

    static TradeDataVO dataVO = null;
    static EntityData entityData = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		dataVO = Mockito.mock(TradeDataVO.class);
		when(dataVO.getPricePerUnit()).thenReturn(102.0);
		when(dataVO.getAgreedFx()).thenReturn(0.90);
		when(dataVO.getUnits()).thenReturn(10);
		when(dataVO.getSettlementDate()).thenReturn("19 Oct 2018");
		when(dataVO.getCurrency()).thenReturn("SAR");
		when(dataVO.getEntityId()).thenReturn(1);
		when(dataVO.getByeSellFlag()).thenReturn("B");
		when(dataVO.isTradeSetteled()).thenReturn(true);
		when(dataVO.getUsdTradeAmt()).thenReturn(1000.1);
		
		
		entityData = Mockito.mock(EntityData.class);
		when(entityData.getBuyingPrice()).thenReturn(100.1);
		when(entityData.getSellingPrice()).thenReturn(100.1);
		when(entityData.getEntityId()).thenReturn(12);
		when(entityData.getEntityName()).thenReturn("BTC");
				
	}

	@Test//(expected = TradingException.class)
	public void testGenerateReport() throws Exception{
		try {
			List<TradeDataVO> tradeDataList = new ArrayList<>();
			
			when(dataVO.getSettlementDateTime()).thenReturn(new Date());
			tradeDataList.add(dataVO);
			Map<String, Map<Integer, EntityData>> settelementData =  new HashMap<>();
			Map<Integer, EntityData> map = new HashMap<>();
			EntityData entityMapMock = Mockito.mock(EntityData.class);
			map.put(1, entityMapMock);
			settelementData.put("test1", map);
			Mockito.when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.any(TypeReference.class))).thenReturn(tradeDataList,settelementData);
			generateReport.generateReport();
		} catch (Exception e) {
			e.printStackTrace();
			////fail("Something went wrong....");
			throw e;
		} 
	}

	@Test
	public void testIsTradeExecuted() {
		Map<String, Map<Integer, EntityData>> settelementData =  new HashMap<>();
		Map<Integer, EntityData> map = new HashMap<>();
		EntityData entityMapMock = Mockito.mock(EntityData.class);
		map.put(1, entityMapMock);
		settelementData.put("test1", map);
		
		Mockito.when(objectMapper.convertValue(Mockito.anyMap(), Mockito.any(TypeReference.class))).thenReturn(map);
		generateReport.isTradeExecuted(dataVO, settelementData);
	}


	@Test(expected= ReadingTradeDataException.class)
	public void testReadData() throws ReadingTradeDataException {
		//fail("Not yet implemented");
		generateReport.readData(""); 
	}
	 
	@Test
	public void testDisplayRank() throws IOException {
		List tradeDataList = new ArrayList<>();
		tradeDataList.add(dataVO);
//		Iterator listIterator = mock(Iterator.class);
//		when(listIterator.hasNext()).thenReturn(true,false);
//		when(listIterator.next()).thenReturn(dataVO);
//		when(tradeDataList.iterator()).thenReturn(listIterator);
			when(dataVO.getSettlementDateTime()).thenReturn(new Date());
			when(dataVO.isTradeSetteled()).thenReturn(true);
			//when(dataVO.getCurrency()).thenThrow(mock(TradingException.class));
			tradeDataList.add(dataVO);
		generateReport.displayRank(tradeDataList, "B");
	}
	
}
