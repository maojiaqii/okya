package com.top.okya.exception;

import com.top.okya.util.response.ResponseObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@ControllerAdvice
public class ControllerExceptionHandler {

	/*@ResponseBody
	@ExceptionHandler({ AuthorizationException.class })
	public Object authorizationExceptionHandler(HttpServletRequest reqest, HttpServletResponse response, Exception e) throws Exception {
		System.out.println(reqest.getServletPath());
		if (isAjax(reqest)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter writer = response.getWriter();
			// 具体操作
			writer.write(new ResponseObject(false, "NoPermission", null).invoke());
			//
			writer.flush();
			writer.close();
			return null;
		} else {
			ModelAndView mav = new ModelAndView("/error403");
			return mav;
		}
	}*/

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public Object otherExceptionHandler(HttpServletRequest reqest, HttpServletResponse response, Exception e) throws Exception {
        e.printStackTrace();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        // 具体操作
        writer.write(new ResponseObject(false, "UnKnowError", null).invoke());
        //
        writer.flush();
        writer.close();
        return null;

    }

    /**
     * 判断是否是ajax请求
     *//*
	public static boolean isAjax(HttpServletRequest httpRequest) {
		return (httpRequest.getHeader("X-Requested-With") != null
				&& "XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With").toString()));
	}*/
}