package markup;

import java.util.List;

public class Strong extends MarkSettings {

    public Strong(List<Mark> list) {
        super(list, "__", "#strong[");
    }
}