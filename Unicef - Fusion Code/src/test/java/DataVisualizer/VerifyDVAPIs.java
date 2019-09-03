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
	private String Accept_Header;
	private String JSON_Path;
	private String WhatToTest;
	static int dataCnt = 1;

	
	
	
	@Before
	public void ReadingCSVData() {
		LOGGER.info("");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("START  --  Test Case ->  "+TC);
		LOGGER.info("Test Case Description ->  "+TestDesc1+" -> "+TestDesc2+
				" -> "+TestDesc3+" -> "+TestDesc4);
	}

	
	@Test
	public void getAPIResponse() throws InterruptedException {
		if(RunTest.toUpperCase().equals("T"))  // Execute test case only if RunTest = 'T' or True
		{
			RequestSpecification request = RestAssured.given();
			request.header("Accept", Accept_Header);
			Response response = getResponse(request);
			Map<String, List> jsonResponse = response.jsonPath().getMap(JSON_Path); 	
			verifyRateOrPercentage(jsonResponse);  
			
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
	

	//  VERIFY VAULES ARE RATES.
		private void verifyRateOrPercentage(Map<String, List> jsonResponse) {
			String moreThanTenth="";
			String result;
			
			for (List<ArrayList> observation : jsonResponse.values()) 
			{
				int obvValuePctInt;
				switch(WhatToTest.toUpperCase())
				{
				case "TOTAL":
					Object obvValue = observation.get(0);
					Double obvValueDbl = Double.valueOf(obvValue.toString());
					Long obvValueLong = Long.valueOf(obvValue.toString());
					if(obvValueDbl-obvValueLong>0.9)
					{
						LOGGER.error("Observation Rate is not an Integer=>  "+obvValueDbl);
					}
					break;
				case "PERCENTAGE":
					Object obvValuePct = observation.get(0);
					Double obvValuePctDbl = Double.valueOf(obvValuePct.toString());
					obvValuePctInt=(int)Math.round(obvValuePctDbl);
					moreThanTenth=obvValuePctDbl.toString();
					result=moreThanTenth.substring(moreThanTenth.indexOf("."), moreThanTenth.length());
					// Verify greater than a tenth
					if(result.replace(".", "").length()>1)
					{
						LOGGER.error("Observation Percentage value is greater than a 10th of a percent=>  "+obvValuePctDbl);
					}
					// Verify Boundaries
					if(obvValuePctDbl>100  ||
						obvValuePctDbl<0	)
					{
						LOGGER.error("Observation Percentage value either less than 0% or greater 100%  =>  "+obvValuePctDbl);
					}
					break;
				}

				
				
			}
		}

	

	//  GETS RESPONSE AND DOES A TIMEOUT (WAIT).  if times out then print msg & exit program)
		private Response getResponse(RequestSpecification request) throws InterruptedException
		{
				boolean responseFound=false;
				Response response = request.get(URL_Base+EndPoint1+getPeriodRange().substring(0,4)+EndPoint2+getPeriodRange().substring(4,8));

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
	
	
	//  GET PERIOD RANGE MIN->MAX.  USED AS A DIMENSION
		private String getPeriodRange() {
			int maxYear = 0;
			int minYear = 0;
			maxYear=Integer.parseInt(Year.now().toString());  
			maxYear++;  // Add 1+ years.  Data Visualizer does this.
			minYear=maxYear-50;
			return Integer.toString(minYear)+Integer.toString(maxYear);
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
			 * @return the accept_Header
			 */
			protected String getAccept_Header() {
				return Accept_Header;
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
	// GETTERS & SETTERS
	
	
}
