/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.worldgrower.terrain;

import java.util.Random;

// http://stackoverflow.com/questions/17440865/using-perlin-noise-to-generate-a-2d-tile-map
public class Noise {
	
	private final Random random;
	
    public Noise(int seed) {
		this.random = new Random(seed);
	}

	public double[] blend(double[] noise1, double[] noise2, double persistence)
    {
        if (noise1 != null && noise2 != null && noise1.length > 0 && noise1.length == noise2.length)
        {
            double[] result = new double[noise1.length];
            for (int i = 0; i < noise1.length; i++)
                result[i] = noise1[i] + (noise2[i] * persistence);
            return result;
        }

        return null;
    }

    public double[] normalize(double[] noise)
    {
        if (noise != null && noise.length > 0)
        {
            double[] result = new double[noise.length];

            double minValue = noise[0];
            double maxValue = noise[0];
            for (int i = 0; i < noise.length; i++)
            {
                if (noise[i] < minValue)
                    minValue = noise[i];
                else if (noise[i] > maxValue)
                    maxValue = noise[i];
            }

            for (int i = 0; i < noise.length; i++)
                result[i] = (noise[i] - minValue) / (maxValue - minValue);

            return result;
        }

        return null;
    }

    public double[] perlinNoise(int width, int height, double exponent)
    {
        int[] p = new int[width * height];
        double[] result = new double[width * height];
        /*final int[] permutation = {151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23,
                                   190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174,
                                   20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230,
                                   220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169,
                                   200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147,
                                   118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44,
                                   154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104,
                                   218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192,
                                   214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24,
                                   72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};*/

        for (int i = 0; i < p.length / 2; i++)
            p[i] = p[i + p.length / 2] = (int) (random.nextDouble() * p.length / 2);//permutation[i];

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                double x = i * exponent / width;                                // FIND RELATIVE X,Y,Z
                double y = j * exponent / height;                                // OF POINT IN CUBE.
                int X = (int) Math.floor(x) & 255;                  // FIND UNIT CUBE THAT
                int Y = (int) Math.floor(y) & 255;                  // CONTAINS POINT.
                int Z = 0;
                x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
                y -= Math.floor(y);                                // OF POINT IN CUBE.
                double u = fade(x);                                // COMPUTE FADE CURVES
                double v = fade(y);                                // FOR EACH OF X,Y,Z.
                double w = fade(Z);
                int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z,      // HASH COORDINATES OF
                        B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z;      // THE 8 CUBE CORNERS,

                result[j + i * width] = lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, Z),  // AND ADD
                                                             grad(p[BA], x - 1, y, Z)), // BLENDED
                                                     lerp(u, grad(p[AB], x, y - 1, Z),  // RESULTS
                                                          grad(p[BB], x - 1, y - 1, Z))),// FROM  8
                                             lerp(v, lerp(u, grad(p[AA + 1], x, y, Z - 1),  // CORNERS
                                                          grad(p[BA + 1], x - 1, y, Z - 1)), // OF CUBE
                                                  lerp(u, grad(p[AB + 1], x, y - 1, Z - 1), grad(p[BB + 1], x - 1, y - 1, Z - 1))));
            }
        }
        return result;
    }

    public double[] smoothNoise(int width, int height, double zoom)
    {
        if (zoom > 0)
        {
            double[] noise = whiteNoise(width, height);
            double[] result = new double[width * height];
            for (int i = 0; i < width; i++)
            {
                for (int j = 0; j < height; j++)
                {
                    double x = i / zoom;
                    double y = j / zoom;

                    // get fractional part of x and y
                    double fractX = x - (int) x;
                    double fractY = y - (int) y;

                    // wrap around
                    int x1 = ((int) x + width) % width;
                    int y1 = ((int) y + height) % height;

                    // neighbor values
                    int x2 = (x1 + width - 1) % width;
                    int y2 = (y1 + height - 1) % height;

                    // smooth the noise with bilinear interpolation
                    result[j + i * width] = fractX * fractY * noise[y1 + x1 * width]
                                            + fractX * (1 - fractY) * noise[y2 + x1 * width]
                                            + (1 - fractX) * fractY * noise[y1 + x2 * width]
                                            + (1 - fractX) * (1 - fractY) * noise[y2 + x2 * width];
                }
            }

            return result;
        }

        return null;
    }

    public double[] turbulence(int width, int height, double zoom)
    {
        // http://lodev.org/cgtutor/randomnoise.html
        double[] result = new double[width * height];
        double initialZoom = zoom;

        while (zoom >= 1)
        {
            result = blend(result, smoothNoise(width, height, zoom), zoom);
            zoom /= 2.0;
        }

        for (int i = 0; i < result.length; i++)
            result[i] = (128.0 * result[i] / initialZoom);

        return result;
    }

    public double[] whiteNoise(int width, int height)
    {
        double[] result = new double[width * height];
        for (int i = 0; i < width * height; i++)
            result[i] = random.nextDouble();
        return result;
    }

    private double fade(double t)
    {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double t, double a, double b)
    {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y, double z)
    {
        int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
        double u = h < 8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
                v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

}
