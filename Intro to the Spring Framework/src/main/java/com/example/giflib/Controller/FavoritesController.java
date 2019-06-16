package com.example.giflib.Controller;

import com.example.giflib.data.GifRepository;
import com.example.giflib.model.Gif;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class FavoritesController {

    @Autowired
    GifRepository gifRepository;

    @RequestMapping("/favorites")
    public String allFavorites(ModelMap modelMap) {
        List<Gif> favoriteGifs = gifRepository.showFavorites();
        modelMap.put("favorites", favoriteGifs);
        return "favorites";
    }
}
