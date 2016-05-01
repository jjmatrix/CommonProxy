package org.jmatrix.proxy.core.mock;

import org.jmatrix.proxy.core.exception.OperationNotSupportException;

/**
 * Visitor Factory
 *
 * @author jmatrix
 * @date 16/5/1
 */
public class VisitorFactory {

    public static Visitor createVisitor(VisitorType visitorType) {
        switch (visitorType) {
            case ACCEPT:
                return new PolicyAcceptVisitor();
            case HANDLER:
                return new PolicyHandlerVisitor();
            default:
                throw new IllegalStateException("wrong visitor type[" + visitorType + "]");
        }
    }

    public enum VisitorType {

        ACCEPT(1, "accept类型"), HANDLER(2, "handler类型");

        private int type;

        private String desc;

        VisitorType(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }
}
