package DAT250.Exercise.Experiment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/Hello")
    public String hello() {
        return "Hello world!";
    }

    @PostMapping
    public String post() {
        return "Hello world!";}
}