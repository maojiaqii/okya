package com.top.okya.service;

import com.top.okya.pojo.AsPermission;
import com.top.okya.util.elHelp.ElTreeBean;

import java.util.List;

/**
 * @author: maojiaqi
 * @Date: 2020/4/9 16:06
 * @describe：
 */
public interface MenuService {

    String getMenuButtonData(String roleCode, String type, String setYear);

    List getCompos(String compoName, String sortProp, String sortOrder, String setYear);

    List<ElTreeBean> getParentCompos(String setYear);

    void saveCompo(AsPermission asPermission);

    void updateCompo(AsPermission asPermission);

    void deleteCompo(List<AsPermission> parseArray, String setYear);

    List<ElTreeBean> getAllCompos(String setYear);

    String[] getComposByRole(String roleCode, String setYear);
}
