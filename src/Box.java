import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Box {
    private String[] contents;

    public Box() {
        contents = new String[0];
    }

    public Box(char c) {
        this("" + c);
    }

    public Box(char c, int width, int height) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < width; i++)
            buffer.append(c);
        String s = buffer.toString();
        contents = new String[height];
        for (int i = 0; i < height; i++)
            contents[i] = s;
    }

    public Box(String s) {
        contents = s.split("\n");
        int w = 0;
        for (String a : contents)
            if (a.length() > w)
                w = a.length();
        for (int i = 0; i < contents.length; i++) {
            int n = contents[i].length();
            contents[i] = replicate(' ', (w - n) / 2) + contents[i]
                    + replicate(' ', w - n - (w - n) / 2);
        }
    }

    public Box(String[] ss) {
        contents = ss;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (contents.length > 0) {
            buffer.append(contents[0]);
            for (int i = 1; i < contents.length; i++)
                buffer.append('\n').append(contents[i]);
        }
        return buffer.toString();
    }

    public int width() {
        return contents.length == 0 ? 0 : contents[0].length();
    }

    public int height() {
        return contents.length;
    }

    public Box widen(int w) {
        int wid = width();
        if (w <= wid)
            return this;
        int hei = height();
        Box left = new Box(' ', (w - wid) / 2, hei);
        Box right = new Box(' ', w - wid - left.width(), hei);
        return left.beside(this).beside(right);
    }

    public Box heighten(int h) {
        int hei = height();
        if (h <= hei)
            return this;
        int wid = width();
        Box top = new Box(' ', wid, (h - hei) / 2);
        Box bottom = new Box(' ', wid, h - hei - top.height());
        return top.above(this).above(bottom);
    }

    public Box heighten2(int h) {
        int hei = height();
        if (h <= hei)
            return this;
        int wid = width();
        Box bottom = new Box(' ', wid, h - hei);
        return above(bottom);
    }

    public Box beside(Box b) {
        Box b1 = heighten(b.height());
        Box b2 = b.heighten(height());
        int h = b1.height();
        String[] result = Arrays.copyOf(b1.contents, b1.contents.length);
        for (int i = 0; i < h; i++)
            result[i] += b2.contents[i];
        return new Box(result);
    }

    public Box beside2(Box b) {
        Box b1 = heighten2(b.height());
        Box b2 = b.heighten2(height());
        int h = b1.height();
        String[] result = Arrays.copyOf(b1.contents, b1.contents.length);
        for (int i = 0; i < h; i++)
            result[i] += b2.contents[i];
        return new Box(result);
    }

    public Box above(Box b) {
        Box b1 = widen(b.width());
        Box b2 = b.widen(width());
        String[] result = Arrays.copyOf(b1.contents, b1.contents.length
                + b2.contents.length);
        System.arraycopy(b2.contents, 0, result, b1.contents.length,
                b2.contents.length);
        return new Box(result);
    }

    public Box frame() {
        Box horiz = new Box('─', width(), 1);
        Box vert = new Box('│', 1, height());
        return (new Box("┌").beside(horiz).beside(new Box("┐"))).above(
                vert.beside(this).beside(vert)).above(
                new Box("└").beside(horiz).beside(new Box("┘")));
    }

    public Box connect(List<Box> list) {
        if (list.size() == 0)
            return this;

        if (list.size() == 1)
            return this.above(new Box('│')).above(list.get(0));

        StringBuffer buffer = new StringBuffer();
        int p = list.get(0).width() / 2;
        buffer.append(replicate(' ', p)).append('┌').append(replicate('─', p));
        for (int i = 1; i < list.size() - 1; i++) {
            Box b = list.get(i);
            p = b.width() / 2;
            buffer.append(replicate('─', p + 1)).append('┬')
                    .append(replicate('─', p));
        }
        p = list.get(list.size() - 1).width() / 2;
        buffer.append(replicate('─', p + 1)).append('┐').append(replicate(' ', p));
        p = buffer.length() / 2;
        char c = buffer.charAt(p);
        buffer.setCharAt(p, c == '─' ? '┴' : '┼');

        Box bs2 = list.get(0);
        for (int i = 1; i < list.size(); i++)
            bs2 = bs2.beside(new Box(" ")).beside2(list.get(i));

        return this.above(new Box(buffer.toString())).above(bs2);
    }

    private static String replicate(char c, int n) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < n; i++)
            buffer.append(c);
        return buffer.toString();
    }

    public static void main(String[] args) {
        Box b0 = new Box("TOP");
        Box b1 = new Box("1234567");
        Box b2 = new Box("123456789");
        Box b3 = new Box("12345678901");
        Box b4 = new Box("123456789");
        Box b5 = new Box("Bye");
        List<Box> list = new LinkedList<Box>();
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        list.add(b5);
        System.out.printf("%s\n", b0.connect(list));
    }
}
