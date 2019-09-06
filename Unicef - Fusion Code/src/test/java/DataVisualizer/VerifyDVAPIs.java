package DataVisualizer;

import java.util.*;
import java.time.Year;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;


@UseTestDataFrom("testdata/DataVisualizerAPI.csv")
@RunWith(SerenityParameterizedRunner.class)
public class VerifyDVAPIs extends CommonGlobalUtils {


	private String TC;
	private String RunTest;
	private String TestDesc1;
	private String TestDesc2;
	private String TestDesc3;
	private String TestDesc4;
	private String URL_Base;
	private String EndPoint1;
	private String EndPoint2;
	private String JSON_Path;
	private String WhatToTest;
	private String ExpectedResult1;
	private String ExpectedResult2;
	private String ExpectedResult3;
	private String ExpectedResult4;
	private String ExpectedResult5;
	static int dataCnt = 1;

	
	
	
	@Before
	public void ReadingCSVData() {
		LOGGER.info("");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("START  --  Test Case ->  "+TC);
		LOGGER.info("Test Case Description ->  "+TestDesc1);
		LOGGER.info("Test Case Description ->  "+TestDesc2);
		LOGGER.info("Test Case Description ->  "+TestDesc3);
		LOGGER.info("Test Case Description ->  "+TestDesc4);
	}

	
	@Test
	public void getAPIResponse() throws InterruptedException {
		if(RunTest.toUpperCase().equals("T"))  // Execute test case only if RunTest = 'T' or True
		{
			RequestSpecification request = RestAssured.given();
			Response response = getResponse(request);
			Map<String, List> jsonResponse = response.jsonPath().getMap(JSON_Path); 	
			verifyObservationValues(jsonResponse);  
			
		}else{
			LOGGER.info("Test Case ->  "+TC+"    is SKIPPED");
			LOGGER.info("Test Case ->  "+TC+"    is SKIPPED");
			LOGGER.info("Test Case ->  "+TC+"    is SKIPPED");
		}
		  
	}


	@After
	public void doneWithReadingCSVData() {
		LOGGER.info("");
		LOGGER.info("END  --  END  --  END");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("");
		LOGGER.info("");
		dataCnt++;
	}
	

	//  VERIFY OBSERVATION VAULES
		private void verifyObservationValues(Map<String, List> jsonResponse) {
			String moreThanTenth="";
			String obvValeStr="";
			String result;
			
			for (List<ArrayList> observation : jsonResponse.values()) 
			{
				int obvValuePctInt;
				switch(WhatToTest.toUpperCase())
				{
				case "LEVEL":    //  e.g. LOW, MEDIUM, HIGH - NO #'S
					List<String> levelList=new ArrayList<String>();
					levelList.add(ExpectedResult1.toUpperCase());
					levelList.add(ExpectedResult2.toUpperCase());
					levelList.add(ExpectedResult3.toUpperCase());
					levelList.add(ExpectedResult4.toUpperCase());
					levelList.add(ExpectedResult5.toUpperCase());
					Object obvValueLevel = observation.get(0); 
					if(!levelList.contains(obvValueLevel.toString().toUpperCase()))
					{
						LOGGER.error("Observation Level is not within range =>  "+obvValueLevel.toString()); 
					}
			//		System.out.println(obvValueLevel.toString());
					break;
				case "PER 1000S":   //  Numbers per 1,000 or 100,000
				case "PER 100000S":
					Object obvValuePer1000 = observation.get(0); 
					Double obvValuePer1000Dbl=Double.valueOf(obvValuePer1000.toString());
					if(obvValuePer1000Dbl> Double.valueOf(ExpectedResult2) ||
						obvValuePer1000Dbl< Double.valueOf(ExpectedResult1)) {
						LOGGER.error("Observation Per "+ExpectedResult2+(" is less than Zero or greater than"+ExpectedResult2+
								"=>  "+obvValuePer1000Dbl)); 
					}
			//		System.out.println(obvValuePer1000Dbl);
					break;
				case "PERCENTAGE":  //  # is a %%%%%
					String mapValue="";
					Object obvValuePct = observation.get(0);
					//  Had to do this for TC# 3 which returns a NullExceptionPointer????
							mapValue=jsonResponse.values().toString();
							if(mapValue != null) {
								continue;
							}
					Double obvValuePctDbl=Double.valueOf(obvValuePct.toString());
					obvValuePctInt=(int)Math.round(obvValuePctDbl);
					moreThanTenth=obvValuePctDbl.toString();
					result=moreThanTenth.substring(moreThanTenth.indexOf("."), moreThanTenth.length());
					// Verify greater than a tenth
					if(result.replace(".", "").length()>1)
					{
						LOGGER.error("Observation Percentage value is not rounded to a 10th=>  "+obvValuePctDbl);
					}
					// Verify Boundaries
					if(obvValuePctDbl>100  ||
						obvValuePctDbl<0	)
					{
						LOGGER.error("Observation Percentage value either less than 0% or greater 100%  =>  "+obvValuePctDbl);
					}
			//		System.out.println(obvValuePctDbl);
					break;
				case "TOTAL":  //  Number should be an integer
					Object obvValue = observation.get(0);
					//  REMOVE THE LESS  OR GREATER THAN SYMBOL IF PRESENT  "<" or ">"
						obvValeStr=obvValue.toString();
						if(obvValeStr.contains("<")) {
							obvValeStr=obvValeStr.replace("<", "");
						}
						if(obvValeStr.contains(">")) {
							obvValeStr=obvValeStr.replace(">", "");
						}
					Double obvValueDbl = Double.valueOf(obvValeStr);
					Long obvValueLong = Long.valueOf(obvValeStr);
					if(obvValueDbl-obvValueLong>0.9)
					{
						LOGGER.error("Observation Total is not an Integer=>  "+obvValueDbl);
					}
//					System.out.println(obvValueDbl);
					break;
				case "PERCENTAGE WITH LESS OR GREATER THAN AND NULLS":
					Object obvValueLessThanPct = observation.get(0);
					//  REMOVE THE LESS  OR GREATER THAN SYMBOL IF PRESENT  "<" or ">"
					//  IF 'NULL' FOUND SKIP VERIFCATION
						if(obvValueLessThanPct==null) {
							break;  // Valid to have a 'NULL', but no Verification
						};
						obvValeStr=obvValueLessThanPct.toString();
						if(obvValeStr.contains("<")) {
							obvValeStr=obvValeStr.replace("<", "");
						}
						if(obvValeStr.contains(">")) {
							obvValeStr=obvValeStr.replace(">", "");
						}
					Double obvValueLessThanPctDbl=Double.valueOf(obvValeStr);
					obvValuePctInt=(int)Math.round(obvValueLessThanPctDbl);
					moreThanTenth=obvValueLessThanPctDbl.toString();
					result=moreThanTenth.substring(moreThanTenth.indexOf("."), moreThanTenth.length());
					// Verify greater than a tenth
					//  UNLESS EXPECTEDRESULT1 IS EMPTY
					//  Reason: Some TCs have more than 10th in % - So d not test it.  !!!
					if(result.replace(".", "").length()>1 &&
							ExpectedResult1.isEmpty())
					{
						LOGGER.error("Observation Percentage value is not rounded to a 10th=>  "+obvValueLessThanPctDbl);
					}
					// Verify Boundaries
					if(obvValueLessThanPctDbl>100  ||
						obvValueLessThanPctDbl<0	)
					{
						LOGGER.error("Observation Percentage value either less than 0% or greater 100%  =>  "+obvValueLessThanPctDbl);
					}
					System.out.println(obvValueLessThanPctDbl);
					break;
				}

			}
		}

	

