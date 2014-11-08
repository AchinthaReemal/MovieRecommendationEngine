///*
// * @cyclops
// */
//
//package Support_Classes;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//
//public class Main {
//    
//    public static void main(String args[]){
//        ArrayList<Integer> userlist = new ArrayList<Integer>();
//        ArrayList<Integer> finalMovieList = new ArrayList<Integer>();
//        ArrayList<Integer> finalMovieListByGenre = new ArrayList<Integer>();
//        ArrayList<Movie> similarMovieList = new ArrayList<Movie>();
//        ArrayList<Movie> similarMovieListByGenre = new ArrayList<Movie>();
//        ArrayList<Movie> topNList = new ArrayList<Movie>();
//        ArrayList<Movie> topNListByGenre = new ArrayList<Movie>();
//    
//     //ArrayList ratingsList = new ArrayList();
//     
//        Recommender recsys = new Recommender();
//        
//        //get ratings of a particular user
//       // User user1 = new User();
//        
//       //user1 = recsys.getUserRatings(1);
//        
//               
//        /*
//        step 1
//        ------
//        find other users (need to get a list of user ids)who rated the selected movie as good ; --> users who rated that movie =5
//        *get the selected movie id
//        and use the "rating" table, find all those users who have rated that movie id =3
//        ||sql query||
//        "select distinct u_id from rating where m_id=<selected movie id> "
//        */
//        int tmid = 213;
//        //eg : find other user who have rated the movie (id=3) as 5 starts
//        userlist = recsys.findUserData(tmid);
//        
//        for(int i=0; i<userlist.size(); i++){
//            System.out.println(userlist.get(i));
//        }
//        
//        System.out.println("");
//        System.out.println("no of users: "+userlist.size());
//        System.out.println("");
//                
//        //get other rated (as 5 stars) movies by those users except the selected movie
//                                                        //M_id
//         System.out.println("other movies rated by those users");
//        finalMovieList = recsys.getOtherMovies(userlist, tmid);
//        //print finalMovieList
//         for(int i=0; i<finalMovieList.size(); i++){
//                 System.out.println(finalMovieList.get(i)+"  \tcount  "+(i+1));
//         }
//        
//        
//        //finalMovieList is the list where we gonna calculate similarities with  the selected movie
//        
//        similarMovieList = recsys.calcSimilarity(finalMovieList, tmid);
//     
//               
//        System.out.println("xxxxxxxxxxxx---Top N(=6) Recommended list---xxxxxxxxxxxxxxxxxxx");
//        topNList = recsys.topNrec(similarMovieList);
//        
//        for(int j=0; j<6; j++ )
//        System.out.println(topNList.get(j).getSimilarity()+"\t"+topNList.get(j).getID()+"\t"+topNList.get(j).getTitle()+"\t"+topNList.get(j).getGenre());
//
//        
//        //2nd time recommendation
//        //need a list of "I have seen this movie" movie list
//        String sampleGenreList[] = new String[5];
//        
//        //this is for testing purposes.......
//        sampleGenreList[0] = "Comedy";
//        sampleGenreList[1] = "Comedy";
//        sampleGenreList[2] = "Adventure";
//        sampleGenreList[3] = "Comedy";
//        sampleGenreList[4] = "Action";
//        
//        //achintha, put the "I already view" checked movie list's genre list as the argument to the following method
//        
//        
//                                                  //Genre list : String[]
//        String favoriteGenre = recsys.getFavGenre(sampleGenreList);
//        System.out.println("Favorite Genre: "+favoriteGenre);
//        
//        //recommend me again...
//        System.out.println("other movies rated by those users filtered by genre");
//        
//        //this is also for testing
//        int excludingMovieList[] = new int[4];
//        
//        excludingMovieList[0] = 1377;
//        excludingMovieList[1] = 242;
//        excludingMovieList[2] = 904;
//        excludingMovieList[3] = 246;
//        
//        System.out.println("");
//        System.out.println("Recommended 2nd Time");
//                                                          //otherUserList                      //int id array
//       finalMovieListByGenre = recsys.getOtherMovies2ndTime(userlist, tmid, favoriteGenre, excludingMovieList);
//                                                                  //selectedMovieID 
//       for(int i=0; i<finalMovieListByGenre.size(); i++){
//         System.out.println(finalMovieListByGenre.get(i)+"  \tcount  "+(i+1));
//       }
//
//       System.out.println("");
//       //again calculate similarity in the movies of the new list
//       similarMovieListByGenre = recsys.calcSimilarity(finalMovieListByGenre, tmid);
//        System.out.println("similarMovieListByGenre");
//       for(int i=0; i<similarMovieListByGenre.size(); i++){
//         System.out.println(similarMovieListByGenre.get(i).getSimilarity()+"  \tcount  "+(i+1));
//       }
//       
//
//       System.out.println("");
//       System.out.println("Sort");
//       System.out.println("xxxxxxxxxxxx---Top N Recommended list (2 nd time)---xxxxxxxxxxxxxxxxxxx");
//       topNListByGenre.clear();
//       topNListByGenre = recsys.topNrecByGenre(similarMovieListByGenre);
//       
//       for(int j=0; j<topNListByGenre.size(); j++ )
//       System.out.println(topNListByGenre.get(j).getSimilarity()+"\t"+topNListByGenre.get(j).getID()+"\t"+topNListByGenre.get(j).getTitle()+"\t"+topNListByGenre.get(j).getGenre());
//
//             
//       
//       
//       
//        
//        
//    }//end of main method
//    
//}
