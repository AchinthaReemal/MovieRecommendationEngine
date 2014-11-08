/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;


import Support_Classes.DatabaseSupport;
import Support_Classes.Movie;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import Support_Classes.RandomPosterGenerater;
import Support_Classes.Recommender;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import persistence_handler.Driver;

/**
 *
 * @author Sony
 */

public class RecommendationCenter extends javax.swing.JFrame {

    /**
     * Creates new form RecommendationCenter
     */
   
//    private static RecommendationCenter torrentcenter = new RecommendationCenter( );
    private SearchMovies mSearch;
    private SenceME sence;
    private RecommendME recommendation;
    private RecommendMEIterator iterator;
    private GenreBasedRecommendation genreRecommend;
    boolean available=false;
    int positionX,positionY;
    private static String username;
    private static String profilename;
    public static ArrayList<String> availableAutomatics = new ArrayList<>();
    Object downloadList[];
    String[] downloadFilenames;
    String[] downloadLinks;
    int clickCount1=0;
    int clickCount2=0;
    int clickCount3=0;
    int selectedMovie;
    ArrayList<String> retreive;
    public ArrayList<String> alreadyWatchedGenres= new ArrayList<>();
    public ArrayList<Integer> alreadyWatchedIDs= new ArrayList<>();
    public ArrayList<Integer> intermediateIDs= new ArrayList<>();
    public ArrayList<Movie> recommendedMovieSet= new ArrayList<>();
    public ArrayList<Movie> sortedRecommendedMovieSet= new ArrayList<>();
    public static int movieSelectionList[]= new int[6];
    Recommender recommend = new Recommender();
    DatabaseSupport database= Driver.createPool();
        
    public RecommendationCenter(){
        
        initComponents();
        this.setIconImage(new ImageIcon(getClass().getResource("FrameLogo.png")).getImage());
        menuBar.setOpaque(true);
        menuBar.setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);
        Holder.setVisible(false);
        MovieDBPAne.setVisible(false);
        
        HomePanel.setVisible(false);
        
        MovieLoadingPane.setVisible(false);
        SearchField.setPrompt("  Search for a movie/TV show ");
        searchDefault.setOpaque(false);
        MovieDBPAne.setOpaque(false);
        
        Holder.setOpaque(false);
        MovieLoadingPane.setOpaque(false);   
        HomePanel.setOpaque(false);
        MovieRecommendationPane.setVisible(false);
        RecommendationLoadingPane.setVisible(false);
        MovieRecommendationPane.setVisible(false);
        menuBar.setBackground(Color.WHITE);
            
        
        
    }   
    
