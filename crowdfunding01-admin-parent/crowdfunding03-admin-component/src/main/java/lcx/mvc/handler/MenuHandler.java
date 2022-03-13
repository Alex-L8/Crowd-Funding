package lcx.mvc.handler;

import lcx.entity.Menu;
import lcx.service.MenuService;
import lcx.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by LCX on 2/9/2022 5:29 PM
 */
@RestController
public class MenuHandler {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTree(){

        // 通过service层方法得到全部Menu对象的List
        List<Menu> menuList = menuService.getAll();

        // 声明一个Menu对象root，用来存放找到的根节点
        Menu root = null;

        // 使用map表示每一个菜单与id的对应关系
        Map<Integer,Menu> menuMap = new HashMap<>();

        // 将菜单id与菜单对象以K-V对模式存入map
        for(Menu menu: menuList){
            menuMap.put(menu.getId(),menu);
        }
        // 遍历menuList
        for (Menu menu :menuList) {
            Integer pid = menu.getPid();

            if(pid == null){
                // pid为null表示该menu是根节点,相当于指向根节点的指针
                root=menu;
                continue;
            }
            // 如果pid不为null，说明当前节点有父节点
            // 通过当前遍历的menu的pid得到其父节点
            Menu father = menuMap.get(pid);

            // 将当前节点存入父节点的children集合
            father.getChildren().add(menu);
        }
        
        // 将根节点作为data返回（返回根节点也就返回了整棵树）
        return ResultEntity.successWithData(root);
    }

    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id") Integer id){
        menuService.removeMenu(id);
        return ResultEntity.successWithoutData();
    }
}
