public class LaboonCrypt
{
    private enum Verbosity{
        NONE,VERBOSE,VERYVERBOSE,ULTRAVERBOSE;
    }
    private static Verbosity verbositylvl = Verbosity.NONE; 
    private static boolean setVerbosity(String arg){
        if (arg.equals("-verbose")){
            verbositylvl = Verbosity.VERBOSE;
        }
        else if(arg.equals("-veryverbose")){
            verbositylvl = Verbosity.VERYVERBOSE;
        }
        else if(arg.equals("-ultraverbose")){
            verbositylvl = Verbosity.ULTRAVERBOSE;
        }
        else{
            return false;
        }
        return true;
    }
    private static void vLog(String msg, int lvl){
        if (verbositylvl.ordinal() >= lvl){
            System.out.print(msg);
        }
        return;
    }
    private static void printMatrix(String[][] m){
        for(int i = 0; i < m[0].length; i++){
            for(int j = 0; j < m[1].length; j++){
                System.out.print(m[i][j]+" ");
            }
            System.out.println("");
        }
    }
    private static void showUsage(){
        System.out.println(
            "Usage:\n"+
            "java LaboonCrypt *string* *verbosity_flag*\n"+
            "Verbosity flag can be omitted for hash output only\n"+
            "Other options: -verbose -veryverbose -ultraverbose");
        System.exit(-1);
    }

    private static String[][] initmatrix(String inputStr){
        String newMatrix[][] = new String[12][12];
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 12; j++){
                if(i == 0 && j == 0){
                    newMatrix[i][j] = LaboonHash.laboonHash(inputStr);
                }
                else if(j == 0)
                {
                    newMatrix[i][j] = LaboonHash.laboonHash(newMatrix[i-1][11]);
                }
                else
                {
                    newMatrix[i][j] = LaboonHash.laboonHash(newMatrix[i][j-1]);
                }
            }
        }

        if(verbositylvl.ordinal() >= 1){
            System.out.println("Initial array: ");
            printMatrix(newMatrix);
        }
        return newMatrix;
    }
    private static String[][] shuffle(String[][] matrix, String inputString){
        int x = 0;
        int y = 0;
        for(char c : inputString.toCharArray()){
            int val = (int) c;
            int down  = val * 11; 
            int right = (val + 3) * 7;

            vLog("Moving " + down + " down and " + right, 2);
            x = (x + down) % 12; y = (y + right) % 12;
            vLog(" modifying [" + x + "," + y + "] from " + matrix[x][y], 2);
            matrix[x][y] = LaboonHash.laboonHash(matrix[x][y]);
            vLog(" to " + matrix[x][y] +"\n", 2);
        }

        if(verbositylvl.ordinal() >= 1){
            System.out.println("Final array: ");
            printMatrix(matrix);
        }
        return matrix;
    }
    private static String hashMatrix(String[][] matrix){
        StringBuffer concat = new StringBuffer();
        for(int i = 0; i < matrix[0].length; i++){
            for(int j = 0; j < matrix[1].length; j++){
                concat.append(matrix[i][j]);
            }   
        }
        if(verbositylvl.ordinal() == 3){
            LaboonHash.setVerbosity(true);
        }
        return LaboonHash.laboonHash(concat.toString());
    }

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2){
            showUsage();
        }
        else if(args.length == 2){
            if (!setVerbosity(args[1])){
                showUsage();
            }
        }
        String inputString = args[0];
        if(inputString.contains("\"")){
            inputString = inputString.replaceAll("^\"|\"$", "");
        }
        
        String[][] matrix = initmatrix(inputString);
        shuffle(matrix, inputString);
        String result = hashMatrix(matrix);
        System.out.println("LaboonCrypt hash: "+result);
        System.exit(0);
    }
}