package com.lxisoft.vegetablestore.web;


import com.lxisoft.vegetablestore.domain.Category;
import com.lxisoft.vegetablestore.domain.Vegetable;
import com.lxisoft.vegetablestore.service.CategoryService;
import com.lxisoft.vegetablestore.service.VegetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@Controller
@MultipartConfig
public class VegetableStoreController {

    @Autowired
    VegetableService vegetableService;

    @Autowired
    CategoryService categoryService;


@GetMapping("/")
public String readVegetable(Model model) {

    model.addAttribute("vegetables",vegetableService.findAll());

    model.addAttribute("categories",categoryService.findAll());

    return "home";
    }


@GetMapping("/add-form")
public String addVegetableForm(Model model){

    model.addAttribute("categories",categoryService.findAll());

return "addVegetable";
}


@PostMapping("/create-vegetable")
public String createVegetable(@ModelAttribute Vegetable vegetable, Model model) throws IOException {
    System.out.println(vegetable.getImageFile().getContentType());
    vegetableService.save(vegetable);

    model.addAttribute("categories",categoryService.findAll());

    return "addVegetable";
}


@GetMapping("/select-vegetable")
public String selectVegetable(@RequestParam("id")long id, Model model) {
Optional<Vegetable> v  =vegetableService.findOne(id);
System.out.println(v.toString());
        model.addAttribute("vegetable",v);

    model.addAttribute("categories",categoryService.findAll());
    return "updateVegetable";
}


@PostMapping("/update-vegetable")
    public String updateVegetable(@ModelAttribute Vegetable vegetable) throws IOException {

    vegetableService.update(vegetable);

return "redirect:/";
    }

@PostMapping("/delete-vegetable")
public String delete(@RequestParam("id")long id) {

 vegetableService.delete(id);

return "redirect:/";
}


@GetMapping("/categories")
public String findVegetablesByCategorId(@RequestParam(required=false,name="id")long id, Model model) {

   model.addAttribute("vegetables",categoryService.findVegetablesByCategoryId(id));
    model.addAttribute("categories",categoryService.findAll());
    return "showResult";
}

    @PostMapping("/create-category")
    public String createCategories(@ModelAttribute Category category, Model model) {

   categoryService.save(category);

        model.addAttribute("categories",categoryService.findAll());
        return "addVegetable";
    }

@GetMapping("/search")
public String search(@RequestParam("search")String word,Model model){

        model.addAttribute("vegetables", vegetableService.search(word));

    model.addAttribute("categories",categoryService.findAll());

return "showResult";
}


@GetMapping("/image")
public void image(@RequestParam("name")String name, HttpServletResponse response) throws IOException {

    vegetableService.image(name,response);
}

@GetMapping("locale")
public String changeLanguage(@RequestParam String language,Model model){

    model.addAttribute("vegetables",vegetableService.findAll());

    model.addAttribute("categories",categoryService.findAll());

   return "home";
}

@GetMapping("/login")
    public String login(){

    return "login";
}

}

