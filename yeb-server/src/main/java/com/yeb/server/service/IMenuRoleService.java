package com.yeb.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeb.server.pojo.MenuRole;
import com.yeb.server.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stx
 * @since 2021-10-16
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
