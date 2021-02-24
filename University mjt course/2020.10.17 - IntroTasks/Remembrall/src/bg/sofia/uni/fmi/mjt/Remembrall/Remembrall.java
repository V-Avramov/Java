public class Remembrall {

    public static boolean isPhoneNumberForgettable(String phoneNumber){
        if(phoneNumber==null){
            return false;
        }

        int numberLen=phoneNumber.length();
        if(numberLen==0){
            return false;
        }
        char curr;

        boolean forgettableForNow=true;

        for (int i = 0; i < numberLen; i++) {
            curr=phoneNumber.charAt(i);
            if(phoneNumber.lastIndexOf(curr)!=i && (curr>='0' && curr<='9')){
                forgettableForNow=false;
            }
            if((curr>='a'&&curr<='z') || (curr>='A' && curr<='Z')){
                return true;
            }
        }
        return forgettableForNow;
    }

    public static void main(String[] args) {
        System.out.println(isPhoneNumberForgettable(""));
        System.out.println(isPhoneNumberForgettable("498-123-123"));
        System.out.println(isPhoneNumberForgettable("0894 123 567"));
        System.out.println(isPhoneNumberForgettable("(888)-FLOWERS"));
        System.out.println(isPhoneNumberForgettable("(444)-greens"));
        System.out.println(isPhoneNumberForgettable("0895 123 567"));
        System.out.println(isPhoneNumberForgettable(null));
    }
}
