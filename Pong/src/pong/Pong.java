package pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Pong implements ActionListener, KeyListener {
    public static Pong pong;

    public Paddle player1;

    public Paddle player2;

    public Ball ball;

    public int width = 640,height = 480;

    public Renderer renderer;

    public boolean bot = false, selectingDifficulty = false;

    public boolean w,s,up,down;

    public int gameStatus = 0, scoreLimit = 7, playerWon;
    //0=stopped
    //1=Paused
    //2=Playing
    //3=Over

    public Random random;

    public int botMoves, botCooldown = 0, botDifficulty = 0;


    public Pong(){
        Timer timer = new Timer(20,this);
        JFrame jFrame = new JFrame("Pong game");
        random = new Random();
        renderer = new Renderer();
        jFrame.setSize(new Dimension(width+15,height+35));
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(3);
        jFrame.setLocationRelativeTo(null);
        jFrame.add(renderer);
        jFrame.addKeyListener(this);


        timer.start();

    }

    public void start(){
        gameStatus=2;
        player1 = new Paddle(this,1);
        player2 = new Paddle(this,2);
        ball = new Ball(this);
    }
    public void update(){

        if(player1.score > scoreLimit){
            playerWon = 1;
            gameStatus = 3;
        }
        if(player2.score > scoreLimit){
            playerWon = 2;
            gameStatus = 3;

        }

        if(w){
            player1.move(true);
        }
        if(s){
            player1.move(false);
        }
        if(!bot) {

            if (up) {
                player2.move(true);
            }
            if (down) {
                player2.move(false);
            }
        }else{
            int speed = 15;
            if(botCooldown > 0){
                botCooldown--;
                if (botCooldown == 0) {

                    botMoves=0;
                }
            }
            if(botMoves < 10) {
                if (player2.y + player2.height / 2 < ball.y) {
                    player2.move(false);
                    botMoves++;
                }
                if (player2.y + player2.height / 2 > ball.y) {
                    player2.move(true);
                    botMoves++;
                }
                if(botDifficulty ==0){

                    botCooldown = 20;
                }
                if(botDifficulty ==1){

                    botCooldown = 15;
                }
                if(botDifficulty ==2){

                    botCooldown = 10;
                }

            }


        }
        ball.update(player1,player2);
    }
    public void render(Graphics2D g) {

        g.setColor(Color.BLACK);
        g.fillRect(0,0,width,height);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        if(gameStatus == 0){
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("PONG", width/2-65,50 );

            if(!selectingDifficulty) {
                g.setFont(new Font("Arial", 1, 30));
                g.drawString("Press space to play", width / 2 - 140, height / 2 - 25);
                g.drawString("Press space to play with Bot", width / 2 - 200, height / 2 + 25);
                g.drawString("<< Score limit " + scoreLimit + " >>", width / 2 - 125, height / 2 + 75);
            }
        }
        if(selectingDifficulty ){
            String difficulty = botDifficulty == 0 ? "Easy" : (botDifficulty == 2 ? "Hard" : "Medium");
            g.setFont(new Font("Arial", 1, 30));
            g.drawString("<< Bot Difficulty: " + difficulty + " >>", width/2-180,height/2 - 25);
            g.drawString("Press space to play", width / 2 - 150, height / 2 + 25);

        }
        if(gameStatus == 2 || gameStatus == 1) {


            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(10));
            g.drawLine(width / 2, 0, width / 2, height);
            g.drawOval(width/2 - 50, height/2 -50, 100, 100);

            g.setFont(new Font("Arial", 1, 50));
            g.drawString(player1.score+"", width/2-65,50 );

            g.setFont(new Font("Arial", 1, 50));
            g.drawString(player2.score+"", width/2+40,50 );

            player1.render(g);
            player2.render(g);

            ball.render(g);
        }
        if(gameStatus == 1){
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("PAUSED", width/2-103, height/2 - 105);
        }
        if(gameStatus == 3){
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("PONG", width/2-65,50 );

            if(bot){
                g.drawString("The Bot WINS!!", width/2-180,150);
            }else {

                g.drawString("Player " + playerWon + " WINS!!", width/2-180,150);
            }


            g.setFont(new Font("Arial", 1, 30));
            g.drawString("Press space to play again", width / 2 - 190, height / 2 - 25);
            g.drawString("Press ESC for Menu", width / 2 - 150, height / 2 + 25);


        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameStatus == 2){
            update();
        }
        renderer.repaint();


    }

    public static void main(String[] args) {
        pong = new Pong();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getKeyCode();

        if(id == KeyEvent.VK_W){
            w = true;
        }
        else if(id == KeyEvent.VK_S){
            s = true;
        }
        else if(id == KeyEvent.VK_UP){
            up = true;
        }
        else if(id == KeyEvent.VK_DOWN){
            down = true;
        }
        else if(id == KeyEvent.VK_RIGHT ){
            if(selectingDifficulty) {
                if (botDifficulty < 2) {
                    botDifficulty++;
                } else {
                    botDifficulty = 2;
                }
            } else if (gameStatus == 0){
                scoreLimit++;
            }
        }
        else if(id == KeyEvent.VK_LEFT ){
            if(selectingDifficulty) {
                if (botDifficulty > 0) {
                    botDifficulty--;
                } else {
                    botDifficulty = 0;
                }

            } else if (gameStatus == 0 && scoreLimit > 0){
                scoreLimit--;
            }
        }
        else if(id == KeyEvent.VK_SHIFT && gameStatus == 0){
            selectingDifficulty = true;
            bot = true;
        }
        else if(id == KeyEvent.VK_SPACE){
            if(gameStatus == 0 || gameStatus == 3){

                if(!selectingDifficulty){
                    bot = false;
                }
                else {
                    selectingDifficulty = false;
                }
                start();

            }
            else if(gameStatus == 1){
                gameStatus = 2;
            }
            else if(gameStatus == 2){
                gameStatus = 1;
            }
        }
        else if ( id == KeyEvent.VK_ESCAPE){
            gameStatus = 0;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int id = e.getKeyCode();

        if(id == KeyEvent.VK_W){
            w = false;
        }
        else if(id == KeyEvent.VK_S){
            s = false;
        }
        else if(id == KeyEvent.VK_UP){
            up = false;
        }
        else if(id == KeyEvent.VK_DOWN){
            down = false;
        }

    }
}
