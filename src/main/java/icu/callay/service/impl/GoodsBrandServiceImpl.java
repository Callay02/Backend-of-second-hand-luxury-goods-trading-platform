package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.Goods;
import icu.callay.entity.GoodsType;
import icu.callay.mapper.GoodsBrandMapper;
import icu.callay.entity.GoodsBrand;
import icu.callay.service.GoodsBrandService;
import icu.callay.vo.GoodsBrandPageVo;
import icu.callay.vo.GoodsPageVo;
import icu.callay.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public SaResult getGoodsBrandPage(int page, int rows) {
        //System.out.println(page+""+rows);
        Page<GoodsBrand> brandPage = new Page<>(page,rows);
        goodsBrandMapper.selectPage(brandPage,null);

        GoodsBrandPageVo goodsBrandPageVo = new GoodsBrandPageVo();
        goodsBrandPageVo.setGoodsBrandList(brandPage.getRecords());
        goodsBrandPageVo.setTotal(brandPage.getTotal());

        return SaResult.data(goodsBrandPageVo);
    }

    @Override
    public SaResult addBrand(GoodsBrand goodsBrand) {
        try {
            save(goodsBrand);
            return SaResult.ok();
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }
}


