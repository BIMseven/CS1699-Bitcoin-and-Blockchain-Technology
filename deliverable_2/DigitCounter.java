public class DigitCounter{
    public static void main(String[] args) {
        int i = 0;
        for(char c : args[0].toCharArray())
            i++;
        System.out.println(i);
    }
}