

package Support_Classes;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;


public class Recommender {
    
    DBConnect dbc = new DBConnect();
    private Connection con;
    private Statement st;
    private ResultSet rs;
    
    ArrayList<Integer> userlist = new ArrayList<Integer>();
    ArrayList<Integer> movielist = new ArrayList<Integer>();
    ArrayList<Movie> similarMovieList = new ArrayList<Movie>();
    ArrayList<Movie> SortedsimMovList =  new ArrayList<Movie>();
    ArrayList<Movie> SortedsimMovList2 =  new ArrayList<Movie>();
    ArrayList<Movie> ml= new ArrayList<Movie>();
    HashSet uniquemovies = new HashSet();
   
    public Recommender(){
        this.con = dbc.getcn();
        this.st  = dbc.getst();
    }
     
    public String[] getData(){             
      String movienames[] = new String[1682];     
      int i=0;     
        try{       
            String query = "select title from movie";
            rs = st.executeQuery(query);           
            while(rs.next()){
                movienames[i] = rs.getString("name");
                i++;
            }   
        }catch(Exception e){
            System.out.println("Error");    
            return null;
        }
        return movienames;
    }//end of method
    
    public void insertUserData(int id, String name){
        try{
        
         PreparedStatement statement = con.prepareStatement("insert into user(id,name,noofratings,avgrating) values(?,?,?,?)");
         
         statement.setInt(1, id);
         statement.setString(2, name);
         statement.setInt(3, 0);
         statement.setFloat(4, 0);
         statement.executeUpdate();
       
             
        
        }catch(Exception e){
            
            System.out.println("Error");    
         
        }
        
    }//end of method
    
        public void insertRatingData(int userid, float[] rating){
        try{
        
         int bandid[] = new int[8];
         for(int i=0; i<8; i++)
             bandid[i]=i+1;
         
         PreparedStatement statement = con.prepareStatement("insert into rating(u_id,b_id,rating) values(?,?,?)");
         
         for(int i=0; i<8; i++){
             
         statement.setInt(1, userid);
         statement.setInt(2, bandid[i]);
         statement.setFloat(3, rating[i]);
         statement.executeUpdate();
         
         }
        
        }catch(Exception e){
            
            System.out.println("Error");    
         
        }
        
    }//end of method
    
//*********************************************************************************************************************        
   //get ratings of a particular user     
    public User getUserRatings(int uid){
        
        User u1 = new User("user1",uid);
        int noOfRatings =0;
        float sum=0, avgRatings = 0;
        try{
            //query one
            String query0 = "select name from user where id =" +uid;
            rs = st.executeQuery(query0);
            while(rs.next()){
                String uname = rs.getString("name");
                //System.out.println("Ratings for user : "+uname);
            }   
            //query two
            String query = "select title, rating from rating natural join movie where u_id =" +uid+ " and m_id = id";
            rs = st.executeQuery(query);
   
            while(rs.next()){
              String name = rs.getString("title");
              float rating = rs.getFloat("rating");
              sum = sum + rating;
              if(rating!=0.0){noOfRatings++;}
             //  System.out.println("Name : "+name);
               // System.out.println("Rating : "+rating);
            }
             
          avgRatings = sum / noOfRatings;
        //  System.out.println("No of Ratings : "+noOfRatings);
        //  System.out.println("Average Ratings : "+avgRatings);         
          u1.setValues(noOfRatings, avgRatings);
        
        }catch(Exception e){          
            System.out.println("Error");              
        }
        return u1;  
    }//end of method
   
