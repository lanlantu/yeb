package com.yeb.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeb.server.pojo.Admin;
import com.yeb.server.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author stx
 * @since 2021-10-16
 */
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 获取所有操作员
     * @param id
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmins(@Param("id") Integer id,@Param("keywords") String keywords);
}
