package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MovieRanker {

    final private BufferedImage bronzeBorder = ImageIO.read(new File("images/bronze.png"));
    final private BufferedImage silverBorder = ImageIO.read(new File("images/silver.png"));
    final private BufferedImage goldenBorder = ImageIO.read(new File("images/golden.png"));

    public MovieRanker() throws IOException {
    }

    /**
     *
     * @param movie
     * @brief Create an image from movie poster based on its votes avarage
     */
    public BufferedImage createImage(Movie movie) throws IOException {
        InputStream inputStream = new URL(movie.poster_path).openStream();
        BufferedImage moviePoster = ImageIO.read(inputStream);
        Graphics2D graphics = (Graphics2D) moviePoster.getGraphics();
        graphics.drawImage(getBorderRank(movie), 0, 0, moviePoster.getWidth(), moviePoster.getHeight(), null);
        return moviePoster;
    }

    /**
     * @brief Save an image from Movie's poster costamazing based in its rank and saving in folder postersRanked with Movie's title
     * @param movie Movie where image will be generated from
     */
    public boolean saveImage(Movie movie){
        try{
            BufferedImage posterRanked = createImage(movie);
            String dirName = "postersRanked/" + getRankName(movie) + "/";
            File dir = new File(dirName);
            if (!dir.exists()) dir.mkdirs();
            ImageIO.write(posterRanked, "png", new File(dirName + movie.title + ".png"));
            return true;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }

    private BufferedImage getBorderRank(Movie movie){
        if (movie.vote_average.floatValue() > 6.5){
            return goldenBorder;
        }else if (movie.vote_average.floatValue() > 4.0){
            return silverBorder;
        }
        return bronzeBorder;
    }

    private String getRankName(Movie movie){
        if (movie.vote_average.floatValue() > 6.5){
            return "golden";
        }else if (movie.vote_average.floatValue() > 4.0){
            return "silver";
        }
        return "bronze";
    }
}
