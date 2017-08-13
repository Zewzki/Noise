import java.util.Random;

/**
 * Created by Michael on 11/11/2016.
 */
public class Perlin {

	private int interpType;// 0 linear, 1 cosine, 2 cubic
	private int seed = 10;
	private int noiseType;
	Random rand = new Random(10);

	private double[][][] gradient;

	public Perlin() {

		interpType = 0;

		noiseType = 3;

		for (int i = 0; i < 256; i++) {
			p[256 + i] = p[i] = permutation[i];
		}

		gradient = new double[1000][1000][2];

		for (int i = 0; i < gradient.length; i++) {
			for (int j = 0; j < gradient[0].length; j++) {

				int theta = rand.nextInt(360);

				gradient[i][j][0] = Math.cos(theta);
				gradient[i][j][1] = Math.sin(theta);

			}
		}

	}

	public double sumOctave(int octave, int x, int y, int z, double persistance, double scale, int low, int high) {

		double maxAmp = 0.0;
		double amp = 1.0;
		double freq = scale;
		double noise = 0;

		for (int i = 0; i < octave; i++) {

			double adding = noise(x * freq, y * freq, z * freq) * amp;
			noise += adding;
			maxAmp += amp;
			amp *= persistance;
			freq *= 2;

		}

		if (noiseType == 2) {

			if (noise <= 0) {
				return 0;
			} else {
				return 255;
			}

		}
		else {
			noise = noise * (high - low) / 2 + (high + low) / 2;
			return noise;
		}
	}

	public double sumOctave(int octave, int x, int y, double persistance, double scale, int low, int high) {

		double maxAmp = 0.0;
		double amp = 1.0;
		double freq = scale;
		double noise = 0;

		for (int i = 0; i < octave; i++) {

			double adding = noise(x * freq, y * freq) * amp;
			noise += adding;
			maxAmp += amp;
			amp *= persistance;
			freq *= 2;
		}

		noise /= maxAmp;

		// noise = 177.5;

		if (noiseType == 2) {

			if (noise <= 0) {
				return 0;
			} else {
				return 255;
			}

		} else {

			noise = noise * (high - low) / 2 + (high + low) / 2;

			return noise;
		}
	}
	
	public double noise(double x, double y, double z) {
		
		if(noiseType == 0) {
			return classicPerlin(x, y, z);
		}
		else if(noiseType == 1) {
			//wood somehow?
			return classicPerlin(x, y, z);
		}
		else if(noiseType == 2) {
			return classicPerlin(x, y, z);
		}
		else {
			noiseType = 0;
			return classicPerlin(x, y, z);
		}
		
	}

	public double noise(double x, double y) {

		if (noiseType == 0) {
			return classicPerlin(x, y, 0);
		} else if (noiseType == 1) {
			return woodNoise(x, y);
		} else if (noiseType == 2) {
			return classicPerlin(x, y, 0);
		} else {
			noiseType = 0;
			return classicPerlin(x, y, 0);
		}

	}

	public double generalInterpolation(double y1, double y2, double mu) {

		if (interpType == 0) {
			return linearInterpolate(y1, y2, mu);
		} else if (interpType == 1) {
			return cosineInterpolate(y1, y2, mu);
		} else if (interpType == 2) {
			return cubicInterpolate(y1, y2, .5 * y1, 2 * y2, mu);
		}

		return 0;
	}

	private double woodNoise(double x, double y) {

		int xRanged = (int) Math.floor(x) & 255;
		int yRanged = (int) Math.floor(y) & 255;

		double xPos = x - Math.floor(x);
		double yPos = y - Math.floor(y);

		double xFade = fade(xPos);
		double yFade = fade(yPos);

		// HASH COORDINATES
		int A = p[xRanged] + yRanged;
		int B = p[xRanged + 1] + yRanged;
		int C = p[yRanged] + xRanged;
		int D = p[yRanged + 1] + xRanged;

		double gradA = grad(p[A], x, y);
		double gradB = grad(p[B], x, y);
		double gradC = grad(p[C], x, y);
		double gradD = grad(p[D], x, y);

		double lerp1 = generalInterpolation(xFade, gradA, gradB);
		double lerp2 = generalInterpolation(xFade, gradC, gradD);

		double finalLerp = generalInterpolation(yFade, lerp1, lerp2);

		finalLerp = Math.abs(finalLerp);
		finalLerp %= 2;
		finalLerp -= 1;

		// finalLerp %= 1;

		// System.out.println(finalLerp);

		return finalLerp;
	}

