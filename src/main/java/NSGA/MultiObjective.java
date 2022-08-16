package NSGA;

import com.basic.ServiceDef;

import java.util.ArrayList;

public class MultiObjective {
	
	static float[][]  Rig = {{1, (float) 0.047619, (float) 0.008197, (float) 0.03125, (float) 0.03125, (float) 0.023256, (float) 0.015873, (float) 0.012195},
			{1, 1, (float) 0.009804, (float) 0.08333, (float) 0.08333, (float) 0.043478, (float) 0.023256, (float) 0.016129},
			{1, 1, 1, (float) 0.00885, (float) 0.00885, (float) 0.008065, (float) 0.006944, (float) 0.006135},
			{1, 1, 1, 1, (float) 0.083333, (float) 0.083333, (float) 0.03125, (float) 0.019608},
			{1, 1, 1, 1, 1, (float) 0.083333, (float) 0.03125, (float) 0.019608},
			{1, 1, 1, 1, 1, 1, (float) 0.047619, (float) 0.025},
			{1, 1, 1, 1, 1, 1, 1, (float) 0.05},
			{1, 1, 1, 1, 1, 1, 1, 1}
			};
	
	static int[][]  Freq = {{0, 99995, 0, 0, 0, 0, 0, 0},
			{0, 0, 290025, 98568, 98567, 0, 0, 0},
			{0, 0, 0, 0, 79741, 98564, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 18827, 0, 98565, 0, 0},
			{0, 0, 0, 0, 0, 0, 98564, 0},
			{0, 0, 0, 0, 0, 0, 0, 4320},
			{0, 0, 0, 0, 0, 0, 0, 0}
			};
	
	public static float getObj1(ArrayList<ServiceDef> serviceList) {
		float p1 = 0, p2 = 2, p3 = 10;
		
		Float obj1Val = (float) 0;
		for (int i = 0; i < serviceList.size(); i++) {
			for(int j = i+1; j < serviceList.size(); j++){
				if(serviceList.get(i).getNodeid() == serviceList.get(j).getNodeid())
					obj1Val = obj1Val + p1 * Rig[i][j];
				else{
					if((serviceList.get(i).getNodeid() == 0) || (serviceList.get(j).getNodeid() == 0))  //�����һ������������
						obj1Val = obj1Val + p3 * Rig[i][j];
					else
						obj1Val = obj1Val + p2 * Rig[i][j];
				}				
			}			
		}		
		return obj1Val;
	}
	
