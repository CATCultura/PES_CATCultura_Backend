package cat.cultura.backend.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoggerInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String info = String.format("[Incoming request][Method: %s][URI: %s]", request.getMethod(), request.getRequestURI());
        logger.info(info);
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {

        String requestInfo = String.format("[%s request to %s]",request.getMethod(), request.getRequestURI());
        logger.info(requestInfo);

        String responseInfo = String.format("[Response][Status code %s]",response.getStatus());
        logger.info(responseInfo);
    }

}
