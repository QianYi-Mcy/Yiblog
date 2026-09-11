
package com.blog.config;











import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
/**

 * 全局异常处理：统一捕获未处理的异常，避免把堆栈直接抛给用户。
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception ex) {
        log.error("请求处理异常: {} {}", request.getMethod(), request.getRequestURI(), ex);
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("message", "服务器开小差了，请稍后再试~");
        mv.addObject("path", request.getRequestURI());
        return mv;
    }
}
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, id)
               .setSql("views = views + 1");
        update(wrapper);
    }
}
