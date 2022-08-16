package Data;

import java.util.IllegalFormatCodePointException;

public class Event {
	
	public int id;
	private String strid;
    private String name;  
    private String state;
    private int traceId;
    
    public Event() {
 		super();
 	}

 	public Event(int id, int traceId) {
 		super();
 		this.id = id;
 		this.traceId = traceId;
 	}

 	public String getName() {
         return name;
     }
    public void setName(String name) {
         this.name = name;
    }
    
    public String getState() {
    	return state;
    }
    public void setState(String state) {
    	this.state = state;
    }
    
	public int getTraceId() {
		return traceId;
	}
	public void setTraceId(int traceId) {
		this.traceId = traceId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getstrId() {
        return strid;
    }
   public void setStrid(String strid) {
        this.strid = strid;
   }
    
    

}