	public static float getObj2(ArrayList<ServiceDef> serviceList) {
		float p1 = 0, p2 = 2, p3 = 10;
		
		Float obj2Val = (float) 0;
		for (int i = 0; i < serviceList.size(); i++) {
			for(int j = i+1; j < serviceList.size(); j++){
				if(serviceList.get(i).getNodeid() == serviceList.get(j).getNodeid())
					obj2Val = obj2Val + p1 * Freq[i][j] * serviceList.get(i).getDataSize();
				else{
					if((serviceList.get(i).getNodeid() == 0) || (serviceList.get(j).getNodeid() == 0))
						obj2Val = obj2Val + p3 * Freq[i][j] * serviceList.get(i).getDataSize();
					else
						obj2Val = obj2Val + p2 * Freq[i][j] * serviceList.get(i).getDataSize();
				}				
			}			
		}		
		return obj2Val;
	}

/*
	public static float evaluateErg(ArrayList<ServiceDef> serviceInsList) {
		float ergForInv = 0, ergForTrans = 0, ergForRec = 0;
		for (int i = 0; i < serviceInsList.size(); i++) {
			ergForInv += ergForInv(Variable.f_inv);
			if (i > 0) {
				ergForRec += ergForRec(serviceInsList.get(i).getDis(serviceInsList.get(i + 1)));
			}
			if (i < serviceInsList.size()) {
				ergForTrans += ergForTrans(Variable.E_k, serviceInsList.get(i).getDis(serviceInsList.get(i + 1)));
			}
		}
		return ergForInv + ergForRec + ergForTrans;
	}

		public static float ergForTrans(int bits, float dis) {
			return (float) (Variable.E_elec * bits + Variable.E_amp * bits * Math.pow(dis, Variable.E_n));
		}

		public static float ergForRec(float bits) {
			return (float) (Variable.E_elec * bits);
		}

		public static float ergForInv(int f) {
			return (float) (f * Variable.E_inv);
		}

		public static float evaluateLbf(List<NodeDef> NodeList, ArrayList<ServiceDef> candiateList) { //write by smy
			float ergSum = 0f;
			float ergTotal;  
			float E_cst = 0f;
			float ecr = 0f;
			float lbf, minLbf = 999999999;
			float sumLbf = 0f;
			
			for (NodeDef node : NodeList) {
				ergSum += Variable.E_inv / node.getRsdErg();
			}
			float thrd = ergSum / NodeList.size();
			
			for (int i = 0; i < candiateList.size(); i++) {
				E_cst = ergForInv(Variable.f_inv);      //����������Ҫ���ĵ�����  
				if (E_cst < NodeList.get(candiateList.get(i).getNodeid()).getRsdErg()) {
					ecr = E_cst / NodeList.get(candiateList.get(i).getNodeid()).getRsdErg();
					lbf = ecr / thrd;
				} else {
					lbf = Float.MIN_VALUE;
				}			
				sumLbf += lbf;
			}
			return sumLbf / candiateList.size();
		}
		
		public static float evaluatePri(List<NodeDef> hwdList,ArrayList<ServiceDef> candiateList)  //write by smy
		{
			float pri = 0f;
			float E_TR = 0f;
	        float E_td = 0f; //�ӳ�ʱ���ܺ���
	        float sumPri = 0f;
			for (int i = 0; i < candiateList.size(); i++) {
				if(i == 0)
					E_TR = ergForTrans(Variable.E_k, hwdList.get(candiateList.get(i).getNodeid()).getDis(hwdList.get(candiateList.get(i + 1).getHwdid())));
				if(i < candiateList.size()-1)
					E_TR = ergForRec(Variable.E_k) + ergForTrans(Variable.E_k, hwdList.get(candiateList.get(i).getNodeid()).getDis(hwdList.get(candiateList.get(i + 1).getHwdid())));
				if(i == candiateList.size()-1)
					E_TR = ergForRec(Variable.E_k);
				//System.out.println("Ӳ���豸��ţ�"+ hwdList.get(candiateList.get(i).getHwdid()).getId());
				if(i > 0)
					if(candiateList.get(i).getNodeid() == candiateList.get(i-1).getNodeid() ){
						E_td = Math.abs((candiateList.get(i).getTemS() - candiateList.get(i-1).getTemE())) * Variable.E_st;
						pri = (E_TR + E_td)/E_TR * (Variable.E_inv);
					}
				else{
					pri = Variable.E_inv;
				}
				sumPri += pri;
			}
			return sumPri / candiateList.size();			
		}

		public static boolean evaluateRsdErg(List<ServiceDef> serviceInsList) {
			boolean isable = true;
			float ergTotal = 0;
			for (int i = 0; i < serviceInsList.size(); i++) {
				ergTotal = ergForInv(Variable.f_inv);
				if (i > 0) {
					ergTotal += ergForRec(serviceInsList.get(i - 1).getDis(serviceInsList.get(i)));
				}
				if (i < serviceInsList.size() - 1) {
					ergTotal += ergForTrans(Variable.E_k, serviceInsList.get(i).getDis(serviceInsList.get(i + 1)));
				}

				if (ergTotal > serviceInsList.get(i).getRsdErg()) {
					isable = false;
					break;
				}
			}
			return isable;
		}

		public static float evaluateTem(List<ServiceDef> serviceInsList, int iteTemS, int iteTemE) {
			float temRet = 0f;
//			int num = 0;
			for (ServiceDef serviceIns : serviceInsList) {
				
				if(evaluateTemRel(iteTemS, iteTemE, serviceIns) > 0){
					temRet += evaluateTemRel(iteTemS, iteTemE, serviceIns)/(iteTemE - iteTemS);
//					num ++;
				}
			}
			return temRet;
		}

		public static float evaluateTemRel(int iteTemS, int iteTemE, ServiceDef serviceIns) {
			int dur1 = iteTemE - iteTemS;
			int dur2 = serviceIns.getTemE() - serviceIns.getTemS();
			int start = iteTemS < serviceIns.getTemS() ? iteTemS : serviceIns.getTemS();
			int end = iteTemE > serviceIns.getTemE() ? iteTemE : serviceIns.getTemE();
			if (end - start > dur1 + dur2) {
				return 0f;
			}
			if (end - start == Math.max(dur1, dur2)) {
				return Math.min(dur1, dur2);
			}
			return (dur1 + dur2 - (end - start)) / 2;
		}
*/
}
