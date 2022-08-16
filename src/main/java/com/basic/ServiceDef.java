package com.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceDef implements Serializable{
	
	private static final long serialVersionUID = -6164217831212045044L;
	private int id;
	private int Nodeid;
	private float DataSize;

	public ServiceDef(int id, int Nodeid, float DataSize) {
		super();
		this.id = id;
		this.Nodeid = Nodeid;
		this.DataSize = DataSize;
	}
	
	public int getid() {
		return id;
	}

	public void setid(int id) {
		this.id = id;
	}

	public int getNodeid() {
		return Nodeid;
	}

	public void setNodeid(int Nodeid) {
		this.Nodeid = Nodeid;
	}
	
	public float getDataSize() {
		return Nodeid;
	}

	public void setDataSize(float DataSize) {
		this.DataSize = DataSize;
	}
		
	@Override
	public String toString() {
		return "SeviceDef [id=" + id + ", Nodeid=" + Nodeid + ", DataSize=" + DataSize + "]";
	}


}