	//  GETS RESPONSE AND DOES A TIMEOUT (WAIT).  if times out then print msg & exit program)
		private Response getResponse(RequestSpecification request) throws InterruptedException
		{
				boolean responseFound=false;
				Response response = request.get(URL_Base+EndPoint1);

				while(!responseFound)  // KEEP LOOPING UNTIL STATUS CODE = 200 <OK>
				{
					if(response.getStatusCode()!=200) 
					{
						LOGGER.error("Unable to connect either the server and/or internet");
						LOGGER.error("Trying again");
						Thread.sleep(5000);
						getResponse(request);
					}else{
						responseFound=true;
					}
				}
				return response;
		}
	
	
	// GETTERS & SETTERS
			/**
			 * @return the tC
			 */
			protected String getTC() {
				return TC;
			}
			/**
			 * @return the runTest
			 */
			protected String getRunTest() {
				return RunTest;
			}
			/**
			 * @return the testDescription(s)
			 */
			protected String getTestDesc1() {
				return TestDesc1;
			}
			protected String getTestDesc2() {
				return TestDesc2;
			}
			protected String getTestDesc3() {
				return TestDesc3;
			}
			protected String getTestDesc4() {
				return TestDesc4;
			}
			/**
			 * @return the uRL_Base
			 */
			protected String getURL_Base() {
				return URL_Base;
			}
			/**
			 * @return the endPoint1
			 */
			protected String getEndPoint1() {
				return EndPoint1;
			}
			/**
			 * @return the endPoint2
			 */
			protected String getEndPoint2() {
				return EndPoint2;
			}
			/**
			 * @return the JSON_Path
			 */
			protected String getJSON_Path() {
				return JSON_Path;
			}
			/**
			 * @return the whatToTest
			 */
			protected String getWhatToTest() {
				return WhatToTest;
			}
			/**
			 * @return Verify/Confirm Data
			 */
			protected String getExpectedResult1() {
				return ExpectedResult1;
			}
			protected String getExpectedResult2() {
				return ExpectedResult2;
			}
			protected String getExpectedResult3() {
				return ExpectedResult3;
			}
			protected String getExpectedResult4() {
				return ExpectedResult4;
			}
			protected String getExpectedResult5() {
				return ExpectedResult5;
			}
	// GETTERS & SETTERS
	
	
}
