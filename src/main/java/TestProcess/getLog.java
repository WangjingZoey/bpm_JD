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

public class getLog {
    public static String filename= "log-0-percent-noise.xes";
    public static List<Map<String,Object>> events = new ArrayList<>();

    public void getLogFromXES(String fileName) throws Exception{

        InputStream inputStream=this.getClass().getResourceAsStream("/"+fileName);
        String txt = IOUtils.toString(inputStream,"utf-8");
        inputStream.close();
        System.out.println(txt.length());

        Document doc = Jsoup.parse(txt);

        events.clear();
        int traceNum = 0;
        for(Element tranceElement : doc.getElementsByTag("trace")) {
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for(Element eventElement : tranceElement.getElementsByTag("event")){
                Map<String,Object> tempEvents=new HashMap<>();
                String name = eventElement.getElementsByAttributeValue("key", "concept:name").attr("value");
                String timeStamp = eventElement.getElementsByAttributeValue("key", "time:timestamp").attr("value");

                tempEvents.put("TraceId",traceNum+1);
                tempEvents.put("Activity",name);
                tempEvents.put("State","complete");
                tempEvents.put("TimeStamp",timeStamp);

                events.add(tempEvents);
            }
            traceNum ++;
            if(traceNum > 9)
                break;
        }

        for(int i = 0; i < events.size(); i ++){
            System.out.println(events.get(i));
        }
    }

    public static List<Map<String, Object>> get() throws Exception {
        getLog gl = new getLog();
        gl.getLogFromXES(filename);
        System.out.println("in get()");
        return events;
    }


    public static void main(String[] args) throws Exception {
        getLog gl = new getLog();
        gl.getLogFromXES(filename);
    }
}
