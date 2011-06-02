import java.awt.Point;

public class Coups{
	Point pDep;
	Point pArr;

	public Coups(Point a, Point b){
		pDep = new Point(a.x,a.y);
		pArr = new Point(b.x,b.y);
	}

	public Coups (int lSrc, int cSrc, int lDest, int cDest) {
		pDep = new Point(lSrc, cSrc);
		pArr = new Point(lDest, cDest);
	}

	public String  toString() {
		return (pDep.x + " " + pDep.y + " " + pArr.x + " " + pArr.y + " ");
		//return (pDep.x + "," + pDep.y + " --> " + pArr.x + "," + pArr.y);
	}
}
