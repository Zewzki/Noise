# Noise
Noise Experiments

An Imgur post with some of the results.
http://imgur.com/a/oMHfP

Several months ago, I became very interested in the concept of "smooth noise" and the popular implementations such as Perlin, Simplex, and Open Simplex noise. I created this application to experiment with several aspects of noise that I had seen on the internet. The project slowly became more and more flushed out as learned about noise.

Basics:
	The goal here is to generate values that are "smoothe" random. The images displayed are not hard coded in any way. Instead, several algorithms are used that can translate x and y coordinates into a value between 0 and 255. Pixels near eachother will have similiar values.

Dimension:
	2D should be fairly self explanatory. It appears as a 2D plane that shifts across the screen.
	3D is displayed as a 2D cross section. As the application moves through time, a different cross section is displayed

Octaves:
	This term is used to describe how "fractal" the noise is. The noise will be summed n times, where n = octaves. For each n, the frequency will be doubled and the max amplitude will be increased by the persistance, then a noise value generated with these two altered values will be added to the running sum.
	
Persistance:
	This can be thought of as how grainy the image is, or the level of definition. A high persistance will grant a very high definition result while a low persistance will generate very smooth and low definition results.

Frequency:
	This is essentially the zoom of image. The lower the frequency, the more zoomed in the image will appear.
	
Perlin:
	This is the original algorithm used to generate these effects, created by Ken Perlin. It has since been updated and modified to run faster. Simplex noise took the place of original perlin.
	
Simplex:
	Simplex noise is the new and improved version of Perlin noise. It is faster and more elegant than the original, however it is under copyright so it can't be used as easily.
	
Open Simplex:
	The open source, non copyright version of Simplex. Not quite as fast, but definitly effective.