	public double classicPerlin(double x, double y, double z) {

		int X = (int) Math.floor(x) & 255, // FIND UNIT CUBE THAT
				Y = (int) Math.floor(y) & 255, // CONTAINS POINT.
				Z = (int) Math.floor(z) & 255;
		x -= Math.floor(x); // FIND RELATIVE X,Y,Z
		y -= Math.floor(y); // OF POINT IN CUBE.
		z -= Math.floor(z);
		double u = fade(x), // COMPUTE FADE CURVES
				v = fade(y), // FOR EACH OF X,Y,Z.
				w = fade(z);
		int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z, // HASH COORDINATES
															// OF
				B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z; // THE 8
																	// CUBE
																	// CORNERS,

		return generalInterpolation(w,
				generalInterpolation(v,
						generalInterpolation(u, grad(p[AA], x, y, z), // AND ADD
								grad(p[BA], x - 1, y, z)), // BLENDED
						generalInterpolation(u, grad(p[AB], x, y - 1, z), // RESULTS
								grad(p[BB], x - 1, y - 1, z))), // FROM 8
				generalInterpolation(v,
						generalInterpolation(u, grad(p[AA + 1], x, y, z - 1), // CORNERS
								grad(p[BA + 1], x - 1, y, z - 1)), // OF CUBE
						generalInterpolation(u, grad(p[AB + 1], x, y - 1, z - 1),
								grad(p[BB + 1], x - 1, y - 1, z - 1))));

	}

	private double fade(double x) {
		return x * x * x * (x * (x * 6 - 15) + 10);
	}

	private double sCurve(double x) {
		return x * x * (3 - 2 * x);
	}

	private double grad(int hash, double x, double y) {
		int h = hash & 15;
		double u = h < 8 ? x : y;
		return ((h & 1) == 0 ? u : -u);
	}

	static double grad(int hash, double x, double y, double z) {
		int h = hash & 15; // CONVERT LO 4 BITS OF HASH CODE
		double u = h < 8 ? x : y, // INTO 12 GRADIENT DIRECTIONS.
				v = h < 4 ? y : h == 12 || h == 14 ? x : z;
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}

	private double linearInterpolate(double y1, double y2, double mu) {
		return y2 + y1 * (mu - y2);
	}

	private double cosineInterpolate(double y1, double y2, double mu) {
		double mu2 = (1 - Math.cos(mu * Math.PI)) / 2;
		return (y1 * (1 - mu2) + y2 * mu2);
	}

	private double cubicInterpolate(double y0, double y1, double y2, double y3, double mu) {
		double mu2 = mu * mu;
		double a0 = y3 - y2 - y0 + y1;
		double a1 = y0 - y1 - a0;
		double a2 = y2 - y0;
		double a3 = y1;
		return (a0 * mu * mu2 + a1 * mu2 + a2 * mu2 + a3);
	}

	public void setNoiseType(int i) {

		if (i < 3) {
			noiseType = i;
		} else {
			noiseType = 0;
		}

	}

	public void setInterpType(int i) {

		if (i < 3) {
			interpType = i;
		} else {
			interpType = 0;
		}
	}

	private int[] p = new int[512];
	private int[] permutation = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103,
			30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203,
			117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134,
			139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245,
			40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135,
			130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147,
			118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119,
			248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79,
			113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162,
			241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121,
			50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61,
			156, 180 };

}
