package com.uestc.crowd.service.impl;

import com.uestc.crowd.entity.Menu;
import com.uestc.crowd.entity.MenuExample;
import com.uestc.crowd.mapper.MenuMapper;
import com.uestc.crowd.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAll() {
        return menuMapper.selectByExample(new MenuExample());
    }

    @Override
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void removeMenu(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateMenu(Menu menu) {
        // 由于pid没有传入所以要选择有选择的更新
        menuMapper.updateByPrimaryKeySelective(menu);
    }
}
