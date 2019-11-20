package id.its.ac.id.rafid.movingSpirites;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;


public class Board extends JPanel implements ActionListener {

    private final int ICRAFT_X = 10;
    private final int ICRAFT_Y = 200;
    private final int DELAY = 10;
    private Timer timer;
    private final int B_WIDTH = 1000;
    private final int B_HEIGHT = 500;
    private SpaceShip spaceShip; 
    private List<Asteroid> asteroids;
    private boolean ingame;
    
    private final int[][] koordinat = {
    		{680, 290}, {750, 379}, {638, 289},
            {780, 109}, {580, 439}, {680, 239},
            {790, 459}, {760, 50}, {790, 350},
            {520, 409}, {560, 45}, {510, 370},
            {930, 159}, {690, 380}, {630, 60},
            {940, 59}, {900, 30}, {920, 200},
            {900, 259}, {660, 50}, {540, 90},
            {810, 220}, {860, 20}, {740, 180},
            {820, 128}, {490, 170}, {700, 30}
            };

    public Board() {

        initBoard();
    }

    private void initBoard() {
    	
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        ingame = true;

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        spaceShip = new SpaceShip(ICRAFT_X, ICRAFT_Y);
        initAsteroids();
        
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void initAsteroids() {
    	asteroids = new ArrayList<>();

        for (int[] k : koordinat) {
            asteroids.add(new Asteroid(k[0], k[1]));
        }
		
	}

	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (ingame) {

            doDrawing(g);

        } else {

            drawGameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

	
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        
        if (spaceShip.isVisible()) 
            g.drawImage(spaceShip.getImage(), spaceShip.getX(), spaceShip.getY(),this);
        
        
        List<Missile> missiles = spaceShip.getMissiles();

        for (Missile missile : missiles) {
            
            g2d.drawImage(missile.getImage(), missile.getX(),
                    missile.getY(), this);
        }
        
        for (Asteroid asteroid : asteroids) {
            if (asteroid.isVisible()) {
                g.drawImage(asteroid.getImage(), asteroid.getX(), asteroid.getY(), this);
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("Asteroids left: " + asteroids.size(), 5, 15);
        
    }
    
    private void drawGameOver(Graphics g) {

        String msg = "You Win!";
        Font small = new Font("Times New Roman ", Font.BOLD, 30);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2,
                B_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    	inGame();
        updateMissiles();
        updateSpaceShip();
        checkCollisions();

        repaint();
    }

    private void inGame() {

        if (!ingame) {
            timer.stop();
        }
    }
    
    private void updateMissiles() {

        List<Missile> missiles = spaceShip.getMissiles();

        for (int i = 0; i < missiles.size(); i++) {

            Missile missile = missiles.get(i);

            if (missile.isVisible())
                missile.move();
            else
                missiles.remove(i);
        }
    }

    private void updateSpaceShip() {
        spaceShip.move();
    }
    
    public void checkCollisions() {

    	Rectangle r3 = spaceShip.getBounds();

        for (Asteroid asteroid : asteroids) {
            
            Rectangle r2 = asteroid.getBounds();

            if (r3.intersects(r2)) {
                
                spaceShip.setVisible(false);
                asteroid.setVisible(false);
                ingame = false;
            }
        }
    	
        List<Missile> missiles = spaceShip.getMissiles();
        
        for (int i = 0; i < missiles.size(); i++) {

            Missile missile = missiles.get(i);
            Rectangle r1 = missile.getBounds();

            for(int j=0; j < asteroids.size();j++) {

            	Asteroid asteroid = asteroids.get(j);
            	Rectangle r2 = asteroid.getBounds();
                if (r1.intersects(r2)) {
                    missile.setVisible(false);
                    missiles.remove(i);
                    asteroid.setVisible(false);
                    asteroids.remove(j);
                    
                }
            }
        }
        
        if(asteroids.size()==0)
        	ingame = false;
        
            
    }
    
        private class TAdapter extends KeyAdapter {

            @Override
            public void keyReleased(KeyEvent e) {
                spaceShip.keyReleased(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                spaceShip.keyPressed(e);
            }
        }
}