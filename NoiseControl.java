import javax.swing.*;

/**
 * Created by Michael on 10/27/2016.
 */
public class NoiseControl implements Runnable {

    private JFrame frame;
    private NPanel panel;
    private boolean running;
    private Thread thread;
    private InputHandler ih;

    public NoiseControl() {
        ih = new InputHandler();
        running = false;
        thread = new Thread(this);
    }

    public synchronized void start() {
        if(running) {
            return;
        }
        running = true;
        thread.start();
    }
    public synchronized void stop() {

        if(!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void run() {

        while(running) {

            long initialTime = System.nanoTime();
            double timeF = 1000000000 / 60;
            double deltaF = 0;
            int frames = 0;
            long timer = System.currentTimeMillis();

            while(running) {

                long currentTime = System.nanoTime();
                deltaF += (currentTime - initialTime) / timeF;
                initialTime = currentTime;

                int x = frame.getWidth();
                int y = frame.getHeight();

                if(deltaF >= 1) {
                    panel.refresh(x, y, ih.getKeys());
                    frames++;
                    deltaF--;
                }

                if(System.currentTimeMillis() - timer > 1000) {
                    //System.out.println("FPS: " + frames);
                    frames = 0;
                    timer += 1000;
                    //panel.snapShot();
                }
            }

        }

    }

    public static void main(String[] args0) {

        int x = 1000;
        int y = 600;

        NoiseControl nc = new NoiseControl();

        nc.frame = new JFrame("Noise Test");
        //nc.frame.setSize(1000,1000);
        //nc.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        nc.frame.setSize(x,y);
        nc.frame.setLocationRelativeTo(null);
        nc.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        nc.panel = new NPanel(x, y);
        nc.frame.add(nc.panel);

        nc.frame.setVisible(true);

        nc.frame.addKeyListener(nc.ih);

        nc.start();
    }


}
