/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import constants.ServerConstants;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.AttributedString;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;

/**
 *
 * @author Pungin
 */
public class CheckCodeImageCreator {

    public static Color getRandColor(int start, int end) {
        Random random = new Random();
        if (start > 255) {
            start = 255;
        }
        if (end > 255) {
            end = 255;
        }
        int randNum;
        if (start > end) {
            randNum = start - end;
        } else {
            randNum = end - start;
        }
        int r = start + random.nextInt(randNum);
        int g = start + random.nextInt(randNum);
        int b = start + random.nextInt(randNum);
        return new Color(r, g, b);
    }

    public static String getRandWord(int length, int width, int height, Graphics2D g) {
        Random random = new Random();
        String finalWord = "", firstWord = "";

        MapleData data;
        MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider("String.wz");
        data = dataProvider.getData("Mob.img");
        List<String> mobNameList = new LinkedList<>();
        for (MapleData mobIdData : data.getChildren()) {
            mobNameList.add(MapleDataTool.getString(mobIdData.getChildByPath("name"), ""));
        }
        for (int i = 0; i < length; i++) {
            firstWord = "";
            switch (ServerConstants.MAPLE_TYPE) {
                case 한국:
                case 日本:
                case 中国:
                case 台港澳:
                    while (firstWord.isEmpty()) {
                        String name = mobNameList.get(random.nextInt(mobNameList.size()));
                        name = name.replaceAll("[a-zA-Z]", "");
                        name = name.replaceAll("[^\\pL]", "");
                        if (!name.isEmpty()) {
                            int nameX = random.nextInt(name.length());
                            firstWord = name.substring(nameX, nameX + 1);
                            break;
                        }
                    }
                    break;
                default:
                    if (random.nextInt(2) == 1) {
                        // 大寫字母還是小寫字母  
                        int temp = random.nextInt(2) == 0 ? 0x41 : 0x61;
                        firstWord = String.valueOf((char) (random.nextInt(0x1A) + temp));
                    } else {
                        firstWord = String.valueOf(random.nextInt(10));
                    }
            }
            finalWord += firstWord;
            coloredAndRotation(firstWord, i, g, length, width, height);
        }

        return finalWord;
    }

    public static void coloredAndRotation(String word, int i, Graphics2D g2d, int length, int width, int height) {
        Random random = new Random();
        g2d.setColor(getRandColor(5, 100));

        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D rc = fm.getStringBounds(word, g2d);

        int x = width / length / 2 + width * i / length;
        int y = height / 2 + 10 - random.nextInt(11);

        int rRand = random.nextInt(30);
        AffineTransform trans = new AffineTransform();
        trans.rotate((-15.0 + rRand) * Math.PI / 180.0, x, y);

        float scaleSize = random.nextFloat() + 0.8F;
        if (scaleSize > 1F) {
            scaleSize = 1F;
        }
        trans.scale(scaleSize, scaleSize);

        g2d.setTransform(trans);

        for (int j = 1; j >= 0; j--) {
            if (j == 1) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(0.5F));
            } else {
                g2d.setComposite(AlphaComposite.SrcOver.derive(1.0F));
            }
            if (random.nextInt(2) == 0) {
                AttributedString as = new AttributedString(word);
                as.addAttribute(TextAttribute.FONT, g2d.getFont());
                as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0, 1);
                g2d.drawString(as.getIterator(), x - (int) (rc.getCenterX()), y + (fm.getHeight() - fm.getDescent()) / 2 * j + random.nextInt(fm.getDescent() / 2));
            } else {
                g2d.drawString(word, x - (int) (rc.getCenterX()), y + (fm.getHeight() - fm.getDescent()) / 2 * j + random.nextInt(fm.getDescent() / 2));
            }
        }
    }

    public static Pair<String, File> createCheckCode() {
        int width = 196, height = 44;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        Random random = new Random();

        g.setColor(getRandColor(254, 255));
        g.fillRect(0, 0, width, height);

        int point = 2000 + random.nextInt(500);
        for (int i = 0; i < point; i++) {
            g.setColor(getRandColor(80, 150));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int z = random.nextInt(2);
            g.fillRect(x, y, z, z);
        }

        int line = 2 + random.nextInt(3);
        for (int i = 0; i < line; i++) {
            g.setColor(getRandColor(80, 150));
            int x = random.nextInt(width - 50);
            int y = random.nextInt(height + 20);
            int xl = 50 + random.nextInt(50);
            int yl = -20 + random.nextInt(50);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String fontStr = "";
        switch (ServerConstants.MAPLE_TYPE) {
            case 한국:
                fontStr = "Batang";
                break;
            case 日本:
                fontStr = "MS Gothic";
                break;
            case 中国:
                fontStr = "KaiTi";
                break;
            case 台港澳:
                fontStr = "DFKai-SB";
                break;
            default:
                fontStr = "Arial";
        }
        Font font = new Font(fontStr, Font.BOLD + Font.ITALIC, 25);
        g.setFont(font);

        String sRand = getRandWord(4, width, height, g);

        if (sRand.isEmpty()) {
            return null;
        }
        g.dispose();
        File file = null;
        try {
            file = File.createTempFile("MapleStoryCheckCode", "");
            ImageIO.write(image, "JPEG", file);
        } catch (IOException ex) {
            Logger.getLogger(CheckCodeImageCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (file == null) {
            return null;
        }
        return new Pair(sRand, file);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Pair<String, File> checkCode = createCheckCode();
        FileInputStream fis = new FileInputStream(checkCode.getRight());
        File toFile = new File(checkCode.getLeft() + ".jpg");
        if (toFile.exists()) {
            toFile.delete();
        }
        toFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(toFile);
        byte[] date = new byte[512];
        int rs = -1;
        while ((rs = fis.read(date)) > 0) {
            fout.write(date, 0, rs);
        }
        fout.close();
        fis.close();
    }
}