    //find other users who have rated the selected movie (as 5 stars)
    public ArrayList<Integer> findUserData(int mid){                     
            try{        
                String query = "select distinct u_id from rating where m_id=" +mid +" and rating=5";
                
                rs = st.executeQuery(query);
                while(rs.next()){
                     userlist.add(rs.getInt("u_id"));
                }            
            }catch(Exception e){
                System.out.println("Error");
            }    
            return userlist;                 
     }//end of method
        
        
     //find other movies which have rated by those other users
     public ArrayList<Integer> getOtherMovies(ArrayList<Integer> list, int selectedM_id){
         movielist.clear();
         uniquemovies.clear();
         int listSize;
         
         if(list.size()<35){
             listSize = list.size();
         }else if((35<list.size())&&(list.size()<45)){
             listSize = (list.size() * 70) / 100 ;
         }else if((45<list.size())&&(list.size()<55)){
             listSize = (list.size() * 60) / 100 ;
         }else if((55<list.size())&&(list.size()<65)){
             listSize = (list.size() * 50) / 100 ;
         }else if((65<list.size())&&(list.size()<75)){
             listSize = (list.size() * 40) / 100 ;
         }else if((75<list.size())&&(list.size()<85)){
             listSize = (list.size() * 30) / 100 ;
         }else if((85<list.size())&&(list.size()<95)){
             listSize = (list.size() * 20) / 100 ;
         }else{
             listSize = (list.size() * 10) / 100;
         }
         
         
           
            try{
           // have to define a criteria to reduce other movies
              for(int i=0; i<listSize; i++){  
                    String query = "select id from movie where id in(select distinct m_id from rating where u_id="+list.get(i)+" and rating=5) and year >=1997";
                    rs = st.executeQuery(query);
                    while(rs.next()){
                        if(!(rs.getInt("id")==selectedM_id)){
                         movielist.add(rs.getInt("id"));
                       }
                    }                    
              }
      
              uniquemovies.addAll(movielist);
              
              movielist.clear();
              movielist.addAll(uniquemovies);
              
              Collections.sort(movielist);
          //    for(int i=0; i<movielist.size(); i++){
         //        System.out.println(movielist.get(i)+"  \tcount  "+(i+1));
         //     }
             
            }catch(Exception e){
                System.out.println("ErrornoM");
            }           
            return movielist;
     }//end of method
        

     /*
     step 3
     ------
     calculate similarities between selected movie and each of the movies in the above list
     for that;
     for each movie in the above list, we need to get the user who rated it and his rating and also his average rating, then we 
     need to subtract average rating from the rating and form the rating vector for each movie in the list.
     */
     
     //1st get the list of users who rate a given movie
     
