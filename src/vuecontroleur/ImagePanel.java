package vuecontroleur;

import modele.item.ItemShape;
import modele.item.SubShape;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image imgBackground;
    private Image[] animBackground;
    private Image imgFront;
    private ItemShape shape;
    private modele.item.ItemColor itemColor;

    private modele.plateau.Direction direction;

    public void setShape(ItemShape _shape) {
        shape = _shape;
    }

    public void setItemColor(modele.item.ItemColor _color) {
        itemColor = _color;
    }

    public void setBackground(Image _imgBackground) {
        imgBackground = _imgBackground;
        animBackground = null;
    }

    public void setAnimBackground(Image[] frames) {
        animBackground = frames;
        imgBackground = null;
    }

    public void setFront(Image _imgFront) {
        imgFront = _imgFront;
    }
    
    public void setDirection(modele.plateau.Direction dir) {
        this.direction = dir;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final int bordure= 1;
        
        // Use full cell width/height for backgrounds to ensure seamless continuity (e.g. for belts)
        final int xBack = 0;
        final int yBack = 0;
        final int widthBack = getWidth();
        final int heigthBack = getHeight();

        final int subPartWidth = (getWidth() - bordure*2) / 4;
        final int subPartHeigth = (getHeight() - bordure*2) / 4;

        final int xFront = bordure + subPartWidth;
        final int yFront = bordure + subPartHeigth;
        final int widthFront = subPartWidth*2;
        final int heigthFront = subPartHeigth*2;

        // cadre (optional grid stroke, drawn behind)
        g.drawRoundRect(bordure, bordure, getWidth() - bordure*2, getHeight() - bordure*2, bordure, bordure);

        Image currentBg = null;
        if (animBackground != null && animBackground.length > 0) {
            currentBg = animBackground[VueControleur.animationFrame % animBackground.length];
        } else if (imgBackground != null) {
            currentBg = imgBackground;
        }

        if (currentBg != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            int drawX = xBack;
            int drawY = yBack;
            int drawW = widthBack;
            int drawH = heigthBack;

            if (direction != null) {
                double angle = 0;
                boolean rotated90 = false;
                switch (direction) {
                    case North: angle = 0; break;
                    case East: angle = Math.PI / 2; rotated90 = true; break;
                    case South: angle = Math.PI; break;
                    case West: angle = -Math.PI / 2; rotated90 = true; break;
                }
                g2d.rotate(angle, xBack + widthBack / 2.0, yBack + heigthBack / 2.0);
                
                if (rotated90) {
                    drawW = heigthBack;
                    drawH = widthBack;
                    drawX = xBack + (widthBack - drawW) / 2;
                    drawY = yBack + (heigthBack - drawH) / 2;
                }
            }
            
            g2d.drawImage(currentBg, drawX, drawY, drawW, drawH, this);
            g2d.dispose();
        }

        if (imgFront != null) {
            g.drawImage(imgFront, xFront, yFront, widthFront, heigthFront, this);
        }


        if (shape != null) {

            // TODO autres layers
            SubShape[] tabS = shape.getSubShapes(ItemShape.Layer.one);
            modele.item.Color[] tabC = shape.getColors(ItemShape.Layer.one);

            for (int i = 0; i < 4; i++) {
                SubShape ss = tabS[i];

                if (ss != SubShape.None) {
                    java.awt.Color c = getColorAwt(tabC[i]);
                    if (c != null) {
                        g.setColor(c);
                    } else {
                        g.setColor(Color.GRAY);
                    }

                    switch (ss) {
                        case SubShape.Carre:
                            g.fillRect(xFront + (widthFront / 2) * ((i >> 1) ^ 1), yFront + (heigthFront / 2) * ((i & 1) ^ ((i >> 1) & 1)), widthFront / 2, heigthFront / 2);
                            break;
                        case SubShape.Circle:
                            g.fillOval(xFront + (widthFront / 2) * ((i >> 1) ^ 1), yFront + (heigthFront / 2) * ((i & 1) ^ ((i >> 1) & 1)), widthFront / 2, heigthFront / 2);
                            break;
                        case SubShape.Star:
                            int xs = xFront + (widthFront / 2) * ((i >> 1) ^ 1);
                            int ys = yFront + (heigthFront / 2) * ((i & 1) ^ ((i >> 1) & 1));
                            int ws = widthFront / 2;
                            int hs = heigthFront / 2;
                            int[] posX = {xs + ws / 2, xs + ws, xs};
                            int[] posY = {ys, ys + hs, ys + hs};
                            g.fillPolygon(posX, posY, 3);
                            break;
                    }
                }
            }
        } else if (itemColor != null) {
            java.awt.Color c = getColorAwt(itemColor.getColor());
            if (c != null) {
                g.setColor(c);
                g.fillOval(xFront + widthFront/4, yFront + heigthFront/4, widthFront/2, heigthFront/2);
            }
        }
    }

    private java.awt.Color getColorAwt(modele.item.Color c) {
        if (c == null) return null;
        switch (c) {
            case modele.item.Color.Red: return Color.RED;
            case modele.item.Color.Green: return Color.GREEN;
            case modele.item.Color.Blue: return Color.BLUE;
            case modele.item.Color.Yellow: return Color.YELLOW;
            case modele.item.Color.Purple: return new Color(128, 0, 128);
            case modele.item.Color.Cyan: return Color.CYAN;
            case modele.item.Color.White: return Color.WHITE;
            default: return Color.GRAY;
        }
    }



    }





