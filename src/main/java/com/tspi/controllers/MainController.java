/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tspi.models.StoreModel;
import com.tspi.models.TestModel;
import java.util.ArrayList;
import java.util.HashMap;


import org.springframework.web.servlet.ModelAndView;

import com.tspi.template.ControllerTemplate;

import org.json.simple.JSONValue;
import org.apache.log4j.Logger;

import com.tspi.template.CoreTemplate;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.tspi.helpers.*;

/**
 *
 * @author JRANGEL
 */

@RestController
@RequestMapping("/service")
public class MainController extends ControllerTemplate implements IMainController{
    
    private TestModel tm = null;
    private StoreModel sm = null;
    private static final Logger logger = Logger.getLogger(MainController.class);
    
    public MainController(){
        this.loggerInfo("LOAD CONSTRUCTOR FOR MainController");
        this.tm = new TestModel();
        this.sm = new StoreModel();
    }
    
    @RequestMapping(value = "/startTest", method = RequestMethod.GET, produces = "text/json")
    public String startTest(){
        String test = "testing";
        //should start
        String xmlString = "<?xml version=\"1.0\"?>\n" +
"<catalog>\n" +
"   <book id=\"bk101\">\n" +
"      <author>Gambardella, Matthew</author>\n" +
"      <title>XML Developer's Guide</title>\n" +
"      <genre>Computer</genre>\n" +
"      <price>44.95</price>\n" +
"      <publish_date>2000-10-01</publish_date>\n" +
"      <description>An in-depth look at creating applications \n" +
"      with XML.</description>\n" +
"   </book>\n" +
"   <book id=\"bk102\">\n" +
"      <author>Ralls, Kim</author>\n" +
"      <title>Midnight Rain</title>\n" +
"      <genre>Fantasy</genre>\n" +
"      <price>5.95</price>\n" +
"      <publish_date>2000-12-16</publish_date>\n" +
"      <description>A former architect battles corporate zombies, \n" +
"      an evil sorceress, and her own childhood to become queen \n" +
"      of the world.</description>\n" +
"   </book>\n" +
"   <book>\n" +
"   </book>\n" +
"   <book id=\"bk104\">\n" +
"      <author>Corets, Eva</author>\n" +
"      <title>Oberon's Legacy</title>\n" +
"      <genre>Fantasy</genre>\n" +
"      <price>5.95</price>\n" +
"      <shit id=\"shit\">\n" +
                "<shits id=\"fuck\">tae1</shits>"+
                "<shits>tae2</shits>"+
"      </shit>\n" +
"      <publish_date>2001-03-10</publish_date>\n" +
"      <description>In post-apocalypse England, the mysterious \n" +
"      agent known only as Oberon helps to create a new life \n" +
"      for the inhabitants of London. Sequel to Maeve \n" +
"      Ascendant.</description>\n" +
"   </book>\n" +
"   <book id=\"bk105\">\n" +
"      <author>Corets, Eva</author>\n" +
"      <title>The Sundered Grail</title>\n" +
"      <genre>Fantasy</genre>\n" +
"      <price>5.95</price>\n" +
"      <publish_date>2001-09-10</publish_date>\n" +
"      <description>The two daughters of Maeve, half-sisters, \n" +
"      battle one another for control of England. Sequel to \n" +
"      Oberon's Legacy.</description>\n" +
"   </book>\n" +
"   <book id=\"bk106\">\n" +
"      <author>Randall, Cynthia</author>\n" +
"      <title>Lover Birds</title>\n" +
"      <genre>Romance</genre>\n" +
"      <price>4.95</price>\n" +
"      <publish_date>2000-09-02</publish_date>\n" +
"      <description>When Carla meets Paul at an ornithology \n" +
"      conference, tempers fly as feathers get ruffled.</description>\n" +
"   </book>\n" +
"   <book id=\"bk107\">\n" +
"      <author>Thurman, Paula</author>\n" +
"      <title>Splish Splash</title>\n" +
"      <genre>Romance</genre>\n" +
"      <price>4.95</price>\n" +
"      <publish_date>2000-11-02</publish_date>\n" +
"      <description>A deep sea diver finds true love twenty \n" +
"      thousand leagues beneath the sea.</description>\n" +
"   </book>\n" +
"   <book id=\"bk108\">\n" +
"      <author>Knorr, Stefan</author>\n" +
"      <title>Creepy Crawlies</title>\n" +
"      <genre>Horror</genre>\n" +
"      <price>4.95</price>\n" +
"      <publish_date>2000-12-06</publish_date>\n" +
"      <description>An anthology of horror stories about roaches,\n" +
"      centipedes, scorpions  and other insects.</description>\n" +
"   </book>\n" +
"   <book id=\"bk109\">\n" +
"      <author>Kress, Peter</author>\n" +
"      <title>Paradox Lost</title>\n" +
"      <genre>Science Fiction</genre>\n" +
"      <price>6.95</price>\n" +
"      <publish_date>2000-11-02</publish_date>\n" +
"      <description>After an inadvertant trip through a Heisenberg\n" +
"      Uncertainty Device, James Salway discovers the problems \n" +
"      of being quantum.</description>\n" +
"   </book>\n" +
"   <book id=\"bk110\">\n" +
"      <author>O'Brien, Tim</author>\n" +
"      <title>Microsoft .NET: The Programming Bible</title>\n" +
"      <genre>Computer</genre>\n" +
"      <price>36.95</price>\n" +
"      <publish_date>2000-12-09</publish_date>\n" +
"      <description>Microsoft's .NET initiative is explored in \n" +
"      detail in this deep programmer's reference.</description>\n" +
"   </book>\n" +
"   <book id=\"bk111\">\n" +
"      <author>O'Brien, Tim</author>\n" +
"      <title>MSXML3: A Comprehensive Guide</title>\n" +
"      <genre>Computer</genre>\n" +
"      <price>36.95</price>\n" +
"      <publish_date>2000-12-01</publish_date>\n" +
"      <description>The Microsoft MSXML3 parser is covered in \n" +
"      detail, with attention to XML DOM interfaces, XSLT processing, \n" +
"      SAX and more.</description>\n" +
"   </book>\n" +
"   <book id=\"bk112\">\n" +
"      <author>Galos, Mike</author>\n" +
"      <title>Visual Studio 7: A Comprehensive Guide</title>\n" +
"      <genre>Computer</genre>\n" +
"      <price>49.95</price>\n" +
"      <publish_date>2001-04-16</publish_date>\n" +
"      <description>Microsoft Visual Studio 7 is explored in depth,\n" +
"      looking at how Visual Basic, Visual C++, C#, and ASP+ are \n" +
"      integrated into a comprehensive development \n" +
"      environment.</description>\n" +
"   </book>\n" +
"</catalog>";
        String xmlString2 = "<ns4:QueryProfileResultMsg xmlns:ns2=\"http://www.huawei.com/bme/cbsinterface/common\" xmlns:ns3=\"http://www.huawei.com/bme/cbsinterface/cbs/businessmgr\" xmlns:ns4=\"http://www.huawei.com/bme/cbsinterface/cbs/businessmgrmsg\">\n" +
"<ResultHeader>\n" +
"<ns2:CommandId>QueryProfile</ns2:CommandId>\n" +
"<ns2:Version>1</ns2:Version>\n" +
"<ns2:TransactionId>2015031915433429360133534013</ns2:TransactionId>\n" +
"<ns2:SequenceId>1</ns2:SequenceId>\n" +
"<ns2:ResultCode>405000000</ns2:ResultCode>\n" +
"<ns2:ResultDesc>Operation successful.</ns2:ResultDesc>\n" +
"</ResultHeader>\n" +
"<QueryProfileResult>\n" +
"<ns3:IVRLang>2</ns3:IVRLang>\n" +
"<ns3:SMSLang>2</ns3:SMSLang>\n" +
"<ns3:PayType>0</ns3:PayType>\n" +
"<ns3:State>1</ns3:State>\n" +
"<ns3:Brand>1059</ns3:Brand>\n" +
"<ns3:MainProductID>30039</ns3:MainProductID>\n" +
"<ns3:ValidityDate>20161231</ns3:ValidityDate>\n" +
"<ns3:SuspendStop>20170219</ns3:SuspendStop>\n" +
"<ns3:DisabelStop>20170301</ns3:DisabelStop>\n" +
"<ns3:ActivationDate>20141116</ns3:ActivationDate>\n" +
"<ns3:PPSBalance>587500</ns3:PPSBalance>\n" +
"<ns3:POSBalance>0</ns3:POSBalance>\n" +
"<ns3:managementstate>000000000000000</ns3:managementstate>\n" +
"</QueryProfileResult>\n" +
"</ns4:QueryProfileResultMsg>";
        String yahooWeather = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<query xmlns:yahoo=\"http://www.yahooapis.com/v1/base.rng\"\n" +
"    yahoo:count=\"1\" yahoo:created=\"2015-03-25T23:40:14Z\" yahoo:lang=\"fil\">\n" +
"    <results>\n" +
"        <channel>\n" +
"            <title>Yahoo! Weather - Nome, AK</title>\n" +
"            <link>http://us.rd.yahoo.com/dailynews/rss/weather/Nome__AK/*http://weather.yahoo.com/forecast/USAK0170_f.html</link>\n" +
"            <description>Yahoo! Weather for Nome, AK</description>\n" +
"            <language>en-us</language>\n" +
"            <lastBuildDate>Wed, 25 Mar 2015 2:52 pm AKDT</lastBuildDate>\n" +
"            <ttl>60</ttl>\n" +
"            <yweather:location\n" +
"                xmlns:yweather=\"http://xml.weather.yahoo.com/ns/rss/1.0\"\n" +
"                city=\"Nome\" country=\"United States\" region=\"AK\"/>\n" +
"            <yweather:units\n" +
"                xmlns:yweather=\"http://xml.weather.yahoo.com/ns/rss/1.0\"\n" +
"                distance=\"mi\" pressure=\"in\" speed=\"mph\" temperature=\"F\"/>\n" +
"            <yweather:wind\n" +
"                xmlns:yweather=\"http://xml.weather.yahoo.com/ns/rss/1.0\"\n" +
"                chill=\"13\" direction=\"250\" speed=\"5\"/>\n" +
"            <yweather:atmosphere\n" +
"                xmlns:yweather=\"http://xml.weather.yahoo.com/ns/rss/1.0\"\n" +
"                humidity=\"77\" pressure=\"29.64\" rising=\"2\" visibility=\"10\"/>\n" +
"            <yweather:astronomy\n" +
"                xmlns:yweather=\"http://xml.weather.yahoo.com/ns/rss/1.0\"\n" +
"                sunrise=\"8:43 am\" sunset=\"9:31 pm\"/>\n" +
"            <image>\n" +
"                <title>Yahoo! Weather</title>\n" +
"                <width>142</width>\n" +
"                <height>18</height>\n" +
"                <link>http://weather.yahoo.com</link>\n" +
"                <url>http://l.yimg.com/a/i/brand/purplelogo//uh/us/news-wea.gif</url>\n" +
"            </image>\n" +
"            <item>\n" +
"                <title>Conditions for Nome, AK at 2:52 pm AKDT</title>\n" +
"                <geo:lat xmlns:geo=\"http://www.w3.org/2003/01/geo/wgs84_pos#\">64.5</geo:lat>\n" +
"                <geo:long xmlns:geo=\"http://www.w3.org/2003/01/geo/wgs84_pos#\">-165.41</geo:long>\n" +
"                <link>http://us.rd.yahoo.com/dailynews/rss/weather/Nome__AK/*http://weather.yahoo.com/forecast/USAK0170_f.html</link>\n" +
"                <pubDate>Wed, 25 Mar 2015 2:52 pm AKDT</pubDate>\n" +
"                <yweather:condition\n" +
"                    xmlns:yweather=\"http://xml.weather.yahoo.com/ns/rss/1.0\"\n" +
"                    code=\"28\" date=\"Wed, 25 Mar 2015 2:52 pm AKDT\"\n" +
"                    temp=\"20\" text=\"Mostly Cloudy\"/>\n" +
"                <description><![CDATA[\n" +
"<img src=\"http://l.yimg.com/a/i/us/we/52/28.gif\"/><br />\n" +
"<b>Current Conditions:</b><br />\n" +
"Mostly Cloudy, 20 F<BR />\n" +
"<BR /><b>Forecast:</b><BR />\n" +
"Wed - Clear. High: 20 Low: 6<br />\n" +
"Thu - Sunny. High: 21 Low: 4<br />\n" +
"Fri - Sunny. High: 24 Low: 10<br />\n" +
"Sat - Mostly Cloudy. High: 27 Low: 18<br />\n" +
"Sun - Partly Cloudy. High: 25 Low: 10<br />\n" +
"<br />\n" +
"<a href=\"http://us.rd.yahoo.com/dailynews/rss/weather/Nome__AK/*http://weather.yahoo.com/forecast/USAK0170_f.html\">Full Forecast at Yahoo! Weather</a><BR/><BR/>\n" +
"(provided by <a href=\"http://www.weather.com\" >The Weather Channel</a>)<br/>\n" +
"]]></description>\n" +
"                <yweather:forecast\n" +
"                    xmlns:yweather=\"http://xml.weather.yahoo.com/ns/rss/1.0\"\n" +
"                    code=\"31\" date=\"25 Mar 2015\" day=\"Wed\" high=\"20\"\n" +
"                    low=\"6\" text=\"Clear\"/>\n" +
"                <yweather:forecast\n" +
"                    xmlns:yweather=\"http://xml.weather.yahoo.com/ns/rss/1.0\"\n" +
"                    code=\"32\" date=\"26 Mar 2015\" day=\"Thu\" high=\"21\"\n" +
"                    low=\"4\" text=\"Sunny\"/>\n" +
"                <yweather:forecast\n" +
"                    xmlns:yweather=\"http://xml.weather.yahoo.com/ns/rss/1.0\"\n" +
"                    code=\"32\" date=\"27 Mar 2015\" day=\"Fri\" high=\"24\"\n" +
"                    low=\"10\" text=\"Sunny\"/>\n" +
"                <yweather:forecast\n" +
"                    xmlns:yweather=\"http://xml.weather.yahoo.com/ns/rss/1.0\"\n" +
"                    code=\"28\" date=\"28 Mar 2015\" day=\"Sat\" high=\"27\"\n" +
"                    low=\"18\" text=\"Mostly Cloudy\"/>\n" +
"                <yweather:forecast\n" +
"                    xmlns:yweather=\"http://xml.weather.yahoo.com/ns/rss/1.0\"\n" +
"                    code=\"30\" date=\"29 Mar 2015\" day=\"Sun\" high=\"25\"\n" +
"                    low=\"10\" text=\"Partly Cloudy\"/>\n" +
"                <guid isPermaLink=\"false\">USAK0170_2015_03_29_7_00_AKDT</guid>\n" +
"            </item>\n" +
"        </channel>\n" +
"    </results>\n" +
"</query>";
        String content = "";

//        try {
//            content = new String(Files.readAllBytes(Paths.get("/Users/Pro/Downloads/gist3080489-282155349da23b26c69ec64c559f90e1b60ee7f4/gistfile1.txt")));
//
//        } catch (Exception e) {
//            CoreTemplate.logDebug(e.getMessage());
//        }

        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("username", "spinoysys");
        requestData.put("password", "spinoy2012");
        requestData.put("mobile_no", "0133534013");
        requestData.put("trx_id", "123123123123123");
        requestData.put("medium", "SMS");
        String resp = RequestHelper.getInstance().setStringURI("http://cbssdev/TGSI/JRANGEL.TEST/testPage.php?fuck=1")
                .setRequestMethod(RequestHelper.RequestMethod.POST)
                .setRequestData(requestData)
                .startRequest().getResponseString();
        if(RequestHelper.getInstance().errorOccurred()){
            this.loggerDebug("ERROR in REQUEST HELPER " + RequestHelper.getInstance().getErrorDescription());
        }
        this.loggerDebug("HEADER RESP CODE " + RequestHelper.getInstance().getHeaderResponseCode());
        this.loggerDebug("CBSSDEV " + resp);
        
        XmlParser xmlp = XmlParser.getInstance().
                setIncludeAttributes(true).
                convertCDATAToText(false).
                setXmlParsingType(XmlParser.XmlParsingType.FIRST).
                xmlToObject(yahooWeather);
//        this.loggerDebug("XML parser Object " + xmlp.toObject());
        if(xmlp.errrorOccurred()){
            return xmlp.errorMsg();
        }
//        this.loggerDebug("MEMORY USED KB " + XmlComposer.getInstance().getTotalMemory());
//        this.loggerDebug("RAW OBJECT " + xmlp.toObject());

        //for XML Composer
        String jsonString = JsonUtility.getInstance().toJsonString(xmlp.toObject());
//        JsonUtility ju = JsonUtility.getInstance().toJsonObject(jsonString);
        String composedXml = XmlComposer.getInstance().objectToXml(xmlp.toObject());
        this.loggerDebug("MEMORY USED KB " + XmlComposer.getInstance().getTotalMemory());
        this.loggerDebug("COMPOSED XML " + composedXml);
//        jsonString = JsonUtility.getInstance().toJsonString(ju.getJsonObjectForKey("prepaid").getJsonObjectForIndex(1).toObject());
        if(JsonUtility.getInstance().errorOccurred()){
            this.loggerDebug("ERROR in JSON " + JsonUtility.getInstance().getErrorDescription());
        }
        return jsonString;
//        String firstStr = JSONValue.toJSONString(xmlp.copyObjectForKey("catalog").getObjectForKey("book").getObjectAtIndex(10).toObject());
//        String secondStr = JSONValue.toJSONString(xmlp.copyObjectForKey("catalog").getObjectForKey("book").getObjectAtIndex(9).toObject());
//        return composedXml;
//        return JSONValue.toJSONString(xmlp.toObject());
//        return "test";
    }
    
