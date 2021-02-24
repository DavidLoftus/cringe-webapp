package cringe.app.util;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;

public class CustomDialect extends AbstractDialect implements IExpressionObjectDialect {

    public CustomDialect() {
        super("Custom Dialect");
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {

            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton("paths");
            }

            @Override
            public Object buildObject(IExpressionContext context,
                                      String expressionObjectName) {
                return new PathsUtil();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
    }
}