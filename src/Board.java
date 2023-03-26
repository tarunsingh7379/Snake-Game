import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {

    int B_HEIGHT = 400;
    int B_WIDTH = 400;

    int MAX_DOTS = 40;
    int DOT_SIZE = 10;
    int DOTS;

    int x[] = new int[MAX_DOTS];
    int y[] = new int[MAX_DOTS];

    int apple_x;
    int apple_y;

    Image apple, body, head;

    Timer timer;

    int delay = 150;

    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;

    boolean isGameOver = false;

    Board(){
        // Adding Key Listener
        KAdapter kAdapter = new KAdapter();
        addKeyListener(kAdapter);
        setFocusable(true);

        // Setting Window
        setPreferredSize(new Dimension(B_WIDTH,B_HEIGHT));
        setBackground(Color.BLACK);

        // Initializing Game
        initGame();

        // Loading Images
        loadImages();
    }

    public void initGame(){
        DOTS = 3;

        // Initialize Snake's position
        x[0] = 50;
        y[0] = 50;
        for(int i = 1; i < DOTS; i++){
            x[i] = x[0] + DOT_SIZE*i;
            y[i] = y[0];
        }

        // Initialize Apple's position
        getApplePosition();

        // Setting Timer Event
        timer = new Timer(delay, this);
        timer.start();
    }

    // Load images from resources folder to image object
    public void loadImages(){
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
    }

    // Draw images at snake's and apple's position
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(!isGameOver) {
            doDrawing(g);
        } else {
            gameOver(g);
            timer.stop();
        }
    }

    public void doDrawing(Graphics g){
        g.drawImage(apple,apple_x,apple_y,this);
        for(int i = 0; i < DOTS; i++){
            if(i == 0){
                g.drawImage(head,x[i],y[i],this);
            } else {
                g.drawImage(body,x[i],y[i],this);
            }
        }
    }

    // Randomize Apple's position
    public void getApplePosition(){
        apple_x = ((int)((Math.random())*39))*DOT_SIZE;
        apple_y = ((int)((Math.random())*39))*DOT_SIZE;
    }

    // When Timer Event Updates, this action will be performed
    @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(!isGameOver){
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

    public void move(){
        for(int i = DOTS-1; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(leftDirection){
            x[0] -= DOT_SIZE;
        }
        if(rightDirection){
            x[0] += DOT_SIZE;
        }
        if(upDirection){
            y[0] -= DOT_SIZE;
        }
        if(downDirection){
            y[0] += DOT_SIZE;
        }
    }

    // Check Apple whether it is eaten or not
    public void checkApple(){
        if(apple_x == x[0] && apple_y == y[0]){
            DOTS++;
            getApplePosition();
        }
    }

    // Check Collisions
    public void checkCollision(){

        // Check Body Collisions
        for(int i = 1; i < DOTS; i++){
            if(x[i] == x[0] && y[i] == y[0]){
                isGameOver = true;
                break;
            }
        }

        // Check Border Collisions
        if(x[0] < 0){
            isGameOver = true;
        }
        if(x[0] >= B_WIDTH){
            isGameOver = true;
        }
        if(y[0] < 0){
            isGameOver = true;
        }
        if(y[0] >= B_HEIGHT){
            isGameOver = true;
        }
    }

    public void gameOver(Graphics g){
        String msg = "Game Over";
        int score = (DOTS-3)*100;
        String scoreMsg = "Score: " + score;
        Font smallFont = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(smallFont);

        g.setColor(Color.WHITE);
        g.setFont(smallFont);
        g.drawString(msg, (B_WIDTH-fontMetrics.stringWidth(msg))/2, B_HEIGHT/4);
        g.drawString(scoreMsg, (B_WIDTH-fontMetrics.stringWidth(scoreMsg))/2, 3*(B_HEIGHT/4));
    }

    private class KAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();
            if(key == keyEvent.VK_LEFT && !rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == keyEvent.VK_RIGHT && !leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == keyEvent.VK_UP && !downDirection){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if(key == keyEvent.VK_DOWN && !upDirection){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}
