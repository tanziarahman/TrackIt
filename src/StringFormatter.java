public class StringFormatter {
    private StringFormatter(){}

    public static String capitalizeFirstLetter(String str){
        if(str==null || str.isEmpty()){
            return str;
        }
        return str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase();
    }
}
