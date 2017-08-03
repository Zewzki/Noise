import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Michael on 11/11/2016.
 */
public class NPanel extends JPanel {

    private int panelWidth;
    private int panelHeight;

    private int sWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int sHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    private final int MAX = 255;
    private final int MIN = 0;

    private int menuType;//0 = dimension selector, 1 = noise selector, 2 = noise customizer

    Simplex simplex ;
    Perlin perlin;
    OpenSimplex openSimplex;

    Color[][] shifted;
    Color[][] colors;

    private int dimension;//0 = 2d, 1 = 3d
    private int noiseType;//0 = perlin, 1 = simplex, 2 - open simplex

    private int xDistance;
    private int yDistance;
    private int zDistance;
    private int xOffs = 1;
    private int yOffs = 1;
    private int zOffs = 3;
    
    private int redValue;
    private int greenValue;
    private int blueValue;

    private double smooth;

    private int octaves ;
    private double persistance;

    private boolean domainWarp;
    private int domainWarpIntensity;
    
    private int colorSetting;

    private boolean optionsVisible;

    private nButton toggleView;
    private nButton usePerlin;
    private nButton basicPerlin;
    private nButton useSimplex;
    private nButton basicSimplex;
    private nButton useOpenSimplex;
    private nButton basicOpenSimplex;
    private nButton marble;
    private nButton wood;
    private nButton perlinStep;
    private nButton simplexStep;
    private nButton domainToggle;
    private nButton colorToggle;

    private JLabel customize;
    private JLabel perlinLabel;
    private JLabel simplexLabel;
    private JLabel openSimplexLabel;
    private JLabel octaveLabel;
    private JLabel persistanceLabel;
    private JLabel smoothLabel;
    
    private JLabel redLabel;
    private JLabel greenLabel;
    private JLabel blueLabel;
    
    private nSlide redSlide;
    private nSlide greenSlide;
    private nSlide blueSlide;

    private nSlide octaveSlide;
    private nSlide persistanceSlide;
    private JSlider smoothSlide;

    private JButton threeD;
    private JButton twoD;

    private JButton back;

    private Font font = new Font("Arial Black", Font.PLAIN, 32);
    private Color fontColor = new Color(0, 18, 255);
    private Color transparent = new Color(0, 0, 0, 0);


