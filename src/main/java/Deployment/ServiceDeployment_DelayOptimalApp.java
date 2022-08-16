package Deployment;

import com.basic.NodeDef;
import com.basic.ServiceDef;
import com.basic.ServiceRequest;
import com.variable.Variable;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServiceDeployment_DelayOptimalApp {


		public static List<NodeDef> NodeList = new ArrayList<NodeDef>();
		public static List<ServiceDef> ServiceList = new ArrayList<ServiceDef>();
	    static ArrayList<ServiceDef> ServiceInNodeList = new ArrayList<ServiceDef>();
	    
	    static ArrayList<ArrayList<ServiceDef>> wgins = new ArrayList<ArrayList<ServiceDef>>();
	    
	    static List<ArrayList<ServiceRequest>> srq = new ArrayList<ArrayList<ServiceRequest>>();
	    
		private List<Float> obj1;
	    
	    static Random random = new Random(System.currentTimeMillis());
	    
	    public void generateNodeData() {
	    	int Nodeid = 0;
	    	NodeList.add(new NodeDef(Nodeid, "cloud",  Variable.CloudCapacity, Variable.cloudRsdErg));
	    	Nodeid = 1;
	    	for(int i = 1; i < Variable.NodeNum; i++){
	    		String property = "edge";
	    		int capacity = Variable.EdgeCapacity;
	    		NodeList.add(new NodeDef(Nodeid, property, capacity, Variable.initRsdErg));
				Nodeid++;
	    	}
	    }   

		public void generateSeviceData() {
			Random random = new Random(System.currentTimeMillis());
			
			int Serviceid = 0;
			int Nodeid = 0;
			float Datasize = (float) 0.0;
			
			List<Integer> NodeCapacityNumList = new ArrayList<Integer>();
			for(int i = 0; i < NodeList.size(); i ++){
				NodeCapacityNumList.add(1);
			}
			//System.out.println(NodeList.size());
			
			for (int i = 0; i < Variable.ServiceNum ; i++) {
				Nodeid = random.nextInt(Variable.NodeNum);
				
				while(NodeList.get(Nodeid).getCapacity() < NodeCapacityNumList.get(Nodeid)){     
					Nodeid = random.nextInt(Variable.NodeNum);
				}
				NodeCapacityNumList.set(Nodeid, NodeCapacityNumList.get(Nodeid)+1);
				
				Datasize = (float) (100*Math.random());
				
				ServiceList.add(new ServiceDef(Serviceid, Nodeid, Datasize));
				Serviceid++;
			}
				
//			for (int j = 0; j < ServiceList.size() ; j++) {
//				System.out.println(ServiceList.get(j));
//			}
		}
		
		public static void adjustServiceData() {
			//Random random = new Random(System.currentTimeMillis());
		
			int Nodeid = 0;
			
			List<Integer> NodeCapacityNumList = new ArrayList<Integer>();
			for(int i = 0; i < NodeList.size(); i ++){
				NodeCapacityNumList.add(1);
			}
			
			for(int i = 0; i < Variable.ServiceNum; i++){
				Nodeid = random.nextInt(Variable.NodeNum);
				while(NodeList.get(Nodeid).getCapacity() < NodeCapacityNumList.get(Nodeid)){     
					Nodeid = random.nextInt(Variable.NodeNum);
				}
				NodeCapacityNumList.set(Nodeid, NodeCapacityNumList.get(Nodeid)+1);
				//ServiceDef sev = new ServiceDef(i, Nodeid, ServiceList.get(i).getDataSize());
				//ServiceList.set(i, sev);
				ServiceList.get(i).setNodeid(Nodeid);
				
			}
		}
		
		public static void writeServiceList(String file) {
			ObjectOutputStream w;
			try {
				w = new ObjectOutputStream(new FileOutputStream(file));
				for (ServiceDef sev : ServiceList) {
					w.writeObject(sev);
				}			
				w.flush();
				w.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		public boolean loadServiceData(String file) {
			ObjectInputStream in;
			try {
				ServiceList = new ArrayList<ServiceDef>();
				in = new ObjectInputStream(new FileInputStream(file));
				Object object;
				while ((object = in.readObject()) != null) {
					if (object instanceof ServiceDef) {
						ServiceDef sev = (ServiceDef) object;
						ServiceList.add(sev);
					}
				}
				in.close();
			} catch (Exception e) {
				System.err.println(e);
				return false;
			}
			return true;
		}
		
		public static void MapServiceInNodeList(List<ArrayList<ServiceDef>> wgins) {
			//int Nodeid = 0;
			ArrayList<ServiceDef> ServiceInNodeListTemp = new ArrayList<ServiceDef>();
			for(int i = 0; i < NodeList.size(); i ++){
				for (int j = 0; j < ServiceList.size() ; j ++) {
					if (ServiceList.get(j).getNodeid() == NodeList.get(i).getId()){
						ServiceInNodeListTemp.add(ServiceList.get(j));
					}
				}
				//Nodeid ++;			
			}
			wgins.add(ServiceInNodeListTemp);
		}
		
		public static void copyArrayList(ArrayList<ServiceDef> from, ArrayList<ServiceDef> to) {
			to.clear();
			if (to.size() == 0) {
				for (int i = 0; i < from.size(); i++) {
					to.add(from.get(i));
				}
			} else {
				for (int i = 0; i < from.size(); i++) {
					to.set(i, from.get(i));
				}
			}
		}
		
		public static void main(String[] args) throws SQLException, IOException {
			
			ServiceDeployment_DelayOptimalApp process = new ServiceDeployment_DelayOptimalApp();
			process.generateNodeData();
			process.generateSeviceData();
			                        
			for (int j = 0; j < ServiceList.size() ; j++) {
				System.out.println(ServiceList.get(j));
			}
			for (int j = 0; j < NodeList.size() ; j++) {
				System.out.println(NodeList.get(j));
			}
			for (int i = 0; i < NodeList.size(); i++) {
				System.out.println(NodeList.get(i).toString());
				System.out.print("[");
				for(int j = 0; j < ServiceList.size(); j++)   
				{
					if(ServiceList.get(j).getNodeid() == NodeList.get(i).getId()){
						NodeList.get(i).addSevInNodeList(ServiceList.get(j));
						System.out.print(+ ServiceList.get(j).getid() +",");
					}
				}
				System.out.println("]");
			}
			
			System.out.println("------------------------------- �����ǳ�ʼ��-------------------------------");
			
			MapServiceInNodeList(wgins);
			
			for(int i = 1; i < Variable.invidualNum; i++){
				adjustServiceData();
				MapServiceInNodeList(wgins);
			}
			


			System.out.println("------------------------------- ��һ�������Ⱥ -------------------------------");
			for(int i = 0; i < wgins.size(); i ++){        
				for(int j = 0; j < wgins.get(i).size(); j ++)
					System.out.print(wgins.get(i).get(j).getid());
				System.out.println();
			}
			

			System.out.println("-----------------------------  result ------------------------------");
				
		}
		
}
