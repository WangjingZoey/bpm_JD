package Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class EventPo {
	
    public int id;
    private String name;
    
    private Date startDate;
    private String startDateStr;
    private Date endDate;
    private String endDateStr;
    
    private Long interval;
    private String intervalString;
    
    private String granularity;
    private int graIndex;
    
    private int traceId;
    
     
    public EventPo() {
		super();
	}

	public EventPo(int traceId) {
		super();
		this.traceId = traceId;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        if(startDate != null && endDate != null){
            setInterval(endDate.getTime() - startDate.getTime());
        }
		if(startDate != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));
			startDateStr = sdf.format(startDate);
		}
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate)throws Exception {
        if(startDate != null && endDate != null){
			setInterval(endDate.getTime() - startDate.getTime());
        }
        if(endDate != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));
			endDateStr = sdf.format(endDate);
		}
        this.endDate = endDate;
        
    }

	public Long getInterval() {
		return interval;
	}


    /*

     * 毫秒转化时分秒毫秒

     */

    public void formatTime(Long ms) {

        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"day");
            this.granularity = "day";
        }
        if(hour > 0) {
            sb.append(hour+"hour");
        }
 		if(minute > 0) {
            sb.append(minute+"min");
        }
 		if(second > 0) {
            sb.append(second+"second");
        }
 		if(milliSecond > 0) {
            sb.append(milliSecond+"millsecond");
        }
        if(day > 0) {
            this.granularity = "day";
            this.graIndex = 5;
        }else if(hour > 0) {
            this.granularity = "hour";
            this.graIndex = 4;
        }else if(minute > 0) {
            this.granularity = "min";
            this.graIndex = 3;
        }else if(second > 0) {
            this.granularity = "second";
            this.graIndex = 2;
        }else if(milliSecond > 0) {
            this.granularity = "millsecond";
            this.graIndex = 1;
        }else {
        	this.granularity = "zero";
        	this.graIndex = 0;
        }
        //return sb.toString();
        this.intervalString = sb.toString();

    }

	public void setInterval(Long interval) {
		this.interval = interval;
		if(interval != null && interval >=0) {
			formatTime(interval);
//			long t;
//			long days = interval / (1000*60*60*24);
//			long hours = (interval % (1000*60*60*24)) / (1000*60*60);
//			long minutes = (interval % (1000*60*60)) / (1000*60);
//			long seconds = (interval % (1000*60)) /1000;
//			interval /= 1000;
//			if(interval <60) {
//				granularity = "second";
//				intervalString = seconds +" seconds ";
//			}else {
//				t = interval % 60;
//				interval /= 60;
//				if(interval < 60) {
//					granularity = "minute";
//					intervalString = minutes +" minutes "+ seconds +" seconds ";
//				}else {
//					interval /= 60;
//					if(interval < 24) {
//						granularity = "hour";
//						intervalString = hours +"hours"+ minutes +" minutes "+ seconds +" seconds ";
//					}else {
//						interval /= 24;
//						if(interval < 7) {
//							granularity = "day";
//							intervalString = days +" days "+ hours +"hours"+ minutes +" minutes "+ seconds +" seconds ";
//						}else {
//							interval /= 7;
//							if(interval <2) {
//								granularity = "week";
//								//intervalString = 
//							}else {
//								granularity = "fortnight";
//								//intervalString = 
//							}
//						}
//					}
//				}
//			}
		}
	}


	
	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getIntervalString() {
		return intervalString;
	}

	public void setIntervalString(String intervalString) {
		this.intervalString = intervalString;
	}

	public String getGranularity() {
		return granularity;
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}

	public int getGraIndex() {
		return graIndex;
	}

	public void setGraIndex(int graIndex) {
		this.graIndex = graIndex;
	}

	public int getTraceId() {
		return traceId;
	}

	public void setTraceId(int traceId) {
		this.traceId = traceId;
	}


    
}
