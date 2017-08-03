/**
 * Created by Michael on 11/11/2016.
 * Used Implementation provided by Stefan Gustavson
 * Based on Ken Perlin's Improved Noise, Simplex Noise
 */
public class Simplex {

    private double F2 = .5 * (Math.sqrt(3.0) - 1.0);
    private double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;

    private int noiseType;

    private int[] perm;
    private int[] permMod12;
    private int[] p = {151,160,137,91,90,15,
            131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
            190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
            88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
            77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
            102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
            135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
            5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
            223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
            129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
            251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
            49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
            138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180};

    private int[][] grad3 = {{1,1,0},{-1,1,0},{1,-1,0},{-1,-1,0},
            {1,0,1},{-1,0,1},{1,0,-1},{-1,0,-1},
            {0,1,1},{0,-1,1},{0,1,-1},{0,-1,-1}};

    public Simplex() {
        perm = new int[512];
        permMod12 = new int[512];
        for(int i = 0; i < 512; i++) {
            perm[i] = p[i & 255];
            permMod12[i] = (perm[i] % 12);
        }
        noiseType = 0;
    }

    public double sumOctave(int iterations, int x, int y, int z, double persistance, double scale, int low, int high, int xOffs, int yOffs) {

        double maxAmp = 0.0;
        double amp = 1.0;
        double freq = scale;
        double noise = 0;

        for(int i = 0; i < iterations; i++) {

            double adding = noise(x * freq, y * freq, z * freq) * amp;
            noise += adding;
            maxAmp += amp;
            amp *= persistance;
            freq *= 2;

        }

        noise /= maxAmp;

        if(noiseType == 1) {
            double xPeriod = 5.0;
            double yPeriod = 10.0;
            double tCoefficient = 5.0;

            noise = x * xPeriod / 900 + y * yPeriod / 600 + tCoefficient * noise;
            double sineVal = 256 * (Math.abs(Math.sin(noise * Math.PI)));
            noise = sineVal % 256;
        }
        else if(noiseType == 2) {

            if(noise <= 0) {
                noise = 0;
            }
            else {
                noise = 255;
            }
        }
        else {
            noise = noise * (high - low) / 2 + (high + low) / 2;
        }

        return noise;

    }

    public double sumOctave(int iterations, int x, int y, double persistance, double scale, int low, int high, int xOffs, int yOffs) {

        double maxAmp = 0.0;
        double amp = 1.0;
        double freq = scale;
        double noise = 0;

        for(int i = 0; i < iterations; i++) {
            double adding = noise((x + xOffs) * freq, (y + yOffs) * freq) * amp;
            noise += adding;
            maxAmp += amp;
            amp *= persistance;
            freq *= 2;

        }

        noise /= maxAmp;

        if(noiseType == 1) {
            double xPeriod = 5.0;
            double yPeriod = 10.0;
            double tCoefficient = 5.0;

            noise = x * xPeriod / 900 + y * yPeriod / 600 + tCoefficient * noise;
            double sineVal = 256 * (Math.abs(Math.sin(noise * Math.PI)));
            noise = sineVal % 256;
        }
        else if(noiseType == 2) {

            if(noise <= 0) {
                noise = 0;
            }
            else {
                noise = 255;
            }
        }
        else {
            noise = noise * (high - low) / 2 + (high + low) / 2;
        }

        return noise;
    }
    
    public double domainWarp(int iterations, int x, int y, double persistance, double scale, int low, int high, int xOffs, int yOffs, int warpIntensity) {
    	
   	 	//int v1 = 4 * (int) sumOctave(iterations, 0, 0, persistance, scale, low, high, xOffs, yOffs);
        //int v2 = 4 * (int) sumOctave(iterations, y + xOffs, x + yOffs, persistance, scale, low, high, xOffs, yOffs);
        
    	int v1 = 4 * (int) sumOctave(iterations, x, y, persistance, scale, low, high, xOffs, yOffs);
        int v2 = 4 * (int) sumOctave(iterations, x + yOffs, y + xOffs, persistance, scale, low, high, xOffs, yOffs);
    	
        if(warpIntensity == 1) {
        	return sumOctave(iterations, x + xOffs + v1, y + yOffs + v2, persistance, scale, low, high, xOffs, yOffs);
        }
        else if(warpIntensity == 2) {
       	 	//int q1 = (int) sumOctave(iterations, x + yOffs + v1, y + xOffs + v1, 0, persistance, scale, low, high, xOffs, yOffs);
       	 	//int q2 = (int) sumOctave(iterations, x + yOffs + v2, y + xOffs + v2, 0, persistance, scale, low, high, xOffs, yOffs);
        	
        	int q1 = (int) sumOctave(iterations, x + v1, y, 0, persistance, scale, low, high, xOffs, yOffs);
       	 	int q2 = (int) sumOctave(iterations, x, y + v2, 0, persistance, scale, low, high, xOffs, yOffs);
            
            return (int) sumOctave(iterations, x + xOffs + q1, y + yOffs + q2, persistance, scale, low, high, xOffs, yOffs);
        }
        else {
        	return 155; 
        }
   }
    
