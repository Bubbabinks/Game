package game.world.worldGenerator.tree;

import com.formdev.flatlaf.FlatDarkLaf;
import game.world.BlockType;
import main.FileManager;
import main.ImageUtil;
import main.SkyBox;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class TreeGeneratorApp {

    private static TreeInfo treeInfo;
    private static JFrame mainFrame;
    private static JFrame blockPickerFrame;
    private static JFrame toolFrame;

    private static MainPanel mainPanel;
    private static BlockPickerPanel blockPickerPanel;
    private static ToolPanel toolPanel;

    private static File file = new File(FileManager.mainFolder+"Generated/tree.json");

    public static void main(String[] args) {
        FileManager.init();
        FileManager.insureFolder(FileManager.mainFolder+"Generated");
        if (file.exists()) {
            treeInfo = (TreeInfo) FileManager.readJson(file.getPath(), TreeInfo.class);
        }else {
            treeInfo = new TreeInfo();
        }
        FlatDarkLaf.setup();
        mainFrame = new JFrame("Tree Generator App");
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplication();
            }
        });
        mainPanel = new MainPanel();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setResizable(false);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);

        mainFrame.setVisible(true);

        blockPickerFrame = new JFrame("Block Picker Frame");
        blockPickerFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        blockPickerFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplication();
            }
        });
        blockPickerPanel = new BlockPickerPanel();
        blockPickerFrame.setContentPane(blockPickerPanel);
        blockPickerFrame.setResizable(false);
        blockPickerFrame.pack();
        Point mainFrameLocation = mainFrame.getLocation();
        blockPickerFrame.setLocation(mainFrameLocation.x+600, mainFrameLocation.y);
        blockPickerFrame.setVisible(true);

        toolFrame = new JFrame("Tools");
        toolFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        toolFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplication();
            }
        });
        toolPanel = new ToolPanel();
        toolFrame.setContentPane(toolPanel);
        toolFrame.setResizable(false);
        toolFrame.pack();
        toolFrame.setLocation(mainFrameLocation.x, mainFrameLocation.y+mainFrame.getHeight());
        toolFrame.setVisible(true);
    }

    public static int chooseFile() {
        JFileChooser fileChooser = new JFileChooser(FileManager.mainFolder+"Generated");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setSelectedFile(file);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String[] split = f.getName().split("\\.");
                if (split.length > 1 && split[split.length-1].equals("json")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return ".json";
            }
        });
        int action = fileChooser.showOpenDialog(mainFrame);
        if (action == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String[] split = file.getName().split("\\.");
            String path = file.getPath();
            if (split.length < 2 || !split[split.length-1].equals("json")) {
                path += ".json";
            }
            TreeGeneratorApp.file = new File(path);
        }
        return action;
    }

    public static void closeApplication() {
        FileManager.writeJson(file.getPath(), treeInfo);
        mainFrame.dispose();
        blockPickerFrame.dispose();
        toolFrame.dispose();
    }

    private static class MainPanel extends JPanel {

        private Image background = SkyBox.day.getImage();
        private Image treeBase = FileManager.loadInternalImage("tree_generator/base");

        private int buttonDown = 0;

        public MainPanel() {
            setPreferredSize(new Dimension(600,600));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, 1000, 1000, null);
            for (int x = 0; x < 15; x++) {
                for (int y = 0; y < 15; y++) {
                    g.setColor(Color.BLACK);
                    g.drawRect(x*40, y*40, 40, 40);
                    if (treeInfo.blocks[x][y] != null) {
                        g.drawImage(treeInfo.blocks[x][y].getImage(), x*40, y*40, 40, 40, null);
                    }
                }
            }
            Point p = treeInfo.baseLocation;
            g.drawImage(treeBase, p.x*40, p.y*40, 40, 40, null);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    buttonDown = e.getButton();
                    if (buttonDown == 1) {
                        Point t = toolPanel.getPoint();
                        Point l = e.getPoint();
                        int x = Math.floorDiv(l.x, 40);
                        int y = Math.floorDiv(l.y, 40);
                        if (t.x == 0 && t.y == 0) {
                            BlockType block = blockPickerPanel.selectedblock;
                            treeInfo.blocks[x][y] = block;

                        }else if (t.x == 0 && t.y == 1) {
                            treeInfo.baseLocation = new Point(x, y);
                        }else if (t.x == 1 && t.y == 0) {
                            treeInfo.blocks[x][y] = null;
                        }
                        repaint();
                    }
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (buttonDown == 1) {
                        Point t = toolPanel.getPoint();
                        Point l = e.getPoint();
                        int x = Math.floorDiv(l.x, 40);
                        int y = Math.floorDiv(l.y, 40);
                        if (t.x == 0 && t.y == 0) {
                            BlockType block = blockPickerPanel.selectedblock;
                            treeInfo.blocks[x][y] = block;

                        }else if (t.x == 1 && t.y == 0) {
                            treeInfo.blocks[x][y] = null;
                        }
                        repaint();
                    }
                }
            });

        }
    }

    private static class BlockPickerPanel extends JPanel {

        private int x, y;
        public BlockType selectedblock = BlockType.stone;

        private final Image blockSheet = FileManager.loadInternalImage("block/block_sheet");

        public BlockPickerPanel() {
            setPreferredSize(new Dimension(blockSheet.getWidth(null), blockSheet.getHeight(null)));
            setSelected(0, 0);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    Point l = e.getPoint();
                    setSelected(l.x, l.y);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(blockSheet, 0, 0, blockSheet.getWidth(null), blockSheet.getHeight(null), null);
            g.setColor(Color.BLUE);
            g.drawRect(x*40, y*40, 40, 40);
            g.drawRect(x*40+1, y*40+1, 38, 38);
            g.drawRect(x*40+2, y*40+2, 36, 36);
        }

        public void setSelected(int x, int y) {
            this.x = Math.floorDiv(x, 40);
            this.y = Math.floorDiv(y, 40);
            Image i = FileManager.getBlockTypeImage(this.x, this.y);
            for (var b: BlockType.values()) {
                if (ImageUtil.imagesEqual(i, b.getImage())) {
                    selectedblock = b;
                    break;
                }
            }
            repaint();
        }
    }

    public static class ToolPanel extends JPanel {

        private int x, y;

        Image tools = FileManager.loadInternalImage("tree_generator/tools");

        public ToolPanel() {
            setPreferredSize(new Dimension(tools.getWidth(null), tools.getHeight(null)));
            setSelected(0, 0);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    Point l = e.getPoint();
                    setSelected(l.x, l.y);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(tools, 0, 0, tools.getWidth(null), tools.getHeight(null), null);
            g.setColor(Color.BLACK);
            g.drawRect(x*30, y*30, 30, 30);
        }

        public void setSelected(int x, int y) {
            int px = this.x;
            int py = this.y;
            this.x = Math.floorDiv(x, 30);
            this.y = Math.floorDiv(y, 30);

            if (this.x == 1 && this.y == 1) {
                this.x = px;
                this.y = py;
                FileManager.writeJson(file.getPath(), treeInfo);
                chooseFile();
                if (file.exists()) {
                    treeInfo = (TreeInfo) FileManager.readJson(file.getPath(), TreeInfo.class);
                }else {
                    treeInfo = new TreeInfo();
                }
                mainPanel.repaint();
            }

            repaint();
        }

        public Point getPoint() {
            return new Point(x, y);
        }
    }

}
