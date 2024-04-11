package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.mapper.PlatformRevenueFlowFormMapper;
import icu.callay.entity.PlatformRevenueFlowForm;
import icu.callay.service.PlatformRevenueFlowFormService;
import icu.callay.vo.PageVo;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.bytecode.analysis.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * (PlatformRevenueFlowForm)表服务实现类
 *
 * @author Callay
 * @since 2024-04-10 14:33:56
 */
@Service("platformRevenueFlowFormService")
@RequiredArgsConstructor
public class PlatformRevenueFlowFormServiceImpl extends ServiceImpl<PlatformRevenueFlowFormMapper, PlatformRevenueFlowForm> implements PlatformRevenueFlowFormService {

    private final PlatformRevenueFlowFormMapper platformRevenueFlowFormMapper;
    @Override
    public SaResult getPageByForm(String type,String from, int page, int rows) {
        try {
            Page<PlatformRevenueFlowForm> platformRevenueFlowFormPage = new Page<>(page,rows);
            QueryWrapper<PlatformRevenueFlowForm> queryWrapper =new QueryWrapper<>();
            if(!Objects.equals(from, "")){
                queryWrapper.eq("type",type);
            }
            if(!Objects.equals(from, "")){
                queryWrapper.eq("from",from);
            }
            platformRevenueFlowFormMapper.selectPage(platformRevenueFlowFormPage,queryWrapper.orderByDesc("update_time"));
            PageVo<PlatformRevenueFlowForm> pageVo = new PageVo<>();
            pageVo.setData(platformRevenueFlowFormPage.getRecords());
            pageVo.setTotal(platformRevenueFlowFormPage.getTotal());
            return SaResult.data(pageVo);
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }
}