    @RequestMapping(value = "/newTest", method = RequestMethod.GET)
    public String newTest(){
        this.test();
        return "TEST";
    }
    
   
    @Override
    @RequestMapping(value = "/forceJson", method = RequestMethod.GET, produces = "application/json")
    public String returnJsonString(){
        ArrayList<HashMap> ret = new ArrayList<>();
        int c = 10;
        for (int i = 0; i < c; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(String.valueOf(i), String.valueOf(i));
            ret.add(map);
        }
        return JSONValue.toJSONString(ret);
    }
    
    @Override
    @RequestMapping(value = "/parsedPage", method = RequestMethod.GET)
    public ModelAndView showParsedPage(){
        boolean isRunning;
        isRunning = this.isRunning("parsedPage");
        ModelAndView mv = new ModelAndView("indexParsed");
        if(!isRunning){
            this.setIsRunning("parsedPage", true);
            logger.info("showParsedPage");
            String s;
//            RequestHelper rh = new RequestHelper();
//    //        rh.setStringURI("http://localhost:8080/myproject/service/eStore/api?username=username1&itemname=bag");
//            rh.setStringURI("http://cbssdev/TGSI/JRANGEL.TEST/testPage.php");
//    //        rh.setRequestProperty(RequestHelper.RequestProperty.REST);
//            rh.setRequestProperty(RequestHelper.RequestProperty.SOAP);
//            rh.setRequestMethod(RequestHelper.RequestMethod.POST);
//    //        rh.setRequestMethod(RequestHelper.RequestMethod.GET);
//            HashMap<String, String> postData = new HashMap<>();
//            postData.put("username", "spinoysys");
//                postData.put("password", "spinoy2012");
//                postData.put("mobile_no", "0133534013");
//                postData.put("trx_id", "123123123123123");
//                postData.put("medium", "SMS");
//            rh.setPostData(postData);
//            rh.setRequestMethod(RequestHelper.RequestMethod.POST);
//            Object obj = rh.requestStart();
//            if(obj==null){
//                this.loggerDebug("null object");
//            }
//            else{
//                this.loggerDebug(obj.toString());
//            this.loggerDebug(rh.getRawResponseString());
//            } 
//            try {
//                Thread.sleep(5000);
//            } catch (Exception e) {
//            }
//            this.setIsRunning("parsedPage", false);
            mv.addObject("data","Executed");
        }else{
            mv.addObject("data","Running");
        }
        this.tm.selectSample();
        return mv;
    }
    
