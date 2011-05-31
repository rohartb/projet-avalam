import java.awt.image.RGBImageFilter;
import java.awt.Color;

public class SetColorFilter extends RGBImageFilter {
	int c;
	public SetColorFilter(Color couleur) {
		canFilterIndexColorModel = true;
		c = couleur.getRGB();
	}


	public int filterRGB(int x, int y, int rgb) {
		if ( x == -1) {

		}
		if ((rgb & 0xff000000) == 0xff000000 ) {
			return c;
		}
		return rgb;
	}
}
