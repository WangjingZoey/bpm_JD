package Deployment;

import NSGA.NewNSGA;
import com.basic.NodeDef;
import com.basic.ServiceDef;
import com.basic.ServiceRequest;
import com.variable.Variable;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServiceDeployment {

	public static List<NodeDef> NodeList = new ArrayList<NodeDef>();
	public static List<ServiceDef> ServiceList = new ArrayList<ServiceDef>();
    static ArrayList<ServiceDef> ServiceInNodeList = new ArrayList<ServiceDef>();
    
    static ArrayList<ArrayList<ServiceDef>> wgins = new ArrayList<ArrayList<ServiceDef>>();
    static ArrayList<ArrayList<ServiceDef>> wgins_2 = new ArrayList<ArrayList<ServiceDef>>();
    
    static List<ArrayList<ServiceRequest>> srq = new ArrayList<ArrayList<ServiceRequest>>();
    
    static Random random = new Random(System.currentTimeMillis());
    
    public void generateNodeData() {
    	int Nodeid = 0;
		NodeList.clear();
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
		ServiceList.clear();
		for (int i = 0; i < Variable.ServiceNum ; i++) {
			Nodeid = random.nextInt(Variable.NodeNum);
			
			while(NodeList.get(Nodeid).getCapacity() < NodeCapacityNumList.get(Nodeid)){     

				Nodeid = random.nextInt(Variable.NodeNum);
			}
			NodeCapacityNumList.set(Nodeid, NodeCapacityNumList.get(Nodeid)+1);
			
			Datasize = (float) (100 * Math.random());                    //���ݴ�С�����������0-100֮��
			
			ServiceList.add(new ServiceDef(Serviceid, Nodeid, Datasize));
			Serviceid++;
		}
			
//		for (int j = 0; j < ServiceList.size() ; j++) {
//			System.out.println(ServiceList.get(j));
//		}
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

	public static void main(String[] args) throws SQLException, IOException {
		
		ServiceDeployment process = new ServiceDeployment();
		process.generateNodeData();
		process.generateSeviceData();
		//process.writeServiceList("Service.txt");
		//process.loadServiceData("Service.txt");
		                        
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
		
		System.out.println("------------------------------- -------------------------------");
		
		MapServiceInNodeList(wgins);
		
		for(int i = 1; i < Variable.invidualNum; i++){             //����һ����Ⱥ
			adjustServiceData();
			MapServiceInNodeList(wgins);
		}
		
		for(int i = 0; i < Variable.invidualNum; i++){             //����һ����Ⱥ
			adjustServiceData();
			MapServiceInNodeList(wgins_2);
		}

		System.out.println("-------------------------------  -------------------------------");
		for(int i = 0; i < wgins.size(); i ++){
			for(int j = 0; j < wgins.get(i).size(); j ++)
				System.out.print(wgins.get(i).get(j).getid());
			System.out.println();
		}
		
		System.out.println("------------------------------- �ڶ��������Ⱥ -------------------------------");
		for(int i = 0; i < wgins_2.size(); i ++){
			for(int j = 0; j < wgins_2.get(i).size(); j ++)
				System.out.print(wgins_2.get(i).get(j).getid());
			System.out.println();
		}
//-----------------------------------------NSGA-------------------------------------------------------
		
		List<ArrayList<ServiceDef>> optPgds = new ArrayList<ArrayList<ServiceDef>>();
		//List<Float> optVpgds = new ArrayList<Float>();
		
		//for (int num = 0; num < Variable.runTimes; num++) {
			List<ArrayList<ServiceDef>> PopAll = new ArrayList<ArrayList<ServiceDef>>();
			//List<Float> vPgdAll = new ArrayList<Float>();
			//ArrayList<ServiceDef> optPgd = new ArrayList<ServiceDef>();
			//float optVpgd = Float.MAX_VALUE;
			NewNSGA nsga = new NewNSGA(wgins, wgins_2, NodeList, 10, 0.2f, 0.1f, Variable.max_gen);
			nsga.init();
			System.out.println("init_finsh");
			nsga.solve();
			System.out.println("solve_finsh");
				//vPgdAll.add(nsga.getvPgd());
				//PopAll.add(nsga.getPgd());
				//if (nsga.getvPgd() < optVpgd) {
				//	optVpgd = nsga.getvPgd();
				//	nsga.copyArrayList(nsga.getPgd(), optPgd);
				//}
			//optPgds.add(optPgd);
			//optVpgds.add(optVpgd);
		//}
//		try {
//			process.buildPgdResult("fitness-ga" + "-20170901(1000)" + ".xls", optPgds, optVpgds);
			
			
			System.out.println("----------------------------- nsga result ------------------------------");
			
			
//		} catch (WriteException e) {
//			e.printStackTrace();
//		}
		
	}
	
}
