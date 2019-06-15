package com.example.giflib.Controller;

import com.example.giflib.model.Gif;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

@Controller
public class GifController {
//    @RequestMapping(value = "/")    // routes to index page
//    //@ResponseBody   // Indicates that string we return should be used as the response without any further processing (Not needed when using a template to process response such as thymeleaf
//    public String listGifs() {
//        return "index";     // renders index.html
//    }
//
//    @RequestMapping("/gif")
//    @ResponseBody
//    public String gifs() {
//        return "Gifs page";
//    }


    @RequestMapping(value = "/")    // routes to index page
    //@ResponseBody   // Indicates that string we return should be used as the response without any further processing (Not needed when using a template to process response such as thymeleaf
    public String listGifs() {
        return "home";     // renders index.html
    }

    @RequestMapping("/gif")
    public String gifDetails(ModelMap modelMap) {
        Gif gif = new Gif("compiler-bot", LocalDate.of(2015,2,13), "Adrian", true);
        modelMap.put("gif", gif);   // adds object to model map for it to be available to the templating engine

        return "gif-details";
    }
}
