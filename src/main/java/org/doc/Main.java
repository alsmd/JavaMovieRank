package org.doc;

public class Main {
    public static void main(String[] args) {
        DefaultApi api = new ApiMovie();
        if (api.success == false){
            System.out.println("Something went wrong with the api! Log: ");
            System.out.println(api.getErrorMessage());
            return ;
        }
        try {
            ImageRanker ranker = new ImageRanker("Movies");
            System.out.println("Ranking Data....");
            for (RankerData data : api.getData()){
                ranker.saveImage(data);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}