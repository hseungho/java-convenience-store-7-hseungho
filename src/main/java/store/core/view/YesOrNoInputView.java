package store.core.view;

import store.commons.util.Command;

public class YesOrNoInputView implements InputView<String, Boolean> {

    private static final String YES = "Y";

    private static final String NO = "N";

    @Override
    public Boolean displayWithInput(String content) {
        this.display("\n" + content);
        String read = Command.read();
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
        throw new IllegalArgumentException("잘못된 입력입니다. 다시 입력해 주세요.");
    }
}
