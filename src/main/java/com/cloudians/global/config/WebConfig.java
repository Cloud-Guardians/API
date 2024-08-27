package com.cloudians.global.config;

import com.cloudians.domain.auth.resolver.AuthArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthArgumentResolver authArgumentResolver;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //모든 컨트롤러 앞에 /api를 붙여줌
        configurer.addPathPrefix(
                "/api",
                // x.user_crud 패키지 이하에 존재하는 @RestController Annotation 달린 모든 컨트롤러 경로 앞에 /api 붙여줌
                HandlerTypePredicate.forBasePackage("com.cloudians")
                        .and(HandlerTypePredicate.forAnnotation(RestController.class))
        );
    }

    // resolver에 추가함
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }
}