//    public static RecommendationCenter getInstance( ) {
//      return torrentcenter;
//    }
        
    class SearchMovies extends SwingWorker<Void, Void>{

        @Override
        public Void doInBackground(){
            try {
            
            Holder.setVisible(false);
            String movieTitle;
            String apiURL = " http://www.imdbapi.com/";
            String totalURL;
            String response;
            String details[];
            InputStream inStream;  
            DataInputStream dataStream;
            
            //request, receive and process Movie details
            movieTitle= SearchField.getText().trim();
            movieTitle=movieTitle.replace(" ", "+");
            totalURL = apiURL+"?i=&t="+movieTitle;            
            URL url = new URL(totalURL);
            inStream = url.openStream(); 
            dataStream  = new DataInputStream(inStream);            
            response=dataStream.readLine();
            //end of processing
            
            //constructing information array
            String[] array = response.replace("\":\"", ":").split("\",\"");
            //end of construction

            //set image
            String imageURL=array[13].substring(7);
            BufferedImage img = ImageIO.read(new URL(imageURL));
            ImageIcon icon = new ImageIcon((Image)img);
            imageLabel.setIcon(icon);
            //end of image setting
            
            //label setting
            title.setText(array[0].substring(8));
            year.setText(array[1].substring(5,9));
            released.setText(array[3].substring(9));
            genre.setText(array[5].substring(6));
            runtime.setText(array[4].substring(8));
            director.setText(array[6].substring(9));
            if(array[8].substring(7).length()>60){
                actors.setText(array[8].substring(7).substring(0, 59)+"\n"+array[8].substring(7).substring(59));
            }else{
                actors.setText(array[8].substring(7));
            }
            actors.setOpaque(false);
            imdbrating.setText(array[15].substring(11));
            if(array[9].substring(5).length()>115){
                plot.setText(array[9].substring(5).substring(0, 59)+"\n"+array[9].substring(5).substring(59,118)+"\n"+array[9].substring(5).substring(118));
            }
            else{
                plot.setText(array[9].substring(5).substring(0, 59)+"\n"+array[9].substring(5).substring(59));
            }
            plot.setOpaque(false);
            Holder.setVisible(true);
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(RecommendationCenter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RecommendationCenter.class.getName()).log(Level.SEVERE, null, ex);
        }
            return null;
        }
        
        @Override
        public void done(){
            MovieLoadingPane.setVisible(false);
            Holder.setVisible(true);            
        }
    }
    
    class SenceME extends SwingWorker<Void, Void>{

        @Override
        public Void doInBackground(){
                MovieLabelPane.setVisible(false);
                DatabaseSupport database= Driver.createPool();
                
                RandomPosterGenerater.setPostersAndLabel(movie1, RandomPosterGenerater.getMoviePosterSet(RandomPosterGenerater.randomNumberGenerater(),database));
                movieSelectionList[0]= RandomPosterGenerater.currentMovieID; 
                RandomPosterGenerater.setPostersAndLabel(movie2, RandomPosterGenerater.getMoviePosterSet(RandomPosterGenerater.randomNumberGenerater(),database));
                movieSelectionList[1]= RandomPosterGenerater.currentMovieID;
                RandomPosterGenerater.setPostersAndLabel(movie3, RandomPosterGenerater.getMoviePosterSet(RandomPosterGenerater.randomNumberGenerater(),database));
                movieSelectionList[2]= RandomPosterGenerater.currentMovieID;
                RandomPosterGenerater.setPostersAndLabel(movie4, RandomPosterGenerater.getMoviePosterSet(RandomPosterGenerater.randomNumberGenerater(),database));
                movieSelectionList[3]= RandomPosterGenerater.currentMovieID; 
                RandomPosterGenerater.setPostersAndLabel(movie5, RandomPosterGenerater.getMoviePosterSet(RandomPosterGenerater.randomNumberGenerater(),database));
                movieSelectionList[4]= RandomPosterGenerater.currentMovieID;
                RandomPosterGenerater.setPostersAndLabel(movie6, RandomPosterGenerater.getMoviePosterSet(RandomPosterGenerater.randomNumberGenerater(),database));
                movieSelectionList[5]= RandomPosterGenerater.currentMovieID;
                                
            
            return null;
        }
        
        @Override
        public void done(){
            RecommendationLoadingPane.setVisible(false);
            MovieLabelPane.setVisible(true);            
        }
    }
    
    class RecommendME extends SwingWorker<Void, Void>{

        int movieID=0;
        
        public RecommendME(int movieID){
            this.movieID=movieID;
            selectedMovie=movieID;
            System.out.println("Selected Movie:" +selectedMovie);
        }
        
        @Override
        public Void doInBackground(){
            
                MovieLabelPane.setVisible(false);
                RecommendationLoadingPane.setVisible(true);
                //recommendedMovieSet.clear();
                //sortedRecommendedMovieSet.clear();
                recommendedMovieSet=recommend.calcSimilarity(recommend.getOtherMovies(recommend.findUserData(movieID), movieID), movieID);     
                System.out.println("Checking Array size: "+recommendedMovieSet.size());
                sortedRecommendedMovieSet=recommend.topNrec(recommendedMovieSet);
                System.out.println("Checking Array size after: "+recommendedMovieSet.size());
                for (int i = 0; i < 5; i++) {
                    System.out.println("Print Reached");
                    System.out.println(sortedRecommendedMovieSet.get(i).getTitle());
                }
                RandomPosterGenerater.setRecommendedPosters(recommend1, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(0).getMovieID(),database));
                RandomPosterGenerater.setRecommendedPosters(recommend2, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(1).getMovieID(),database));
                RandomPosterGenerater.setRecommendedPosters(recommend3, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(2).getMovieID(),database));
            return null;
        }
        
        @Override
        public void done(){
            genreBasedRecButtonPane.setVisible(true);
            RecommendationLoadingPane.setVisible(false);
            MovieRecommendationPane.setVisible(true);
            alreadyWatchedCheck1.setVisible(true);
            alreadyWatchedCheck2.setVisible(true);
            alreadyWatchedCheck3.setVisible(true);
            LoadingPane1.setVisible(false);
            LoadingPane2.setVisible(false);
            LoadingPane3.setVisible(false);
            genreFrame.setVisible(false);
            recommendedGenre.setVisible(false);
        }
    }
    
    class RecommendMEIterator extends SwingWorker<Void, Void>{

        int iteration;
        int componentID;
        
        public RecommendMEIterator(int iteration, int componentID){
            this.iteration=iteration;
            this.componentID=componentID;
        }
        
        @Override
        public Void doInBackground(){
            if(iteration<sortedRecommendedMovieSet.size()){            
                if(componentID==1){
                    LabelPane1.setVisible(false);
                    LoadingPane1.setVisible(true);
                    RandomPosterGenerater.setRecommendedPosters(recommend1, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(iteration).getMovieID(),database));
                }
                else if(componentID==2){
                    LabelPane2.setVisible(false);
                    LoadingPane2.setVisible(true);
                    RandomPosterGenerater.setRecommendedPosters(recommend2, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(iteration).getMovieID(),database));
                }
                else if(componentID==3){
                    LabelPane3.setVisible(false);
                    LoadingPane3.setVisible(true);
                    RandomPosterGenerater.setRecommendedPosters(recommend3, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(iteration).getMovieID(),database));
                }
            
                alreadyWatchedIDs.add(recommendedMovieSet.get(iteration).getMovieID());
                alreadyWatchedGenres.add(recommendedMovieSet.get(iteration).getGenre());
            }                
            return null;
        }
        
        @Override
        public void done(){
            if(componentID==1){
                LabelPane1.setVisible(true);
                LoadingPane1.setVisible(false);
                alreadyWatchedCheck1.setSelected(false);
            }
            else if(componentID==2){
                LabelPane2.setVisible(true);
                LoadingPane2.setVisible(false);
                alreadyWatchedCheck2.setSelected(false);
            }
            else if(componentID==3){
                LabelPane3.setVisible(true);
                LoadingPane3.setVisible(false);
                alreadyWatchedCheck3.setSelected(false);
            }
        }
    }
    
        class GenreBasedRecommendation extends SwingWorker<Void, Void>{

        String favoriteGenre;    
            
        @Override
        public Void doInBackground(){            
                MovieRecommendationPane.setVisible(false);
                RecommendationLoadingPane.setVisible(true);
                String genresList[] = new String[alreadyWatchedGenres.size()];
                genresList=alreadyWatchedGenres.toArray(genresList);
                favoriteGenre=recommend.getFavGenre(genresList);
                
                System.out.println("Favorite:   "+favoriteGenre);
                int idList[] = new int[alreadyWatchedIDs.size()];
                
                for (int i = 0; i < alreadyWatchedIDs.size(); i++) {
                    idList[i]=alreadyWatchedIDs.get(i).intValue();
                    System.out.println("Already watched:"+idList[i]+"   "+genresList[i]);
                }
                System.out.println("********Genre Based*******");
                //recommendedMovieSet.clear();
                //sortedRecommendedMovieSet.clear();
                intermediateIDs=recommend.getOtherMovies2ndTime(recommend.findUserData(selectedMovie), selectedMovie,favoriteGenre,idList);        
                recommendedMovieSet=recommend.calcSimilarity(intermediateIDs, selectedMovie);
                sortedRecommendedMovieSet=recommend.topNrecByGenre(recommendedMovieSet);
                
                if(sortedRecommendedMovieSet.size()>2){
                    for (int i = 0; i < 5; i++) {
                        System.out.println("    "+sortedRecommendedMovieSet.get(i).getMovieID()+"   "+sortedRecommendedMovieSet.get(i).getTitle());
                    }
                    RandomPosterGenerater.setRecommendedPosters(recommend1, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(0).getMovieID(),database));
                    RandomPosterGenerater.setRecommendedPosters(recommend2, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(1).getMovieID(),database));
                    RandomPosterGenerater.setRecommendedPosters(recommend3, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(2).getMovieID(),database));
                }  
                else if(sortedRecommendedMovieSet.size()==2){
                    RandomPosterGenerater.setRecommendedPosters(recommend1, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(0).getMovieID(),database));
                    RandomPosterGenerater.setRecommendedPosters(recommend2, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(1).getMovieID(),database));
                }
                else if(sortedRecommendedMovieSet.size()==1){
                    RandomPosterGenerater.setRecommendedPosters(recommend2, RandomPosterGenerater.getMoviePosterSet(sortedRecommendedMovieSet.get(0).getMovieID(),database));
                }
                else{
                    JOptionPane.showMessageDialog(null, "Further Recommendation for "+favoriteGenre+" cannot be found");
                }
            return null;
        }
        
        @Override
        public void done(){
            RecommendationLoadingPane.setVisible(false);
            MovieRecommendationPane.setVisible(true);
            alreadyWatchedCheck1.setVisible(false);
            alreadyWatchedCheck2.setVisible(false);
            alreadyWatchedCheck3.setVisible(false);
            genreFrame.setVisible(true);
            recommendedGenre.setVisible(true);
            genreFrame.setText("Genre Based Recommendations");
            recommendedGenre.setText("Genre: "+favoriteGenre);
            genreBasedRecButtonPane.setVisible(false);
            clickCount1=0;
            clickCount2=0;
            clickCount3=0;
        }
    }
    
    
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        ButtonsPane = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        LayeredPane = new javax.swing.JLayeredPane();
        searchDefault = new javax.swing.JPanel();
        randomPoster1 = new javax.swing.JLabel();
        randomPoster2 = new javax.swing.JLabel();
        randomPoster5 = new javax.swing.JLabel();
        randomPoster6 = new javax.swing.JLabel();
        randomPoster3 = new javax.swing.JLabel();
        MovieDBPAne = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        Holder = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        title = new javax.swing.JLabel();
        genre = new javax.swing.JLabel();
        released = new javax.swing.JLabel();
        year = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        imdbrating = new javax.swing.JLabel();
        director = new javax.swing.JLabel();
        runtime = new javax.swing.JLabel();
        actors = new javax.swing.JTextArea();
        plot = new javax.swing.JTextArea();
        imageLabel = new javax.swing.JLabel();
        MovieLoadingPane = new javax.swing.JPanel();
        HomePanel = new javax.swing.JPanel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        MovieLabelPane = new javax.swing.JPanel();
        movie1 = new javax.swing.JLabel();
        like1 = new javax.swing.JButton();
        like6 = new javax.swing.JButton();
        like2 = new javax.swing.JButton();
        like7 = new javax.swing.JButton();
        like8 = new javax.swing.JButton();
        like3 = new javax.swing.JButton();
        movie4 = new javax.swing.JLabel();
        movie2 = new javax.swing.JLabel();
        movie5 = new javax.swing.JLabel();
        movie6 = new javax.swing.JLabel();
        movie3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        refreshButton = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        RecommendationLoadingPane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        MovieRecommendationPane = new javax.swing.JPanel();
        genreBasedRecButtonPane = new javax.swing.JPanel();
        rerecommend = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        layeredPaneContainer = new javax.swing.JPanel();
        Rec3Layered = new javax.swing.JLayeredPane();
        LabelPane3 = new javax.swing.JPanel();
        alreadyWatchedCheck3 = new javax.swing.JCheckBox();
        recommend3 = new javax.swing.JLabel();
        LoadingPane3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        Rec2Layered = new javax.swing.JLayeredPane();
        LabelPane2 = new javax.swing.JPanel();
        alreadyWatchedCheck2 = new javax.swing.JCheckBox();
        recommend2 = new javax.swing.JLabel();
        LoadingPane2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        Rec1Layered = new javax.swing.JLayeredPane();
        LabelPane1 = new javax.swing.JPanel();
        alreadyWatchedCheck1 = new javax.swing.JCheckBox();
        recommend1 = new javax.swing.JLabel();
        LoadingPane1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        goBackButtonPane = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        titlePanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        recommendedGenre = new javax.swing.JLabel();
        genreFrame = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 51));
        setUndecorated(true);

        ButtonsPane.setBackground(new java.awt.Color(0, 0, 0));

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setText("Movie Database");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setText("Sense Me");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(51, 51, 51));
        jButton10.setText("Exit");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ButtonsPaneLayout = new javax.swing.GroupLayout(ButtonsPane);
        ButtonsPane.setLayout(ButtonsPaneLayout);
        ButtonsPaneLayout.setHorizontalGroup(
            ButtonsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ButtonsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ButtonsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ButtonsPaneLayout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ButtonsPaneLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        ButtonsPaneLayout.setVerticalGroup(
            ButtonsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ButtonsPaneLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        randomPoster5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/poster_9.jpg"))); // NOI18N

        randomPoster6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/poster_13.jpg"))); // NOI18N

        randomPoster3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/poster_7.jpg"))); // NOI18N

        javax.swing.GroupLayout searchDefaultLayout = new javax.swing.GroupLayout(searchDefault);
        searchDefault.setLayout(searchDefaultLayout);
        searchDefaultLayout.setHorizontalGroup(
            searchDefaultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchDefaultLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchDefaultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(searchDefaultLayout.createSequentialGroup()
                        .addComponent(randomPoster1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(randomPoster3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(randomPoster5, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(randomPoster6, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(randomPoster2, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        searchDefaultLayout.setVerticalGroup(
            searchDefaultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchDefaultLayout.createSequentialGroup()
                .addComponent(randomPoster2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchDefaultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(randomPoster1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(randomPoster5, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(randomPoster6, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(randomPoster3, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        LayeredPane.add(searchDefault);
        searchDefault.setBounds(0, 10, 850, 580);

        searchButton.setBackground(new java.awt.Color(102, 102, 102));
        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        Holder.setBackground(new java.awt.Color(153, 153, 153));
        Holder.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei", 1, 11)); // NOI18N
        jLabel2.setText("Title");

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 1, 11)); // NOI18N
        jLabel3.setText("Genre");

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 1, 11)); // NOI18N
        jLabel4.setText("Year");

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei", 1, 11)); // NOI18N
        jLabel5.setText("Released");

        title.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N

        genre.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N

        released.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N

        year.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei", 1, 11)); // NOI18N
        jLabel6.setText("Run Time");

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei", 1, 11)); // NOI18N
        jLabel7.setText("Director");

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei", 1, 11)); // NOI18N
        jLabel8.setText("Actors");

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei", 1, 11)); // NOI18N
        jLabel9.setText("Plot");

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei", 1, 11)); // NOI18N
        jLabel10.setText("IMDB Rating");

        imdbrating.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N

        director.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N

        runtime.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N

        actors.setBackground(new java.awt.Color(153, 153, 153));
        actors.setColumns(20);
        actors.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        actors.setRows(5);

        plot.setBackground(new java.awt.Color(153, 153, 153));
        plot.setColumns(20);
        plot.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        plot.setRows(5);

        imageLabel.setText("                          Image");

        javax.swing.GroupLayout HolderLayout = new javax.swing.GroupLayout(Holder);
        Holder.setLayout(HolderLayout);
        HolderLayout.setHorizontalGroup(
            HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HolderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(HolderLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(plot))
                    .addGroup(HolderLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imdbrating, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(HolderLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(actors))
                    .addGroup(HolderLayout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(director, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(HolderLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(runtime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(HolderLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(released, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(HolderLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(year, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(HolderLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(genre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(HolderLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(154, 154, 154))
        );
        HolderLayout.setVerticalGroup(
            HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(HolderLayout.createSequentialGroup()
                        .addGroup(HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(29, 29, 29)
                        .addGroup(HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(genre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(29, 29, 29)
                        .addGroup(HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(released, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5))
                        .addGap(29, 29, 29)
                        .addGroup(HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(runtime, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(director, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(25, 25, 25)
                        .addGroup(HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(actors, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(imdbrating, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10))
                        .addGap(33, 33, 33)
                        .addGroup(HolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(plot, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        HolderLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {director, genre, released, runtime, title});

        jLayeredPane1.add(Holder);
        Holder.setBounds(10, 10, 810, 510);

        javax.swing.GroupLayout MovieLoadingPaneLayout = new javax.swing.GroupLayout(MovieLoadingPane);
        MovieLoadingPane.setLayout(MovieLoadingPaneLayout);
        MovieLoadingPaneLayout.setHorizontalGroup(
            MovieLoadingPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );
        MovieLoadingPaneLayout.setVerticalGroup(
            MovieLoadingPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );

        jLayeredPane1.add(MovieLoadingPane);
        MovieLoadingPane.setBounds(233, 10, 330, 350);

        javax.swing.GroupLayout MovieDBPAneLayout = new javax.swing.GroupLayout(MovieDBPAne);
        MovieDBPAne.setLayout(MovieDBPAneLayout);
        MovieDBPAneLayout.setHorizontalGroup(
            MovieDBPAneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MovieDBPAneLayout.createSequentialGroup()
                .addGap(666, 666, 666)
                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 53, Short.MAX_VALUE))
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        MovieDBPAneLayout.setVerticalGroup(
            MovieDBPAneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MovieDBPAneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                .addContainerGap())
        );

        LayeredPane.add(MovieDBPAne);
        MovieDBPAne.setBounds(0, 0, 830, 590);

        MovieLabelPane.setBackground(new java.awt.Color(255, 255, 255));

        like1.setText("Like");
        like1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                like1ActionPerformed(evt);
            }
        });

        like6.setText("Like");
        like6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                like6ActionPerformed(evt);
            }
        });

        like2.setText("Like");
        like2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                like2ActionPerformed(evt);
            }
        });

        like7.setText("Like");
        like7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                like7ActionPerformed(evt);
            }
        });

        like8.setText("Like");
        like8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                like8ActionPerformed(evt);
            }
        });

        like3.setText("Like");
        like3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                like3ActionPerformed(evt);
            }
        });

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("Select a Movie which you have");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("watched and loved from ");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setText("the given set of movies, ");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("by clicking 'Like'");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setText("If the list does not contain");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("a Movie which you prefer,");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel21.setText("you can change the selection");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setText("set by clicking 'Refresh'");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel23.setText("Recommendations will be ");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setText("based on your preference,");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel25.setText("on the movies provide in ");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel26.setText("this window");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addGap(32, 32, 32)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22)
                .addGap(68, 68, 68)
                .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        javax.swing.GroupLayout MovieLabelPaneLayout = new javax.swing.GroupLayout(MovieLabelPane);
        MovieLabelPane.setLayout(MovieLabelPaneLayout);
        MovieLabelPaneLayout.setHorizontalGroup(
            MovieLabelPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MovieLabelPaneLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(MovieLabelPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(like6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(like1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(movie4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(movie1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(MovieLabelPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(movie2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(like2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(movie5, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(like7, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(MovieLabelPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(movie6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(movie3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(like8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(like3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MovieLabelPaneLayout.setVerticalGroup(
            MovieLabelPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MovieLabelPaneLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(MovieLabelPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MovieLabelPaneLayout.createSequentialGroup()
                        .addComponent(movie2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(like2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(movie5, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(like7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MovieLabelPaneLayout.createSequentialGroup()
                        .addGroup(MovieLabelPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MovieLabelPaneLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(movie1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(like1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(movie4, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(MovieLabelPaneLayout.createSequentialGroup()
                                .addComponent(movie3, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(like3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(movie6, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(MovieLabelPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(like6, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(like8, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(43, Short.MAX_VALUE))
            .addGroup(MovieLabelPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane2.add(MovieLabelPane);
        MovieLabelPane.setBounds(0, 10, 840, 570);

        RecommendationLoadingPane.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/loading_anim.gif"))); // NOI18N

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel29.setText("Please wait while retrieving information");

        javax.swing.GroupLayout RecommendationLoadingPaneLayout = new javax.swing.GroupLayout(RecommendationLoadingPane);
        RecommendationLoadingPane.setLayout(RecommendationLoadingPaneLayout);
        RecommendationLoadingPaneLayout.setHorizontalGroup(
            RecommendationLoadingPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RecommendationLoadingPaneLayout.createSequentialGroup()
                .addGroup(RecommendationLoadingPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RecommendationLoadingPaneLayout.createSequentialGroup()
                        .addGap(303, 303, 303)
                        .addComponent(jLabel1))
                    .addGroup(RecommendationLoadingPaneLayout.createSequentialGroup()
                        .addGap(244, 244, 244)
                        .addComponent(jLabel29)))
                .addContainerGap(264, Short.MAX_VALUE))
        );
        RecommendationLoadingPaneLayout.setVerticalGroup(
            RecommendationLoadingPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RecommendationLoadingPaneLayout.createSequentialGroup()
                .addGap(137, 137, 137)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addContainerGap(138, Short.MAX_VALUE))
        );

        jLayeredPane2.add(RecommendationLoadingPane);
        RecommendationLoadingPane.setBounds(60, 50, 740, 440);

        genreBasedRecButtonPane.setBackground(new java.awt.Color(102, 102, 102));

        rerecommend.setText("Re-Recommend");
        rerecommend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rerecommendActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setText("Further Recommendations can be requested after selecting already watched movies");

        javax.swing.GroupLayout genreBasedRecButtonPaneLayout = new javax.swing.GroupLayout(genreBasedRecButtonPane);
        genreBasedRecButtonPane.setLayout(genreBasedRecButtonPaneLayout);
        genreBasedRecButtonPaneLayout.setHorizontalGroup(
            genreBasedRecButtonPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, genreBasedRecButtonPaneLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(rerecommend)
                .addGap(21, 21, 21))
        );
        genreBasedRecButtonPaneLayout.setVerticalGroup(
            genreBasedRecButtonPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(genreBasedRecButtonPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(genreBasedRecButtonPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rerecommend)
                    .addComponent(jLabel27))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        alreadyWatchedCheck3.setText("Already Watched");
        alreadyWatchedCheck3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alreadyWatchedCheck3ActionPerformed(evt);
            }
        });

        recommend3.setDoubleBuffered(true);
        recommend3.setPreferredSize(new java.awt.Dimension(230, 327));

        javax.swing.GroupLayout LabelPane3Layout = new javax.swing.GroupLayout(LabelPane3);
        LabelPane3.setLayout(LabelPane3Layout);
        LabelPane3Layout.setHorizontalGroup(
            LabelPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LabelPane3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(alreadyWatchedCheck3)
                .addGap(66, 66, 66))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LabelPane3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recommend3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        LabelPane3Layout.setVerticalGroup(
            LabelPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LabelPane3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(recommend3, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alreadyWatchedCheck3)
                .addGap(12, 12, 12))
        );

        Rec3Layered.add(LabelPane3);
        LabelPane3.setBounds(0, 0, 254, 381);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/loading_anim.gif"))); // NOI18N

        javax.swing.GroupLayout LoadingPane3Layout = new javax.swing.GroupLayout(LoadingPane3);
        LoadingPane3.setLayout(LoadingPane3Layout);
        LoadingPane3Layout.setHorizontalGroup(
            LoadingPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoadingPane3Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );
        LoadingPane3Layout.setVerticalGroup(
            LoadingPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoadingPane3Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(137, Short.MAX_VALUE))
        );

        Rec3Layered.add(LoadingPane3);
        LoadingPane3.setBounds(0, 0, 250, 370);

        alreadyWatchedCheck2.setText("Already Watched");
        alreadyWatchedCheck2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alreadyWatchedCheck2ActionPerformed(evt);
            }
        });

        recommend2.setDoubleBuffered(true);
        recommend2.setPreferredSize(new java.awt.Dimension(230, 327));

        javax.swing.GroupLayout LabelPane2Layout = new javax.swing.GroupLayout(LabelPane2);
        LabelPane2.setLayout(LabelPane2Layout);
        LabelPane2Layout.setHorizontalGroup(
            LabelPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LabelPane2Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(alreadyWatchedCheck2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LabelPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recommend2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        LabelPane2Layout.setVerticalGroup(
            LabelPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LabelPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recommend2, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(alreadyWatchedCheck2)
                .addContainerGap())
        );

        Rec2Layered.add(LabelPane2);
        LabelPane2.setBounds(0, 0, 254, 381);

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/loading_anim.gif"))); // NOI18N

        javax.swing.GroupLayout LoadingPane2Layout = new javax.swing.GroupLayout(LoadingPane2);
        LoadingPane2.setLayout(LoadingPane2Layout);
        LoadingPane2Layout.setHorizontalGroup(
            LoadingPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoadingPane2Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jLabel13)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        LoadingPane2Layout.setVerticalGroup(
            LoadingPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoadingPane2Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(132, Short.MAX_VALUE))
        );

        Rec2Layered.add(LoadingPane2);
        LoadingPane2.setBounds(0, 0, 250, 370);

        alreadyWatchedCheck1.setText("Already Watched");
        alreadyWatchedCheck1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alreadyWatchedCheck1ActionPerformed(evt);
            }
        });

        recommend1.setDoubleBuffered(true);
        recommend1.setPreferredSize(new java.awt.Dimension(230, 327));

        javax.swing.GroupLayout LabelPane1Layout = new javax.swing.GroupLayout(LabelPane1);
        LabelPane1.setLayout(LabelPane1Layout);
        LabelPane1Layout.setHorizontalGroup(
            LabelPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LabelPane1Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(alreadyWatchedCheck1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(LabelPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recommend1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        LabelPane1Layout.setVerticalGroup(
            LabelPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LabelPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recommend1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alreadyWatchedCheck1)
                .addContainerGap())
        );

        Rec1Layered.add(LabelPane1);
        LabelPane1.setBounds(0, 0, 250, 370);

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/loading_anim.gif"))); // NOI18N

        javax.swing.GroupLayout LoadingPane1Layout = new javax.swing.GroupLayout(LoadingPane1);
        LoadingPane1.setLayout(LoadingPane1Layout);
        LoadingPane1Layout.setHorizontalGroup(
            LoadingPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoadingPane1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );
        LoadingPane1Layout.setVerticalGroup(
            LoadingPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoadingPane1Layout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(jLabel12)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        Rec1Layered.add(LoadingPane1);
        LoadingPane1.setBounds(0, 0, 250, 370);

        javax.swing.GroupLayout layeredPaneContainerLayout = new javax.swing.GroupLayout(layeredPaneContainer);
        layeredPaneContainer.setLayout(layeredPaneContainerLayout);
        layeredPaneContainerLayout.setHorizontalGroup(
            layeredPaneContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layeredPaneContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Rec1Layered, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(Rec2Layered, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(Rec3Layered, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layeredPaneContainerLayout.setVerticalGroup(
            layeredPaneContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layeredPaneContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layeredPaneContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Rec2Layered, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Rec3Layered, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Rec1Layered, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        goBackButtonPane.setBackground(new java.awt.Color(102, 102, 102));

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel28.setText("Go back to initial recommendation selection process");

        javax.swing.GroupLayout goBackButtonPaneLayout = new javax.swing.GroupLayout(goBackButtonPane);
        goBackButtonPane.setLayout(goBackButtonPaneLayout);
        goBackButtonPaneLayout.setHorizontalGroup(
            goBackButtonPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, goBackButtonPaneLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        goBackButtonPaneLayout.setVerticalGroup(
            goBackButtonPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(goBackButtonPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(goBackButtonPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton)
                    .addComponent(jLabel28))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setText("You Might Also Like");

        recommendedGenre.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N

        genreFrame.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, titlePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(genreFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115)
                .addComponent(recommendedGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(200, 200, 200))
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addGap(313, 313, 313)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(recommendedGenre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genreFrame, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout MovieRecommendationPaneLayout = new javax.swing.GroupLayout(MovieRecommendationPane);
        MovieRecommendationPane.setLayout(MovieRecommendationPaneLayout);
        MovieRecommendationPaneLayout.setHorizontalGroup(
            MovieRecommendationPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MovieRecommendationPaneLayout.createSequentialGroup()
                .addGroup(MovieRecommendationPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(goBackButtonPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(genreBasedRecButtonPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(layeredPaneContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(MovieRecommendationPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
        MovieRecommendationPaneLayout.setVerticalGroup(
            MovieRecommendationPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MovieRecommendationPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(layeredPaneContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(genreBasedRecButtonPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(goBackButtonPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane2.add(MovieRecommendationPane);
        MovieRecommendationPane.setBounds(0, 0, 850, 590);

        javax.swing.GroupLayout HomePanelLayout = new javax.swing.GroupLayout(HomePanel);
        HomePanel.setLayout(HomePanelLayout);
        HomePanelLayout.setHorizontalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
        );
        HomePanelLayout.setVerticalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );

        LayeredPane.add(HomePanel);
        HomePanel.setBounds(0, 0, 850, 590);

        menuBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuBarMousePressed(evt);
            }
        });
        menuBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                menuBarMouseDragged(evt);
            }
        });

        jMenu2.setBackground(new java.awt.Color(0, 0, 0));
        jMenu2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });
        menuBar.add(jMenu2);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ButtonsPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
            .addComponent(ButtonsPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        searchDefault.setVisible(false);
        MovieDBPAne.setVisible(true);
        HomePanel.setVisible(false);     
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        movieSearchBusy.setBusy(true);
        MovieLoadingPane.setVisible(true);
        mSearch = new SearchMovies();
        mSearch.execute();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void menuBarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuBarMousePressed
        positionX=evt.getX();
        positionY=evt.getY();
    }//GEN-LAST:event_menuBarMousePressed

    private void menuBarMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuBarMouseDragged
        setLocation(getLocation().x+evt.getX()-positionX,getLocation().y+evt.getY()-positionY);
    }//GEN-LAST:event_menuBarMouseDragged

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        searchDefault.setVisible(false);
        MovieDBPAne.setVisible(false);
        HomePanel.setVisible(true);
        MovieLabelPane.setVisible(false);
        MovieRecommendationPane.setVisible(false);
        RecommendationLoadingPane.setVisible(true);
        sence = new SenceME();
        sence.execute();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        int choice = JOptionPane.showConfirmDialog(null,"Do You Really Want to Close the Recommendation Engine", "Exiting Recommendation Engine",  JOptionPane.YES_NO_OPTION);
        if(choice== JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void like1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_like1ActionPerformed
        recommendation = new RecommendME(movieSelectionList[0]);
        recommendation.execute();
    }//GEN-LAST:event_like1ActionPerformed

    private void like2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_like2ActionPerformed
        recommendation = new RecommendME(movieSelectionList[1]);
        recommendation.execute();
    }//GEN-LAST:event_like2ActionPerformed

    private void like3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_like3ActionPerformed
        recommendation = new RecommendME(movieSelectionList[2]);
        recommendation.execute();
    }//GEN-LAST:event_like3ActionPerformed

    private void like6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_like6ActionPerformed
        recommendation = new RecommendME(movieSelectionList[3]);
        recommendation.execute();
    }//GEN-LAST:event_like6ActionPerformed

    private void like7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_like7ActionPerformed
        recommendation = new RecommendME(movieSelectionList[4]);
        recommendation.execute();
    }//GEN-LAST:event_like7ActionPerformed

    private void like8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_like8ActionPerformed
        recommendation = new RecommendME(movieSelectionList[5]);
        recommendation.execute();
    }//GEN-LAST:event_like8ActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        RecommendationLoadingPane.setVisible(true);
        sence = new SenceME();
        sence.execute();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void alreadyWatchedCheck1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alreadyWatchedCheck1ActionPerformed
     if(alreadyWatchedCheck1.isSelected()){  
        clickCount1++;
        iterator = new RecommendMEIterator(clickCount1*3,1);
        iterator.execute();
     }   
    }//GEN-LAST:event_alreadyWatchedCheck1ActionPerformed

    private void alreadyWatchedCheck2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alreadyWatchedCheck2ActionPerformed
     if(alreadyWatchedCheck2.isSelected()){  
        clickCount2++;
        iterator = new RecommendMEIterator(clickCount2*3+1,2);
        iterator.execute();
     }  
    }//GEN-LAST:event_alreadyWatchedCheck2ActionPerformed

    private void alreadyWatchedCheck3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alreadyWatchedCheck3ActionPerformed
        if(alreadyWatchedCheck3.isSelected()){  
           clickCount3++;
           iterator = new RecommendMEIterator(clickCount3*3+2,3);
           iterator.execute();
        }  
    }//GEN-LAST:event_alreadyWatchedCheck3ActionPerformed

    private void rerecommendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rerecommendActionPerformed
        if(clickCount1>0 || clickCount2>0 || clickCount3>0){
            genreRecommend = new GenreBasedRecommendation();
            genreRecommend.execute();
        }  
        else{
            JOptionPane.showConfirmDialog(null,"Please Select the Movies you have already watched");
        }
    }//GEN-LAST:event_rerecommendActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
       MovieLabelPane.setVisible(true);
       MovieRecommendationPane.setVisible(false);
       recommendedMovieSet.clear();
       intermediateIDs.clear();
       alreadyWatchedGenres.clear();
       alreadyWatchedIDs.clear();
       sortedRecommendedMovieSet.clear();
       clickCount1=0;
       clickCount2=0;
       clickCount3=0;
       RecommendationLoadingPane.setVisible(true);
       sence = new SenceME();
       sence.execute();
    }//GEN-LAST:event_backButtonActionPerformed

    public static void setUsername(String uname){
        username=uname;
    }
    
    public static void setProfilename(String pname){
        profilename=pname;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getProfilename(){
        return profilename;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
           /* for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            */
           //UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RecommendationCenter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RecommendationCenter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RecommendationCenter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RecommendationCenter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                    new RecommendationCenter().setVisible(true);                
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonsPane;
    private javax.swing.JPanel Holder;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JPanel LabelPane1;
    private javax.swing.JPanel LabelPane2;
    private javax.swing.JPanel LabelPane3;
    private javax.swing.JLayeredPane LayeredPane;
    private javax.swing.JPanel LoadingPane1;
    private javax.swing.JPanel LoadingPane2;
    private javax.swing.JPanel LoadingPane3;
    private javax.swing.JPanel MovieDBPAne;
    private javax.swing.JPanel MovieLabelPane;
    private javax.swing.JPanel MovieLoadingPane;
    private javax.swing.JPanel MovieRecommendationPane;
    private javax.swing.JLayeredPane Rec1Layered;
    private javax.swing.JLayeredPane Rec2Layered;
    private javax.swing.JLayeredPane Rec3Layered;
    private javax.swing.JPanel RecommendationLoadingPane;
    private javax.swing.JTextArea actors;
    private javax.swing.JCheckBox alreadyWatchedCheck1;
    private javax.swing.JCheckBox alreadyWatchedCheck2;
    private javax.swing.JCheckBox alreadyWatchedCheck3;
    private javax.swing.JButton backButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel director;
    private javax.swing.JLabel genre;
    private javax.swing.JPanel genreBasedRecButtonPane;
    private javax.swing.JLabel genreFrame;
    private javax.swing.JPanel goBackButtonPane;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel imdbrating;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel layeredPaneContainer;
    private javax.swing.JButton like1;
    private javax.swing.JButton like2;
    private javax.swing.JButton like3;
    private javax.swing.JButton like6;
    private javax.swing.JButton like7;
    private javax.swing.JButton like8;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel movie1;
    private javax.swing.JLabel movie2;
    private javax.swing.JLabel movie3;
    private javax.swing.JLabel movie4;
    private javax.swing.JLabel movie5;
    private javax.swing.JLabel movie6;
    private javax.swing.JTextArea plot;
    private javax.swing.JLabel randomPoster1;
    private javax.swing.JLabel randomPoster2;
    private javax.swing.JLabel randomPoster3;
    private javax.swing.JLabel randomPoster5;
    private javax.swing.JLabel randomPoster6;
    private javax.swing.JLabel recommend1;
    private javax.swing.JLabel recommend2;
    private javax.swing.JLabel recommend3;
    private javax.swing.JLabel recommendedGenre;
    private javax.swing.JButton refreshButton;
    private javax.swing.JLabel released;
    private javax.swing.JButton rerecommend;
    private javax.swing.JLabel runtime;
    private javax.swing.JButton searchButton;
    private javax.swing.JPanel searchDefault;
    private javax.swing.JLabel title;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JLabel year;
    // End of variables declaration//GEN-END:variables
}