    public NPanel(int x, int y) {

        panelWidth = x;
        panelHeight = y;

        octaves = 8;
        persistance = .6;
        smooth = .01;
        
        redValue = 255;
        greenValue = 180;
        blueValue = 0;

        initializeComponents();

        noiseType = 1;

        simplex = new Simplex();
        perlin = new Perlin();
        openSimplex = new OpenSimplex();
        colors = new Color[sWidth + 100][sHeight + 100];

        xDistance = 0;
        yDistance = 0;
        zDistance = 0;

        optionsVisible = true;
        
        domainWarp = false;
        domainWarpIntensity = 0;
        
        colorSetting = 0;

        menuType = 0;

        ///////LAYOUT//////////////////

        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        gc.anchor = GridBagConstraints.CENTER;

        gc.weightx = .5;
        gc.weighty = .5;

        /////Menu 0 | Dimension Selector/////

        gc.gridx = 0;
        gc.gridy = 0;
        add(twoD, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        add(threeD, gc);
        
        /////Menu 1 | Noise Selector/////

        gc.gridx = 0;
        gc.gridy = 0;
        add(usePerlin, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        add(useSimplex, gc);

        gc.gridx = 2;
        gc.gridy = 0;
        add(useOpenSimplex, gc);

        /////Menu 2| Noise Customizer/////

        //Perlin 2d//

        gc.gridx = 0;
        gc.gridy = 0;
        add(perlinLabel, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        add(basicPerlin, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        add(wood, gc);

        gc.gridx = 0;
        gc.gridy = 5;
        add(perlinStep, gc);

        //Simplex 2d//

        gc.gridx = 0;
        gc.gridy = 0;
        add(simplexLabel, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        add(basicSimplex, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        add(marble, gc);

        gc.gridx = 0;
        gc.gridy = 5;
        add(simplexStep, gc);
        
        gc.gridx = 0;
        gc.gridy = 6;
        add(domainToggle, gc);

        //Open Simplex 2d//

        gc.gridx = 0;
        gc.gridy = 0;
        add(openSimplexLabel, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        add(basicOpenSimplex, gc);

        /////THESE DO NOT CHANGE ACROSS NOISE ADJUSTOR PANELS/////

        gc.gridx = 1;
        gc.gridy = 0;
        add(customize, gc);
        
        gc.gridx = 1;
        gc.gridy = 2;
        add(octaveLabel, gc);
        
        gc.gridx = 1;
        gc.gridy = 3;
        add(octaveSlide, gc);
        
        gc.gridx = 1;
        gc.gridy = 4;
        add(persistanceLabel, gc);
        
        gc.gridx = 1;
        gc.gridy = 5;
        add(persistanceSlide, gc);
        
        gc.gridx = 1;
        gc.gridy = 6;
        add(smoothLabel, gc);
        
        gc.gridx = 1;
        gc.gridy = 7;
        add(smoothSlide, gc);

        gc.gridx = 0;
        gc.gridy = 8;
        add(back, gc);
        
        gc.gridx = 2;
        gc.gridy = 2;
        add(redLabel, gc);
        
        gc.gridx = 2;
        gc.gridy = 3;
        add(redSlide, gc);
        
        gc.gridx = 2;
        gc.gridy = 4;
        add(greenLabel, gc);
        
        gc.gridx = 2;
        gc.gridy = 5;
        add(greenSlide, gc);
        
        gc.gridx = 2;
        gc.gridy = 6;
        add(blueLabel, gc);
        
        gc.gridx = 2;
        gc.gridy = 7;
        add(blueSlide, gc);
        
        gc.gridx = 2;
        gc.gridy = 8;
        add(colorToggle, gc);

        initializeArray();
        updateMenu();

        shifted = colors;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int x = 0; x < panelWidth; x++) {
            for(int y = 0; y < panelHeight; y++) {
                try {
                    g.setColor(colors[x][y]);
                    g.fillRect(x, y, 1, 1);
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    //System.out.println("Pausing While Resizing");
                }

            }
        }

    }

    public void refresh(int x, int y, boolean[] keys) {
        try {
            updateColors();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            //System.out.println("Pausing While Resizing");
        }

        if(keys[KeyEvent.VK_S]) {
            snapShot();
        }

        if(dimension == 0) {
            xDistance += xOffs;
            yDistance += yOffs;
        }
        else {
            zDistance += zOffs;
        }
        repaint();
        panelWidth = x;
        panelHeight = y;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
    }

    private void updateColors() throws ArrayIndexOutOfBoundsException{

        if(dimension == 0) {
            for(int x = 0; x < panelWidth - xOffs; x++) {
                for(int y = 0; y < panelHeight - yOffs; y++) {

                    shifted[x][y] = colors[x+xOffs][y+yOffs];

                }
            }

            if(noiseType == 0) {
                for(int y = 0; y < panelHeight; y++) {
                    for (int x = panelWidth - xOffs; x < panelWidth; x++) {
                        int result = (int) perlin.sumOctave(octaves, x + xDistance, y + yDistance, persistance, smooth, MIN, MAX);
                        setColor(x, y, result);
                    }
                }
                for(int x = 0; x < panelWidth - xOffs; x++) {
                    for(int y = panelHeight - yOffs; y < panelHeight; y++) {
                        int result = (int) perlin.sumOctave(octaves, x + xDistance, y + yDistance, persistance, smooth, MIN, MAX);
                        setColor(x, y, result);
                    }
                }
            }

            else if(noiseType == 1) {

                for (int y = 0; y < panelHeight; y++) {
                    for (int x = panelWidth - xOffs; x < panelWidth; x++) {
                        int result;

                        if(domainWarp) {

                            result = (int) simplex.domainWarp(octaves, x + xDistance, y + yDistance, persistance, smooth, MIN, MAX, xOffs, yOffs, domainWarpIntensity);

                        }
                        else {
                            result = (int) simplex.sumOctave(octaves, x + xDistance, y + yDistance, persistance, smooth, MIN, MAX, xOffs, yOffs);
                        }

                        setColor(x, y, result);
                    }
                }

                for (int x = 0; x < panelWidth - xOffs; x++) {
                    for (int y = panelHeight - yOffs; y < panelHeight; y++) {
                        int result;

                        if(domainWarp) {
                            
                            result = (int) simplex.domainWarp(octaves, x + xDistance, y + yDistance, persistance, smooth, MIN, MAX, xOffs, yOffs, domainWarpIntensity);

                        }
                        else {
                            result = (int) simplex.sumOctave(octaves, x + xDistance, y + yDistance, persistance, smooth, MIN, MAX, xOffs, yOffs);
                        }
                        setColor(x, y, result);
                    }
                }
            }

            else if(noiseType == 2) {
                for(int y = 0; y < panelHeight; y++) {
                    for (int x = panelWidth - xOffs; x < panelWidth; x++) {
                        int result = (int) openSimplex.sumOctave(octaves, x + xDistance, y + yDistance, persistance, smooth, MIN, MAX, xOffs, yOffs);
                        setColor(x, y, result);
                    }
                }
                for(int x = 0; x < panelWidth - xOffs; x++) {
                    for(int y = panelHeight - yOffs; y < panelHeight; y++) {
                        int result = (int) openSimplex.sumOctave(octaves, x + xDistance, y + yDistance, persistance, smooth, MIN, MAX, xOffs, yOffs);
                        setColor(x, y, result);
                    }
                }
            }
            colors = shifted;
        }

        else if(dimension == 1) {

            if(noiseType == 0) {

                for(int y = 0; y < panelHeight; y++) {

                    for(int x = 0; x < panelWidth; x++) {

                        int result = (int) perlin.sumOctave(octaves, x + xDistance, y + yDistance, zDistance, persistance, smooth, MIN, MAX);
                        setColor(x, y, result);

                    }

                }

            }
            else if(noiseType == 1) {

                for(int y = 0; y < panelHeight; y++) {

                    for(int x = 0; x < panelWidth; x++) {

                        int result;

                        if(domainWarp) {
                           

                            result = (int) simplex.domainWarp(octaves, x + xDistance, y + yDistance, zDistance, persistance, smooth, MIN, MAX, xOffs, yOffs, domainWarpIntensity);

                        }
                        else {
                            result = (int) simplex.sumOctave(octaves, x + xDistance, y + yDistance, zDistance, persistance, smooth, MIN, MAX, xOffs, yOffs);
                        }
                        setColor(x, y, result);
                    }

                }

            }
            else if(noiseType == 2) {
            	
            	for(int y = 0; y < panelHeight; y++) {
            		
            		for(int x = 0; x < panelWidth; x++) {
            			
            			int result;
            			
            			result = (int) openSimplex.sumOctave(octaves, x + xDistance, y + yDistance, zDistance, persistance, smooth, MIN, MAX, xOffs, yOffs);
            			
            			setColor(x, y, result);
            		}
            		
            	}
            	
            }


        }

    }

    private void initializeArray() {

        if(noiseType == 0) {
            System.out.println("INITIALIZED WITH PERLIN");
        }
        else if(noiseType == 1) {
            System.out.println("INITIALIZED WITH SIMPLEX");
        }
        else if(noiseType == 2) {
            System.out.println("INITIALIZED WITH OPEN SIMPLEX");
        }

        for(int x = 0; x < panelWidth; x++) {
            for(int y = 0; y < panelHeight; y++) {

                if(noiseType == 0) {//PERLIN
                    int result = (int) perlin.sumOctave(octaves, x, y, persistance, smooth, MIN, MAX);
                    setColor(x, y, result);
                }
                else if(noiseType == 1) {//SIMPLEX

                    int result;
                    result = (int) simplex.sumOctave(octaves, x, y, persistance, smooth, MIN, MAX, xOffs, yOffs);
                    setColor(x, y, result);
                }
                else if(noiseType == 2) {//OPEN SIMPLEX

                    int result = (int) openSimplex.sumOctave(octaves, x, y, persistance, smooth, MIN, MAX, xOffs, yOffs);
                    setColor(x, y, result);
                }
            }
        }
    }

    private void setColor(int x, int y, int result) {
        
        try {
        	
        	if(colorSetting == 1) {
        		colors[x][y] = new Color(result, result, result); //GRAYSCALE
        	}
        	else if(colorSetting == 2) {
        		result = (int)map(result, 0, 255, 60, 190); //LAVA
        		colors[x][y] = new Color(255, result, 20); // LAVA
        	}
        	else if(colorSetting == 3) {
        		colors[x][y] = new Color(0, result, result); //OCEAN
        	}
        	else if(colorSetting == 4) {
        		result = (int)map(result, 0, 255, 0, 25); //CLOUD
        		colors[x][y] = new Color(3 + (result * 10), 154 + (result *  4), 229 + result); //CLOUD
        	}
        	else if(colorSetting == 5) {
        		result = (int)map(result, 0, 255, 60, 255); //WOOD
        		colors[x][y] = new Color(result, result - 20, result - 40); // WOOD
        	}
        	else if(colorSetting == 0) {
        		
        		int rVal = (int)map(result, 0, 255, 0, redValue);
            	int gVal = (int)map(result, 0, 255, 0, greenValue);
            	int bVal = (int)map(result, 0, 255, 0, blueValue);
            	
            	colors[x][y] = new Color(rVal, gVal, bVal);
        		
        	}
            
        } catch(IllegalArgumentException e) {
            colors[x][y] = new Color(100, 255, 0);
        }

    }

    public double map(double input, int min1,int max1, int min2, int max2) {
        return (((input - min1) / (max1 - min1)) * (max2 - min2)) + min2;
    }

    public void snapShot() {
        BufferedImage output = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < panelWidth; x++) {
            for(int y = 0; y < panelHeight; y++) {
                int rgb = 0;
                try {
                    rgb = colors[x][y].getRGB();
                }
                catch(NullPointerException e) {
                    return;
                }
                output.setRGB(x, y, rgb);
            }
        }
        try {
            File f = new File("C:\\Users\\Michael\\eclipse\\java-neon\\eclipse\\IJWorkspace\\Workspace\\Noise\\newOutputs\\output" + zDistance + ".jpg");
            ImageIO.write(output, "jpg", f);
            System.out.println("IMAGE SAVED");
        } catch (IOException e) {
            System.out.println("ERROR SAVING IMAGE");
        }

    }

    private void initializeComponents() {

        ////JLABELS////

        perlinLabel = new JLabel("Perlin");
        perlinLabel.setFont(font);
        perlinLabel.setForeground(fontColor);

        simplexLabel = new JLabel("Simplex");
        simplexLabel.setFont(font);
        simplexLabel.setForeground(fontColor);

        openSimplexLabel = new JLabel("Open Simplex");
        openSimplexLabel.setFont(font);
        openSimplexLabel.setForeground(fontColor);

        octaveLabel = new JLabel("Octaves Summed");
        octaveLabel.setFont(font);
        octaveLabel.setForeground(fontColor);

        persistanceLabel = new JLabel("Persistance");
        persistanceLabel.setFont(font);
        persistanceLabel.setForeground(fontColor);

        smoothLabel = new JLabel("Smoothness");
        smoothLabel.setFont(font);
        smoothLabel.setForeground(fontColor);

        customize = new JLabel("Customize");
        customize.setFont(font);
        customize.setForeground(fontColor);
        
        redLabel = new JLabel("Red");
        redLabel.setFont(font);
        redLabel.setForeground(fontColor);
        
        greenLabel = new JLabel("Green");
        greenLabel.setFont(font);
        greenLabel.setForeground(fontColor);
        
        blueLabel = new JLabel("Blue");
        blueLabel.setFont(font);
        blueLabel.setForeground(fontColor);

        ////BUTTONS////

        twoD = new nButton("2D");
        twoD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dimension = 0;
                menuType++;
                updateMenu();
            }
        });

        threeD = new nButton("3D");
        threeD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dimension = 1;
                menuType++;
                updateMenu();
            }
        });