     public ArrayList<Movie> calcSimilarity(ArrayList<Integer> finalMovList, int selectedMovie){
         
         
     similarMovieList.clear();
     ArrayList<User> tempUserList = new ArrayList<User>();
     Recommender rc = new Recommender();
     String smtitle = null;
     try{
         String q = "select title from movie where id="+selectedMovie;
         rs = st.executeQuery(q);
         while(rs.next()){
             smtitle = rs.getString("title");
         }
     }catch(Exception e){System.out.println("Error1");}
     
     Movie selectedMv = new Movie(smtitle,selectedMovie);
    /*
         System.out.println("==========================================================");
         System.out.println("Selected Movie : ");
         selectedMv.printIDandTitle();
     */
     for(int p=0; p<finalMovList.size(); p++){
         try{
         String q2 = "select title, genre from movie where id="+finalMovList.get(p);
         rs = st.executeQuery(q2);
         while(rs.next()){
             Movie m = new Movie(rs.getString("title"),finalMovList.get(p));
             String genre = rs.getString("genre");
             
             m.setGenre(genre);
             similarMovieList.add(m);
         }
     }catch(Exception e){System.out.println("Error2");}
     
     }
     
     //for each movie in the finalMovList and for the selected movie
     //get the list of users who have rated bothe the movies
     
     for(int i=0; i<finalMovList.size(); i++){ 
        // System.out.println("Step1 ================================================");
        // System.out.println("MovieList :"+finalMovList.get(i));
         try{        //users who have rated both the given movies
                String query = "select r1.u_id from rating r1 inner join rating r2 on r1.u_id = r2.u_id where r1.m_id="+selectedMovie+" and r2.m_id="+finalMovList.get(i);
                
                rs = st.executeQuery(query);
                while(rs.next()){                    
                   tempUserList.add(rc.getUserRatings(rs.getInt("u_id")));                
                }
         //==================================       
         /*System.out.println("Step2 ================================================");
         System.out.println("Users who have both rated the 2 movies");
         for(int tmp=0; tmp<tempUserList.size(); tmp++)
                 System.out.println(tempUserList.get(tmp).getID()+"\tAvgRat= "+tempUserList.get(tmp).getAvg());
             System.out.println("");      
         */  
                 //for each of the user above, get the two ratings for two movies,
                     for(int j=0; j<tempUserList.size(); j++){                      
                         try{                            
                             String query2 = "(select rating from rating where u_id =" +tempUserList.get(j).getID()+" and m_id="+selectedMovie+") union all (select rating from rating where u_id =" +tempUserList.get(j).getID()+" and m_id="+finalMovList.get(i)+")";
                             rs = st.executeQuery(query2);                          
                             int k=0;
                             int temprat[] = new int[2];                             
                             while(rs.next()){                                 
                                temprat[k] = rs.getInt("rating");
                                k++;                          
                              }
                             //got the 2 rating of a particular user, then add to ratingVector of each movie                             
                             selectedMv.addToRatingVector(temprat[0]-tempUserList.get(j).getAvg());
                             similarMovieList.get(i).addToRatingVector(temprat[1]-tempUserList.get(j).getAvg());                                   
                         }catch(Exception e){System.out.println("Error3");}       
                     }//end of for  
            
               //==================================       
        /*
         System.out.println("Step3 ================================================");
         System.out.println("for each of the user above, get the two ratings for two movies"); 
         System.out.println("then add to ratingVector of each movie");
         System.out.println("Rating Vector of Selected Movie :");
         selectedMv.printRatingVector();
         System.out.println("Rating Vector of Movie from the list :");
         similarMovieList.get(i).printRatingVector();
          */           
             //now calculate similarity of each movie to the selected movie
                     
                     ArrayList<Double> rvS = new ArrayList<Double>();
                     ArrayList<Double> rvX = new ArrayList<Double>();
                     
                     rvS = selectedMv.getRatingVector();
                     rvX = similarMovieList.get(i).getRatingVector();
                     double numerator=0;
                     double denominator1=0;
                     double denominator2=0;
                     
                     for(int h=0; h<rvS.size(); h++){
                        numerator = numerator + (rvS.get(h)*rvX.get(h));
                        denominator1 = denominator1+ Math.pow(rvS.get(h), 2);
                        denominator2 = denominator2+ Math.pow(rvX.get(h), 2);
                     }
                    
                     denominator1 = Math.sqrt(denominator1);
                     denominator2 = Math.sqrt(denominator2);
                     
                     double similarity = numerator / (denominator1*denominator2);
                     similarMovieList.get(i).setSimilarity(similarity);
                     tempUserList.clear();
                     selectedMv.clearRatingVector();
                  
         }catch(Exception e){System.out.println("Error4 "+e);}  
     }  

         System.out.println("inside Calc:" +similarMovieList.size());
         return similarMovieList;
     }//end of method
     
     //finally sorting the similar movie list and selecting top n movies and recommend

     
          
