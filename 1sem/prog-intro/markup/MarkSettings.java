package markup;

import java.util.List;

public abstract class MarkSettings extends Mark {
    protected final List<Mark> list;
    protected final String markForMarkDown;
    protected final String markForTypst;

    public MarkSettings(List<Mark> lst, String mark1, String mark2) {
        this.list = lst;
        this.markForMarkDown = mark1;
        this.markForTypst = mark2;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(markForMarkDown);
        for (Mark lst : list) {
            lst.toMarkdown(sb);
        }
        sb.append(markForMarkDown);
    }

    @Override
    public void toTypst(StringBuilder sb) {
        sb.append(markForTypst);
        for (Mark lst : list) {
            lst.toTypst(sb);
        }
        sb.append("]");
    }
}
