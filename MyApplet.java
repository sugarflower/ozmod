import java.applet.*;
import java.awt.*;
import java.io.*;
import java.net.*;

import ozmod.*;

public class MyApplet extends Applet implements Runnable {

    public MyApplet()
    {   
        tr = new Thread(this);
        run_ = true;
        tr.start();       
    }
    
    public void init()
    {
        OZMod ozm = new OZMod();
        ozm.initOutput();
        
        PipeIn in;
        in = new PipeIn(new File( __YOURFILE__ ), PipeIn.LITTLEENDIAN);
        
        player_ = new S3MPlayer();

        player_.load(in);
        player_.setLoopable(true);
        player_.play();
        
        offscreen_ = createImage(640, 400);
        offscreengfx_ = offscreen_.getGraphics();

        setBackground(Color.BLACK);
    }

    public void start()
    {
    }

    public void stop()
    {
    }

    public void destroy()
    {
        player_.done();
    }

    public void run()
    {
        while(run_ == true)
        {
            repaint();
           
            try {
                Thread.sleep(10);
            }
            catch(InterruptedException e){
            }        
        }
    }
    
    public void update(Graphics g)
    {
        paint(g);
    }
    
    public void paint(Graphics _g)
    {
        byte pcm[] = player_.getMixBuffer();
        
        if (pcm == null)
            return;

        offscreengfx_.setColor(Color.BLACK);
        offscreengfx_.fillRect(0, 0, 600, 400);
        
        offscreengfx_.setColor(Color.WHITE);

        int x = 0;
        for (int i = 0; i < 2400; i+= 4) {
            int b1L = pcm[i+0];
            int b2L = pcm[i+1];
            b1L &= 0xff;
            b2L &= 0xff;
            short curL = (short) ((b1L << 8) | b2L);
            curL >>= 6;
            curL += 100;
            
            int b1R = pcm[i+2];
            int b2R = pcm[i+3];
            b1R &= 0xff;
            b2R &= 0xff;
            short curR = (short) ((b1R << 8) | b2R);
            curR >>= 6;
            curR += 300;

            offscreengfx_.drawLine(x, curL, x, curL);
            offscreengfx_.drawLine(x, curR, x, curR);
            x++;
        }

        _g.drawImage(offscreen_, 0, 0, this);
    }
    
    OZMod ozmod_;
    S3MPlayer player_;
    
    Image offscreen_;
    Graphics offscreengfx_;
    Thread tr;
    static boolean run_;
}
