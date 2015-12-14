import javaslang.collection.Tree;

import java.util.LinkedList;
import java.util.List;

public class BoxTree {

    public static <T> Box box(Tree<T> tree) {
        if (tree.isEmpty())
            return new Box("▣");
        Box b = new Box(tree.getValue().toString()).frame();
        if (tree.isSingletonType())
            return b;
        List<Box> lst = new LinkedList<>();
        for (Tree.Node<T> t : tree.getChildren())
            lst.add(box(t));
        return b.connect(lst);
    }


    public static void main(String[] args) {
        Tree<String> t =
                Tree.of("Ann",
                        Tree.of("Mary",
                                Tree.of("John",
                                        Tree.of("Avila")),
                                Tree.of("Karen"),
                                Tree.of("Steven\nAbbot\nBraddock")),
                        Tree.of("Peter",
                                Tree.of("Paul\nPalucci"),
                                Tree.of("Anthony")));
        System.out.println(t);
        System.out.println(box(t));
    }
}