    public double domainWarp(int iterations, int x, int y, int z, double persistance, double scale, int low, int high, int xOffs, int yOffs, int warpIntensity) {
    	
    	 int v1 = 4 * (int) sumOctave(iterations, 0, 0, 0, persistance, scale, low, high, xOffs, yOffs);
         int v2 = 4 * (int) sumOctave(iterations, y + xOffs, x + yOffs, z, persistance, scale, low, high, xOffs, yOffs);
         
         if(warpIntensity == 1) {
        	return sumOctave(iterations, x + xOffs + v1, y + yOffs + v2, z, persistance, scale, low, high, xOffs, yOffs);
            
         }
         else if(warpIntensity == 2) {
        	 int q1 = (int) sumOctave(iterations, x + xOffs + v1, y + yOffs + v1, 0, persistance, scale, low, high, xOffs, yOffs);
             int q2 = (int) sumOctave(iterations, x + xOffs + v2, y + yOffs + v2, 0, persistance, scale, low, high, xOffs, yOffs);
             
             return (int) sumOctave(iterations, x + xOffs + q1, y + yOffs + q2, z, persistance, scale, low, high, xOffs, yOffs);
         }
         else {
        	return 155; 
         }
    }

    public double noise(double xin, double yin) {

        double n0, n1, n2; // Noise contributions from the three corners

        // Skew the input space to determine which simplex cell we're in
        double s = (xin+yin)*F2; // Hairy factor for 2D
        int i = fastFloor(xin+s);
        int j = fastFloor(yin+s);
        double t = (i+j)*G2;
        double X0 = i-t; // Unskew the cell origin back to (x,y) space
        double Y0 = j-t;
        double x0 = xin-X0; // The x,y distances from the cell origin
        double y0 = yin-Y0;

        // For the 2D case, the simplex shape is an equilateral triangle.
        // Determine which simplex we are in.
        int i1, j1; // Offsets for second (middle) corner of simplex in (i,j) coords
        if(x0>y0) {i1=1; j1=0;} // lower triangle, XY order: (0,0)->(1,0)->(1,1)
        else {i1=0; j1=1;}      // upper triangle, YX order: (0,0)->(0,1)->(1,1)

        // A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
        // a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
        // c = (3-sqrt(3))/6
        double x1 = x0 - i1 + G2; // Offsets for middle corner in (x,y) unskewed coords
        double y1 = y0 - j1 + G2;
        double x2 = x0 - 1.0 + 2.0 * G2; // Offsets for last corner in (x,y) unskewed coords
        double y2 = y0 - 1.0 + 2.0 * G2;

        // Work out the hashed gradient indices of the three simplex corners
        int ii = i & 255;
        int jj = j & 255;
        int gi0 = permMod12[ii+perm[jj]];
        int gi1 = permMod12[ii+i1+perm[jj+j1]];
        int gi2 = permMod12[ii+1+perm[jj+1]];

        // Calculate the contribution from the three corners
        double t0 = 0.5 - x0*x0-y0*y0;
        if(t0<0) n0 = 0.0;
        else {
            t0 *= t0;
            n0 = t0 * t0 * dot(grad3[gi0], x0, y0);  // (x,y) of grad3 used for 2D gradient
        }

        double t1 = 0.5 - x1*x1-y1*y1;
        if(t1<0) n1 = 0.0;
        else {
            t1 *= t1;
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
        }

        double t2 = 0.5 - x2*x2-y2*y2;
        if(t2<0) n2 = 0.0;
        else {
            t2 *= t2;
            n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
        }

        // Add contributions from each corner to get the final noise value.
        // The result is scaled to return values in the interval [-1,1].
        return 70.0 * (n0 + n1 + n2);
    }

    public double noise(double x, double y, double z) {

        int X = fastFloor(x);
        int Y = fastFloor(y);
        int Z = fastFloor(z);

        x = x - X;
        y = y - Y;
        z = z - Z;

        X = X & 255;
        Y = Y & 255;
        Z = Z & 255;

        int gi000 = perm[X+perm[Y+perm[Z]]] % 12;
        int gi001 = perm[X+perm[Y+perm[Z+1]]] % 12;
        int gi010 = perm[X+perm[Y+1+perm[Z]]] % 12;
        int gi011 = perm[X+perm[Y+1+perm[Z+1]]] % 12;
        int gi100 = perm[X+1+perm[Y+perm[Z]]] % 12;
        int gi101 = perm[X+1+perm[Y+perm[Z+1]]] % 12;
        int gi110 = perm[X+1+perm[Y+1+perm[Z]]] % 12;
        int gi111 = perm[X+1+perm[Y+1+perm[Z+1]]] % 12;

        double n000= dot(grad3[gi000], x, y, z);
        double n100= dot(grad3[gi100], x-1, y, z);
        double n010= dot(grad3[gi010], x, y-1, z);
        double n110= dot(grad3[gi110], x-1, y-1, z);
        double n001= dot(grad3[gi001], x, y, z-1);
        double n101= dot(grad3[gi101], x-1, y, z-1);
        double n011= dot(grad3[gi011], x, y-1, z-1);
        double n111= dot(grad3[gi111], x-1, y-1, z-1);

        double u = fade(x);
        double v = fade(y);
        double w = fade(z);

        double nx00 = mix(n000, n100, u);
        double nx01 = mix(n001, n101, u);
        double nx10 = mix(n010, n110, u);
        double nx11 = mix(n011, n111, u);

        double nxy0 = mix(nx00, nx10, v);
        double nxy1 = mix(nx01, nx11, v);

        double nxyz = mix(nxy0, nxy1, w);

        return nxyz;

    }

    private static double mix(double a, double b, double t) {
        return (1-t)*a + t*b;
    }
    private static double fade(double t) {
        return t*t*t*(t*(t*6-15)+10);
    }

    private double dot(int g[], double x, double y) {
        return g[0]*x + g[1]*y;
    }

    private static double dot(int g[], double x, double y, double z) {
        return g[0]*x + g[1]*y + g[2]*z; }

    private int fastFloor(double x) {
        return x>0 ? (int)x : (int)x-1;
    }

    public void setNoiseType(int i) {
        if(i < 3) {
            noiseType = i;
        }
        else {
            noiseType = 0;
        }
        System.out.println(noiseType);
    }

}
