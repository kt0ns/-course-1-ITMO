package markup;

import java.util.List;

public class Paragraph implements Markdown {
    private final List<Mark> lst;

    public Paragraph(List<Mark> list) {
        this.lst = list;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        for (Mark t : lst) {
            t.toMarkdown(sb);
        }
    }

    @Override
    public void toTypst(StringBuilder sb) {
        for (Mark t : lst) {
            t.toTypst(sb);
        }
    }
}