     public ArrayList<Movie> topNrecByGenre(ArrayList<Movie> simMovList){
            //SortedsimMovList2.clear();
         //sort        
            SortedsimMovList2 = simMovList;
            Collections.sort(SortedsimMovList2, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return Double.compare(m2.getSimilarity(), m1.getSimilarity());
            }
            });

         return SortedsimMovList2;
         
     }
     
     
     
     public ArrayList<Movie> topNrec(ArrayList<Movie> simMvList){
            //SortedsimMovList.clear();
         //sort        
            SortedsimMovList = simMvList;
            Collections.sort(SortedsimMovList, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return Double.compare(m2.getSimilarity(), m1.getSimilarity());
            }
            });
         return SortedsimMovList;
         
     }
 
     //method for get the seen movies
     
     public String getFavGenre(String[] checkedMovies){
         
         String favGenre = "";
         int genList[] = new int[19];
         for(int j=0; j<genList.length;j++) {genList[j]=0;}
         
         for(int i=0; i<checkedMovies.length; i++){
             String genre = checkedMovies[i];
             
                if(genre.equals("unknown"))         {genList[0]++;}
                else if(genre.equals("Action"))     {genList[1]++;}
                else if(genre.equals("Adventure"))  {genList[2]++;}
                else if(genre.equals("Animation"))  {genList[3]++;}
                else if(genre.equals("Children's")) {genList[4]++;}
                else if(genre.equals("Comedy"))     {genList[5]++;}
                else if(genre.equals("Crime"))      {genList[6]++;}
                else if(genre.equals("Documentary")){genList[7]++;}
                else if(genre.equals("Drama"))      {genList[8]++;}     
                else if(genre.equals("Fantasy"))    {genList[9]++;}
                else if(genre.equals("Film-Noir"))  {genList[10]++;}     
                else if(genre.equals("Horror"))     {genList[11]++;}
                else if(genre.equals("Musical"))    {genList[12]++;}    
                else if(genre.equals("Mystery"))    {genList[13]++;}     
                else if(genre.equals("Romance"))    {genList[14]++;}
                else if(genre.equals("Sci-Fi"))     {genList[15]++;}     
                else if(genre.equals("Thriller"))   {genList[16]++;}
                else if(genre.equals("War"))        {genList[17]++;}
                else if(genre.equals("Western"))    {genList[18]++;}
                     
         }
         
         int max = genList[0],maxGen=0;
         for(int j=1; j<genList.length;j++) {
             if(genList[j]>max){
                 max = genList[j];
                 maxGen = j;
             }
         }
         
                if(maxGen==0)           {favGenre="unknown";}           
                else if(maxGen==1)      {favGenre="Action";}
                else if(maxGen==2)      {favGenre="Adventure";}
                else if(maxGen==3)      {favGenre="Animation";}
                else if(maxGen==4)      {favGenre="Children's";}
                else if(maxGen==5)      {favGenre="Comedy";}
                else if(maxGen==6)      {favGenre="Crime";}
                else if(maxGen==7)      {favGenre="Documentary";}
                else if(maxGen==8)      {favGenre="Drama";}
                else if(maxGen==9)      {favGenre="Fantasy";}
                else if(maxGen==10)     {favGenre="Film-Noir";}
                else if(maxGen==11)     {favGenre="Horror";}
                else if(maxGen==12)     {favGenre="Musical";}
                else if(maxGen==13)     {favGenre="Mystery";}
                else if(maxGen==14)     {favGenre="Romance";}
                else if(maxGen==15)     {favGenre="Sci-Fi";}
                else if(maxGen==16)     {favGenre="Thriller";}
                else if(maxGen==17)     {favGenre="War";}
                else if(maxGen==18)     {favGenre="Western";}
       
                return favGenre;
     }
     
    
          //find other movies filtered by particular genre
     public ArrayList<Integer> getOtherMovies2ndTime(ArrayList<Integer> list, int selectedM_id, String genre, int[] excludeMovieList){
         movielist.clear();
         uniquemovies.clear();
         int listSize;
         
         if(list.size()<35){
             listSize = list.size();
         }else if((35<list.size())&&(list.size()<45)){
             listSize = (list.size() * 70) / 100 ;
         }else if((45<list.size())&&(list.size()<55)){
             listSize = (list.size() * 60) / 100 ;
         }else if((55<list.size())&&(list.size()<65)){
             listSize = (list.size() * 50) / 100 ;
         }else if((65<list.size())&&(list.size()<75)){
             listSize = (list.size() * 40) / 100 ;
         }else if((75<list.size())&&(list.size()<85)){
             listSize = (list.size() * 30) / 100 ;
         }else if((85<list.size())&&(list.size()<95)){
             listSize = (list.size() * 20) / 100 ;
         }else{
             listSize = (list.size() * 10) / 100;
         }
                   
            try{          
              for(int i=0; i<listSize; i++){  
                    String query = "select id from movie where id in(select distinct m_id from rating where u_id="+list.get(i)+" and rating=5) and year >=1997 and genre like '_"+genre+"%'";
                    rs = st.executeQuery(query);
                    while(rs.next()){
                        if(!(rs.getInt("id")==selectedM_id)){
                            
                          //to exclude already watched movies
                           int temp=0;
                           for(int k=0; k<excludeMovieList.length;k++){
                               if(rs.getInt("id")==excludeMovieList[k]){
                                   temp++;
                               }
                           }
                            if(temp==0){
                            movielist.add(rs.getInt("id"));
                            }
                       }
                    }                    
              }
      
              uniquemovies.addAll(movielist);
              
              movielist.clear();
              movielist.addAll(uniquemovies);
              
              Collections.sort(movielist);
           //   for(int i=0; i<movielist.size(); i++){
           //      System.out.println(movielist.get(i)+"  \tcount  "+(i+1));
          //    }
             
            }catch(Exception e){
                System.out.println("ErrornoM");
            }           
            return movielist;
     }//end of method
}//end of class

