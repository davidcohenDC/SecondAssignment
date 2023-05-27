public class Main {

    public static void main(String[] args) {

        if (args.length > 0) {
            System.out.println(args.length);
            new AnalyserCLI(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        } else {
            System.out.println("ciao");
            new AnalyserGUI();
        }
    }
}