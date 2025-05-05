
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
     private JButton restartButton;
    
    GamePanel() {

        random = new SecureRandom();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        initializeRestartButton();
        startGame();
      

    }
    public void startGame() {
        applesEaten = 0;
        bodyParts = 6; // Reset body parts
        direction = 'R'; // Reset direction
        running = true;
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        newApple();
        timer = new Timer(DELAY, this);
        timer.start();
        restartButton.setVisible(false); // Hide the button after starting the game
        repaint();

    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);    

    }
    public void draw(Graphics g) {  
          if(running){
        // for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
            
        //     g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
        //     g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        // }
        g.setColor(Color.green);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for(int i=0;i<bodyParts;i++) {
            if(i==0) {
                g.setColor(Color.pink);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(185, 95, 0));
                g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
       
    }
    else{
        gameOver(g);
    }

    }
    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
       
    }
    public void move() {
        for(int i=bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];

        }

        switch(direction) {
        case 'U':
            y[0] = y[0] - UNIT_SIZE;
            break;
        case 'D':
            y[0] = y[0] + UNIT_SIZE;    
            break;
        case 'L':        
            x[0] = x[0] - UNIT_SIZE;
            break;        
        case 'R':
            x[0] = x[0] + UNIT_SIZE;
            break;
        }   

    }
    public void checkApple() {

        if(x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }
    public void checkCollisions() {
        for(int i=bodyParts;i>0;i--) {
            if(x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        if(x[0] < 0) {
            running = false;
        }
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        if(y[0] < 0) {
            running = false;
        }
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(!running) {
            timer.stop();
        }

    }
    private void initializeRestartButton() {
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Ink Free", Font.BOLD, 30));
        restartButton.setBounds((SCREEN_WIDTH - 200) / 2, (SCREEN_HEIGHT / 2) + 50, 200, 50);
        restartButton.setVisible(false); // Initially hide the button

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        add(restartButton);
    }
    public void gameOver(Graphics g) {

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
       
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, SCREEN_HEIGHT/3);
        restartButton.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();

    }

    

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if(direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(direction != 'L') {
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_UP:    
                if(direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:  
                if(direction != 'U') {
                    direction = 'D';
                }
                break;
            }

        }

    }
    

}
