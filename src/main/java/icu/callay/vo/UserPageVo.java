package icu.callay.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserPageVo<T> {
    private List<T> userVoList;
    private Long total;
}
