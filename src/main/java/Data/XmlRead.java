package Data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlRead {
	
	public static Map<String, List<EventPo>> getMap(String fileName) throws Exception{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		File file = new File(fileName);
	    Document doc = Jsoup.parse(file, "utf-8");
	    Map<String, List<EventPo>> map = new HashMap<>();
	    Map<String, EventPo> tempMap = new HashMap<>();
	    int traceNum =0;
	    for(Element tranceElement : doc.getElementsByTag("trace")){
	    	traceNum ++;
	            for(Element eventElement : tranceElement.getElementsByTag("event")){
	                String name = eventElement.getElementsByAttributeValue("key", "concept:name").attr("value");
	                EventPo eventPo;
	                if(tempMap.containsKey(name)){
	                    eventPo = tempMap.get(name);
	                } else {
	                    eventPo = new EventPo(traceNum);
	                    eventPo.setName(name);
	                    tempMap.put(eventPo.getName(), eventPo);
	                }
	                String lifecycle = eventElement.getElementsByAttributeValue("key", "lifecycle:transition").attr("value");
	                String time = eventElement.getElementsByAttributeValue("key", "time:timestamp").attr("value");
	                if("start".equals(lifecycle)){
	                    eventPo.setStartDate(sdf.parse(time));
	                }
	                if("complete".equals(lifecycle)){
	                    eventPo.setEndDate(sdf.parse(time)); 
	                }
	                
	                	
	                if(eventPo.getStartDate() != null && eventPo.getEndDate() != null){
	                    if(map.containsKey(eventPo.getName())) {
	                        map.get(eventPo.getName()).add(eventPo);
	                    } else{
	                        List<EventPo> list = new ArrayList<>();
	                        list.add(eventPo);
	                        map.put(eventPo.getName(), list);
	                    }
	                    tempMap.remove(eventPo.getName());
	                } 
	         
	                else if(eventPo.getStartDate() == null && eventPo.getEndDate() != null){
                        eventPo.setStartDate(eventPo.getEndDate());
	                	eventPo.setInterval((long) 0);
	                	
	                	//eventPo.setEndDate(null);
	                }
	                else if(eventPo.getStartDate() != null && eventPo.getEndDate() == null){
	                	eventPo.setEndDate(eventPo.getStartDate());
	                	eventPo.setInterval((long) 0);
	                	//eventPo.setEndDate(null);

	                }

                	eventPo.setEndDate(null);
	            }
	        }
	        System.out.println("trace num:"+traceNum);
	        return map;
	    }

	
}
