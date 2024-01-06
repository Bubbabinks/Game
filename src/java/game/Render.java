package game;

import game.world.BlockType;
import game.world.World;
import main.Manager;
import ui.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Render extends JPanel {

    public final static int sw = WindowManager.WINDOW_WIDTH, sh = WindowManager.WINDOW_HEIGHT, hsw = sw/2, hsh = sh/2;
    public static int x = 0, y = 0, xoffset = 0, yoffset = 0, bs = 40, hbs = bs/2, px = x+1, py = x, bw = sw/bs+2, hbw = bw/2, bh = sh/bs+2, hbh = bh/2;

    private final static ArrayList<GameObject> renderedObjects = new ArrayList<GameObject>();
    private final static ArrayList<RenderBlock> renderedBlocks = new ArrayList<RenderBlock>();
    private static Render render;
    private static final int fps = (int)(1000d/60d);

    private final static ArrayList<MouseListener> addMouseListeners = new ArrayList<MouseListener>();
    private final static ArrayList<MovementListener> movementListeners = new ArrayList<MovementListener>();

    protected Render() {
        render = this;
        for (MouseListener m: addMouseListeners) {
            addMouseListener(m);
        }
        Thread rerenderThread = new Thread(() -> {
            while (Manager.applicationRunning) {
                if (px != x || py != y) {
                    px = x;
                    py = y;
                    renderedBlocks.clear();
                    for (int y = Render.y-hbh; y < Render.y+hbh+1; y++) {
                        for (int x = Render.x-hbw; x < Render.x+hbw+1; x++) {
                            renderedBlocks.add(new RenderBlock(x, y, World.getBlock(x, y)));
                        }
                    }
                }

                repaint();
                try {
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        rerenderThread.start();
    }

    public static void regatherWorld() {
        px+= 40;
    }

    @Override
    protected void paintComponent(Graphics g) {

        int rx = x;
        int ry = y;
        int rxo = xoffset;
        int ryo = yoffset;

        //GameObject Drawing
        g.setColor(GameColors.SKY_COLOR);
        g.fillRect(0,0, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);
        for (GameObject o: renderedObjects) {
            g.drawImage(o.entityType.getImage(), ((o.x-rx)*bs+(o.xoffset-rxo))+hsw-o.halfWidth, -((o.y-ry)*bs+(o.yoffset-ryo))+hsh-o.halfHeight, o.width, o.height, null);
        }

        //World Drawing
        for (RenderBlock renderBlock: renderedBlocks) {
            if (renderBlock.block != null) {
                BlockType block = renderBlock.block;
                g.drawImage(block.getImage(), (((renderBlock.x)-rx)*bs+(-rxo))+hsw-hbs, -(((renderBlock.y)-ry)*bs+(-ryo))+hsh-hbs, bs, bs, null);
            }
        }

        //Debug
        Player player = World.getPlayer();
        g.setColor(Color.BLACK);
        g.drawString(player.x+" "+player.y+" "+player.xoffset+" "+player.yoffset, 10, 10);
    }

    public static void addRenderedObject(GameObject o) {
        renderedObjects.add(o);
    }

    public static void removeRenderedObject(GameObject o) {
        renderedObjects.remove(o);
    }

    public static void addML(MouseListener mouseListener) {
        if (render == null) {
            addMouseListeners.add(mouseListener);
        }else {
            render.addMouseListener(mouseListener);
        }
    }

    private class RenderBlock {

        public int x, y;
        public BlockType block;

        public RenderBlock(int x, int y, BlockType block) {
            this.x = x;
            this.y = y;
            this.block = block;
        }

    }

    public interface MovementListener {
        public void onPositionChange(int x, int y);
    }
}
