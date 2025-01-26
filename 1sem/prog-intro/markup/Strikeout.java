package markup;

import java.util.List;

public class Strikeout extends MarkSettings {

    public Strikeout(List<Mark> list) {
        super(list, "~", "#strike[");
    }
}
