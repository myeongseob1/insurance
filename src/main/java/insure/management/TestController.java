package insure.management;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Api(value = "TestController")
public class TestController {
    @GetMapping("hello")
    @ApiOperation(value = "Test",notes = "테스트용 Url")
    public String hello(Model model){
        model.addAttribute("data","hello");
        return "hello";
    }

}
