package pong;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Pong extends JPanel 
{

    public static JFrame frame = null;
    public static JFrame start = null;
    public static JTextArea text = null;
    
    Container con = null;
    Container startCon = null;
    public static int paddleX = 10, paddleY = 270, paddleWidth = 10, paddleHeight = 100;
    public static int paddle2X = 865, paddle2Y = 270;
    public static int upSpeed = 15;
    public static int downSpeed = 15;
    public static int ballX, ballY, ballWidth = 25, ballHeight = 25;
    public static int dx = 3, dy = 3;
    public static int x = 0;
    public static boolean flag = false, gameOver = false, howToFlag = false, startGame = true;
    public static int lives = 3, score = 0;

    public void creatWindow() 
    {    
        start = new JFrame("Start");
        
        startCon = start.getContentPane();
        start.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        start.setBounds(200, 50, 900, 600);
        
        JButton howTo = new JButton("How To Play");
        howTo.setBounds(350, 310, 160, 40);
        startCon.add(howTo);
        howTo.addActionListener(new handleButton());
     
        JButton startBtn = new JButton("Start");
        startBtn.setBounds(350, 250, 160, 40);
        startCon.add(startBtn);
        startBtn.addActionListener(new handleButton());
        
        Font f = null;
        f = new Font("TimesRoman", Font.BOLD, 20);
        
        text = new JTextArea();
        text.setFont(f);
        text.setBackground(new Color(255, 255, 51));
        text.setText("\n > To move left paddle up and down use W and S \n\n > To move right paddle up and down use Up and Down arrow key");
        text.setBounds(120, 380, 650, 130);
        text.setEditable(false);
        text.setVisible(false);
        
        startCon.add(text);
        start.setLayout(null);
        startCon.setBackground(new Color(97, 76, 124));
        start.setVisible(true);
        
        frame = new JFrame("Pong");  
        con = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 50, 900, 600);
        frame.setVisible(false);

        con.add(this);
        con.setFocusable(true);
        con.addKeyListener(new classKeyEvents());

        Runnable ml = new mainLoop();
        Thread th = new Thread(ml);
        th.start();

    }

    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, getWidth(), getHeight());

        if(gameOver && --lives > 0)
        {
            startGame = true;
            gameOver = false;
            flag = false;
        }

        if (!flag) 
        {
            ballX = paddleX + 10;
            ballY = paddleY + 35;
            g.setColor(Color.red);
            g.fillOval(ballX, ballY, ballWidth, ballHeight);
        } 
        else 
        {
            g.setColor(Color.red);
            g.fillOval(ballX + dx, ballY + dy, ballWidth, ballHeight);
        }
        
        g.setColor(Color.YELLOW);
        g.drawRect(paddleX, paddleY, paddleWidth, paddleHeight);
        g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);

        g.setColor(Color.white);
        g.drawRect(paddle2X, paddle2Y, paddleWidth, paddleHeight);
        g.fillRect(paddle2X, paddle2Y, paddleWidth, paddleHeight);
    
        if(startGame)
        {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("press Space to start", 350, 240);
        }
        
        else if(gameOver && lives <= 0)
        {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            g.setColor(Color.red);
            g.drawString("GAME OVER!!!!!!!", 200, 200);
            g.drawString("Score :   " + score, 200, 300);
            return;

        }
        
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Lives :   " + lives, 570, 30);
        g.drawString("Score :   " + score, 200, 30);

    }

    static class classKeyEvents implements KeyListener
    {

        public void keyTyped(KeyEvent e) {}

        public void keyPressed(KeyEvent e) 
        {
            if (e.getKeyCode() == KeyEvent.VK_W)
            {
                if ((Pong.paddleY + Pong.upSpeed) > 30)
                {
                    Pong.paddleY -= Pong.upSpeed;
                }

            } 
            
            else if (e.getKeyCode() == KeyEvent.VK_S) 
            {
                if ((Pong.paddleY + Pong.downSpeed + Pong.HEIGHT) < 460)
                {
                    Pong.paddleY += Pong.downSpeed;
                }

            }
            
            else if (e.getKeyCode() == KeyEvent.VK_SPACE)
            {
                flag = true;
                Pong.startGame = false;
            } 
            
            else if (e.getKeyCode() == KeyEvent.VK_UP)
            {
                if ((Pong.paddle2Y + Pong.upSpeed) > 30) 
                {
                    Pong.paddle2Y -= Pong.upSpeed;
                }

            } 
            
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) 
            {
                if ((Pong.paddle2Y + Pong.downSpeed + Pong.HEIGHT) < 460)
                {
                    Pong.paddle2Y += Pong.downSpeed;
                }

            }
            
            Pong.frame.repaint();

        }

        public void keyReleased(KeyEvent e) { }

    }

    class mainLoop implements Runnable 
    {

        public void run()
        {
            while (true) 
            {
                ballX += dx;
                ballY += dy;

                if (ballY > 530) 
                {
                    dy = -dy;
                } 
                else if (ballY < 20) 
                {
                    dy = Math.abs(dy);
                }

                if (ballX <= 20 && ballX > 0 && ballY >= paddleY && ballY <= paddleY + paddleHeight)
                {
                    dx = Math.abs(dx);
                    if(flag)score++;
                } 
                else if (ballX == 842 && ballX < 845 && ballY >= paddle2Y && ballY <= paddle2Y + paddleHeight) 
                {
                    dx = -dx;
                    if(flag)score++;
                } 
                else if (ballX <= 0 || ballX > 842) 
                {

                    gameOver = true;
                }

                      
                try 
                {
                    Thread.sleep(15);
                } 
                catch (InterruptedException ex) 
                {
                    Logger.getLogger(Pong.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                frame.repaint();

            }

        }

    }

    public static void main(String[] args) 
    {
        Pong p = new Pong();
        p.creatWindow();

    }

}


class handleButton implements ActionListener
{

    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("Start"))
        {
            Pong.start.setVisible(false);
            Pong.frame.setVisible(true);
        }
        
        else if(e.getActionCommand().equals("How To Play"))
        {
            if(Pong.howToFlag == false)
            Pong.text.setVisible(true);
            
            if(Pong.howToFlag)
            {
                Pong.text.setVisible(false);
                Pong.howToFlag = false;
            }
            else
                Pong.howToFlag = true;
        }
    }
    
}