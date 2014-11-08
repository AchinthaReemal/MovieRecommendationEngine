

package Support_Classes;

import java.util.ArrayList;



public class Movie {
    
    private String title;
    private int movie_id;
    private ArrayList<Double> ratingVector = new ArrayList<Double>();
    double similarityToSelectedMovie;
    String genre;
    
    private int avgRatings;
        
    
    public Movie(String name, int id){
        movie_id = id;
        this.title = name;
    }
    
    public void addToRatingVector(Double rating){
        ratingVector.add(rating);
    }
    
    public ArrayList<Double> getRatingVector(){
        return ratingVector;
    }
    
    public void printRatingVector(){
        System.out.println("====Printing rating vector=======");
        System.out.print("( ");
        for(int i=0; i<ratingVector.size(); i++){
            System.out.print(ratingVector.get(i)+" ");
        }
        System.out.print(" )");
        System.out.println("");
    }
    
    public void printIDandTitle(){
        System.out.println("ID: "+movie_id);
        System.out.println("Title: "+title);
    }
  
    public void setSimilarity(double sim){
        similarityToSelectedMovie = sim;
    }
    
    public double getSimilarity(){
        return similarityToSelectedMovie;
    }
    
    public void clearRatingVector(){
        ratingVector.clear();
    }
    
    public void setGenre(String g){
        genre = g;
    }
    
    public String getGenre(){
    
      String temp = null;
      
      temp = temp+ genre.charAt(1)+genre.charAt(2);
      temp = temp.substring(4);
      
      if(temp.equals("un")){genre="unknown";}
      else if(temp.equals("Ac")){genre="Action";}
      else if(temp.equals("Ad")){genre="Adventure";}
      else if(temp.equals("An")){genre="Animation";}
      else if(temp.equals("Ch")){genre="Children's";}
      else if(temp.equals("Co")){genre="Comedy";}
      else if(temp.equals("Cr")){genre="Crime";}
      else if(temp.equals("Do")){genre="Documentary";}
      else if(temp.equals("Dr")){genre="Drama";}     
      else if(temp.equals("Fa")){genre="Fantasy";}
      else if(temp.equals("Fi")){genre="Film-Noir";}     
      else if(temp.equals("Ho")){genre="Horror";}
      else if(temp.equals("Mu")){genre="Musical";}    
      else if(temp.equals("My")){genre="Mystery";}     
      else if(temp.equals("Ro")){genre="Romance";}
      else if(temp.equals("Sc")){genre="Sci-Fi";}     
      else if(temp.equals("Th")){genre="Thriller";}
      else if(temp.equals("Wa")){genre="War";}
      else if(temp.equals("We")){genre="Western";}

       return genre;        
    }

    public String getTitle(){
        return title;        
    }
    
   public int getMovieID(){
        return movie_id;        
    } 
    
    
}//end of the class
