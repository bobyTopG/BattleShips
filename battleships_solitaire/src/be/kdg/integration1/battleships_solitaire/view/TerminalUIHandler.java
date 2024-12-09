package be.kdg.integration1.battleships_solitaire.view;

public class TerminalUIHandler implements UIHandler {

    @Override
    public void showStartScreen() {
        System.out.println("""
                    ____        __  __  __          __    _               _____       ___ __        _        \s
                   / __ )____ _/ /_/ /_/ /__  _____/ /_  (_)___  _____   / ___/____  / (_) /_____ _(_)_______\s
                  / __  / __ `/ __/ __/ / _ \\/ ___/ __ \\/ / __ \\/ ___/   \\__ \\/ __ \\/ / / __/ __ `/ / ___/ _ \\
                 / /_/ / /_/ / /_/ /_/ /  __(__  ) / / / / /_/ (__  )   ___/ / /_/ / / / /_/ /_/ / / /  /  __/
                /_____/\\__,_/\\__/\\__/_/\\___/____/_/ /_/_/ .___/____/   /____/\\____/_/_/\\__/\\__,_/_/_/   \\___/\s
                                                       /_/                                                   \s
                                                      \s""");
    }

}