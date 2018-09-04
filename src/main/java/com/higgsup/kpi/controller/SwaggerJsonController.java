package com.higgsup.kpi.controller;

import com.higgsup.kpi.dto.SwaggerJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

@RestController
public class SwaggerJsonController {

    private final static String swaggerResource = "[\n" +
            "  {\n" +
            "    \"name\": \"kpi\",\n" +
            "    \"location\": \"/v2/api-docs\",\n" +
            "    \"url\": \"/v2/api-docs\",\n" +
            "    \"swaggerVersion\": \"2.0\"\n" +
            "  }\n" +
            "]";

    private final static String uiConfiguration = "{\n" +
            "  \"apisSorter\": \"alpha\",\n" +
            "  \"jsonEditor\": false,\n" +
            "  \"showRequestHeaders\": false,\n" +
            "  \"deepLinking\": true,\n" +
            "  \"displayOperationId\": false,\n" +
            "  \"defaultModelsExpandDepth\": 1,\n" +
            "  \"defaultModelExpandDepth\": 1,\n" +
            "  \"defaultModelRendering\": \"example\",\n" +
            "  \"displayRequestDuration\": false,\n" +
            "  \"docExpansion\": \"none\",\n" +
            "  \"filter\": false,\n" +
            "  \"operationsSorter\": \"alpha\",\n" +
            "  \"showExtensions\": false,\n" +
            "  \"tagsSorter\": \"alpha\"\n" +
            "}";

    private static String securityConfiguration = "{\n" +
            "  \"apiKeyName\": \"api_key\",\n" +
            "  \"scopeSeparator\": \",\",\n" +
            "  \"apiKeyVehicle\": \"header\"\n" +
            "}";

    private SwaggerJson swaggerJson;

    @Autowired
    public SwaggerJsonController(SwaggerJson swaggerJson) {
        this.swaggerJson = swaggerJson;
    }

    @RequestMapping(value = {"/v2/api-docs"}, method = {RequestMethod.GET},
            produces = {"application/json", "application/hal+json"}
    )
    public ResponseEntity<Json> getDocumentation() {
        return new ResponseEntity(swaggerJson.getJson(), HttpStatus.OK);
    }

    @RequestMapping(value = {"/swagger-resources"}, method = {RequestMethod.GET}, produces = {"application/json"})
    public ResponseEntity<Json> getSwaggerResource() {
        return new ResponseEntity(swaggerResource, HttpStatus.OK);
    }

    @RequestMapping(value = {"/swagger-resources/configuration/ui"}, method = {RequestMethod.GET},
            produces = {"application/json"})
    public ResponseEntity<Json> getUIConfiguration() {

        return new ResponseEntity(uiConfiguration, HttpStatus.OK);
    }

    @RequestMapping(value = {"/swagger-resources/configuration/security"}, method = {RequestMethod.GET},
            produces = {"application/json"})
    public ResponseEntity<Json> getSecurityConfiguration() {
        return new ResponseEntity(securityConfiguration, HttpStatus.OK);
    }
}
