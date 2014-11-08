
package Support_Classes;

public class User {
    
    private String name;
    private int user_id;
    private int noOfRatings;
    private double avgRatings;
        
    public User(){
  
    }
    
    public User(String name, int id){
        user_id = id;
        this.name = name;
    }
      
    public void setValues(int nor, float avg){
        this.noOfRatings = nor;
        this.avgRatings = avg;
    }
    
    public int getNoR(){
        return noOfRatings;
    }
    
    public double getAvg(){
        return avgRatings;
    }
    
    public int getID(){
        return user_id;
    }
}
