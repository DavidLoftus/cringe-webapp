package cringe.app.component;

import cringe.app.db.Cart;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class SessionStore {

    private Cart cart;

    private String loginReferer;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getLoginReferer() {
        return loginReferer;
    }

    public void setLoginReferer(String loginReferer) {
        this.loginReferer = loginReferer;
    }
}
