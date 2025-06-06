package parser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class TokenNormalizer {
    void normalize(Node node) {
        node.getAllContainedComments().forEach(Comment::remove);
        node.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(SimpleName name, Void arg) {
                super.visit(name, arg);
                name.setIdentifier("simpleName");
            }

            @Override
            public void visit(VariableDeclarator variable, Void arg) {
                super.visit(variable, arg);
                variable.setName("variable");
            }

            @Override
            public void visit(NameExpr name, Void arg) {
                super.visit(name, arg);
                name.setName("nameExpr");
            }

            @Override
            public void visit(Parameter parameter, Void arg) {
                super.visit(parameter, arg);
                parameter.setName("parameter");
            }
        }, null);
    }
}
