package parser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class TokenNormalizer {
    void normalize(Node node) {
        node.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(SimpleName name, Void arg) {
                name.setIdentifier("variable");
            }

            @Override
            public void visit(VariableDeclarator variable, Void arg){
                variable.setName("variable");
            }

            @Override
            public void visit(NameExpr name, Void arg){
                name.setName("variable");
            }
        }, null);
    }
}
