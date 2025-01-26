package markup;

import java.util.List;

public class Emphasis extends MarkSettings {
    public Emphasis(List<Mark> list) {
        super(list, "*", "#emph[");
    }
}
