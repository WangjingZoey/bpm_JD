package TestProcess;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlMining {

	// public static String fileRoot = "D:\\zoey\\coding\\BPMmining\\src\\main\\resources\\";
	public static String filename= "log-0-percent-noise.xes";

	public static List<String> activity = new ArrayList<String>();
	public static List<ArrayList<Integer>> ServiceRequest = new ArrayList<ArrayList<Integer>>();

	public void StoreListName(){
		activity.add("Triage");              //0
		activity.add("Register");            //1
		activity.add("Check");               //2
		activity.add("X-Ray");               //3
		activity.add("Visit");               //4
		activity.add("Final Visit");         //5
		activity.add("Prepare");             //6
		activity.add("Organize Ambulance");	 //7	
	}

	public void getMap(String fileName) throws Exception{

		// File file = new File(fileName);
	    // Document doc = Jsoup.parse(file, "utf-8");
		InputStream inputStream=this.getClass().getResourceAsStream("/"+fileName);
		String txt = IOUtils.toString(inputStream,"utf-8");
		inputStream.close();
		System.out.println(txt.length());

		Document doc = Jsoup.parse(txt);

		ServiceRequest.clear();
		int traceNum = 0;
	    int eventNum = 0;
	    for(Element tranceElement : doc.getElementsByTag("trace")) { 	    
	    	//System.out.println("traceNum = "+traceNum);
	    	ArrayList<Integer> temp = new ArrayList<Integer>();

			for(Element eventElement : tranceElement.getElementsByTag("event")){
				Map<String,Object> tempEvents=new HashMap<>();

				eventNum ++;
	    		String name = eventElement.getElementsByAttributeValue("key", "concept:name").attr("value");
				String timeStamp = eventElement.getElementsByAttributeValue("key", "time:timestamp").attr("value");

				if (name.equals(activity.get(0)))
	    			temp.add(0);
	    		if (name.equals(activity.get(1)))
	    			temp.add(1);
	    		if (name.equals(activity.get(2)))
	    			temp.add(2);
	    		if (name.equals(activity.get(3)))
	    			temp.add(3);
	    		if (name.equals(activity.get(4)))
	    			temp.add(4);
	    		if (name.equals(activity.get(5)))
	    			temp.add(5);
	    		if (name.equals(activity.get(6)))
	    			temp.add(6);
	    		if (name.equals(activity.get(7)))
	    			temp.add(7);
	    	}
	    	ServiceRequest.add(temp);

			traceNum ++;
		    if(traceNum > 50)
		    	break;
	    }
	    
	    for(int i = 0; i < ServiceRequest.size(); i ++){
        	for(int j = 0; j < ServiceRequest.get(i).size(); j ++){
        		System.out.print(ServiceRequest.get(i).get(j));
        	}
        	System.out.println();
        }



	}

	public static void main(String[] args) throws Exception {
		XmlMining aMining=new XmlMining();
	    aMining.StoreListName();
		// String path = fileRoot + filename;
		// aMining.getMap(path);
		aMining.getMap(filename);
	}


}
