package NSGA;

import com.basic.NodeDef;
import com.basic.ServiceDef;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewNSGA {

	
	private ArrayList<ArrayList<ServiceDef>> wgins;
	private ArrayList<ArrayList<ServiceDef>> wgins_2;
	List<NodeDef> NodeList;
	private int scale;
	private int MAX_GEN;
	private float Pc;
	private float Pm;


	private Random random;
	private int t;

	private int bestT;

	private ArrayList<ServiceDef> bestTour;

	private ArrayList<ArrayList<ServiceDef>> oldPopulation;
	private ArrayList<ArrayList<ServiceDef>> oldPopulation_copy;
	private ArrayList<ArrayList<ServiceDef>> newPopulation;
	
	private ArrayList<ArrayList<Float>> floatList;
	private List<Float> obj1;
	private List<Float> obj2;
	private List<Integer> sortList;
	

	public NewNSGA(ArrayList<ArrayList<ServiceDef>> wgins, ArrayList<ArrayList<ServiceDef>> wgins_2, List<NodeDef> NodeList, int scale, float pc, float pm, int mAX_GEN) {   //write by smy
		super();
		this.wgins = wgins;
		this.wgins_2 = wgins_2;
		this.NodeList = NodeList;
		this.scale = scale;
		this.Pc = pc;
		this.Pm = pm;
		this.MAX_GEN = mAX_GEN;
	}
	
	public void init() {
		t = 0;
		//bestT = 0;
		//bestLength = Integer.MAX_VALUE;
		//bestTour = new ArrayList<ServiceDef>();

		newPopulation = new ArrayList<ArrayList<ServiceDef>>();
		oldPopulation = new ArrayList<ArrayList<ServiceDef>>();
		obj1 = new ArrayList<Float>();
		obj2 = new ArrayList<Float>();
	
		copyListArrayList(wgins, oldPopulation);
		oldPopulation.addAll(wgins_2);             //oldPopulation��Ⱥ��ǰscale�ǵ�һ�����壬��scale-2scale�ǵڶ�������
		//copyListArrayList(wgins_2, newPopulation);

		//floatList = new ArrayList<ArrayList<Float>>();
		//Pi = new float[scale];

		//random = new Random(System.currentTimeMillis());


//		initGroup();
		

		for (int k = 0; k < 2 * scale; k ++) {
			//fitness.add(k, getFitness(oldPopulation.get(k)));
			obj1.add(k, MultiObjective.getObj1(oldPopulation.get(k)));
			obj2.add(k, MultiObjective.getObj2(oldPopulation.get(k)));		
		}
		
		Sorting();
		//select();
		

		//countRate();
	}
	
	public void Sorting() {
		int sortNum = 0;
		int num = 0;
		sortList = new ArrayList<Integer>();
		for(int m = 0; m < oldPopulation.size(); m ++)
			sortList.add(0);
		
		oldPopulation_copy = new ArrayList<ArrayList<ServiceDef>>();
		copyListArrayList(oldPopulation, oldPopulation_copy);

		while (oldPopulation_copy.size() != 0){
			for(int i = 0; i < oldPopulation_copy.size(); i ++){
				for(int j = 0; j < oldPopulation_copy.size(); j ++){
					if(!((obj1.get(i) >= MultiObjective.getObj1(oldPopulation_copy.get(j))) && (obj2.get(i) >= MultiObjective.getObj2(oldPopulation_copy.get(j)))) ) {

						for(int k = 0; k < oldPopulation.size(); k ++){
							if(oldPopulation_copy.get(i) == oldPopulation.get(k))
								num = k;
						}
						sortList.set(num, sortNum);
						//System.out.println("remove");
						//System.out.println(sortNum);
						oldPopulation_copy.remove(oldPopulation_copy.get(i));
					}
				}
				sortNum ++;
			}
			//sortNum ++;
//			System.out.println("oldPopluation_copy.size() = " + oldPopulation_copy.size());
		}
//		System.out.println("-------------------------------�������--------------------------------");
	}


	public void selectBest() {

		System.out.println("selectBest");
		int sortNum = 0;
		int num = 0;
		
		ArrayList<ArrayList<ServiceDef>> newPopulation_copy = new ArrayList<ArrayList<ServiceDef>>();
		copyListArrayList(newPopulation, newPopulation_copy);
		
		while (newPopulation_copy.size() != 0){
		
			for(int i = 0; i < newPopulation_copy.size(); i++){
				for(int j = 0; j < newPopulation_copy.size(); j++){
					if(!((obj1.get(i) > MultiObjective.getObj1(newPopulation_copy.get(j))) && (obj2.get(i) > MultiObjective.getObj2(newPopulation_copy.get(j)))) ) {
						for(int k = 0; k < newPopulation.size(); k++){
							if(newPopulation_copy.get(i) == newPopulation.get(k))
								{num = k;}
						}
						sortList.set(num, sortNum);
						newPopulation_copy.remove(newPopulation_copy.get(i));
					}
				}
				sortNum ++;
			}
			
		}
		
		List<ArrayList<ServiceDef>> result = new ArrayList<ArrayList<ServiceDef>>();
		for(int k = 0; k < scale; k ++) {
			if(sortList.get(k) == 0) {
				result.add(newPopulation.get(k));
			}
		}
		
//		for(int m = 0; m < result.size(); m ++){
//			System.out.println("result printf");
//			System.out.println(result.get(m));
//		}
		
		Random random = new Random(System.currentTimeMillis());
		int randNum = random.nextInt(result.size());
		bestTour = result.get(randNum);			
	}
	
	public void select() {
		int sortNum = 0;
		
		for(int i = 0; i < sortList.size(); i ++){
			System.out.print(sortList.get(i));
		}
		System.out.println();
		
		while (newPopulation.size() < scale){
			//System.out.println("1111");
			for (int i = 0; i < oldPopulation.size(); i ++){
				//System.out.println("2222"+oldPopulation.size());
				if (sortList.get(i) == sortNum){
					//System.out.println("3333");
					newPopulation.add(oldPopulation.get(i));
//					System.out.println(oldPopulation.get(i));
				}
			}
			sortNum ++;
		}
		if(newPopulation.size() > scale){
			for(int j = newPopulation.size()-1; j > scale; j --) {
				newPopulation.remove(j);
			}				
		}
//		System.out.println("printf newPopulation");
//		for(int i = 0; i < newPopulation.size(); i ++){
//			System.out.println(newPopulation.get(i));
//		}
	}
	

	public void evolution() {
		int k;

		select();

		Random random = new Random(System.currentTimeMillis());
		float r;

		for (k = 1; k < scale / 2 - 1; k += 2) {
			r = random.nextFloat();
			if (r < Pc) {
				OXCross(k, k + 1);

				r = random.nextFloat();
			}
		}


	}


	public void OXCross(int k1, int k2) {
		Random random = new Random(System.currentTimeMillis());
		
		int minLength = newPopulation.get(k1).size() < newPopulation.get(k2).size() ? newPopulation.get(k1).size()
				: newPopulation.get(k2).size();
		minLength--;
		int ran1 = random.nextInt(65535) % minLength;
		int ran2 = random.nextInt(65535) % minLength;
		while (ran1 > ran2) {
			ran1 = random.nextInt(65535) % minLength;
			ran2 = random.nextInt(65535) % minLength;
		}
		// ����ran1��ran2֮��ģ�����ran1��ran2
		ArrayList<ServiceDef> tmp1 = newPopulation.get(k1);
		ArrayList<ServiceDef> tmp2 = newPopulation.get(k2);
		for (int i = ran1; i < ran2; i++) {

			tmp1.set(i, wgins.get(k1).get(ran1));
			tmp2.set(i, wgins.get(k2).get(ran2));
		}
		newPopulation.set(k1, tmp1);
		newPopulation.set(k2, tmp2);
	}

	public void solve() {
		int i,k;
		
		ArrayList<ArrayList<ServiceDef>> oldPopulation_copy2 = new ArrayList<ArrayList<ServiceDef>>();
		copyListArrayList(oldPopulation, oldPopulation_copy2);
		

		for (t = 0; t < MAX_GEN; t++) {
			evolution();
			
			oldPopulation.clear();
			oldPopulation.addAll(newPopulation);
			for(int m = scale; m < 2 * scale; m ++){
				oldPopulation.add(m, oldPopulation.get(m));
			}
			
			for (k = 0; k < 2 * scale; k++) {
				obj1.set(k, MultiObjective.getObj1(oldPopulation.get(k)));
				obj2.set(k, MultiObjective.getObj2(oldPopulation.get(k)));
			}
			//countRate();
		}

//		System.out.println("�����Ⱥ...");


		
		selectBest();
//		System.out.println("���·����");
		for (i = 0; i < bestTour.size(); i++) {
			System.out.print(bestTour.get(i).getNodeid());
		}
			
	}

	public void copyFloatList(List<Float> from, List<Float> to) {
		for (int i = 0; i < from.size(); i++) {
			to.add(i, from.get(i));
		}
	}

	public void copyListArrayList(List<ArrayList<ServiceDef>> from, List<ArrayList<ServiceDef>> to) {
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

	public void copyArrayList(ArrayList<ServiceDef> from, ArrayList<ServiceDef> to) {
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
	
	//write by smy
/*
	public float getObj1(ArrayList<ServiceDef> candidateSIlist) {
		float fitness = 0;
		float erg = 0;    //�������������ĵ�����
		for (int i = 0; i < candidateSIlist.size(); i++) {
			float ergConsumed = erg;
			erg += MultiObject.ergForInv(Variable.f_inv);
			if (i == 0) {
				erg += MultiObject.ergForTrans(Variable.E_k, candidateSIlist.get(i).getDis(candidateSIlist.get(i + 1)));
			} else if (i < candidateSIlist.size() - 1) {
				erg += MultiObject.ergForTrans(Variable.E_k, candidateSIlist.get(i).getDis(candidateSIlist.get(i + 1)));
				erg += MultiObject.ergForRec(Variable.E_k);
			} else {
				erg += MultiObject.ergForRec(Variable.E_k);
			}

			ergConsumed = erg - ergConsumed;
			if (ergConsumed > candidateSIlist.get(i).getRsdErg()) {
				erg = Float.MAX_VALUE;
				return Float.MAX_VALUE;
			}
		}
		float lbf = MultiObject.evaluateLbf(hwdList, candidateSIlist);
		float pri = MultiObject.evaluatePri(hwdList, candidateSIlist);
		float tem = MultiObject.evaluateTem(candidateSIlist, iteDuration.getIteS(), iteDuration.getIteE());
		float loc = MultiObject.evaluateLoc(candidateSIlist, iteRound);

		w1 = 0.4f;
		w2 = 0.3f;
		w3 = 0.3f;
		alpha = 0.4f;
		beta = 0.6f;
		phi = 0.5f;
		delta = 0.5f;
		fitness += w1 * erg * 1.0/ Variable.initRsdErg
				+ w2 * (1.0 * alpha * pri + 1.0 * beta * lbf) 
				+ w3 * (-1.0) * ( 1.0 * phi * loc + 1.0 * delta * tem);
		return fitness;
	}
*/	
	public ArrayList<ServiceDef> getPgd() {
		return bestTour;
	}

//	public float getvPgd() {
//		return bestLength;
//	}

	public static void main(String[] args) {

	}

}
