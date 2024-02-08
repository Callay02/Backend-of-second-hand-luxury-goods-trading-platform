package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.mapper.GoodsBrandMapper;
import icu.callay.entity.GoodsBrand;
import icu.callay.service.GoodsBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (GoodsBrand)表服务实现类
 *
 * @author Callay
 * @since 2024-01-30 19:07:20
 */
@Service("goodsBrandService")
public class GoodsBrandServiceImpl extends ServiceImpl<GoodsBrandMapper, GoodsBrand> implements GoodsBrandService {

    @Autowired
    private GoodsBrandMapper goodsBrandMapper;

    @Override
    public SaResult getGoodsBrand() {
        List<GoodsBrand> goodsBrandList = goodsBrandMapper.getGoodsBrand();
        return SaResult.data(goodsBrandList);
    }
}


