package cringe.app.security;

import cringe.app.component.SessionStore;
import cringe.app.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class RefererAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    static class AddParamsToHeader extends HttpServletRequestWrapper {

        private final Map<String, String> extraParams;

        public AddParamsToHeader(HttpServletRequest request, Map<String, String> extraParams) {
            super(request);
            this.extraParams = extraParams;
        }

        @Override
        public String getParameter(String name) {
            if (extraParams.containsKey(name)) {
                return extraParams.get(name);
            }
            return super.getParameter(name);
        }
    }

    @Autowired
    private SessionStore sessionStore;

    @Autowired
    private CartService cartService;

    public RefererAwareAuthenticationSuccessHandler() {
        setTargetUrlParameter("target");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        cartService.sync(authentication);
        if (sessionStore.getLoginReferer() != null) {
            request = new AddParamsToHeader(request, Map.of(getTargetUrlParameter(), sessionStore.getLoginReferer()));
            sessionStore.setLoginReferer(null);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }
}
