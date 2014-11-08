package Support_Classes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import persistence_handler.Driver;

public class RandomPosterGenerater {

    public static String currentMovie;
    public static int recommendationCount=0;
    public static int currentMovieID=0;

    public static int randomNumberGenerater() {
        int numberSet = 0;
        int randomValue = 0;

        Random numberGenerator = new Random();


        randomValue = numberGenerator.nextInt(500);
        numberSet = randomValue;


        return numberSet;
    }

    public static String getMoviePosterSet(int movieNumber, DatabaseSupport database) {
        try {
            String movieName = null;
            String moviePoster = null;
//                Connection con = Driver.connect();
//                ResultSet rst1 = Handler.getData(con, "Select title from movie where id='"+ movieNumber +"'");
//                
//                while (rst1.next()) {
//                    movieName = rst1.getString(1);
//                }


            ResultSet rs = database.query("Select title from movie where id='" + movieNumber + "'");

            while (rs.next()) {
                movieName = rs.getString(1);
            }

            String movieTitle;
            String apiURL = " http://www.imdbapi.com/";
            String totalURL;
            String response;
            InputStream inStream;
            DataInputStream dataStream;

            //request, receive and process Movie details
            try {
                currentMovie = movieName;
                movieTitle = currentMovie;
                currentMovieID=movieNumber;

                movieTitle = movieTitle.replace(" ", "+");
                totalURL = apiURL + "?i=&t=" + movieTitle;
                System.out.println("Movie :" +currentMovie);
                URL url = new URL(totalURL);
                inStream = url.openStream();
                dataStream = new DataInputStream(inStream);
                response = dataStream.readLine();
                //end of processing
                //constructing information array
                String[] array = response.replace("\":\"", ":").split("\",\"");
                //end of construction

                moviePoster = array[13].substring(7);

            } catch (MalformedURLException ex) {
                JOptionPane.showMessageDialog(null, "Movie poster for "+ currentMovie+" cannot be retrieved");
            } catch (IOException ex) {
                Logger.getLogger(RandomPosterGenerater.class.getName()).log(Level.SEVERE, null, ex);
            }

            return moviePoster;
        } catch (SQLException ex) {
            Logger.getLogger(RandomPosterGenerater.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void setPostersAndLabel(final JLabel poster, String moviePosterURL) {

        try {
            BufferedImage bi = new BufferedImage(158, 212, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bi.createGraphics();
            Image img = ImageIO.read(new URL(moviePosterURL));
            g.drawImage(img, 0, 0, 158, 212, null);
            g.dispose();
            ImageIcon icon = new ImageIcon((Image) bi);
            poster.setIcon(icon);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Movie poster for"+ currentMovie+" cannot be retrieved");
            Logger.getLogger(RandomPosterGenerater.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    
    public static void setRecommendedPosters(final JLabel poster, String moviePosterURL) {

        try {
            BufferedImage bi = new BufferedImage(230, 327, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bi.createGraphics();
            Image img = ImageIO.read(new URL(moviePosterURL));
            g.drawImage(img, 0, 0, 230, 327, null);
            g.dispose();
            ImageIcon icon = new ImageIcon((Image) bi);
            poster.setIcon(icon);
        } catch (IOException ex) {
            Logger.getLogger(RandomPosterGenerater.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public static void setRunningThread(final JLabel thread, String loadingthread) {
        thread.setText(loadingthread);
        thread.repaint();

    }
    
    public static void iterateRecommendationList(final JLabel recommendation){
        
    }
}
