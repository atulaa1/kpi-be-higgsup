package com.higgsup.kpi.configure;

import com.higgsup.kpi.dto.SwaggerJson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;

@Configuration
public class SwaggerJsonConfiguration {

    @Bean
    public SwaggerJson swaggerConfiguration(
            ResourceLoader loader) throws IOException {

        InputStream istream = getResource("swagger.json", loader);

        StringBuilder builder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(istream, "UTF-8"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }

        String json = builder.toString();

        SwaggerJson swaggerJson = new SwaggerJson();
        swaggerJson.setJson(json);

        return swaggerJson;
    }

    private InputStream getResource(String location, ResourceLoader loader) {

        InputStream istream = null;
        try {
            istream = loader.getResource(location).getInputStream();
        } catch (IOException e) {
            // write log
        }

        if (istream == null) {
            Set<String> schemes = new LinkedHashSet<>();
            schemes.add("file:./");
            schemes.add("file:./");
            schemes.add("classpath:/");
            schemes.add("classpath:/");
            for (String scheme : schemes) {
                try {
                    istream = loader.getResource(scheme + location).getInputStream();
                } catch (IOException e) {
                    // write log
                }
                if (istream != null) {
                    return istream;
                }
            }
        }

        return null;
    }
}
