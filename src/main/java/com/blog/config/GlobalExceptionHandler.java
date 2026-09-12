package com.blog.config;

import com.blog.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理。
 * <ul>
 *     <li>对 /api/** 的请求：返回统一的 JSON {@link Result}，便于前端处理；</li>
 *     <li>对页面请求：返回 error 模板页，避免把堆栈直接抛给用户。</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Object handleException(HttpServletRequest request, Exception ex) {
        log.error("请求处理异常: {} {}", request.getMethod(), request.getRequestURI(), ex);

        String uri = request.getRequestURI();
        if (uri != null && uri.startsWith("/api/")) {
            // 接口请求返回 JSON
            return Result.fail("服务器开小差了，请稍后再试~");
        }

        // 页面请求返回错误页
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("message", "服务器开小差了，请稍后再试~");
        mv.addObject("path", uri);
        return mv;
    }
}
