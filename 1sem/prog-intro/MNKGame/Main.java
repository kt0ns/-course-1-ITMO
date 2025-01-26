package MNKGame;

public class Main {

    //:NOTE: у вас в репозитории есть некомпилирующийся код
    public static void main(String[] args) {
        final Game game = new Game(false, new HumanPlayer(), new RandomPlayer());
        int result;
        do {
            result = game.play(SettingsOfGame.chooseTheGame());
            System.out.println("Game result: " + result);
            if (result == -1) {
                System.out.println("Something going wrong :/");
                break;
            }
        } while (result == 0);
    }
}
