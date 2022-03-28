package com.yeb.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页公共返回对象
 * @author Suntingxing
 * @date 2021/10/23 0:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespPageBean {

    private long total; // 总条数
    private List<?> data; // 数据list
}
