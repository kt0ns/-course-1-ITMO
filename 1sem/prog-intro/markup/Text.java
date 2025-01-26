package markup;

public class Text extends Mark {
    private final String s;

    public Text(String str) {
        this.s = str;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(s);
    }

    @Override
    public void toTypst(StringBuilder sb) { sb.append(s); }
}