        usePerlin = new nButton("Perlin");
        usePerlin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noiseType = 0;
                perlin.setNoiseType(0);
                menuType++;
                updateMenu();
            }
        });

        basicPerlin = new nButton("Basic Perlin");
        basicPerlin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noiseType = 0;
                perlin.setNoiseType(0);
            }
        });

        useSimplex = new nButton("Simplex");
        useSimplex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noiseType = 1;
                simplex.setNoiseType(0);
                menuType++;
                updateMenu();
            }
        });

        basicSimplex = new nButton("Basic Simplex");
        basicSimplex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noiseType = 1;
                simplex.setNoiseType(0);
            }
        });

        useOpenSimplex = new nButton("Open Simplex");
        useOpenSimplex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noiseType = 2;
                menuType++;
                updateMenu();
            }
        });

        basicOpenSimplex = new nButton("Basic Open Simplex");
        basicOpenSimplex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSimplex.setNoiseType(0);
            }
        });

        toggleView = new nButton("Hide/Show Tools");
        toggleView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleVisibility();
            }
        });

        marble = new nButton("Marble");
        marble.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noiseType = 1;
                simplex.setNoiseType(1);
            }
        });

        wood = new nButton("Wood");
        wood.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                noiseType = 0;
                perlin.setNoiseType(1);
            }
        });

        perlinStep = new nButton("Step");
        perlinStep.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                noiseType = 0;
                perlin.setNoiseType(2);
            }

        });

        simplexStep = new nButton("Step");
        simplexStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noiseType = 1;
                simplex.setNoiseType(2);
            }
        });

        back = new nButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuType--;
                updateMenu();
            }
        });
        
        domainToggle = new nButton("Domain Warp: 0");
        domainToggle.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if(domainWarpIntensity < 2) {
        			domainWarpIntensity++;
        			domainWarp = true;
        		}
        		else {
        			domainWarpIntensity = 0;
        			domainWarp = false;
        		}
        		domainToggle.setText("Domain Warp: " + domainWarpIntensity);
        	}
        });
        
        colorToggle = new nButton("Sliders");
        colorToggle.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if(colorSetting < 5) {
        			colorSetting++;
        		}
        		else {
        			colorSetting = 0;
        		}
        		if(colorSetting == 0) {
        			colorToggle.setText("Sliders");
        		}
        		else if(colorSetting == 1) {
        			colorToggle.setText("Grayscale");
        		}
        		else if(colorSetting == 2) {
        			colorToggle.setText("Lava");
        		}
        		else if(colorSetting == 3) {
        			colorToggle.setText("Ocean?");
        		}
        		else if(colorSetting == 4) {
        			colorToggle.setText("Cloud");
        		}
        		else if(colorSetting == 5) {
        			colorToggle.setText("Wood");
        		}
        	}
        });

        ////SLIDERS////

        octaveSlide = new nSlide(JSlider.HORIZONTAL, 1, 8, octaves);
        octaveSlide.setMajorTickSpacing(1);
        octaveSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                octaves = source.getValue();
            }
        });

        persistanceSlide = new nSlide(JSlider.HORIZONTAL, 0, 100, (int) (persistance * 100.0));
        persistanceSlide.setMajorTickSpacing(10);
        persistanceSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                persistance = source.getValue() / 100.0;
            }

        });

        smoothSlide = new nSlide(JSlider.HORIZONTAL, 0, 200, (int) (smooth * 1000.0));
        smoothSlide.setMajorTickSpacing(50);
        smoothSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                smooth = source.getValue() / 1000.0;
                if(smooth == 0) {
                    smooth = .0001;
                }

            }
        });
        
        redSlide = new nSlide(JSlider.HORIZONTAL, 0, 255, redValue);
        redSlide.setMajorTickSpacing(51);
        redSlide.addChangeListener(new ChangeListener() {
        	
        	@Override
        	public void stateChanged(ChangeEvent e) {
        		JSlider source = (JSlider)e.getSource();
        		redValue = source.getValue();
        	}
        });
        
        blueSlide = new nSlide(JSlider.HORIZONTAL, 0, 255, blueValue);
        blueSlide.setMajorTickSpacing(51);
        blueSlide.addChangeListener(new ChangeListener() {
        	
        	@Override
        	public void stateChanged(ChangeEvent e) {
        		JSlider source = (JSlider)e.getSource();
        		blueValue = source.getValue();
        	}
        });
        
        greenSlide = new nSlide(JSlider.HORIZONTAL, 0, 255, greenValue);
        greenSlide.setMajorTickSpacing(51);
        greenSlide.addChangeListener(new ChangeListener() {
        	
        	@Override
        	public void stateChanged(ChangeEvent e) {
        		JSlider source = (JSlider)e.getSource();
        		greenValue = source.getValue();
        	}
        });
        


    }

    private void updateMenu() {

        if(menuType == 0) {

            setAllInvisible();

            twoD.setVisible(true);
            threeD.setVisible(true);

        }
        else if(menuType == 1) {

            setAllInvisible();

            usePerlin.setVisible(true);
            useSimplex.setVisible(true);
            useOpenSimplex.setVisible(true);

            back.setVisible(true);

        }
        else if(menuType == 2) {

            setAllInvisible();

            if(noiseType == 0) {

                perlinLabel.setVisible(true);
                basicPerlin.setVisible(true);
                if(dimension == 0) {
                	wood.setVisible(true);
                }
                else {
                	wood.setVisible(false);
                }
                perlinStep.setVisible(true);

            }
            else if(noiseType == 1) {

                simplexLabel.setVisible(true);
                basicSimplex.setVisible(true);
                marble.setVisible(true);
                simplexStep.setVisible(true);
                domainToggle.setVisible(true);

            }
            else if(noiseType == 2) {

                openSimplexLabel.setVisible(true);
                basicOpenSimplex.setVisible(true);

            }


            back.setVisible(true);

            customize.setVisible(true);
            octaveLabel.setVisible(true);
            octaveSlide.setVisible(true);
            persistanceLabel.setVisible(true);
            persistanceSlide.setVisible(true);
            smoothLabel.setVisible(true);
            smoothSlide.setVisible(true);
            
            redLabel.setVisible(true);
            redSlide.setVisible(true);
            greenLabel.setVisible(true);
            greenSlide.setVisible(true);
            blueLabel.setVisible(true);
            blueSlide.setVisible(true);
            
            colorToggle.setVisible(true);
        }


    }

    private void setAllInvisible() {

        perlinLabel.setVisible(false);
        simplexLabel.setVisible(false);
        openSimplexLabel.setVisible(false);
        redLabel.setVisible(false);
        greenLabel.setVisible(false);
        blueLabel.setVisible(false);

        toggleView.setVisible(false);
        usePerlin.setVisible(false);
        basicPerlin.setVisible(false);
        useSimplex.setVisible(false);
        basicSimplex.setVisible(false);
        useOpenSimplex.setVisible(false);
        basicOpenSimplex.setVisible(false);
        marble.setVisible(false);
        wood.setVisible(false);
        perlinStep.setVisible(false);
        simplexStep.setVisible(false);
        domainToggle.setVisible(false);
        colorToggle.setVisible(false);

        perlinLabel.setVisible(false);
        simplexLabel.setVisible(false);
        openSimplexLabel.setVisible(false);
        octaveLabel.setVisible(false);
        persistanceLabel.setVisible(false);
        smoothLabel.setVisible(false);

        octaveSlide.setVisible(false);
        persistanceSlide.setVisible(false);
        smoothSlide.setVisible(false);
        
        redSlide.setVisible(false);
        greenSlide.setVisible(false);
        blueSlide.setVisible(false);

        threeD.setVisible(false);
        twoD.setVisible(false);

        back.setVisible(false);
        customize.setVisible(false);

    }

    private void toggleVisibility() {

        if(optionsVisible) {

            optionsVisible = false;

            perlinLabel.setVisible(optionsVisible);
            simplexLabel.setVisible(optionsVisible);
            octaveLabel.setVisible(optionsVisible);
            persistanceLabel.setVisible(optionsVisible);
            smoothLabel.setVisible(optionsVisible);

            usePerlin.setVisible(optionsVisible);
            useSimplex.setVisible(optionsVisible);
            marble.setVisible(optionsVisible);
            wood.setVisible(optionsVisible);

            octaveSlide.setVisible(optionsVisible);
            persistanceSlide.setVisible(optionsVisible);
            smoothSlide.setVisible(optionsVisible);

        }
        else if(!optionsVisible) {

            optionsVisible = true;

            perlinLabel.setVisible(optionsVisible);
            simplexLabel.setVisible(optionsVisible);
            octaveLabel.setVisible(optionsVisible);
            persistanceLabel.setVisible(optionsVisible);
            smoothLabel.setVisible(optionsVisible);
            wood.setVisible(optionsVisible);

            usePerlin.setVisible(optionsVisible);
            useSimplex.setVisible(optionsVisible);
            marble.setVisible(optionsVisible);

            octaveSlide.setVisible(optionsVisible);
            persistanceSlide.setVisible(optionsVisible);
            smoothSlide.setVisible(optionsVisible);

        }


    }

}
