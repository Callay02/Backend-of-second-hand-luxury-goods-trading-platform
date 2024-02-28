package icu.callay.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVo<T> {
    private List<T> data;
    private Long total;
}
