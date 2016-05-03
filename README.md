[![Build Status](https://travis-ci.org/dashaub/kohonen4j.svg?branch=master)](https://travis-ci.org/dashaub/kohonen4j)
# kohonen4j
Self-Organizing Maps in Java

![alt text](https://github.com/dashaub/kohonen4j/blob/master/GUI.png "User interface for constructing the Kohonen network")

The kohonen4j fits a [self-organizing map](https://en.wikipedia.org/wiki/Self-organizing_map), a type of artificial neural network, to an input csv data file. The input csv must be rectangular and nonjagged with only numeric values. As output, the program plots a heatmap that displays a 2D representation of the data. Observations are maped to their closest nodes, and the output plot displays the most frequently mapped nodes in the brightest shade, while nodes that are not maped to any observations are black.
![alt text](https://github.com/dashaub/kohonen4j/blob/master/output.png "Output from a trained network on a 5x5 map")

For training and plotting to succeed, the data must have at least two observations, at least two columns, and no more columns than rows. Furthermore, the dimensions for the training grid and epochs must be positive integers. Training on large grids or datasets and with a large number of epochs can be quite slow. 


## Building and running
Stable release can be downloaded [here](https://github.com/dashaub/kohonen4j/releases).
The current development version can be obtained by cloning the repo.

Building from source requires OpenJDK >= 1.7. To install this on Debian/Ubuntu flavors,

```
sudo apt-get install openjdk-7-jdk
```

To compile the byte code, run 
```
javac Kohonen.java
```
Run as the program as usual
```
java Kohonen
```
## License
(c) 2016 David Shaub

kohonen4j is free software, released under the terms of the [GPL-3](http://www.gnu.org/licenses/gpl-3.0.en.html) license.
