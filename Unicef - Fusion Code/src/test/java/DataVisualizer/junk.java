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
public class junk extends CommonGlobalUtils {


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
			RequestSpecification request = RestAssured.given();
//			request.header("Accept", "application/json, text/javascript, */*; q=0.01");
			Response response = getResponse(request);
			Map<String, List> jsonResponse = response.jsonPath().getMap("dataSets[0].observations"); 	
			verifyRateOrPercentage(jsonResponse);  
			
		  
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
			for (List<ArrayList> observation : jsonResponse.values()) 
			{
				Object obvValue = observation.get(0);
				//Double obvValueDbl = Double.valueOf(obvValue.toString());

				System.out.println(obvValue);
				
			}
		}

	

	//  GETS RESPONSE AND DOES A TIMEOUT (WAIT).  if times out then print msg & exit program)
		private Response getResponse(RequestSpecification request)
		{

				try {
					Response response = request.get("https://unicef-registry.sdmxcloud.org/ws/public/sdmxapi/rest/data/UNICEF,CME_DF,1.0/.TMM0+TMY0+TMY0T4+TMY5T14..269.?format=sdmx-json&includeHistory=true&includeMetadata=true&dimensionAtObservation=AllDimensions&includeAllAnnotations=true");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;

				
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
