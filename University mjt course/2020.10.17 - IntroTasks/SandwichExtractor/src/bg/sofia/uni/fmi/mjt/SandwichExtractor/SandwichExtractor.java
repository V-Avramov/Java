import java.util.Arrays;

public class SandwichExtractor {

    /*public static String[] extractIngredients(String sandwich){

        if(sandwich.lastIndexOf("bread")-sandwich.indexOf("bread")<=5){
            return new String[0];//returns []
        }

        int ingredientsLen=0;
        String ingredient="";
        int breadPos=sandwich.indexOf("bread")+"bread".length();//get the index of the first "bread" + its length
        int breadEndPos=sandwich.lastIndexOf("bread");//get the index ot the last "bread"

        for (int i = breadPos; i < breadEndPos; i++) {//gets how many ingredients we have excluding the olives
            if(ingredient.equals("olives")&&sandwich.charAt(i+1)=='-'){
                while(sandwich.charAt(i)!='-'&&i<breadEndPos){
                    i++;
                }
                ingredient="";
                continue;
            }
            if(sandwich.charAt(i)=='-'||i==breadEndPos-1){
                ingredientsLen++;
                ingredient="";
                continue;
            }
            ingredient+=sandwich.charAt(i);
        }

        String[] ingredients = new String[ingredientsLen];

        int currentIngredient=0;
        ingredient="";

        for (int i = breadPos; i < breadEndPos+1; i++) {//breadEndPos+1 == 'b'
            if(ingredient.equals("olives")&&sandwich.charAt(i+1)=='-'){
                while(sandwich.charAt(i)!='-'&&i<breadEndPos){
                    i++;
                }
                ingredient="";
                continue;
            }
            if(sandwich.charAt(i)=='-'||i==breadEndPos){
                ingredients[currentIngredient]=ingredient;
                currentIngredient++;
                ingredient="";
                continue;
            }
            ingredient+=sandwich.charAt(i);
        }

        Arrays.sort(ingredients);

        return ingredients;
    }*/

    public static String[] extractIngredients(String sandwich){
        int firstBread=sandwich.indexOf("bread")+"bread".length();
        int lastBread=sandwich.lastIndexOf("bread");
        if(lastBread-firstBread==0){
            return new String[0];
        }

        String currIngredient="";
        int ingredientsLen=0;

        String allIngredients="";

        for (int i = firstBread; i < lastBread+1; i++) {
            if(currIngredient.equals("olives")&&sandwich.charAt(i)=='-'){
                currIngredient="";
            }
            else if(sandwich.charAt(i)=='-'||i==lastBread){
                if(currIngredient.equals("olives")){//check if it's the last ingredient
                    continue;
                }
                allIngredients+=(currIngredient+'-');
                ingredientsLen++;
                currIngredient="";
            }
            else{
                currIngredient+=sandwich.charAt(i);
            }
        }

        String[] resultIngredients=new String[ingredientsLen];
        int currentAdded=0;

        int allIngredientsLen=allIngredients.length();
        currIngredient="";

        for (int i = 0; i < allIngredientsLen; i++) {
            if(allIngredients.charAt(i)=='-'){
                resultIngredients[currentAdded]=currIngredient;
                currentAdded++;
                currIngredient="";
                continue;
            }
            currIngredient+=allIngredients.charAt(i);
        }

        Arrays.sort(resultIngredients);

        return resultIngredients;

        }

    public static void main(String[] args) {

        System.out.println(Arrays.toString(extractIngredients("olivesbreadolives-olives-tomato-olives-olivesbreadblabla")));
        System.out.println(Arrays.toString(extractIngredients("olivesbreadolivesfake-olives-tomato-olives-hahaolivesbreadblabla")));
        System.out.println(Arrays.toString(extractIngredients("breadham-tomato-mayobread")));
        System.out.println(Arrays.toString(extractIngredients("asdbreadham-olives-tomato-olives-mayobreadblabla")));
        System.out.println(Arrays.toString(extractIngredients("asdbreadham")));
        System.out.println(Arrays.toString(extractIngredients("asdbreadooolives-haolives-ham-olives-tomato-olives-olivess-mayo-olivessbreadblabla")));
        System.out.println(Arrays.toString(extractIngredients("asdbreadhambreadblabla")));

    }
}
