package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.GoodsBrand;
import icu.callay.mapper.GoodsTypeMapper;
import icu.callay.entity.GoodsType;
import icu.callay.service.GoodsTypeService;
import icu.callay.vo.GoodsBrandPageVo;
import icu.callay.vo.GoodsTypePageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (GoodsType)表服务实现类
 *
 * @author Callay
 * @since 2024-01-30 19:07:47
 */
@Service("goodsTypeService")
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements GoodsTypeService {

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public SaResult getGoodsType() {
        List<GoodsType> goodsTypeList =goodsTypeMapper.getGoodsType();
        return SaResult.data(goodsTypeList);
    }

    @Override
    public SaResult getGoodsTypePage(int page, int rows) {
        //System.out.println(page+""+rows);
        Page<GoodsType> goodsTypePage = new Page<>(page,rows);
        goodsTypeMapper.selectPage(goodsTypePage,null);

        GoodsTypePageVo goodsTypePageVo = new GoodsTypePageVo();
        goodsTypePageVo.setGoodsTypeList(goodsTypePage.getRecords());
        goodsTypePageVo.setTotal(goodsTypePage.getTotal());

        return SaResult.data(goodsTypePageVo);
    }

    @Override
    public SaResult addGoodsType(GoodsType goodsType) {
        try {
            if(count(new QueryWrapper<GoodsType>().eq("type",goodsType.getType()))==0){
                save(goodsType);
                return SaResult.ok("添加成功");
            }
            return SaResult.error("该类型已存在");

        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult deleteTypeById(int type) {
        try{
            remove(new QueryWrapper<GoodsType>().eq("type",type));
            return SaResult.ok("删除成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult updateTypeName(GoodsType goodsType) {
        try {
            update(new UpdateWrapper<GoodsType>().eq("type",goodsType.getType()).set("name",goodsType.getName()));
            return SaResult.ok("修改成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }
}


