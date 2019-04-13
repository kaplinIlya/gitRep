package com.example.testSpringBoot;

import com.example.testSpringBoot.beans.Header;
import com.example.testSpringBoot.domain.Car;
import com.example.testSpringBoot.domain.enums.Colors;
import com.example.testSpringBoot.repository.CarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private CarRepo carRepo;
    @Autowired
    private Header header;

    @Value("${upload.path}")
    private String uploadPath;

    @RequestMapping("/")
    public String start(){

        return "redirect:/main";
    }

    @RequestMapping("/main")
    public String index(Model model){
        model.addAttribute("header", header.getHeader());
        model.addAttribute("cars",  carRepo.findAll());
        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String firm,
                      @RequestParam String model,
                      @RequestParam int power,
                      @RequestParam String color,
                      @RequestParam("file") MultipartFile file,
                      Model models){
        Car car = new Car();
        car.setFirm(firm);
        car.setModel(model);
        car.setColor(Colors.valueOf(color));
        car.setPower(power);
        if (file != null){
            File upload = new File(uploadPath);
            if (!upload.exists())
                upload.mkdir();
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile.concat(".").concat(file.getOriginalFilename());

            try {
                file.transferTo(new File( uploadPath + "/" + resultFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            car.setFilename(resultFileName);
        }
        carRepo.save(car);
        models.addAttribute("header", header.getHeader());
        models.addAttribute("cars",  carRepo.findAll());
        models.addAttribute("uploadPath", uploadPath);
        return "main";
    }
}
