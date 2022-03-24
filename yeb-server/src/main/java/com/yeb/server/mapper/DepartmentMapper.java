package com.yeb.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeb.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author stx
 * @since 2021-10-16
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 获取所有部门
     * @return
     */
    List<Department> getAllDepartments(Integer parentId);

    /**
     * 添加部门
     * @param dep
     */
    void addDep(Department dep);

    /**
     * 删除部门
     * @param dep
     */
    void deleteDep(Department dep);
}
