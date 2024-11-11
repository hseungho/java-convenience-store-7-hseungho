package store.core.view;

import camp.nextstep.edu.missionutils.Console;

public class YesOrNoInputView implements InputView<String, Boolean> {

    private static final String YES = "Y";

    private static final String NO = "N";

    @Override
    public Boolean displayWithInput(String content) {
        this.display("\n" + content);
        String read = Console.readLine();
        validateYesOrNo(read);
        return YES.equalsIgnoreCase(read);
    }

    @Override
    public void display(String content) {
        System.out.println(content);
    }

    private void validateYesOrNo(String read) {
        if (read.equalsIgnoreCase(YES) || read.equalsIgnoreCase(NO)) {
            return;
        }
        throw new IllegalArgumentException("Invalid yes or no.");
    }
}
