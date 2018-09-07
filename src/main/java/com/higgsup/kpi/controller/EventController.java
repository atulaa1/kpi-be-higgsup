package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL +"/events")
public class EventController {
}
