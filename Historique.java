import java.util.*;
class Historique{
	private Stack<ElemHist> annuler;
	private Stack<ElemHist> rejouer;
	
	Historique(){
		annuler = new Stack<ElemHist>();
		rejouer = new Stack<ElemHist>();
	}
	
	boolean annulerVide(){
		return annuler.empty();
	}
	boolean rejouerVide(){
		return rejouer.empty();
	}
	
	void viderRejouer(){
		rejouer = new Stack<ElemHist>();
	}
	
	void ajouterAnnuler(ElemHist e){
		annuler.push(e);
	}
	void ajouterRejouer(ElemHist e){
		rejouer.push(e);
	}
	
	int tailleAnnuler(){
		return annuler.size();
	}
	
	ElemHist annuler(){
		return annuler.pop();
	}
	ElemHist rejouer(){
		return rejouer.pop();
	}
	
	public String toString(){
		Iterator it = annuler.listIterator();
		String s = "";
		while(it.hasNext()){
			s += it.next().toString()+"\n";
		}
		return s;
	}
	
}
