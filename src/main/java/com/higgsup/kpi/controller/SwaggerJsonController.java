package com.higgsup.kpi.controller;

import com.higgsup.kpi.dto.SwaggerJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;

/**
 * {@code SwaggerJsonController} is responsible for fulfilling Swagger/SpringFox
 * related requests .
 * <p/>
 *
 * @author Indra Basak
 * @since 11/23/17
 */
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

    private SwaggerJson swaggerJson;

    @Autowired
    public SwaggerJsonController(SwaggerJson swaggerJson) {
        this.swaggerJson = swaggerJson;
    }

    /**
     * Responsible for returning the Swagger JSON document.
     *
     * @param swaggerGroup
     * @param servletRequest
     * @return
     */
    @RequestMapping(
            value = {"/v2/api-docs"},
            method = {RequestMethod.GET},
            produces = {"application/json", "application/hal+json"}
    )
    @ResponseBody
    public ResponseEntity<Json> getDocumentation(
            @RequestParam(value = "group", required = false) String swaggerGroup,
            HttpServletRequest servletRequest) {

        return new ResponseEntity(swaggerJson.getJson(), HttpStatus.OK);
    }

    /**
     * Responsible for returning {@code SwaggerResource} when requested by
     * swagger-ui.html.
     *
     * @param servletRequest
     * @return
     */
    @RequestMapping(value = {"/swagger-resources"},
            method = {RequestMethod.GET},
            produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Json> getSwaggerResource(
            HttpServletRequest servletRequest) {
        return new ResponseEntity(swaggerResource, HttpStatus.OK);
    }

    /**
     * Responsible for returning {@code UIConfiguration} when requested by
     * swagger-ui.html.
     *
     * @param servletRequest
     * @return
     */
    @RequestMapping(value = {"/swagger-resources/configuration/ui"},
            method = {RequestMethod.GET},
            produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Json> getUIConfiguration(
            HttpServletRequest servletRequest) {

        return new ResponseEntity(uiConfiguration, HttpStatus.OK);
    }

}
