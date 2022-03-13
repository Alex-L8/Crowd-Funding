package lcx.service;

import lcx.entity.Menu;

import java.util.List;

/**
 * Create by LCX on 2/9/2022 5:30 PM
 */
public interface MenuService {
    List<Menu> getAll();

    void saveMenu(Menu menu);

    void updateMenu(Menu menu);

    void removeMenu(Integer id);
}
