package com.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NodeDef implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
//	private float x, y; // Location
	private String property;
	private int capacity;
	private List<ServiceDef> SevInNodeList;
	private float rsdErg;
		
	public NodeDef(int id, String property, int capacity, float rsdErg) {
		super();
		this.id = id;
		this.property = property;
		this.capacity = capacity;
		this.rsdErg = rsdErg;
		this.SevInNodeList = new ArrayList<ServiceDef>();
//		this.IoTS_s_inIoTNList = new ArrayList<IoTS_specific>();
	}
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
		
	public float getRsdErg() {
		return rsdErg;
	}
	public void setRsdErg(float rsdErg) {
		this.rsdErg = rsdErg;
	}
		
	public List<ServiceDef> getSevInNodeList() {
		return SevInNodeList;
	}
	public void setSevInNodeList(List<ServiceDef> SevInNodeList) {
		this.SevInNodeList = SevInNodeList;
	}
	public void addSevInNodeList(ServiceDef sev) {
		this.SevInNodeList.add(sev);
	}


//	public List<IoTS_specific> getIoTS_s_inIoTNList() {
//		return IoTS_s_inIoTNList;
//	}
//	public void setIoTS_s_inIoTNList(List<IoTS_specific> IoTS_s_inIoTNList) {
//		this.IoTS_s_inIoTNList = IoTS_s_inIoTNList;
//	}
//	public void addIoTS_s_inIoTNList(IoTS_specific IoTS) {
//		this.IoTS_s_inIoTNList.add(IoTS);
//	}

//	public float getDis(NodeDef node) {
//		return (float) Math.sqrt((this.x - IoTN.x) * (this.x - IoTN.x) + (this.y - IoTN.y) * (this.y - IoTN.y)); 
//	}

	@Override
	public String toString() {
		return "Node [id=" + id + ", property=" + property + ", capacity=" + capacity + ", rsdErg=" + rsdErg + "]";
		
	}


}

