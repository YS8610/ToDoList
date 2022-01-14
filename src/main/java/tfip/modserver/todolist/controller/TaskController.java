package tfip.modserver.todolist.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import tfip.modserver.todolist.repo.TaskRepo;


@Controller
@RequestMapping(path = {"/task"},produces = "text/html")
public class TaskController {

    @Autowired
    TaskRepo taskrepo;

    final private Logger logger = Logger.getLogger(TaskController.class.getName());
    
    @RequestMapping(path = "/save")
    @PostMapping(consumes = "application/x-www-form-urlencoded")
    private String save(@RequestBody MultiValueMap<String, String> form){
        String contents = form.getFirst("content");
        List<String> tasks = new ArrayList<>();

        if (null!= contents && !contents.isBlank() ){
            for (String task : contents.split("\\|")) tasks.add(task);
        }
        logger.info(contents);
        taskrepo.save("mytodolist", contents);
        return "index";
    }


    @RequestMapping(path = "/load")
    @PostMapping(consumes = "application/x-www-form-urlencoded")
    private String load(Model model){
        // List<String> tasks = new ArrayList<>();
        String taskString = taskrepo.get("mytodolist").toString();
        logger.info("taken from redis"+taskString);
        List<String> tasks = Arrays.asList(taskString.substring(9,taskString.length()-1).split("\\|"));

        model.addAttribute("tasks", tasks);
        model.addAttribute("contents", taskString.substring(9,taskString.length()-1));

        return "tasks";
    }

    @PostMapping(
        // consumes = "application/x-www-form-urlencoded",
        produces = "text/html"
        )
    private String toDoList(@RequestBody MultiValueMap<String, String> form, Model model){
        
        String taskName = form.getFirst("taskName");
        String contents = form.getFirst("content");

        List<String> tasks = new ArrayList<>();
        tasks.add(taskName);
        if (null!= contents && !contents.isBlank() ){
            for (String task : contents.split("\\|")) tasks.add(task);
        }
        contents = tasks.stream().reduce("", (s0,s1) -> s1+"|"+s0);

        model.addAttribute("contents", contents);
        model.addAttribute("tasks", tasks);

        return "tasks";
    }
}
