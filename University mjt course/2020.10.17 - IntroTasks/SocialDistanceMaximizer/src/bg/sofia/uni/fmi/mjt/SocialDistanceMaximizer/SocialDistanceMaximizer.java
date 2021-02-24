public class SocialDistanceMaximizer {

    public static int maxDistance(int[] seats){
        int currDist=0;
        int maxDist=0;

        boolean leftSeatTaken=false;
        boolean rightSeatTaken=false;

        int seatsLen=seats.length;
        for (int i = 0; i < seatsLen; i++) {
            if(seats[i]==0){
                currDist++;
            }
            else{
                if(!leftSeatTaken){
                    if(currDist>maxDist) {
                        maxDist = currDist;
                    }
                    leftSeatTaken=true;
                }
                else if(!rightSeatTaken){
                    if (currDist % 2 == 0) {
                        if (currDist / 2 > maxDist) {
                            maxDist = currDist / 2;
                        }
                    }
                    else{
                        if((currDist/2)+1>maxDist){
                            maxDist=(currDist/2)+1;
                        }
                    }
                }
                currDist=0;
            }
        }
        if(currDist>maxDist){
            maxDist=currDist;
        }
        return maxDist;
    }

    public static void main(String[] args) {
        int[] arr={1, 0, 0, 0, 1, 0, 1};
        int[] arr2={1,0,0,0};
        int[] arr3={0, 1};
        int[] arr4={1,1,0,0,0,0,1};
        int[] arr5={0,0,0,0,0,0,1};
        int[] arr6={0,0,0,0,0,0,0,1};
        int[] arr7={1,0,0,0,0,0,0,0};
        int[] arr8={1,0,1,0,1,0,0,0};
        int[] arr9={1,0,1,0,1,0,0,0,0,0,1,0};
        int[] arr10={0,0,0,0,1,0,0,0,0,0,1,0,0};
        System.out.println(maxDistance(arr8));
    }
}
