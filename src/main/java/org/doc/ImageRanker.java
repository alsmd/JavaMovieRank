package org.doc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageRanker {
    private String path;
    final private BufferedImage bronzeBorder = ImageIO.read(new File("images/bronze.png"));
    final private BufferedImage silverBorder = ImageIO.read(new File("images/silver.png"));
    final private BufferedImage goldenBorder = ImageIO.read(new File("images/golden.png"));

    public ImageRanker(String path) throws IOException {
        this.path = path;
    }

    /**
     *
     * @param movie
     * @brief Create an image from movie poster based on its votes avarage
     */
    public BufferedImage createImage(RankerData movie) throws IOException {
        InputStream inputStream = new URL(movie.imageUrl).openStream();
        BufferedImage moviePoster = ImageIO.read(inputStream);
        Graphics2D graphics = (Graphics2D) moviePoster.getGraphics();
        graphics.drawImage(getBorderRank(movie), 0, 0, moviePoster.getWidth(), moviePoster.getHeight(), null);
        return moviePoster;
    }

    /**
     * @brief Save an image from RankerData's poster costamazing based in its rank and saving in folder postersRanked with RankerData's title
     * @param movie RankerData where image will be generated from
     */
    public boolean saveImage(RankerData data){
        try{
            BufferedImage posterRanked = createImage(data);
            String dirName = this.path + "/" + getRankName(data) + "/";
            File dir = new File(dirName);
            if (!dir.exists()) dir.mkdirs();
            String regexNotAlphaNum = "[^A-Za-z0-9]";
            String fileName = dirName + data.name.replaceAll(regexNotAlphaNum, "_") + ".png";
            ImageIO.write(posterRanked, "png", new File(fileName));
            return true;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }

    private BufferedImage getBorderRank(RankerData data){
        if (data.vote.floatValue() > 6.5){
            return goldenBorder;
        }else if (data.vote.floatValue() > 4.0){
            return silverBorder;
        }
        return bronzeBorder;
    }
    private String getRankName(RankerData data){
        if (data.vote.floatValue() > 6.5){
            return "golden";
        }else if (data.vote.floatValue() > 4.0){
            return "silver";
        }
        return "bronze";
    }
}
