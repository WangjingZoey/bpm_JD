package com.basic;

public class ServiceRequest {

	 
	int id;
    int DurTime;

    public ServiceRequest(int id, int DurTime) {
    	super();
    	this.id = id;
        this.DurTime = DurTime;  
    }
    
	public int getid() {
		return id;
	}
	public void setid(int id) {
		this.id = id;
	}

	
	public int getDurTime() {
		return DurTime;
	}
	public void setDurTime(int DurTime) {
		this.DurTime = DurTime;
	}
	

	@Override
	public String toString() {
		return "SRQ [id=" + id + ", DurTime=" + DurTime + "]";
	}


}
