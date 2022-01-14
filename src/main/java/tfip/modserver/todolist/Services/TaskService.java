package tfip.modserver.todolist.Services;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import tfip.modserver.todolist.repo.TaskRepo;



public class TaskService {
    
    @Autowired
    TaskRepo taskrepo;

    public boolean hasKey(String key){
        Optional<String> opt = taskrepo.get(key);
        return opt.isPresent();
    }

    public List<String> get(String key) {
        Optional<String>opt =taskrepo.get(key);
        List<String> list = new LinkedList<>();
        if (opt.isPresent()){
            list = Arrays.stream(opt.toString().split("\\|")).collect(Collectors.toList());
        }
        return list;
    }

    public void save(String key, List<String> values) {
        String l = values.stream().collect(Collectors.joining("|"));
            taskrepo.save(key, l);
    }

    public void save(String key, String value) {
        taskrepo.save(key, value);
    }
}