    @Override
    @RequestMapping(value = "/eStore", method = RequestMethod.GET)
    public ModelAndView showStore(){
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("msg", "Welcome suki!");
        return mv;
    }
    
    @RequestMapping(value = "/eStore/api", method = RequestMethod.GET)
    @ResponseBody
    public Store storeIndex(
            @RequestParam(value = "username", required = true) String usernameString, 
            @RequestParam(value = "itemname", required = true) String itemnameString){
        String methodName = "eStore/API";
        this.loggerInfo("START");
        this.loggerInfo("PARAMS START : " + "\nusername = " + usernameString + "\nitemname=" + itemnameString + " \nPARAMS END");
        Store store = new Store();
        ArrayList<HashMap> row = sm.getContent(usernameString, itemnameString);
        if(this.sm.errorEncountered() == true){
            //return model.errorDescription();
            store.setStatusCode("203");
            store.setStatusDesc("Error Accessing database!");
            this.loggerInfo(store.getStatusDesc());
            this.loggerInfo("END");
            return store;
        }
        if(row.isEmpty()){
            store.setStatusCode("203");
            store.setStatusDesc("Username or ItemName does not exist!");
            this.loggerInfo(store.getStatusDesc());
            this.loggerInfo("END");
            return store;
        }

        HashMap<String, String> map = row.get(0);
        double accBalance = Double.parseDouble(map.get("acc_balance"));
        double itemPrice = Double.parseDouble(map.get("item_price"));
        String itemId = map.get("item_id");
        String itemName = map.get("item_name");
        String accId = map.get("acc_id");
        String accUsername = map.get("acc_username");
        if(accBalance < itemPrice){
            store.setStatusCode("200");
            store.setStatusDesc("Insufficient Balance!");
            this.loggerInfo(store.getStatusDesc());
            this.loggerInfo("END");
            return store;
        }
        double newBalance = accBalance - itemPrice;
        if(this.sm.updateAccountBalance(accId, String.valueOf(newBalance))==0){
            store.setStatusCode("202");
            store.setStatusDesc("Balance Update Failed, Please try again!");
            this.loggerInfo(store.getStatusDesc());
            this.loggerInfo("END");
            return store;
        }
        
        if(this.sm.insertSoldItem(accId, itemId, String.valueOf(itemPrice))>0){
            store.setStatusCode("100");
            store.setStatusDesc("Success!");
            this.loggerInfo(store.getStatusDesc());
            this.loggerInfo("END");
            return  store;
        }
        store.setStatusCode("203");
        store.setStatusDesc("Insertion to sold item failed, Please contact system admin!");
        this.loggerInfo(store.getStatusDesc());
        this.loggerInfo("END");
        return store;
    }
}
