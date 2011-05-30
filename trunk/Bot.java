public class Bot {
	Avalam a;
	int niveau;

	public Bot (Avalam a) {
		this.a = a;
	}
	
	public void choisirBot(){
	//On choisit pour chaque niveau de l'ordinateur une partie a jouer
		switch(niveau){
			case 1 : //bot lvl1
				partieFacile();
				break;
			case 2 : // botlvl2
				partieMoyen();			
				break;
			case 3 : 
				partieDifficile();
				break;
		}
	}
	
	public void partieFacile(){
	
	}
	
	public void partieMoyen(){
	
	}
	
	public void partieDifficile(){
	
	}
}
