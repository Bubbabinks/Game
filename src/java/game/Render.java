package game;

import game.entity.Coord;
import game.entity.GameObject;
import game.entity.Player;
import game.inventory.Inventory;
import game.inventory.PlayerInventory;
import game.update.Update;
import game.world.BackgroundType;
import game.world.BlockType;
import game.world.Chunk;
import game.world.World;
import ui.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

public class Render extends JPanel {

    public final static int sw = WindowManager.WINDOW_WIDTH, sh = WindowManager.WINDOW_HEIGHT, hsw = sw/2, hsh = sh/2;
    public static int x = 0, y = 0, xoffset = 0, yoffset = 0, bs = 40, hbs = bs/2, px = x+1, py = x, bw = sw/bs+2, hbw = bw/2, bh = sh/bs+2, hbh = bh/2;

    private final static ArrayList<RenderBlock> renderedBlocks = new ArrayList<RenderBlock>();
    private final static ArrayList<RenderBackground> renderBackgrounds = new ArrayList<RenderBackground>();
    private static Render render;
    private static final int fps = (int)(1000d/60d);

    private final static ArrayList<MouseListener> addMouseListeners = new ArrayList<MouseListener>();
    private final static ArrayList<MouseWheelListener> addMouseWheelListeners = new ArrayList<MouseWheelListener>();

    public static Inventory renderedInventory = null;

    protected Render() {
        render = this;
        for (MouseListener m: addMouseListeners) {
            addMouseListener(m);
        }
        for (var m: addMouseWheelListeners) {
            addMouseWheelListener(m);
        }
        Update.addRenderUpdate(() -> {
            if (World.isWorldLoaded()) {
                if (px != x || py != y) {
                    px = x;
                    py = y;
                    renderedBlocks.clear();
                    renderBackgrounds.clear();
                    for (int y = Render.y-hbh; y < Render.y+hbh+1; y++) {
                        for (int x = Render.x-hbw; x < Render.x+hbw+1; x++) {
                            BlockType block = World.getBlock(x, y);
                            if (block != null) {
                                renderedBlocks.add(new RenderBlock(x, y, block));
                            }

                            BackgroundType background = World.getBackground(x, y);
                            if (background != null) {
                                renderBackgrounds.add(new RenderBackground(x, y, background));
                            }
                        }
                    }
                }
            }
            Render.render.repaint();
        });
    }

    public static Point getMousePos() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, Render.render);
        if (p.x < 0 || p.y < 0 || p.x > WindowManager.WINDOW_WIDTH || p.y > WindowManager.WINDOW_HEIGHT) {
            return null;
        }
        return p;
    }

    public static void regatherWorld() {
        px+= 40;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (World.isWorldLoaded()) {
            int rx = x;
            int ry = y;
            int rxo = xoffset;
            int ryo = yoffset;

            //Background drawing
            ArrayList<RenderBackground> rbg = (ArrayList<RenderBackground>)renderBackgrounds.clone();
            for (var renderBackground: rbg) {
                BackgroundType block = renderBackground.background;
                g.drawImage(block.getImage(), (((renderBackground.x)-rx)*bs+(-rxo))+hsw-hbs, -(((renderBackground.y)-ry)*bs+(-ryo))+hsh-hbs, bs, bs, null);
            }

            //Block Drawing
            ArrayList<RenderBlock> rb = (ArrayList<RenderBlock>)renderedBlocks.clone();
            for (RenderBlock renderBlock: rb) {
                BlockType block = renderBlock.block;
                g.drawImage(block.getImage(), (((renderBlock.x)-rx)*bs+(-rxo))+hsw-hbs, -(((renderBlock.y)-ry)*bs+(-ryo))+hsh-hbs, bs, bs, null);
            }

            //Entity Drawing
            for (var e: World.getEntities()) {
                Coord o = e.coord;
                g.drawImage(e.getEntityType().getImage(), ((o.x-rx)*bs+(o.xoffset-rxo))+hsw-e.halfWidth, -((o.y-ry)*bs+(o.yoffset-ryo))+hsh-e.halfHeight, e.width, e.height, null);
                e.draw(g);
            }

            //Draw HotBar
            Player player = World.getClient();
            if (player.drawUpperHotbar) {
                ((PlayerInventory)player.getInventory()).drawUpperHotbar(g);
            }else {
                ((PlayerInventory)player.getInventory()).drawLowerHotBar(g);
            }

            //Draw Opened Inventory
            if (renderedInventory != null) {
                renderedInventory.draw(g);
            }


            //Debug
            g.setColor(Color.BLACK);
            g.drawString(player.coord.x+" "+player.coord.y+" "+Math.floorDiv(player.coord.x, Chunk.chunkSize)+" "+Math.floorDiv(player.coord.y, Chunk.chunkSize), 10, 10);
        }
    }

    public static Point convertWorldToPixel(Coord coord) {
        if (coord != null) {
            return new Point(((coord.x-x)*bs+(coord.xoffset-xoffset))+hsw-hbs, -((coord.y-y)*bs+(coord.yoffset-yoffset))+hsh+hbs);
        }else {
            return new Point(0, 0);
        }

    }

    public static void addML(MouseListener mouseListener) {
        if (render == null) {
            addMouseListeners.add(mouseListener);
        }else {
            render.addMouseListener(mouseListener);
        }
    }

    public static void removeML(MouseListener mouseListener) {
        if (render == null) {
            addMouseListeners.remove(mouseListener);
        }else {
            render.removeMouseListener(mouseListener);
        }
    }

    public static void addMWL(MouseWheelListener mouseWheelListener) {
        if (render == null) {
            addMouseWheelListeners.add(mouseWheelListener);
        }else {
            render.addMouseWheelListener(mouseWheelListener);
        }
    }

    public static void removeMWL(MouseWheelListener mouseWheelListener) {
        if (render == null) {
            addMouseWheelListeners.remove(mouseWheelListener);
        }else {
            render.removeMouseWheelListener(mouseWheelListener);
        }
    }


    private static class RenderBlock {

        public int x, y;
        public BlockType block;

        public RenderBlock(int x, int y, BlockType block) {
            this.x = x;
            this.y = y;
            this.block = block;
        }

    }

    private static class RenderBackground {
        public int x, y;
        public BackgroundType background;

        public RenderBackground(int x, int y, BackgroundType background) {
            this.x = x;
            this.y = y;
            this.background = background;
        }
    }

    public static Render getPanel() {
        return render;
    }
}
