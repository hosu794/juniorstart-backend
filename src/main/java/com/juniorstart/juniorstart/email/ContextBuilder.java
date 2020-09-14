package com.juniorstart.juniorstart.email;

import lombok.NoArgsConstructor;
import org.thymeleaf.context.Context;

/** Class for build Context.class more clearly 12-09-2020.
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@NoArgsConstructor
public class ContextBuilder {

    Context context = new Context();

    public ContextBuilder addVariable(String name, Object value) {
        context.setVariable(name,value);
        return this;
    }

    public Context build() {
        return context;
    }
}
