public class HelloWord {

    public static void main (String [] arg){
	
	 System.out.println("Hello EveryOne this is My First Project!");

    }
    public static void reverString(){
    
	char[] chars = str.toCharArray();
	System.out.println(chars);
        for (int i = 0, len = chars.length; i < len / 2; i++) {
	char temp = chars[i];
	chars[i] = chars[len - 1 - i];
	chars[len - 1 - i] = temp;
	}
        System.out.println(chars);
        return;
   }
}

    
}
