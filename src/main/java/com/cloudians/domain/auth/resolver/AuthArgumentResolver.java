package com.cloudians.domain.auth.resolver;


import com.cloudians.domain.auth.controller.AuthUser;
import com.cloudians.domain.user.entity.User;
import com.google.rpc.context.AttributeContext;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    // 파라미터에 어노테이션 붙어 있는지, user 타입인지 체크
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasAuthUserAnnotation = parameter.hasParameterAnnotation(AuthUser.class);
        boolean isUserType = User.class.isAssignableFrom(parameter.getParameterType());
        return hasAuthUserAnnotation && isUserType;
    }

    // 어노테이션이 있으면 Attribute에서 user 빼서 리턴
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
       return (User) webRequest.getAttribute("user", RequestAttributes.SCOPE_REQUEST);
    }
}
