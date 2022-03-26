package com.yeb.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeb.server.pojo.MenuRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author stx
 * @since 2021-10-16
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     *
     * @param rid
     * @param mids
     */
    Integer insertRecord(@Param("rid") Integer rid,@Param("mids") Integer[] mids);
}
