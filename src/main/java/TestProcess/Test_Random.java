package TestProcess;

import Deployment.ServiceDeployment;
import com.basic.NodeDef;
import com.basic.ServiceDef;
import com.variable.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Test_Random {
	static List<Integer> ServicetoNode = new ArrayList<Integer>();
	static int state = 0;
	static List<Float> ErgCons = new ArrayList<Float>();
	static List<Float> DelayCons = new ArrayList<Float>();

	static List<Float> minErgs = new ArrayList<Float>();
	static List<Integer> minIndexs = new ArrayList<Integer>();
	static List<Float> vars = new ArrayList<Float>();
	static List<Float> ErgIni = new ArrayList<Float>();
	//----------------------------------------------------------------------------------------------
	//Determine the data returned to the front page
	static ArrayList<Integer> bestServiceRequest = new ArrayList<Integer>();
	static List<ServiceDef> bestServiceList = new ArrayList<ServiceDef>();
	static float bestvar = 0;
	static float bestErg = 0;
	static float bestErgCon = 0;
	static float bestDelayCon = 0;
	//----------------------------------------------------------------------------------------------


	public static void DistributionNode(List<ServiceDef> ServiceList) {    //�������Ӧ�ڵ��������
		float size = 0;
		for(int i = 0; i < ServiceList.size(); i ++){
			//ServiceList.get(i).setNodeid(ServicetoNode.get(i));
			size = (float)Math.random() * 150 + 50;
			ServiceList.get(i).setDataSize(size);
		}
	}

	public static void Process(List<ServiceDef> ServiceList, List<NodeDef> NodeList, List<ArrayList<Integer>> ServiceRequest) {

		float ComputeEnergy = (float) 0.0;
		float TransEnergy = (float) 0.0;
		float ComputeDelay = (float) 0.0;
		float TransDelay = (float) 0.0;
		for(int m = 0; m < NodeList.size(); m ++){    //��ʼ����ErgIniÿ���ڵ�ʣ������
			ErgIni.add(NodeList.get(m).getRsdErg());
		}

		for(int i = 0; i < ServiceRequest.size(); i ++){   //��������srq
			float EnergyConsuption = (float) 0.0;
			float TimeDelay = (float) 0.0;
			for(int j = 0; j < ServiceRequest.get(i).size() - 1; j ++){  //����ÿ��srq�е�����service
				if(ServiceList.get(ServiceRequest.get(i).get(j)).getNodeid() != ServiceList.get(ServiceRequest.get(i).get(j+1)).getNodeid() ){  //�ж��������ڵķ����Ƿ���ͬһ��EN��
					if((ServiceList.get(ServiceRequest.get(i).get(j)).getNodeid() == 0) || (ServiceList.get(ServiceRequest.get(i).get(j+1)).getNodeid() == 0) ){  //�ж��Ƿ���һ��������Cloud��
						state = 2;  //�������ڵķ�����EN��Cloud��
						ComputeEnergy = ComputeEnergy(ServiceList.get(ServiceRequest.get(i).get(j)),ServiceList.get(ServiceRequest.get(i).get(j)).getDataSize() / 10);
						TransEnergy = TransEnergy(ServiceList.get(ServiceRequest.get(i).get(j)), state);
						NodeList.get(ServiceList.get(ServiceRequest.get(i).get(j)).getNodeid()).setRsdErg(NodeList.get(ServiceList.get(ServiceRequest.get(i).get(j)).getNodeid()).getRsdErg() - ComputeEnergy - TransEnergy);
						ComputeDelay = ComputeDelay(ServiceList.get(ServiceRequest.get(i).get(j)), ServiceList.get(ServiceRequest.get(i).get(j)).getDataSize() / 10);
						TransDelay = CommunicateDelay(ServiceList.get(ServiceRequest.get(i).get(j)), state);
					}
					else{
						state = 1;  //�������ڵķ����ڲ�ͬ��EN��
						ComputeEnergy = ComputeEnergy(ServiceList.get(ServiceRequest.get(i).get(j)), ServiceList.get(ServiceRequest.get(i).get(j)).getDataSize() / 10);
						TransEnergy = TransEnergy(ServiceList.get(ServiceRequest.get(i).get(j)), state);
						NodeList.get(ServiceList.get(ServiceRequest.get(i).get(j)).getNodeid()).setRsdErg(NodeList.get(ServiceList.get(ServiceRequest.get(i).get(j)).getNodeid()).getRsdErg() - ComputeEnergy - TransEnergy);
						ComputeDelay = ComputeDelay(ServiceList.get(ServiceRequest.get(i).get(j)), ServiceList.get(ServiceRequest.get(i).get(j)).getDataSize() / 10);
						TransDelay = CommunicateDelay(ServiceList.get(ServiceRequest.get(i).get(j)), state);
					}
				}
				else{
					state = 0;  //�������ڵķ�����ͬһ��EN��
					ComputeEnergy = ComputeEnergy(ServiceList.get(ServiceRequest.get(i).get(j)), ServiceList.get(ServiceRequest.get(i).get(j)).getDataSize() / 10);
					TransEnergy = TransEnergy(ServiceList.get(ServiceRequest.get(i).get(j)), state);
					NodeList.get(ServiceList.get(ServiceRequest.get(i).get(j)).getNodeid()).setRsdErg(NodeList.get(ServiceList.get(ServiceRequest.get(i).get(j)).getNodeid()).getRsdErg() - ComputeEnergy - TransEnergy);
					ComputeDelay = ComputeDelay(ServiceList.get(ServiceRequest.get(i).get(j)), ServiceList.get(ServiceRequest.get(i).get(j)).getDataSize() / 10);
					TransDelay = CommunicateDelay(ServiceList.get(ServiceRequest.get(i).get(j)), state);
				}
				EnergyConsuption = EnergyConsuption + ComputeEnergy + TransEnergy;
				TimeDelay = TimeDelay + ComputeDelay + TransDelay;
			}
			//ServiceRequest
//			System.out.println("��ǰServiceRequestΪ " + ServiceRequest.get(i));
//			System.out.println("��ǰServiceListΪ" + ServiceList);
//			System.out.println("�������� = " + EnergyConsuption);
//			System.out.println("�ӳ� = " + TimeDelay);

			DelayCons.add(TimeDelay);

			//������Сʣ������
			float sumErg = 0;
			float minErg = Integer.MAX_VALUE;
			int minIndex = 0;

			for (int m = 0; m < NodeList.size(); m++) {
				//sumErg += NodeList.get(m).getRsdErg();
				if (NodeList.get(m).getRsdErg() < minErg) {
					minErg = NodeList.get(m).getRsdErg();
					minIndex = m;
				}
			}
			minErg = (float) (minErg * 1.0 / Variable.initRsdErg);// 0-1
			minErgs.add(minErg);
			minIndexs.add(minIndex);

			//������������
			float ErgCon = 0;

			for(int j = 0; j < NodeList.size(); j ++){
				ErgCon += ErgIni.get(j) - NodeList.get(j).getRsdErg();
				ErgIni.set(j, (float)(NodeList.get(j).getRsdErg()));
			}
			ErgCons.add(ErgCon);

			//����ʣ����������
			for (int m = 1; m < NodeList.size(); m ++) {    //ֻ����EN�ڵ��
				sumErg += NodeList.get(m).getRsdErg();
			}
			float avgErg = sumErg / (NodeList.size() - 1);
			avgErg = avgErg / Variable.initRsdErg;// 0-1
			float sumV = 0;

			for (int n = 1; n < NodeList.size(); n ++) {
				sumV += (NodeList.get(n).getRsdErg() / Variable.initRsdErg - avgErg)
						* (NodeList.get(n).getRsdErg() / Variable.initRsdErg - avgErg);
			}

			float var = sumV / (NodeList.size() - 1);
			vars.add(var);
			//----------------------------------------------------------------------------------------------
			if(ErgCon > bestErgCon){
				bestvar = var;
				bestServiceRequest = ServiceRequest.get(i);
				bestErg = minErg;
				bestErgCon = ErgCon;
				bestDelayCon = TimeDelay;
				bestServiceList = ServiceList;
			}
			//----------------------------------------------------------------------------------------------
		}

	}

	public static float ComputeEnergy(ServiceDef sev, float u_k) {    //�ڵ�����ܺ�
		float energy = 0;
		if (sev.getNodeid() == 0)
		{
			energy = (float) (Variable.E_0 + Variable.c * Variable.f_0 * Variable.f_0 / 100 * u_k * 1024 );
		}
		else{
			energy = (float) (Variable.E_0 + Variable.c * Variable.f_n * Variable.f_n / 100 * u_k * 1024 );
		}
		return energy;
	}

	public static float TransEnergy(ServiceDef sev, int d) {  //�ڵ㴫���ܺ�
		float transErg = 0;
		if(d == 0)
			transErg = 0;
		if(d == 1)
			transErg = (2 * Variable.E_elec * sev.getDataSize());
		if(d == 2)
			transErg = (float) (Variable.E_elec * sev.getDataSize() + Variable.E_amp * sev.getDataSize() * Math.pow(Variable.dis, Variable.p));
		return (float) transErg;
	}

	public static float ComputeDelay(ServiceDef sev, float u_k) {    //�ڵ�����ӳ�
		float delay = 0;
		if(sev.getNodeid() == 0)
			delay = (float) (u_k * 1024 / Variable.f_0);
		else
			delay = (float) (u_k * 1024 / Variable.f_n);
		return delay;
	}

	public static float CommunicateDelay(ServiceDef sev, int d) {  //�ڵ�ͨѶ�ӳ�
		float CommDly = 0;
		if(d == 0)
			CommDly = 0;
		if(d == 1)
			CommDly = (float) (sev.getDataSize() / Variable.w_n);
		if(d == 2)
			CommDly = (float) (sev.getDataSize() / Variable.w_0 + Variable.t_0);
		return (float) CommDly;
	}

	public static Map RANDOM() throws Exception {
		state = 0;
		ErgCons.clear();
		DelayCons.clear();

		minErgs.clear();
		minIndexs.clear();
		vars.clear();
		ErgIni.clear();

		ServiceDeployment aDeployment = new ServiceDeployment();
		aDeployment.generateNodeData();
		aDeployment.generateSeviceData();

//		for(int i = 0; i < aDeployment.ServiceList.size(); i ++){
//			System.out.println(aDeployment.ServiceList.get(i));
//		}

		//StoreServicetoNode();
		DistributionNode(aDeployment.ServiceList);

//		System.out.println("-------------------------------------------------");
//		for(int i = 0; i < aDeployment.ServiceList.size(); i ++){
//			System.out.println(aDeployment.ServiceList.get(i));
//		}


		XmlMining aMining = new XmlMining();  //�õ�ServiceRequest
		aMining.StoreListName();
		// String path = aMining.fileRoot + aMining.filename;
		String path = aMining.filename;
		aMining.getMap(path);

		Process(aDeployment.ServiceList,aDeployment.NodeList, aMining.ServiceRequest);


		Map<String,Object> best=new HashMap<>();
		ArrayList<Integer> newbestServiceRequest=new ArrayList<Integer>(bestServiceRequest);
		best.put("FCFSServiceRequest",newbestServiceRequest);
		List<ServiceDef> newbestServiceList=new ArrayList<ServiceDef>(bestServiceList);
		best.put("FCFSServiceList",newbestServiceList);
		best.put("FCFSErgCon",Double.valueOf(bestErgCon));
		best.put("FCFSDelayCon",Double.valueOf(bestDelayCon));

		//Determine the data returned to the front page
//		ArrayList<String> best = new ArrayList<String>();
//		best.add(bestServiceRequest.toString());
//		best.add(bestServiceList.toString());
//		best.add(String.valueOf(bestErgCon));
//		best.add(String.valueOf(bestDelayCon));

		bestServiceRequest.clear();
		bestServiceList.clear();
		bestvar = 0;
		bestErg = 0;
		bestErgCon = 0;
		bestDelayCon = 0;

		//buildErgResult("����������2" + ".xls", minErgs, ErgCons, vars, DelayCons);
		System.out.println("over");
		return(best);
	}

}
